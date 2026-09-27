package br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception;

public final class ServicoConsultaIsbnIndisponivelException extends RuntimeException {

    public ServicoConsultaIsbnIndisponivelException(final String mensagem) {
        super(mensagem);
    }
}
