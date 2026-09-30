package br.com.infnet.bibliotecafacil.catalogo.api.dto;

import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;
import java.time.LocalDateTime;

public record LivroResponseDto(
        Long id,
        String titulo,
        String isbn10,
        String isbn13,
        String editora,
        Integer anoPublicacao,
        String edicao,
        String descricao,
        String urlImagemCapa,
        boolean ativo,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao) {

    public static LivroResponseDto de(final Livro livro) {
        return new LivroResponseDto(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn10(),
                livro.getIsbn13(),
                livro.getEditora(),
                livro.getAnoPublicacao(),
                livro.getEdicao(),
                livro.getDescricao(),
                livro.getUrlImagemCapa(),
                livro.isAtivo(),
                livro.getDataCriacao(),
                livro.getDataAtualizacao());
    }
}
