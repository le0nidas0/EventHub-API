package com.example.EventHub_API.dto;

import com.example.EventHub_API.enums.MetodoPagamento;

import java.math.BigDecimal;



public record PagamentoSimulacaoDTO(
        Long inscricaoId,
        BigDecimal valor,
        MetodoPagamento metodo
) {}