package br.com.infnet.bibliotecafacil.catalogo.api.dto;

import br.com.infnet.bibliotecafacil.catalogo.aplicacao.dto.LivroCatalogo;
import java.util.List;

public record LivroCatalogoResponseDto(
        Long id,
        String titulo,
        String isbn13,
        String editora,
        Integer anoPublicacao,
        String urlImagemCapa,
        List<String> autores,
        List<String> categorias,
        List<DisponibilidadeBibliotecaResponseDto> bibliotecas) {

    public static LivroCatalogoResponseDto de(final LivroCatalogo livro) {
        return new LivroCatalogoResponseDto(
                livro.id(),
                livro.titulo(),
                livro.isbn13(),
                livro.editora(),
                livro.anoPublicacao(),
                livro.urlImagemCapa(),
                livro.autores(),
                livro.categorias(),
                livro.bibliotecas().stream()
                        .map(biblioteca -> new DisponibilidadeBibliotecaResponseDto(
                                biblioteca.id(),
                                biblioteca.nome(),
                                biblioteca.quantidadeDisponivel()))
                        .toList());
    }

    public record DisponibilidadeBibliotecaResponseDto(
            Long id,
            String nome,
            int quantidadeDisponivel) {
    }
}
