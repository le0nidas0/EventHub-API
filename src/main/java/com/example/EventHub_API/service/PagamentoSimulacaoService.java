package com.example.EventHub_API.service;


import com.example.EventHub_API.enums.MetodoPagamento;
import com.example.EventHub_API.enums.StatusPagamento;
import com.example.EventHub_API.model.Inscricao;
import com.example.EventHub_API.model.Pagamento;
import com.example.EventHub_API.repository.PagamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PagamentoSimulacaoService {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoSimulacaoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @Transactional
    public Pagamento simularPagamento(
            Inscricao inscricao,
            MetodoPagamento metodo,
            BigDecimal precoEsperado,
            BigDecimal valorSimulado
    ) {
        StatusPagamento statusSimulado;

        if (metodo == MetodoPagamento.BOLETO) {
            statusSimulado = StatusPagamento.PENDENTE;
        }
        else if (valorSimulado != null && valorSimulado.compareTo(precoEsperado) == 0) {
            statusSimulado = StatusPagamento.APROVADO;
        }
        else {
            statusSimulado = StatusPagamento.RECUSADO;
        }

        Pagamento pagamento = Pagamento.builder()
                .inscricao(inscricao)
                .metodo(metodo)
                .status(statusSimulado)
                .dataPagamento(LocalDateTime.now())
                .build();

        return pagamentoRepository.save(pagamento);
    }
}