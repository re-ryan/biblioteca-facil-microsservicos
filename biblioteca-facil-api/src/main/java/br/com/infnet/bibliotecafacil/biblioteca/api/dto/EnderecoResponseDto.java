package br.com.infnet.bibliotecafacil.biblioteca.api.dto;

import br.com.infnet.bibliotecafacil.biblioteca.dominio.Endereco;
import java.time.LocalDateTime;

public record EnderecoResponseDto(
        Long id,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String uf,
        Double latitude,
        Double longitude,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao) {

    public static EnderecoResponseDto de(final Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        return new EnderecoResponseDto(
                endereco.getId(),
                endereco.getCep(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getUf(),
                endereco.getLatitude(),
                endereco.getLongitude(),
                endereco.getDataCriacao(),
                endereco.getDataAtualizacao());
    }
}
