package br.com.infnet.bibliotecafacil.usuario.aplicacao.command;

import br.com.infnet.bibliotecafacil.usuario.dominio.TipoUsuario;
import java.time.LocalDate;

public record UsuarioCommand(
        String nomeCompleto,
        LocalDate dataNascimento,
        String login,
        String email,
        String senhaHash,
        TipoUsuario tipoUsuario,
        Long bibliotecaId) {
}
