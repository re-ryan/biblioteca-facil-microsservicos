package br.com.infnet.consultaisbn.api.dto;

import java.time.LocalDateTime;

public record ErroApi(LocalDateTime momento, int status, String mensagem) {
}
