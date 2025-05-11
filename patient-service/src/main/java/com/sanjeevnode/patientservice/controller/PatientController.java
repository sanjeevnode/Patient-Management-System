package com.sanjeevnode.patientservice.controller;

import com.sanjeevnode.patientservice.dto.PatientRequestDTO;
import com.sanjeevnode.patientservice.service.PatientService;
import com.sanjeevnode.patientservice.utils.ApiResponse;
import com.sanjeevnode.patientservice.utils.AppLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
@Tag(name = "Patient", description = "Patient Management API")
public class PatientController {

    private final PatientService patientService;
    private final AppLogger logger = new AppLogger(PatientController.class, "PatientController");

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get all patients")
    public ResponseEntity<ApiResponse> getPatients() {
        logger.info("GET /patient");
        var patients = patientService.getPatients();
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("Patients fetched successfully")
                .data(patients)
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID")
    public ResponseEntity<ApiResponse> getPatientById(@PathVariable String id) {
        logger.info("GET /patient/%s", id);
        var patient = patientService.getPatientById(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("Patient fetched successfully")
                .data(patient)
                .build());
    }

    @PostMapping
    @Operation(summary = "Create a new patient")
    public ResponseEntity<ApiResponse> createPatient(@RequestBody @Valid PatientRequestDTO patientRequestDTO) {
        logger.info("POST /patient with data %s", patientRequestDTO.toString());
        var createdPatient = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                .status(HttpStatus.CREATED)
                .message("Patient created successfully")
                .data(createdPatient)
                .build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing patient")
    public ResponseEntity<ApiResponse> updatePatient(@PathVariable String id, @RequestBody @Valid PatientRequestDTO patientRequestDTO) {
        logger.info("PUT /patient/%s with data %s", id, patientRequestDTO.toString());
        var updatedPatient = patientService.updatePatient(id, patientRequestDTO);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("Patient updated successfully")
                .data(updatedPatient)
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a patient")
    public ResponseEntity<ApiResponse> deletePatient(@PathVariable String id) {
        logger.info("DELETE /patient/%s", id);
        patientService.deletePatient(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("Patient deleted successfully")
                .data(null)
                .build());
    }
}

