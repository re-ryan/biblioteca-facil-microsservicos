package br.com.infnet.bibliotecafacil.catalogo.api.dto;

import br.com.infnet.bibliotecafacil.catalogo.dominio.Categoria;
import java.time.LocalDateTime;

public record CategoriaResponseDto(
        Long id,
        String nome,
        String descricao,
        boolean ativa,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao) {

    public static CategoriaResponseDto de(final Categoria categoria) {
        return new CategoriaResponseDto(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.isAtiva(),
                categoria.getDataCriacao(),
                categoria.getDataAtualizacao());
    }
}
