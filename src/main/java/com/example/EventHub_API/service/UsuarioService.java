package com.example.EventHub_API.service;

import com.example.EventHub_API.dto.UsuarioCreateDTO;
import com.example.EventHub_API.dto.UsuarioResponseDTO;
import com.example.EventHub_API.exception.RecursoNaoEncontradoException;
import com.example.EventHub_API.model.Usuario;
import com.example.EventHub_API.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getOrganizador()
        );
    }

    @Transactional
    public UsuarioResponseDTO criarUsuario(UsuarioCreateDTO dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado.");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.senha());

        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(senhaCriptografada)
                .telefone(dto.telefone())
                .organizador(dto.organizador() != null ? dto.organizador() : false)
                .build();

        usuario = usuarioRepository.save(usuario);
        return toResponseDTO(usuario);
    }

    public List<UsuarioResponseDTO> buscarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário com ID " + id + " não encontrado."));
        return toResponseDTO(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário com e-mail " + email + " não encontrado."));
    }

    public Long buscarIdPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(Usuario::getId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário com e-mail " + email + " não encontrado."));
    }



    @Transactional
    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Usuário com ID " + id + " não encontrado para deleção.");
        }
        usuarioRepository.deleteById(id);
    }
}