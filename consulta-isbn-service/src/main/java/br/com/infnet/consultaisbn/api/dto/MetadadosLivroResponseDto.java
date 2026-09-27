package br.com.infnet.consultaisbn.api.dto;

public record MetadadosLivroResponseDto(
        String isbn,
        String titulo,
        String editora,
        String descricao,
        Integer anoPublicacao,
        String urlImagemCapa) {
}
