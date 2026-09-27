package br.com.infnet.consultaisbn.aplicacao.exception;

public final class IsbnNaoEncontradoException extends RuntimeException {

    public IsbnNaoEncontradoException(final String mensagem) {
        super(mensagem);
    }
}
