package com.example.ppay.controller;

import com.example.ppay.dto.ExceptionResponseDto;
import com.example.ppay.exception.BalanceException;
import com.example.ppay.exception.OperationNotAllowedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ExceptionResponseDto handleEntityNotFoundException(EntityNotFoundException e) {
        return new ExceptionResponseDto(e.getMessage());
    }

    @ExceptionHandler(BalanceException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ExceptionResponseDto handleBalanceException(BalanceException e) {
        return new ExceptionResponseDto(e.getMessage());
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ExceptionResponseDto handleBalanceException(OperationNotAllowedException e) {
        return new ExceptionResponseDto(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponseDto handleBalanceException(Exception e) {
        return new ExceptionResponseDto(e.getMessage());
    }
}
