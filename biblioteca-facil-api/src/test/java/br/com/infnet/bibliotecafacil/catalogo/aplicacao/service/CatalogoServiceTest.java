package br.com.infnet.bibliotecafacil.catalogo.aplicacao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.infnet.bibliotecafacil.biblioteca.dominio.Acervo;
import br.com.infnet.bibliotecafacil.biblioteca.dominio.Biblioteca;
import br.com.infnet.bibliotecafacil.biblioteca.infraestrutura.repository.BibliotecaRepository;
import br.com.infnet.bibliotecafacil.catalogo.aplicacao.dto.LivroCatalogo;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Autor;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Autoria;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Categoria;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;
import br.com.infnet.bibliotecafacil.catalogo.infraestrutura.repository.AutorRepository;
import br.com.infnet.bibliotecafacil.catalogo.infraestrutura.repository.CategoriaRepository;
import br.com.infnet.bibliotecafacil.catalogo.infraestrutura.repository.LivroRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(CatalogoService.class)
class CatalogoServiceTest {

    @Autowired
    private CatalogoService catalogoService;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    @BeforeEach
    void prepararCatalogo() {
        final Livro domCasmurro = this.criarLivro(
                "Dom Casmurro",
                "9788535902778",
                "Machado de Assis",
                "Romance");
        final Livro domainDrivenDesign = this.criarLivro(
                "Domain-Driven Design",
                "9780321125217",
                "Eric Evans",
                "Tecnologia");

        this.criarAcervo("Biblioteca Central", "12345678000199", domCasmurro, 3, true);
        this.criarAcervo("Biblioteca do Bairro", "98765432000188", domCasmurro, 0, true);
        this.criarAcervo("Biblioteca Tecnologia", "11222333000177", domainDrivenDesign, 2, true);
        this.criarAcervo("Biblioteca Inativa", "11222333000166", domCasmurro, 4, false);
    }

    @Test
    void deveListarLivrosAgrupadosComBibliotecasEEstoque() {
        final List<LivroCatalogo> resultado = this.catalogoService.buscar(null, null, null);

        assertEquals(2, resultado.size());
        final LivroCatalogo domCasmurro = resultado.stream()
                .filter(livro -> livro.titulo().equals("Dom Casmurro"))
                .findFirst()
                .orElseThrow();
        assertEquals("Dom Casmurro", domCasmurro.titulo());
        assertEquals(List.of("Machado de Assis"), domCasmurro.autores());
        assertEquals(List.of("Romance"), domCasmurro.categorias());
        assertEquals(2, domCasmurro.bibliotecas().size());
        assertEquals("Biblioteca Central", domCasmurro.bibliotecas().getFirst().nome());
        assertEquals(3, domCasmurro.bibliotecas().getFirst().quantidadeDisponivel());
        assertEquals(0, domCasmurro.bibliotecas().getLast().quantidadeDisponivel());
    }

    @Test
    void deveCombinarFiltrosIgnorandoMaiusculasEEspacosExternos() {
        final List<LivroCatalogo> resultado = this.catalogoService.buscar(
                "  dom cas ",
                "MACHADO",
                "romance");

        assertEquals(1, resultado.size());
        assertEquals("Dom Casmurro", resultado.getFirst().titulo());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverCorrespondencia() {
        assertEquals(
                List.of(),
                this.catalogoService.buscar(null, "Autor inexistente", null));
    }

    private Livro criarLivro(
            final String titulo,
            final String isbn13,
            final String nomeAutor,
            final String nomeCategoria) {
        final Autor autor = new Autor();
        autor.setNome(nomeAutor);
        autor.setNomeCatalogacao(nomeAutor);
        this.autorRepository.saveAndFlush(autor);

        final Categoria categoria = new Categoria();
        categoria.setNome(nomeCategoria);
        this.categoriaRepository.saveAndFlush(categoria);

        final Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setIsbn13(isbn13);
        livro.setCategorias(List.of(categoria));

        final Autoria autoria = new Autoria();
        autoria.setLivro(livro);
        autoria.setAutor(autor);
        autoria.setOrdem(1);
        livro.setAutorias(List.of(autoria));
        return this.livroRepository.saveAndFlush(livro);
    }

    private void criarAcervo(
            final String nomeBiblioteca,
            final String cpfCnpj,
            final Livro livro,
            final int quantidadeDisponivel,
            final boolean ativa) {
        final Biblioteca biblioteca = new Biblioteca();
        biblioteca.setNome(nomeBiblioteca);
        biblioteca.setCpfCnpj(cpfCnpj);
        biblioteca.setEmail(cpfCnpj + "@biblioteca.com");
        biblioteca.setAtiva(ativa);

        final Acervo acervo = new Acervo();
        acervo.setBiblioteca(biblioteca);
        acervo.setLivro(livro);
        acervo.setQuantidadeReal(Math.max(1, quantidadeDisponivel));
        acervo.setQuantidadeDisponivel(quantidadeDisponivel);
        biblioteca.setAcervos(List.of(acervo));
        this.bibliotecaRepository.saveAndFlush(biblioteca);
    }
}
