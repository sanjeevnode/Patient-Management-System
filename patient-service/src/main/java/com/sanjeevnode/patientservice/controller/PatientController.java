package com.sanjeevnode.patientservice.controller;

import com.sanjeevnode.patientservice.dto.PatientRequestDTO;
import com.sanjeevnode.patientservice.service.PatientService;
import com.sanjeevnode.patientservice.utils.ApiResponse;
import com.sanjeevnode.patientservice.utils.AppLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @Operation(summary = "Get all patients")
    @GetMapping
    public ResponseEntity<ApiResponse> getPatients() {
        logger.info("GET /patient");
        return patientService.getPatients();
    }

    @Operation(summary = "Get patient by id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPatientById(@PathVariable String id) {
        logger.info("GET /patient/%s", id);
        return patientService.getPatientById(id);
    }

    @Operation(summary = "Create a new patient")
    @PostMapping
    public ResponseEntity<ApiResponse> createPatient(@RequestBody @Valid  PatientRequestDTO patientRequestDTO) {
        logger.info("POST /patient with data %s", patientRequestDTO.toString());
        return patientService.createPatient(patientRequestDTO);
    }

    @Operation(summary = "Update an existing patient")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePatient(@PathVariable String id, @RequestBody @Valid PatientRequestDTO patientRequestDTO) {
        logger.info("PUT /patient/%s with data %s", id, patientRequestDTO.toString());
        return patientService.updatePatient(id, patientRequestDTO);
    }

    @Operation(summary = "Delete a patient")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePatient(@PathVariable String id) {
        logger.info("DELETE /patient/%s", id);
        return patientService.deletePatient(id);
    }
}
