package com.sanjeevnode.patientservice.controller;

import com.sanjeevnode.patientservice.dto.PatientRequestDTO;
import com.sanjeevnode.patientservice.service.PatientService;
import com.sanjeevnode.patientservice.utils.ApiResponse;
import com.sanjeevnode.patientservice.utils.AppLogger;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final AppLogger logger = new AppLogger(PatientController.class, "PatientController");

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getPatients() {
        logger.info("GET /patient");
        return patientService.getPatients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPatientById(@PathVariable String id) {
        logger.info("GET /patient/%s", id);
        return patientService.getPatientById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createPatient(@RequestBody @Valid  PatientRequestDTO patientRequestDTO) {
        logger.info("POST /patient with data %s", patientRequestDTO.toString());
        return patientService.createPatient(patientRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePatient(@PathVariable String id, @RequestBody @Valid PatientRequestDTO patientRequestDTO) {
        logger.info("PUT /patient/%s with data %s", id, patientRequestDTO.toString());
        return patientService.updatePatient(id, patientRequestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePatient(@PathVariable String id) {
        logger.info("DELETE /patient/%s", id);
        return patientService.deletePatient(id);
    }
}
