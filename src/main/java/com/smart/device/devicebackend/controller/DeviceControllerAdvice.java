package com.smart.device.devicebackend.controller;

import com.smart.device.devicebackend.exception.ResourceNotFound;
import com.smart.device.devicebackend.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@ControllerAdvice
public class DeviceControllerAdvice {

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ErrorMessage> resourceNotFoundExceptionHandler(ResourceNotFound ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.NOT_FOUND.value(), Instant.now(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(ResourceNotFound ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), Instant.now(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
