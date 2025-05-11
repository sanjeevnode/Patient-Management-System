package com.sanjeevnode.patientservice.dto;

import com.sanjeevnode.patientservice.model.Patient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequestDTO {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Date of birth is required")
    private String dateOfBirth;

    @NotBlank(groups = CreatePatientValidationGroup.class, message =
            "Registered date is required")
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
}
