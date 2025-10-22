package com.example.EventHub_API.dto;

import com.example.EventHub_API.enums.StatusPagamento;

import java.time.LocalDateTime;

public record InscricaoResponseDTO(
        Long id,
        Long usuarioId,
        Long eventoId,
        String nomeEvento,
        StatusPagamento statusPagamento,
        LocalDateTime dataInscricao
) {}