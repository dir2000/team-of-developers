package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {
    public static final int SUCCESSFUL = 0;
    public static final int VALIDATION_ERROR = 1;
    public static final int PERSISTENCE_ERROR = 2;
    public static final int EXCEPTIONAL_ERROR = 3;

    public <T> ResponseEntity<ResponseDTO<T>> success(final String message, final HttpStatus httpStatus) {
        final ResponseDTO<T> responseDto = new ResponseDTO<>(SUCCESSFUL, message, null);
        return new ResponseEntity<>(responseDto, httpStatus);
    }

    public <T> ResponseEntity<ResponseDTO<T>> success(final String message, final T dto, final HttpStatus httpStatus) {
        final ResponseDTO<T> responseDto = new ResponseDTO<>(SUCCESSFUL, message, dto);
        return new ResponseEntity<>(responseDto, httpStatus);
    }

    public <T> ResponseEntity<ResponseListDTO<T>> success(final String message, final List<T> list, final HttpStatus httpStatus) {
        final ResponseListDTO<T> responseDto = new ResponseListDTO<>(SUCCESSFUL, message, list);
        return new ResponseEntity<>(responseDto, httpStatus);
    }

    public <T> ResponseEntity<ResponseDTO<T>> validationError(final String message) {
        return error(VALIDATION_ERROR, message, HttpStatus.BAD_REQUEST);
    }

    public <T> ResponseEntity<ResponseDTO<T>> persistenceError(final String message) {
        return error(PERSISTENCE_ERROR, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public <T> ResponseEntity<ResponseDTO<T>> exceptionalError(final String message) {
        return error(EXCEPTIONAL_ERROR, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private <T> ResponseEntity<ResponseDTO<T>> error(final int code, final String message, final HttpStatus httpStatus) {
        final ResponseDTO<T> responseDto = new ResponseDTO<>(code, message, null);
        return new ResponseEntity<>(responseDto, httpStatus);
    }
}
