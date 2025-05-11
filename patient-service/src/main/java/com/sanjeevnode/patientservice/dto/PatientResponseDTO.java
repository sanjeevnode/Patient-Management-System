package com.sanjeevnode.patientservice.dto;

import com.sanjeevnode.patientservice.model.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDTO {
    private String id;
    private String name;
    private String email;
    private String address;
    private String dateOfBirth;
    private String registeredDate;

    public Patient toModel() {
        Patient patient = new Patient();
        patient.setName(name);
        patient.setEmail(email);
        patient.setAddress(address);
        patient.setDateOfBirth(LocalDate.parse(dateOfBirth));
        patient.setRegisteredDate(LocalDate.parse(registeredDate));
        return patient;
    }

    public  static  PatientResponseDTO fromModel(Patient patient) {
        PatientResponseDTO dto = new PatientResponseDTO();
        dto.setId(patient.getId().toString());
        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setAddress(patient.getAddress());
        dto.setDateOfBirth(patient.getDateOfBirth().toString());
        dto.setRegisteredDate(patient.getRegisteredDate().toString());
        return dto;
    }
}
