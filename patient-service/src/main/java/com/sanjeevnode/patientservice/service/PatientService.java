package com.sanjeevnode.patientservice.service;

import com.sanjeevnode.patientservice.dto.PatientRequestDTO;
import com.sanjeevnode.patientservice.dto.PatientResponseDTO;
import com.sanjeevnode.patientservice.exception.EmailAlreadyExistsException;
import com.sanjeevnode.patientservice.exception.PatientNotFoundException;
import com.sanjeevnode.patientservice.repository.PatientRepository;
import com.sanjeevnode.patientservice.utils.AppLogger;
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

    public List<PatientResponseDTO> getPatients() {
        logger.info("Fetched all patients");
        return patientRepository.findAll()
                .stream()
                .map(PatientResponseDTO::fromModel)
                .collect(Collectors.toList());
    }

    public PatientResponseDTO getPatientById(String id) {
        UUID uuid = UUID.fromString(id);
        logger.info("Fetched patient with id %s", id);
        return patientRepository.findById(uuid)
                .map(PatientResponseDTO::fromModel)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        logger.info("Created patient with email %s", patientRequestDTO.getEmail());
        return PatientResponseDTO.fromModel(patientRepository.save(patientRequestDTO.toModel()));
    }

    public PatientResponseDTO updatePatient(String id, PatientRequestDTO patientRequestDTO) {
        UUID uuid = UUID.fromString(id);
        var existingPatient = patientRepository.findById(uuid)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), uuid)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        existingPatient.setName(patientRequestDTO.getName());
        existingPatient.setEmail(patientRequestDTO.getEmail());
        existingPatient.setAddress(patientRequestDTO.getAddress());
        existingPatient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        existingPatient.setRegisteredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));

        logger.info("Updated patient with id %s", id);
        return PatientResponseDTO.fromModel(patientRepository.save(existingPatient));
    }

    public void deletePatient(String id) {
        UUID uuid = UUID.fromString(id);
        if (!patientRepository.existsById(uuid)) {
            throw new PatientNotFoundException("Patient not found with ID: " + id);
        }

        patientRepository.deleteById(uuid);
        logger.info("Deleted patient with id %s", id);
    }
}
