package com.example.EventHub_API.service;

import com.example.EventHub_API.dto.EventoCreateDTO;
import com.example.EventHub_API.dto.EventoResponseDTO;
import com.example.EventHub_API.dto.EventoUpdateDTO;
import com.example.EventHub_API.exception.AutorizacaoNegadaException;
import com.example.EventHub_API.exception.RecursoNaoEncontradoException;
import com.example.EventHub_API.model.Evento;
import com.example.EventHub_API.model.Usuario;
import com.example.EventHub_API.repository.EventoRepository;
import com.example.EventHub_API.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final InscricaoService inscricaoService;

    public EventoService(EventoRepository eventoRepository,
                         UsuarioRepository usuarioRepository,
                         InscricaoService inscricaoService) {
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.inscricaoService = inscricaoService;
    }

    private EventoResponseDTO toResponseDTO(Evento evento) {
        Integer vagas = inscricaoService.contarVagasDisponiveis(evento.getId(), evento.getCapacidadeMaxima());

        return new EventoResponseDTO(
                evento.getId(),
                evento.getNome(),
                evento.getDescricao(),
                evento.getData(),
                evento.getPrecoIngresso(),
                evento.getCapacidadeMaxima(),
                vagas,
                evento.getOrganizador().getNome(),
                vagas <= 0
        );
    }

    private void verificarPropriedade(Evento evento, Long usuarioId) {
        if (!evento.getOrganizador().getId().equals(usuarioId)) {
            throw new AutorizacaoNegadaException("Você não tem permissão para modificar este evento.");
        }
    }

    @Transactional
    public EventoResponseDTO criarEvento(EventoCreateDTO dto, Long organizadorId) {

        Usuario organizador = usuarioRepository.findById(organizadorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Organizador com ID " + organizadorId + " não encontrado."));

        if (!Boolean.TRUE.equals(organizador.getOrganizador())) {
            throw new AutorizacaoNegadaException("Apenas organizadores podem criar eventos. Seu status é 'usuario'.");
        }


        Evento evento = Evento.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .data(dto.data())
                .capacidadeMaxima(dto.capacidadeMaxima())
                .precoIngresso(dto.precoIngresso())
                .organizador(organizador)
                .build();

        evento = eventoRepository.save(evento);
        return toResponseDTO(evento);
    }

    public List<EventoResponseDTO> listarTodosEventos() {
        return eventoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<EventoResponseDTO> listarEventosFuturos() {
        return eventoRepository.findByDataGreaterThanEqual(LocalDateTime.now()).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public EventoResponseDTO buscarEventoPorId(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Evento com ID " + id + " não encontrado."));
        return toResponseDTO(evento);
    }

    @Transactional
    public EventoResponseDTO atualizarEvento(Long eventoId, EventoUpdateDTO dto, Long usuarioQueEstaAtualizandoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Evento com ID " + eventoId + " não encontrado."));

        verificarPropriedade(evento, usuarioQueEstaAtualizandoId);

        if (dto.nome() != null) evento.setNome(dto.nome());
        if (dto.descricao() != null) evento.setDescricao(dto.descricao());
        if (dto.data() != null) evento.setData(dto.data());
        if (dto.capacidadeMaxima() != null) evento.setCapacidadeMaxima(dto.capacidadeMaxima());
        if (dto.precoIngresso() != null) evento.setPrecoIngresso(dto.precoIngresso());

        return toResponseDTO(evento);
    }

    @Transactional
    public void deletarEvento(Long eventoId, Long usuarioQueEstaDeletandoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Evento com ID " + eventoId + " não encontrado para deleção."));

        verificarPropriedade(evento, usuarioQueEstaDeletandoId);

        eventoRepository.delete(evento);
    }
}