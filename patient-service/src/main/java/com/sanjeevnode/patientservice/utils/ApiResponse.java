package com.sanjeevnode.patientservice.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {
    private HttpStatus status;
    private String message;
    private Object data;
}
