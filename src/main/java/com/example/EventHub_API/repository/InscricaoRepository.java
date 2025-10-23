package com.example.EventHub_API.repository;

import com.example.EventHub_API.enums.StatusPagamento;
import com.example.EventHub_API.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    Optional<Inscricao> findByUsuarioIdAndEventoId(Long usuarioId, Long eventoId);

    int countByEventoIdAndStatusPagamento(Long eventoId, StatusPagamento statusPagamento);

    List<Inscricao> findByUsuarioId(Long usuarioId);
}