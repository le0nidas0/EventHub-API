package com.example.EventHub_API.repository;

import com.example.EventHub_API.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    Optional<Pagamento> findByInscricaoId(Long inscricaoId);
}