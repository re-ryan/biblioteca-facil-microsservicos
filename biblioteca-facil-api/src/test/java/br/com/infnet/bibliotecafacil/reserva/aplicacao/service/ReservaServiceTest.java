package br.com.infnet.bibliotecafacil.reserva.aplicacao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.infnet.bibliotecafacil.biblioteca.aplicacao.service.AcervoService;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.OperacaoNaoPermitidaException;
import br.com.infnet.bibliotecafacil.biblioteca.dominio.Acervo;
import br.com.infnet.bibliotecafacil.biblioteca.dominio.Biblioteca;
import br.com.infnet.bibliotecafacil.usuario.dominio.Bibliotecario;
import br.com.infnet.bibliotecafacil.biblioteca.dominio.Endereco;
import br.com.infnet.bibliotecafacil.usuario.dominio.Leitor;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;
import br.com.infnet.bibliotecafacil.reserva.dominio.Reserva;
import br.com.infnet.bibliotecafacil.reserva.dominio.StatusReserva;
import br.com.infnet.bibliotecafacil.usuario.dominio.TipoUsuario;
import br.com.infnet.bibliotecafacil.biblioteca.infraestrutura.repository.BibliotecaRepository;
import br.com.infnet.bibliotecafacil.catalogo.infraestrutura.repository.LivroRepository;
import br.com.infnet.bibliotecafacil.usuario.infraestrutura.repository.UsuarioRepository;
import br.com.infnet.bibliotecafacil.usuario.aplicacao.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({ReservaService.class, AcervoService.class, UsuarioService.class})
class ReservaServiceTest {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void deveCriarReservaPendenteEReduzirDisponibilidade() {
        final Acervo acervo = this.criarAcervo(2);
        final Leitor leitor = this.criarLeitor();

        final Reserva reserva = this.reservaService.solicitar(leitor.getId(), acervo.getId());

        assertEquals(StatusReserva.PENDENTE, reserva.getStatus());
        assertEquals(1, acervo.getQuantidadeDisponivel());
        assertNotNull(reserva.getDataReserva());
    }

    @Test
    public void naoDeveReservarSemDisponibilidade() {
        final Acervo acervo = this.criarAcervo(0);
        final Leitor leitor = this.criarLeitor();

        assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> this.reservaService.solicitar(leitor.getId(), acervo.getId()));
        assertEquals(0, acervo.getQuantidadeDisponivel());
    }

    @Test
    public void leitorInativoNaoDeveReservar() {
        final Acervo acervo = this.criarAcervo(1);
        final Leitor leitor = this.criarLeitor();
        leitor.setAtivo(false);

        assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> this.reservaService.solicitar(leitor.getId(), acervo.getId()));
        assertEquals(1, acervo.getQuantidadeDisponivel());
    }

    @Test
    public void bibliotecaInativaNaoDevePermitirReserva() {
        final Acervo acervo = this.criarAcervo(1);
        final Leitor leitor = this.criarLeitor();
        acervo.getBiblioteca().setAtiva(false);

        assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> this.reservaService.solicitar(leitor.getId(), acervo.getId()));
    }

    @Test
    public void acervoInativoNaoDevePermitirReserva() {
        final Acervo acervo = this.criarAcervo(1);
        final Leitor leitor = this.criarLeitor();
        acervo.setAtivo(false);

        assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> this.reservaService.solicitar(leitor.getId(), acervo.getId()));
    }

    @Test
    public void livroInativoNaoDevePermitirReserva() {
        final Acervo acervo = this.criarAcervo(1);
        final Leitor leitor = this.criarLeitor();
        acervo.getLivro().setAtivo(false);

        assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> this.reservaService.solicitar(leitor.getId(), acervo.getId()));
    }

    @Test
    public void leitorNaoDeveDuplicarReservaPendenteParaMesmoAcervo() {
        final Acervo acervo = this.criarAcervo(2);
        final Leitor leitor = this.criarLeitor();
        this.reservaService.solicitar(leitor.getId(), acervo.getId());

        assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> this.reservaService.solicitar(leitor.getId(), acervo.getId()));
        assertEquals(1, acervo.getQuantidadeDisponivel());
    }

    @Test
    public void deveConfirmarReservaPendente() {
        final Acervo acervo = this.criarAcervo(1);
        final Leitor leitor = this.criarLeitor();
        final Bibliotecario bibliotecario = this.criarBibliotecario(acervo.getBiblioteca());
        final Reserva reserva = this.reservaService.solicitar(leitor.getId(), acervo.getId());

        final Reserva confirmada = this.reservaService.confirmar(reserva.getId(), bibliotecario.getId());

        assertEquals(StatusReserva.CONFIRMADA, confirmada.getStatus());
        assertEquals(0, acervo.getQuantidadeDisponivel());
    }

    @Test
    public void deveLiberarUnidadeAoRejeitarReserva() {
        final Acervo acervo = this.criarAcervo(1);
        final Leitor leitor = this.criarLeitor();
        final Bibliotecario bibliotecario = this.criarBibliotecario(acervo.getBiblioteca());
        final Reserva reserva = this.reservaService.solicitar(leitor.getId(), acervo.getId());

        final Reserva rejeitada = this.reservaService.rejeitar(reserva.getId(), bibliotecario.getId());

        assertEquals(StatusReserva.REJEITADA, rejeitada.getStatus());
        assertEquals(1, acervo.getQuantidadeDisponivel());
    }

    @Test
    public void bibliotecarioDeOutraBibliotecaNaoDeveProcessarReserva() {
        final Acervo acervo = this.criarAcervo(1);
        final Leitor leitor = this.criarLeitor();
        final Reserva reserva = this.reservaService.solicitar(leitor.getId(), acervo.getId());
        final Bibliotecario bibliotecario = this.criarBibliotecario(
                this.criarBiblioteca("Biblioteca Bairro", "98765432000188").getBiblioteca());

        assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> this.reservaService.confirmar(reserva.getId(), bibliotecario.getId()));
        assertEquals(StatusReserva.PENDENTE, reserva.getStatus());
    }

    @Test
    public void naoDeveProcessarReservaDuasVezes() {
        final Acervo acervo = this.criarAcervo(1);
        final Leitor leitor = this.criarLeitor();
        final Bibliotecario bibliotecario = this.criarBibliotecario(acervo.getBiblioteca());
        final Reserva reserva = this.reservaService.solicitar(leitor.getId(), acervo.getId());
        this.reservaService.confirmar(reserva.getId(), bibliotecario.getId());

        assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> this.reservaService.rejeitar(reserva.getId(), bibliotecario.getId()));
        assertEquals(0, acervo.getQuantidadeDisponivel());
    }

    private Acervo criarAcervo(final int quantidadeDisponivel) {
        return this.criarBiblioteca(
                "Biblioteca Central",
                "12345678000199",
                quantidadeDisponivel);
    }

    private Acervo criarBiblioteca(final String nome, final String cpfCnpj) {
        return this.criarBiblioteca(nome, cpfCnpj, 1);
    }

    private Acervo criarBiblioteca(
            final String nome,
            final String cpfCnpj,
            final int quantidadeDisponivel) {
        final String isbn13 = "12345678000199".equals(cpfCnpj)
                ? "9788532508126"
                : "9788535902778";
        final Livro livro = this.criarLivro(isbn13);
        final Biblioteca biblioteca = new Biblioteca();
        biblioteca.setNome(nome);
        biblioteca.setCpfCnpj(cpfCnpj);
        biblioteca.setEmail(cpfCnpj + "@biblioteca.com");
        biblioteca.setEndereco(this.criarEndereco());
        final Acervo acervo = new Acervo();
        acervo.setBiblioteca(biblioteca);
        acervo.setLivro(livro);
        acervo.setQuantidadeReal(Math.max(1, quantidadeDisponivel));
        acervo.setQuantidadeDisponivel(quantidadeDisponivel);
        biblioteca.setAcervos(java.util.List.of(acervo));
        this.bibliotecaRepository.saveAndFlush(biblioteca);
        return acervo;
    }

    private Livro criarLivro(final String isbn13) {
        final Livro livro = new Livro();
        livro.setTitulo("Dom Casmurro");
        livro.setIsbn13(isbn13);
        return this.livroRepository.saveAndFlush(livro);
    }

    private Endereco criarEndereco() {
        final Endereco endereco = new Endereco();
        endereco.setCep("20040020");
        endereco.setLogradouro("Rua Principal");
        endereco.setNumero("10");
        endereco.setBairro("Centro");
        endereco.setCidade("Rio de Janeiro");
        endereco.setUf("RJ");
        return endereco;
    }

    private Leitor criarLeitor() {
        final Leitor leitor = new Leitor();
        leitor.setNomeCompleto("Ana Souza");
        leitor.setLogin("ana.souza");
        leitor.setEmail("ana@email.com");
        leitor.setSenhaHash("hash-seguro-ana");
        leitor.setTipoUsuario(TipoUsuario.LEITOR);
        return this.usuarioRepository.saveAndFlush(leitor);
    }

    private Bibliotecario criarBibliotecario(final Biblioteca biblioteca) {
        final Bibliotecario bibliotecario = new Bibliotecario();
        bibliotecario.setNomeCompleto("Carlos Lima");
        bibliotecario.setLogin("carlos.lima" + biblioteca.getCpfCnpj());
        bibliotecario.setEmail("carlos" + biblioteca.getCpfCnpj() + "@biblioteca.com");
        bibliotecario.setSenhaHash("hash-seguro-carlos");
        bibliotecario.setTipoUsuario(TipoUsuario.BIBLIOTECARIO);
        bibliotecario.setBiblioteca(biblioteca);
        return this.usuarioRepository.saveAndFlush(bibliotecario);
    }
}
