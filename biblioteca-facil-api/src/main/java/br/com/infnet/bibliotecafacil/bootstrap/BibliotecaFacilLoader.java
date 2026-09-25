package br.com.infnet.bibliotecafacil.bootstrap;

import br.com.infnet.bibliotecafacil.catalogo.aplicacao.service.AutorService;
import br.com.infnet.bibliotecafacil.biblioteca.aplicacao.service.BibliotecaService;
import br.com.infnet.bibliotecafacil.catalogo.aplicacao.service.CategoriaService;
import br.com.infnet.bibliotecafacil.catalogo.aplicacao.service.LivroService;
import br.com.infnet.bibliotecafacil.reserva.aplicacao.service.ReservaService;
import br.com.infnet.bibliotecafacil.usuario.aplicacao.service.UsuarioService;
import br.com.infnet.bibliotecafacil.biblioteca.dominio.Acervo;
import br.com.infnet.bibliotecafacil.usuario.dominio.Administrador;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Autor;
import br.com.infnet.bibliotecafacil.biblioteca.dominio.Biblioteca;
import br.com.infnet.bibliotecafacil.usuario.dominio.Bibliotecario;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Categoria;
import br.com.infnet.bibliotecafacil.biblioteca.dominio.Endereco;
import br.com.infnet.bibliotecafacil.usuario.dominio.Leitor;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;
import br.com.infnet.bibliotecafacil.reserva.dominio.Reserva;
import br.com.infnet.bibliotecafacil.usuario.dominio.TipoUsuario;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BibliotecaFacilLoader implements CommandLineRunner {

    private final AutorService autorService;
    private final BibliotecaService bibliotecaService;
    private final CategoriaService categoriaService;
    private final LivroService livroService;
    private final ReservaService reservaService;
    private final UsuarioService usuarioService;

    public BibliotecaFacilLoader(
            final AutorService autorService,
            final BibliotecaService bibliotecaService,
            final CategoriaService categoriaService,
            final LivroService livroService,
            final ReservaService reservaService,
            final UsuarioService usuarioService) {
        this.autorService = autorService;
        this.bibliotecaService = bibliotecaService;
        this.categoriaService = categoriaService;
        this.livroService = livroService;
        this.reservaService = reservaService;
        this.usuarioService = usuarioService;
    }

    @Override
    @Transactional
    public void run(final String... args) {
        final Autor machadoDeAssis = new Autor();
        machadoDeAssis.setNome("Machado de Assis");
        machadoDeAssis.setNomeCatalogacao("ASSIS, Machado de");

        final Autor clariceLispector = new Autor();
        clariceLispector.setNome("Clarice Lispector");
        clariceLispector.setNomeCatalogacao("LISPECTOR, Clarice");
        this.autorService.incluir(machadoDeAssis);
        this.autorService.incluir(clariceLispector);

        final Categoria romance = new Categoria();
        romance.setNome("Romance");
        romance.setDescricao("Narrativas de ficção em prosa.");

        final Categoria literaturaBrasileira = new Categoria();
        literaturaBrasileira.setNome("Literatura brasileira");
        literaturaBrasileira.setDescricao("Obras publicadas por autores brasileiros.");
        this.categoriaService.incluir(romance);
        this.categoriaService.incluir(literaturaBrasileira);

        final Livro domCasmurro = new Livro();
        domCasmurro.setTitulo("Dom Casmurro");
        domCasmurro.setIsbn13("978-85-359-0277-8");
        domCasmurro.setEditora("Companhia das Letras");
        domCasmurro.setAnoPublicacao(1899);
        domCasmurro.setEdicao("1ª edição");
        domCasmurro.setDescricao("Romance de Machado de Assis.");
        this.livroService.adicionarAutor(domCasmurro, machadoDeAssis, 1);
        this.livroService.adicionarCategoria(domCasmurro, romance);
        this.livroService.adicionarCategoria(domCasmurro, literaturaBrasileira);

        final Livro aHoraDaEstrela = new Livro();
        aHoraDaEstrela.setTitulo("A Hora da Estrela");
        aHoraDaEstrela.setIsbn10("85-325-0812-X");
        aHoraDaEstrela.setIsbn13("9788532508126");
        aHoraDaEstrela.setEditora("Rocco");
        aHoraDaEstrela.setAnoPublicacao(1977);
        aHoraDaEstrela.setEdicao("1ª edição");
        aHoraDaEstrela.setDescricao("Romance de Clarice Lispector.");
        this.livroService.adicionarAutor(aHoraDaEstrela, clariceLispector, 1);
        this.livroService.adicionarCategoria(aHoraDaEstrela, romance);
        this.livroService.adicionarCategoria(aHoraDaEstrela, literaturaBrasileira);
        this.livroService.incluir(domCasmurro);
        this.livroService.incluir(aHoraDaEstrela);

        final Endereco endereco = new Endereco();
        endereco.setCep("22230060");
        endereco.setLogradouro("Rua Marquês de Abrantes");
        endereco.setNumero("55");
        endereco.setComplemento(null);
        endereco.setBairro("Flamengo");
        endereco.setCidade("Rio de Janeiro");
        endereco.setUf("RJ");
        endereco.setLatitude(-22.932337);
        endereco.setLongitude(-43.177787);

        final Biblioteca biblioteca = new Biblioteca();
        biblioteca.setNome("Biblioteca Parceira Flamengo");
        biblioteca.setCpfCnpj("12345678000199");
        biblioteca.setEmail("contato@bibliotecafacil.com");
        biblioteca.setTelefone("(21) 2222-3333");
        biblioteca.setEndereco(endereco);
        final Acervo acervoDomCasmurro = this.bibliotecaService.adicionarLivro(
                biblioteca, null, domCasmurro, 3);
        this.bibliotecaService.adicionarLivro(biblioteca, null, aHoraDaEstrela, 2);
        this.bibliotecaService.incluir(biblioteca);

        final Leitor leitor = new Leitor();
        leitor.setNomeCompleto("Ana Souza");
        leitor.setDataNascimento(LocalDate.of(1992, 5, 14));
        leitor.setLogin("ana.souza");
        leitor.setEmail("ana.souza@email.com");
        leitor.setSenhaHash("hash-seguro-ana");
        leitor.setTipoUsuario(TipoUsuario.LEITOR);

        final Bibliotecario bibliotecario = new Bibliotecario();
        bibliotecario.setNomeCompleto("Carlos Lima");
        bibliotecario.setDataNascimento(LocalDate.of(1985, 8, 20));
        bibliotecario.setLogin("carlos.lima");
        bibliotecario.setEmail("carlos.lima@biblioteca.com");
        bibliotecario.setSenhaHash("hash-seguro-carlos");
        bibliotecario.setTipoUsuario(TipoUsuario.BIBLIOTECARIO);
        bibliotecario.setBiblioteca(biblioteca);

        final Administrador administrador = new Administrador();
        administrador.setNomeCompleto("Marina Alves");
        administrador.setDataNascimento(null);
        administrador.setLogin("marina.alves");
        administrador.setEmail("marina@bibliotecafacil.com");
        administrador.setSenhaHash("hash-seguro-marina");
        administrador.setTipoUsuario(TipoUsuario.ADMINISTRADOR);
        this.usuarioService.incluir(leitor);
        this.usuarioService.incluir(bibliotecario);
        this.usuarioService.incluir(administrador);
        final Reserva reserva = this.reservaService.solicitar(leitor.getId(), acervoDomCasmurro.getId());

        this.apresentarBiblioteca(biblioteca);
        this.apresentarConsultas();
        System.out.println(leitor);
        System.out.println(bibliotecario);
        System.out.println(administrador);
        System.out.println(reserva);
        this.reservaService.confirmar(reserva.getId(), bibliotecario.getId());

        System.out.println("\nApós a confirmação:");
        System.out.println(reserva);
        System.out.println(acervoDomCasmurro);
    }

    private void apresentarBiblioteca(final Biblioteca biblioteca) {
        System.out.println("\n=== Biblioteca Fácil - Dados iniciais ===");
        System.out.println(biblioteca);
        for (final Acervo acervo : biblioteca.getAcervos()) {
            System.out.println(acervo);
            System.out.println(acervo.getLivro());
        }
    }

    private void apresentarConsultas() {
        System.out.println("\n=== Consultas Spring Data ===");
        System.out.println("Títulos cadastrados: " + this.livroService.listarTitulos());
        System.out.println("Livros encontrados por 'estrela': " + this.livroService.buscarPorTitulo("estrela"));
        System.out.println("Autores ordenados: " + this.autorService.listarOrdenadosPorNome());
        System.out.println("Categorias ativas: " + this.categoriaService.listarAtivas());
        System.out.println("Nomes das bibliotecas: " + this.bibliotecaService.listarNomes());
        System.out.println("Leitores: " + this.usuarioService.filtrarPorTipo(TipoUsuario.LEITOR));
    }
}
