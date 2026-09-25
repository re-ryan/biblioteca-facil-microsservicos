package br.com.infnet.bibliotecafacil.reserva.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReservaRequestDto(
        @NotNull(message = "O leitor é obrigatório.")
        @Positive(message = "O identificador do leitor deve ser positivo.")
        Long leitorId,
        @NotNull(message = "O acervo é obrigatório.")
        @Positive(message = "O identificador do acervo deve ser positivo.")
        Long acervoId) {
}
