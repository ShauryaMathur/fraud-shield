package com.fraudplatform.transaction_ingestion_service.exception;

public class TransactionNotFoundException extends RuntimeException{
    public TransactionNotFoundException(String transactionId) {
        super("Transaction with ID: " + transactionId + " not found");
    }
}
