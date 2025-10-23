package com.example.EventHub_API.repository;

import com.example.EventHub_API.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByIdAndOrganizadorTrue(Long id);
}