package br.com.infnet.bibliotecafacil.usuario.api.dto;

import br.com.infnet.bibliotecafacil.usuario.dominio.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record UsuarioRequestDto(
        @NotBlank(message = "O nome completo do usuário é obrigatório.") String nomeCompleto,
        @Past(message = "A data de nascimento deve estar no passado.") LocalDate dataNascimento,
        @NotBlank(message = "O login do usuário é obrigatório.") String login,
        @NotBlank(message = "O e-mail do usuário é obrigatório.")
        @Email(message = "O e-mail do usuário deve ser válido.")
        String email,
        @NotBlank(message = "O hash da senha do usuário é obrigatório.") String senhaHash,
        @NotNull(message = "O tipo do usuário é obrigatório.") TipoUsuario tipoUsuario,
        @Positive(message = "O identificador da biblioteca deve ser positivo.") Long bibliotecaId) {
}
