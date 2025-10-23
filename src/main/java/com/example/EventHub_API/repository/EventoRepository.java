package com.example.EventHub_API.repository;

import com.example.EventHub_API.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByDataGreaterThanEqual(LocalDateTime data);

    List<Evento> findByOrganizadorId(Long organizadorId);
}