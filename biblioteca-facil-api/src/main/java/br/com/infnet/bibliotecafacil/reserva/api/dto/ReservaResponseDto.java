package br.com.infnet.bibliotecafacil.reserva.api.dto;

import br.com.infnet.bibliotecafacil.reserva.dominio.StatusReserva;
import java.time.LocalDateTime;

public record ReservaResponseDto(
        Long id,
        Long leitorId,
        String leitorNome,
        Long acervoId,
        Long bibliotecaId,
        String bibliotecaNome,
        Long livroId,
        String livroTitulo,
        LocalDateTime dataReserva,
        StatusReserva status) {
}
