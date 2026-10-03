package br.com.infnet.notificacao.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificacaoResponseDto(
        UUID eventoId,
        Long reservaId,
        Long leitorId,
        String emailLeitor,
        String mensagem,
        LocalDateTime processadaEm) {
}
