package br.com.infnet.bibliotecafacil.catalogo.importacao.api.dto;

public record ImportacaoLivrosResponseDto(
        Long execucaoId,
        String status,
        long registrosLidos,
        long registrosImportados,
        long registrosFiltrados) {
}
