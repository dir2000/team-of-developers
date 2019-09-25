package com.ra.janus.developersteam.exception;

import com.ra.janus.developersteam.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {
    private final transient ResponseService responseService;

    @Autowired
    public ExceptionControllerAdvice(final ResponseService responseService) {
        this.responseService = responseService;
    }

    @ExceptionHandler
    public ResponseEntity handleException(final Exception exception) {
        return responseService.exceptionalError(exception.getMessage());
    }
}
