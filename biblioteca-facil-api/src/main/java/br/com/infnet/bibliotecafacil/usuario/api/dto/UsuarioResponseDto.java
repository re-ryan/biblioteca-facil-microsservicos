package br.com.infnet.bibliotecafacil.usuario.api.dto;

import br.com.infnet.bibliotecafacil.biblioteca.dominio.Biblioteca;
import br.com.infnet.bibliotecafacil.usuario.dominio.Bibliotecario;
import br.com.infnet.bibliotecafacil.usuario.dominio.TipoUsuario;
import br.com.infnet.bibliotecafacil.usuario.dominio.Usuario;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record UsuarioResponseDto(
        Long id,
        String nomeCompleto,
        LocalDate dataNascimento,
        String login,
        String email,
        TipoUsuario tipoUsuario,
        boolean ativo,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        Long bibliotecaId,
        String bibliotecaNome) {

    public static UsuarioResponseDto de(final Usuario usuario) {
        final Biblioteca biblioteca = usuario instanceof Bibliotecario bibliotecario
                ? bibliotecario.getBiblioteca()
                : null;
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNomeCompleto(),
                usuario.getDataNascimento(),
                usuario.getLogin(),
                usuario.getEmail(),
                usuario.getTipoUsuario(),
                usuario.isAtivo(),
                usuario.getDataCriacao(),
                usuario.getDataAtualizacao(),
                biblioteca == null ? null : biblioteca.getId(),
                biblioteca == null ? null : biblioteca.getNome());
    }
}
