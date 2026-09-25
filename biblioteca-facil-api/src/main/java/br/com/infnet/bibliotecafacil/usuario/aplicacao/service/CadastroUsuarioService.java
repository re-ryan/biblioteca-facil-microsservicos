package br.com.infnet.bibliotecafacil.usuario.aplicacao.service;

import br.com.infnet.bibliotecafacil.biblioteca.aplicacao.service.BibliotecaService;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.DadosInvalidosException;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.OperacaoNaoPermitidaException;
import br.com.infnet.bibliotecafacil.usuario.aplicacao.command.UsuarioCommand;
import br.com.infnet.bibliotecafacil.usuario.dominio.Administrador;
import br.com.infnet.bibliotecafacil.usuario.dominio.Bibliotecario;
import br.com.infnet.bibliotecafacil.usuario.dominio.Leitor;
import br.com.infnet.bibliotecafacil.usuario.dominio.TipoUsuario;
import br.com.infnet.bibliotecafacil.usuario.dominio.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CadastroUsuarioService {

    private final UsuarioService usuarioService;
    private final BibliotecaService bibliotecaService;

    public CadastroUsuarioService(
            final UsuarioService usuarioService,
            final BibliotecaService bibliotecaService) {
        this.usuarioService = usuarioService;
        this.bibliotecaService = bibliotecaService;
    }

    public Usuario incluir(final UsuarioCommand command) {
        final Usuario usuario = this.criarUsuario(command.tipoUsuario());
        this.preencherUsuario(usuario, command);
        return this.usuarioService.incluir(usuario);
    }

    public Usuario alterar(final Long id, final UsuarioCommand command) {
        final Usuario usuario = this.usuarioService.obterPorId(id);
        this.validarTipoImutavel(usuario, command.tipoUsuario());
        this.preencherUsuario(usuario, command);
        return this.usuarioService.alterar(usuario);
    }

    private Usuario criarUsuario(final TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            throw new DadosInvalidosException("O tipo do usuário é obrigatório.");
        }
        return switch (tipoUsuario) {
            case LEITOR -> new Leitor();
            case BIBLIOTECARIO -> new Bibliotecario();
            case ADMINISTRADOR -> new Administrador();
        };
    }

    private void preencherUsuario(final Usuario usuario, final UsuarioCommand command) {
        usuario.setNomeCompleto(command.nomeCompleto());
        usuario.setDataNascimento(command.dataNascimento());
        usuario.setLogin(command.login());
        usuario.setEmail(command.email());
        usuario.setSenhaHash(command.senhaHash());
        usuario.setTipoUsuario(command.tipoUsuario());
        if (usuario instanceof Bibliotecario bibliotecario) {
            this.vincularBiblioteca(bibliotecario, command.bibliotecaId());
        }
    }

    private void vincularBiblioteca(final Bibliotecario bibliotecario, final Long bibliotecaId) {
        if (bibliotecaId == null) {
            throw new DadosInvalidosException("A biblioteca do bibliotecário é obrigatória.");
        }
        bibliotecario.setBiblioteca(this.bibliotecaService.obterPorId(bibliotecaId));
    }

    private void validarTipoImutavel(final Usuario usuario, final TipoUsuario tipoInformado) {
        if (tipoInformado == null) {
            throw new DadosInvalidosException("O tipo do usuário é obrigatório.");
        }
        if (usuario.getTipoUsuario() != tipoInformado) {
            throw new OperacaoNaoPermitidaException("O tipo do usuário não pode ser alterado.");
        }
    }
}
