package br.com.infnet.consultaisbn.api.controller;

import br.com.infnet.consultaisbn.api.dto.ErroApi;
import br.com.infnet.consultaisbn.aplicacao.exception.IsbnNaoEncontradoException;
import br.com.infnet.consultaisbn.aplicacao.exception.ProvedorBibliograficoIndisponivelException;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public final class ApiExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroApi> tratarIsbnInvalido(final ConstraintViolationException exception) {
        return this.criarResposta(HttpStatus.BAD_REQUEST, "O ISBN-13 informado é inválido.");
    }

    @ExceptionHandler(IsbnNaoEncontradoException.class)
    public ResponseEntity<ErroApi> tratarNaoEncontrado(final IsbnNaoEncontradoException exception) {
        return this.criarResposta(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(ProvedorBibliograficoIndisponivelException.class)
    public ResponseEntity<ErroApi> tratarProvedorIndisponivel(
            final ProvedorBibliograficoIndisponivelException exception) {
        return this.criarResposta(HttpStatus.BAD_GATEWAY, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroApi> tratarErroInesperado(final Exception exception) {
        return this.criarResposta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno inesperado.");
    }

    private ResponseEntity<ErroApi> criarResposta(final HttpStatus status, final String mensagem) {
        return ResponseEntity.status(status)
                .body(new ErroApi(LocalDateTime.now(), status.value(), mensagem));
    }
}
