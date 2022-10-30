package com.example.consolelog.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiException {
    private String errorMessage;
    private HttpStatus httpStatus;
}
