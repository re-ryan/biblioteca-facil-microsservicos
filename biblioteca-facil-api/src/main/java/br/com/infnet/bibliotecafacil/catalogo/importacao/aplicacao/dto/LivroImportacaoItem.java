package br.com.infnet.bibliotecafacil.catalogo.importacao.aplicacao.dto;

public record LivroImportacaoItem(
        String titulo,
        String isbn13,
        String editora,
        String anoPublicacao,
        String edicao) {
}
