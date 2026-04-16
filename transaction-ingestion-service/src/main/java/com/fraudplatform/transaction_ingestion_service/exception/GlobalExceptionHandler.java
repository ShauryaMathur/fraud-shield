package com.fraudplatform.transaction_ingestion_service.exception;

import com.fraudplatform.transaction_ingestion_service.dto.CreateTransactionResponse;
import com.fraudplatform.transaction_ingestion_service.enums.TransactionStatus;
import com.mongodb.DuplicateKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.OK)
    public CreateTransactionResponse handleDuplicateKey(DuplicateKeyException ex) {
        // Mongo unique index violation — idempotency key already exists
        // return 200 not 500 — this is not an error, it's expected behavior
        log.warn("Duplicate key violation — idempotent request: {}", ex.getMessage());
        return CreateTransactionResponse.builder()
                .status(TransactionStatus.CREATED)
                .message("Duplicate request handled idempotently")
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGeneral(Exception ex) {
        log.error("Unexpected error", ex);
        return Map.of("error", "Internal server error");
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleTransactionNotFound(TransactionNotFoundException ex) {
        log.error("Transaction not found exception: {}", ex.getMessage());
        return Map.of("error", ex.getMessage());
    }
}