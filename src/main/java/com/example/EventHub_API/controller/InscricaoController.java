package com.example.EventHub_API.controller;

import com.example.EventHub_API.dto.InscricaoCreateDTO;
import com.example.EventHub_API.dto.InscricaoResponseDTO;
import com.example.EventHub_API.service.InscricaoService;
import com.example.EventHub_API.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/inscricoes")
public class InscricaoController {

    private final InscricaoService inscricaoService;
    private final UsuarioService usuarioService;

    public InscricaoController(InscricaoService inscricaoService, UsuarioService usuarioService) {
        this.inscricaoService = inscricaoService;
        this.usuarioService = usuarioService;
    }

    private Long getUserIdFromPrincipal(UserDetails userDetails) {
        return usuarioService.buscarIdPorEmail(userDetails.getUsername());
    }

    @PostMapping("/evento/{eventoId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InscricaoResponseDTO> realizarInscricao(
            @PathVariable Long eventoId,
            @Valid @RequestBody InscricaoCreateDTO createDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long usuarioId = getUserIdFromPrincipal(userDetails);

        InscricaoResponseDTO inscricao = inscricaoService.inscreverUsuario(usuarioId, eventoId, createDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(inscricao);
    }

    @GetMapping("/minhas")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<InscricaoResponseDTO>> listarMinhasInscricoes(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long usuarioId = getUserIdFromPrincipal(userDetails);

        List<InscricaoResponseDTO> inscricoes = inscricaoService.listarInscricoesPorUsuario(usuarioId);
        return ResponseEntity.ok(inscricoes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InscricaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inscricaoService.buscarPorId(id));
    }

    @DeleteMapping("/{inscricaoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public InscricaoResponseDTO cancelarInscricao(
            @PathVariable Long inscricaoId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long usuarioId = getUserIdFromPrincipal(userDetails);

        return inscricaoService.cancelarInscricao(inscricaoId, usuarioId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZADOR')")
    public ResponseEntity<List<InscricaoResponseDTO>> listarTodasInscricoes() {
        List<InscricaoResponseDTO> inscricoes = inscricaoService.listarTodasInscricoes();
        return ResponseEntity.ok(inscricoes);
    }
}