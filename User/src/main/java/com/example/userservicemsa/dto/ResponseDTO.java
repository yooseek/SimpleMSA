package com.example.userservicemsa.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseDTO {
    private String message;
    private HttpStatus status;
}
