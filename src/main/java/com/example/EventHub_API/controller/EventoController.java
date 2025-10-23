package com.example.EventHub_API.controller;

import com.example.EventHub_API.dto.EventoCreateDTO;
import com.example.EventHub_API.dto.EventoResponseDTO;
import com.example.EventHub_API.dto.EventoUpdateDTO;
import com.example.EventHub_API.service.EventoService;
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
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;
    private final UsuarioService usuarioService;

    public EventoController(EventoService eventoService, UsuarioService usuarioService) {
        this.eventoService = eventoService;
        this.usuarioService = usuarioService;
    }


    private Long getUserIdFromPrincipal(UserDetails userDetails) {
        return usuarioService.buscarIdPorEmail(userDetails.getUsername());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ORGANIZADOR')")
    public ResponseEntity<EventoResponseDTO> criarEvento(
            @Valid @RequestBody EventoCreateDTO createDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long organizadorId = getUserIdFromPrincipal(userDetails);

        EventoResponseDTO evento = eventoService.criarEvento(createDTO, organizadorId);

        return ResponseEntity.status(HttpStatus.CREATED).body(evento);
    }

    @GetMapping
    public ResponseEntity<List<EventoResponseDTO>> listarTodosEventos() {
        List<EventoResponseDTO> eventos = eventoService.listarTodosEventos();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/futuros")
    public ResponseEntity<List<EventoResponseDTO>> listarEventosFuturos() {
        List<EventoResponseDTO> eventos = eventoService.listarEventosFuturos();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> buscarEventoPorId(@PathVariable Long id) {
        EventoResponseDTO evento = eventoService.buscarEventoPorId(id);
        return ResponseEntity.ok(evento);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZADOR')")
    public ResponseEntity<EventoResponseDTO> atualizarEvento(
            @PathVariable Long id,
            @Valid @RequestBody EventoUpdateDTO updateDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long usuarioId = getUserIdFromPrincipal(userDetails);

        EventoResponseDTO eventoAtualizado = eventoService.atualizarEvento(id, updateDTO, usuarioId);

        return ResponseEntity.ok(eventoAtualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ORGANIZADOR')")
    public void deletarEvento(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long usuarioId = getUserIdFromPrincipal(userDetails);

        eventoService.deletarEvento(id, usuarioId);
    }
}