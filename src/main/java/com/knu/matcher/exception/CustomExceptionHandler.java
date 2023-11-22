package com.knu.matcher.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletException;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> IllegalArgumentExceptionHandler(IllegalArgumentException e){
        return new ResponseEntity<>(new ErrorResponse("IllegalArgumentException", e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> ResponseStatusExceptionHandler(ResponseStatusException e){
        return new ResponseEntity<>(new ErrorResponse(e.getStatus().toString(), e.getReason()), e.getStatus());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){

        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : fieldErrors)
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());

        return new ResponseEntity<>(new ErrorResponse("MethodArgumentNotValidException", errors.toString()), BAD_REQUEST);
    }

    @ExceptionHandler(value = ServletException.class)
    public ResponseEntity<ErrorResponse> ServletExceptionHandler(ServletException e){
        return new ResponseEntity<>(new ErrorResponse("NoSuchElementException", e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ErrorResponse> InvalidDataAccessApiUsageExceptionHandler(InvalidDataAccessApiUsageException e){
        return new ResponseEntity<>(new ErrorResponse("InvalidDataAccessApiUsageException", e.getMessage()), BAD_REQUEST);
    }


    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> NoSuchElementExceptionHandler(NoSuchElementException e) {
        return new ResponseEntity<>(new ErrorResponse("NoSuchElementException", e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(value = JwtException.class)
    public ResponseEntity<ErrorResponse> JwtExceptionHandler(JwtException e) {
        return new ResponseEntity<>(new ErrorResponse("JwtException", e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> RuntimeExceptionHandler(RuntimeException e) {
        return new ResponseEntity<>(new ErrorResponse("RuntimeException", e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(value = FileAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> FileAlreadyExistsExceptionHandler(FileAlreadyExistsException e) {
        return new ResponseEntity<>(new ErrorResponse("FileAlreadyExistsException", e.getMessage()), BAD_REQUEST);
    }
}
