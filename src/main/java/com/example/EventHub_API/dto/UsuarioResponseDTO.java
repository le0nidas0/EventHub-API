package com.example.EventHub_API.dto;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        Boolean organizador
) {}