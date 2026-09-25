package br.com.infnet.bibliotecafacil.usuario.aplicacao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.infnet.bibliotecafacil.biblioteca.aplicacao.service.BibliotecaService;
import br.com.infnet.bibliotecafacil.biblioteca.dominio.Biblioteca;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.DadosInvalidosException;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.OperacaoNaoPermitidaException;
import br.com.infnet.bibliotecafacil.usuario.aplicacao.command.UsuarioCommand;
import br.com.infnet.bibliotecafacil.usuario.dominio.Bibliotecario;
import br.com.infnet.bibliotecafacil.usuario.dominio.Leitor;
import br.com.infnet.bibliotecafacil.usuario.dominio.TipoUsuario;
import br.com.infnet.bibliotecafacil.usuario.dominio.Usuario;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CadastroUsuarioServiceTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private BibliotecaService bibliotecaService;

    private CadastroUsuarioService cadastroUsuarioService;

    @BeforeEach
    void configurar() {
        this.cadastroUsuarioService = new CadastroUsuarioService(
                this.usuarioService,
                this.bibliotecaService);
    }

    @Test
    void deveCriarLeitorComOsDadosInformados() {
        when(this.usuarioService.incluir(any(Usuario.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        final Usuario usuario = this.cadastroUsuarioService.incluir(
                this.criarCommand(TipoUsuario.LEITOR, null));

        assertInstanceOf(Leitor.class, usuario);
        assertEquals("Ana Souza", usuario.getNomeCompleto());
        assertEquals(TipoUsuario.LEITOR, usuario.getTipoUsuario());
    }

    @Test
    void deveVincularBibliotecarioAUmaBibliotecaExistente() {
        final Biblioteca biblioteca = new Biblioteca();
        biblioteca.setId(10L);
        when(this.bibliotecaService.obterPorId(10L)).thenReturn(biblioteca);
        when(this.usuarioService.incluir(any(Usuario.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        final Usuario usuario = this.cadastroUsuarioService.incluir(
                this.criarCommand(TipoUsuario.BIBLIOTECARIO, 10L));

        final Bibliotecario bibliotecario = assertInstanceOf(Bibliotecario.class, usuario);
        assertSame(biblioteca, bibliotecario.getBiblioteca());
    }

    @Test
    void deveExigirBibliotecaParaBibliotecario() {
        final UsuarioCommand command = this.criarCommand(TipoUsuario.BIBLIOTECARIO, null);

        assertThrows(
                DadosInvalidosException.class,
                () -> this.cadastroUsuarioService.incluir(command));
        verify(this.usuarioService, never()).incluir(any(Usuario.class));
    }

    @Test
    void naoDeveAlterarOTipoDoUsuario() {
        final Leitor leitor = new Leitor();
        leitor.setTipoUsuario(TipoUsuario.LEITOR);
        when(this.usuarioService.obterPorId(1L)).thenReturn(leitor);

        final UsuarioCommand command = this.criarCommand(TipoUsuario.ADMINISTRADOR, null);

        assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> this.cadastroUsuarioService.alterar(1L, command));
        verify(this.usuarioService, never()).alterar(any(Usuario.class));
    }

    private UsuarioCommand criarCommand(final TipoUsuario tipoUsuario, final Long bibliotecaId) {
        return new UsuarioCommand(
                "Ana Souza",
                LocalDate.of(1990, 1, 1),
                "ana.souza",
                "ana@email.com",
                "hash-seguro",
                tipoUsuario,
                bibliotecaId);
    }
}
