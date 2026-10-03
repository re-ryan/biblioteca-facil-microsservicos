package br.com.infnet.bibliotecafacil.reserva.aplicacao.evento;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservaSolicitadaEvent(
        UUID eventoId,
        String tipo,
        Long reservaId,
        Long leitorId,
        String emailLeitor,
        String tituloLivro,
        String nomeBiblioteca,
        LocalDateTime ocorridaEm) {
}
