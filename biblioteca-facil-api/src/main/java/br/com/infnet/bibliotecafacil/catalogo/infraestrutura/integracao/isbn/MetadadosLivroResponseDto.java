package br.com.infnet.bibliotecafacil.catalogo.infraestrutura.integracao.isbn;

public record MetadadosLivroResponseDto(
        String isbn,
        String titulo,
        String editora,
        String descricao,
        Integer anoPublicacao,
        String urlImagemCapa) {
}
