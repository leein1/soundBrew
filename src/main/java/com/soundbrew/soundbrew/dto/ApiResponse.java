package com.soundbrew.soundbrew.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ApiResponse {

    private int status;
    private String message;


}
