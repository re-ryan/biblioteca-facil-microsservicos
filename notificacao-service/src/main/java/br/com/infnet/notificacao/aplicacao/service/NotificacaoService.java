package br.com.infnet.notificacao.aplicacao.service;

import br.com.infnet.notificacao.api.dto.NotificacaoResponseDto;
import br.com.infnet.notificacao.mensageria.ReservaSolicitadaMessage;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public final class NotificacaoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificacaoService.class);

    private final Map<UUID, NotificacaoResponseDto> notificacoes = new ConcurrentHashMap<>();

    public void registrar(final ReservaSolicitadaMessage evento) {
        final NotificacaoResponseDto notificacao = new NotificacaoResponseDto(
                evento.eventoId(),
                evento.reservaId(),
                evento.leitorId(),
                evento.emailLeitor(),
                "Reserva do livro '%s' solicitada na biblioteca '%s'."
                        .formatted(evento.tituloLivro(), evento.nomeBiblioteca()),
                LocalDateTime.now());
        this.notificacoes.putIfAbsent(evento.eventoId(), notificacao);
        LOGGER.info(
                "Notificação processada para reserva={}, leitor={}, evento={}",
                evento.reservaId(),
                evento.leitorId(),
                evento.eventoId());
    }

    public List<NotificacaoResponseDto> listar() {
        return this.notificacoes.values().stream()
                .sorted(Comparator.comparing(NotificacaoResponseDto::processadaEm))
                .toList();
    }
}
