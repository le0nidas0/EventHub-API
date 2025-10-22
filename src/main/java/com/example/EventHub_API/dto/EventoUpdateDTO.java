package com.example.EventHub_API.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EventoUpdateDTO(
        String nome,
        String descricao,

        @FutureOrPresent(message = "A data deve ser futura ou presente.")
        LocalDate data,

        @Min(value = 1, message = "A capacidade mínima deve ser 1.")
        Integer capacidadeMaxima,

        @DecimalMin(value = "0.00", message = "O preço não pode ser negativo.")
        BigDecimal precoIngresso
) {}