package com.example.EventHub_API.dto;

import com.example.EventHub_API.enums.MetodoPagamento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record InscricaoCreateDTO(

        @NotNull(message = "O método de pagamento é obrigatório.")
        MetodoPagamento metodoPagamento,

        @NotNull(message = "O valor de pagamento simulado é obrigatório.")
        @DecimalMin(value = "0.00", message = "O valor deve ser positivo.")
        BigDecimal valorSimulado
) {}