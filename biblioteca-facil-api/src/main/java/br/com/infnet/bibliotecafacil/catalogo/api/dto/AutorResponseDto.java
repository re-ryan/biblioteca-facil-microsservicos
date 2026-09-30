package br.com.infnet.bibliotecafacil.catalogo.api.dto;

import br.com.infnet.bibliotecafacil.catalogo.dominio.Autor;
import java.time.LocalDateTime;

public record AutorResponseDto(
        Long id,
        String nome,
        String nomeCatalogacao,
        boolean ativo,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao) {

    public static AutorResponseDto de(final Autor autor) {
        return new AutorResponseDto(
                autor.getId(),
                autor.getNome(),
                autor.getNomeCatalogacao(),
                autor.isAtivo(),
                autor.getDataCriacao(),
                autor.getDataAtualizacao());
    }
}
