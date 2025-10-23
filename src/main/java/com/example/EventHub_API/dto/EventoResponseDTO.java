package com.example.EventHub_API.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventoResponseDTO(
        Long id,
        String nome,
        String descricao,
        LocalDateTime data,
        BigDecimal precoIngresso,
        Integer capacidadeMaxima,
        Integer vagasDisponiveis,
        String nomeOrganizador,
        boolean estaCheio
) {}