package com.sanjeevnode.patientservice.service;

import com.sanjeevnode.patientservice.dto.PatientRequestDTO;
import com.sanjeevnode.patientservice.dto.PatientResponseDTO;
import com.sanjeevnode.patientservice.exception.EmailAlreadyExistsException;
import com.sanjeevnode.patientservice.exception.PatientNotFoundException;
import com.sanjeevnode.patientservice.repository.PatientRepository;
import com.sanjeevnode.patientservice.utils.ApiResponse;
import com.sanjeevnode.patientservice.utils.AppLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppLogger logger = new AppLogger(PatientService.class, "PatientService");

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public ResponseEntity<ApiResponse> getPatients() {
        try {
            List<PatientResponseDTO> patients = patientRepository.findAll()
                    .stream()
                    .map(PatientResponseDTO::fromModel)
                    .collect(Collectors.toList());

            logger.info("Fetched all patients");

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .status(HttpStatus.OK)
                            .message("Patients fetched successfully")
                            .data(patients)
                            .build()
            );
        } catch (Exception e) {
            logger.error("Error fetching patients", e, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .message(e.getMessage())
                            .data(null)
                            .build());
        }
    }

    public ResponseEntity<ApiResponse> getPatientById(String id) {
        try {
            UUID uuid = UUID.fromString(id);

            PatientResponseDTO patient = patientRepository.findById(uuid)
                    .map(PatientResponseDTO::fromModel)
                    .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

            logger.info("Fetched patient with id %s", id);

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .status(HttpStatus.OK)
                            .message("Patient fetched successfully")
                            .data(patient)
                            .build()
            );
        } catch (EmailAlreadyExistsException | PatientNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching patient with id %s", e, id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .message(e.getMessage())
                            .data(null)
                            .build());
        }
    }

    public ResponseEntity<ApiResponse> createPatient(PatientRequestDTO patientRequestDTO) {
        try {
            if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
                throw new EmailAlreadyExistsException("Email already exists");
            }

            PatientResponseDTO savedPatient = PatientResponseDTO.fromModel(
                    patientRepository.save(patientRequestDTO.toModel())
            );

            logger.info("Created patient with email %s", patientRequestDTO.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.builder()
                            .status(HttpStatus.CREATED)
                            .message("Patient created successfully")
                            .data(savedPatient)
                            .build());
        } catch (EmailAlreadyExistsException | PatientNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error creating patient", e, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .message(e.getMessage())
                            .data(null)
                            .build());
        }
    }

    public ResponseEntity<ApiResponse> updatePatient(String id, PatientRequestDTO patientRequestDTO) {
        try {
            UUID uuid = UUID.fromString(id);

            var existingPatient = patientRepository.findById(uuid)
                    .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

            if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), uuid)) {
                throw new EmailAlreadyExistsException("Email already exists");
            }

            // Update the fields
            existingPatient.setName(patientRequestDTO.getName());
            existingPatient.setEmail(patientRequestDTO.getEmail());
            existingPatient.setAddress(patientRequestDTO.getAddress());
            existingPatient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
            existingPatient.setRegisteredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));

            var savedPatient = patientRepository.save(existingPatient);

            logger.info("Updated patient with id %s", id);

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .status(HttpStatus.OK)
                            .message("Patient updated successfully")
                            .data(PatientResponseDTO.fromModel(savedPatient))
                            .build()
            );
        } catch (EmailAlreadyExistsException | PatientNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error updating patient with id %s", e, id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .message("Failed to update patient")
                            .data(null)
                            .build());
        }
    }

    public ResponseEntity<ApiResponse> deletePatient(String id) {
        try {
            UUID uuid = UUID.fromString(id);

            if (!patientRepository.existsById(uuid)) {
                throw new PatientNotFoundException("Patient not found with ID: " + id);
            }

            patientRepository.deleteById(uuid);

            logger.info("Deleted patient with id %s", id);

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .status(HttpStatus.OK)
                            .message("Patient deleted successfully")
                            .data(null)
                            .build()
            );
        } catch (EmailAlreadyExistsException | PatientNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting patient with id %s", e, id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .message(e.getMessage())
                            .data(null)
                            .build());
        }
    }
}
