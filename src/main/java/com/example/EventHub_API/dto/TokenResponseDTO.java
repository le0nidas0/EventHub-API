package com.example.EventHub_API.dto;

public record TokenResponseDTO(
        String token,
        String email,
        Boolean organizador,
        String tipo
) {
    public TokenResponseDTO(String token, String email, Boolean organizador) {
        this(token, email, organizador, "Bearer");
    }
}