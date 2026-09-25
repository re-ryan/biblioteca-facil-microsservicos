package br.com.infnet.bibliotecafacil.catalogo.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record LivroRequestDto(
        @NotBlank(message = "O título do livro é obrigatório.") String titulo,
        String isbn10,
        String isbn13,
        String editora,
        @Positive(message = "O ano de publicação deve ser positivo.") Integer anoPublicacao,
        String edicao,
        String descricao,
        String urlImagemCapa) {
}
