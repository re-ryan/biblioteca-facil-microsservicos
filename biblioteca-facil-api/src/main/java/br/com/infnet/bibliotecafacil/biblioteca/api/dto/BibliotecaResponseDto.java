package br.com.infnet.bibliotecafacil.biblioteca.api.dto;

import br.com.infnet.bibliotecafacil.biblioteca.dominio.Biblioteca;
import java.time.LocalDateTime;

public record BibliotecaResponseDto(
        Long id,
        String nome,
        String cpfCnpj,
        String email,
        String telefone,
        EnderecoResponseDto endereco,
        boolean ativa,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao) {

    public static BibliotecaResponseDto de(final Biblioteca biblioteca) {
        return new BibliotecaResponseDto(
                biblioteca.getId(),
                biblioteca.getNome(),
                biblioteca.getCpfCnpj(),
                biblioteca.getEmail(),
                biblioteca.getTelefone(),
                EnderecoResponseDto.de(biblioteca.getEndereco()),
                biblioteca.isAtiva(),
                biblioteca.getDataCriacao(),
                biblioteca.getDataAtualizacao());
    }
}
