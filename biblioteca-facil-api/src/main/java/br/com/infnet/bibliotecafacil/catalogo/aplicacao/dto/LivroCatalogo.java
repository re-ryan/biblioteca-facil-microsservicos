package br.com.infnet.bibliotecafacil.catalogo.aplicacao.dto;

import java.util.List;

public record LivroCatalogo(
        Long id,
        String titulo,
        String isbn13,
        String editora,
        Integer anoPublicacao,
        String urlImagemCapa,
        List<String> autores,
        List<String> categorias,
        List<DisponibilidadeBiblioteca> bibliotecas) {

    public LivroCatalogo {
        autores = List.copyOf(autores);
        categorias = List.copyOf(categorias);
        bibliotecas = List.copyOf(bibliotecas);
    }

    public record DisponibilidadeBiblioteca(
            Long id,
            String nome,
            int quantidadeDisponivel) {
    }
}
