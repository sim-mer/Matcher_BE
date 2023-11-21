package com.knu.matcher.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String status;
    private String message;
}
