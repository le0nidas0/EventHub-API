package com.example.EventHub_API.service;


import com.example.EventHub_API.dto.InscricaoCreateDTO;
import com.example.EventHub_API.dto.InscricaoResponseDTO;
import com.example.EventHub_API.enums.StatusPagamento;
import com.example.EventHub_API.exception.EventoCheioException;
import com.example.EventHub_API.exception.RecursoNaoEncontradoException;
import com.example.EventHub_API.model.Evento;
import com.example.EventHub_API.model.Inscricao;
import com.example.EventHub_API.model.Usuario;
import com.example.EventHub_API.repository.EventoRepository;
import com.example.EventHub_API.repository.InscricaoRepository;
import com.example.EventHub_API.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscricaoService {

    private final InscricaoRepository inscricaoRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PagamentoSimulacaoService pagamentoService;

    public InscricaoService(InscricaoRepository inscricaoRepository, EventoRepository eventoRepository,
                            UsuarioRepository usuarioRepository, PagamentoSimulacaoService pagamentoService) {
        this.inscricaoRepository = inscricaoRepository;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.pagamentoService = pagamentoService;
    }

    @Transactional(readOnly = true)
    public Integer contarVagasDisponiveis(Long eventoId, Integer capacidadeMaxima) {
        long inscricoesAprovadas = inscricaoRepository.countByEventoIdAndStatusPagamento(
                eventoId, StatusPagamento.APROVADO
        );
        return capacidadeMaxima - (int) inscricoesAprovadas;
    }

    private InscricaoResponseDTO toResponseDTO(Inscricao inscricao) {
        return new InscricaoResponseDTO(
                inscricao.getId(),
                inscricao.getUsuario().getId(),
                inscricao.getEvento().getId(),
                inscricao.getEvento().getNome(),
                inscricao.getStatusPagamento(),
                inscricao.getDataInscricao()
        );
    }

    @Transactional
    public InscricaoResponseDTO inscreverUsuario(Long usuarioId, Long eventoId, InscricaoCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Evento não encontrado."));

        if (inscricaoRepository.findByUsuarioIdAndEventoId(usuarioId, eventoId).isPresent()) {
            throw new IllegalStateException("Usuário já inscrito neste evento.");
        }

        int inscricoesAprovadas = inscricaoRepository.countByEventoIdAndStatusPagamento(
                eventoId, StatusPagamento.APROVADO
        );

        if (inscricoesAprovadas >= evento.getCapacidadeMaxima()) {
            throw new EventoCheioException("O evento atingiu sua capacidade máxima.");
        }

        Inscricao inscricao = Inscricao.builder()
                .usuario(usuario)
                .evento(evento)
                .statusPagamento(StatusPagamento.PENDENTE)
                .build();

        var pagamento = pagamentoService.simularPagamento(
                inscricao,
                dto.metodoPagamento(),
                evento.getPrecoIngresso(),
                dto.valorSimulado()
        );

        inscricao.setStatusPagamento(pagamento.getStatus());

        return toResponseDTO(inscricaoRepository.save(inscricao));
    }

    @Transactional(readOnly = true)
    public List<InscricaoResponseDTO> listarTodasInscricoes() {
        return inscricaoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InscricaoResponseDTO buscarPorId(Long id) {
        Inscricao inscricao = inscricaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Inscrição não encontrada."));
        return toResponseDTO(inscricao);
    }

    @Transactional(readOnly = true)
    public List<InscricaoResponseDTO> listarInscricoesPorUsuario(Long usuarioId) {
        return inscricaoRepository.findByUsuarioId(usuarioId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public InscricaoResponseDTO cancelarInscricao(Long inscricaoId, Long usuarioLogadoId) {
        Inscricao inscricao = inscricaoRepository.findById(inscricaoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Inscrição não encontrada."));

        if (!inscricao.getUsuario().getId().equals(usuarioLogadoId)) {
            throw new IllegalStateException("Você não tem permissão para cancelar esta inscrição.");
        }

        if (inscricao.getStatusPagamento() == StatusPagamento.CANCELADO) {
            throw new IllegalStateException("Esta inscrição já está cancelada.");
        }

        inscricao.setStatusPagamento(StatusPagamento.CANCELADO);

        return toResponseDTO(inscricaoRepository.save(inscricao));
    }
}