package com.example.EventHub_API.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventoCreateDTO(
        @NotBlank(message = "O nome do evento é obrigatório.")
        String nome,

        @NotBlank(message = "A descrição é obrigatória.")
        String descricao,

        @NotNull(message = "A data e hora do evento são obrigatórias.")
        @FutureOrPresent(message = "A data do evento deve ser futura ou presente.")
        LocalDateTime data,

        @NotNull(message = "A capacidade máxima é obrigatória.")
        @Min(value = 1, message = "A capacidade mínima deve ser 1.")
        Integer capacidadeMaxima,

        @NotNull(message = "O preço do ingresso é obrigatório.")
        @DecimalMin(value = "0.00", message = "O preço não pode ser negativo.")
        BigDecimal precoIngresso

) {}