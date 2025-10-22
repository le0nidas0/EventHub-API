package com.example.EventHub_API.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EventoResponseDTO(
        Long id,
        String nome,
        String descricao,
        LocalDate data,
        BigDecimal precoIngresso,
        Integer capacidadeMaxima,
        Integer vagasDisponiveis,
        String nomeOrganizador,
        boolean estaCheio
) {}