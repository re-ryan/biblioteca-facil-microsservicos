package br.com.infnet.bibliotecafacil.reserva.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProcessamentoReservaRequestDto(
        @NotNull(message = "O bibliotecário é obrigatório.")
        @Positive(message = "O identificador do bibliotecário deve ser positivo.")
        Long bibliotecarioId) {
}
