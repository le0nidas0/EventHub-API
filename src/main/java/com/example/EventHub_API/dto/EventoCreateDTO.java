package com.example.EventHub_API.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EventoCreateDTO(
        @NotBlank(message = "O nome do evento é obrigatório.")
        String nome,

        @NotBlank(message = "A descrição é obrigatória.")
        String descricao,

        @NotNull(message = "A data do evento é obrigatória.")
        @FutureOrPresent(message = "A data do evento deve ser futura ou presente.")
        LocalDate data,

        @NotNull(message = "A capacidade máxima é obrigatória.")
        @Min(value = 1, message = "A capacidade mínima deve ser 1.")
        Integer capacidadeMaxima,

        @NotNull(message = "O preço do ingresso é obrigatório.")
        @DecimalMin(value = "0.00", message = "O preço não pode ser negativo.")
        BigDecimal precoIngresso,

        @NotNull(message = "O ID do organizador é obrigatório.")
        Long organizadorId
) {}