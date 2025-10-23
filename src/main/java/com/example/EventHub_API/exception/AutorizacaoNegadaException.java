package com.example.EventHub_API.exception;

public class AutorizacaoNegadaException extends RuntimeException {
    public AutorizacaoNegadaException(String message) {
        super(message);
    }
}