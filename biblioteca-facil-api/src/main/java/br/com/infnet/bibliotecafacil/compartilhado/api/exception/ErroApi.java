package br.com.infnet.bibliotecafacil.compartilhado.api.exception;

import java.time.LocalDateTime;

public record ErroApi(LocalDateTime momento, int status, String mensagem) {
}
