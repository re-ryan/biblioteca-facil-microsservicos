package br.com.infnet.notificacao.mensageria;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservaSolicitadaMessage(
        UUID eventoId,
        String tipo,
        Long reservaId,
        Long leitorId,
        String emailLeitor,
        String tituloLivro,
        String nomeBiblioteca,
        LocalDateTime ocorridaEm) {
}
