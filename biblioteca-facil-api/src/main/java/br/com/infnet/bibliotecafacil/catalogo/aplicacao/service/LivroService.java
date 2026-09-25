package br.com.infnet.bibliotecafacil.catalogo.aplicacao.service;

import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.DadosInvalidosException;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.ObjetoNaoEncontradoException;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.OperacaoNaoPermitidaException;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Autor;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Autoria;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Categoria;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;
import br.com.infnet.bibliotecafacil.catalogo.infraestrutura.repository.LivroRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LivroService {

    private static final Sort ORDENACAO_PADRAO = Sort.by(Sort.Direction.ASC, "titulo");

    private final LivroRepository livroRepository;

    public LivroService(final LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Transactional
    public Livro incluir(final Livro livro) {
        this.prepararIsbn(livro);
        this.validarLivro(livro);
        return this.livroRepository.save(livro);
    }

    @Transactional
    public Livro alterar(final Livro livro) {
        this.prepararIsbn(livro);
        this.validarLivro(livro);
        final Livro livroPersistido = this.obterPorId(livro.getId());
        this.copiarDados(livro, livroPersistido);
        return this.livroRepository.save(livroPersistido);
    }

    public void prepararIsbn(final Livro livro) {
        if (livro == null) {
            throw new DadosInvalidosException("O livro é obrigatório.");
        }

        final String isbn10 = this.possuiValor(livro.getIsbn10())
                ? this.normalizarIsbn(livro.getIsbn10())
                : null;
        final String isbn13 = this.possuiValor(livro.getIsbn13())
                ? this.normalizarIsbn(livro.getIsbn13())
                : null;
        this.validarIsbn(isbn10, isbn13);
        livro.setIsbn10(isbn10);
        livro.setIsbn13(isbn13 != null ? isbn13 : this.converterIsbn10ParaIsbn13(isbn10));
    }

    public void adicionarAutor(final Livro livro, final Autor autor, final int ordem) {
        if (livro == null) {
            throw new DadosInvalidosException("O livro é obrigatório.");
        }
        if (autor == null) {
            throw new DadosInvalidosException("O autor é obrigatório.");
        }
        if (ordem <= 0) {
            throw new DadosInvalidosException("A ordem de autoria deve ser positiva.");
        }
        if (livro.getAutorias().stream()
                .anyMatch(autoria -> Objects.equals(
                        autoria.getAutor().getId(), autor.getId()))) {
            throw new OperacaoNaoPermitidaException(
                    "O autor já está relacionado ao livro.");
        }
        if (livro.getAutorias().stream()
                .anyMatch(autoria -> autoria.getOrdem() == ordem)) {
            throw new OperacaoNaoPermitidaException(
                    "A ordem de autoria já está relacionada ao livro.");
        }

        final Autoria autoria = new Autoria();
        autoria.setLivro(livro);
        autoria.setAutor(autor);
        autoria.setOrdem(ordem);
        final List<Autoria> autorias = new ArrayList<>(livro.getAutorias());
        autorias.add(autoria);
        autorias.sort(Comparator.comparingInt(Autoria::getOrdem));
        livro.setAutorias(autorias);
        livro.setDataAtualizacao(LocalDateTime.now());
    }

    public void adicionarCategoria(final Livro livro, final Categoria categoria) {
        if (livro == null) {
            throw new DadosInvalidosException("O livro é obrigatório.");
        }
        if (categoria == null) {
            throw new DadosInvalidosException("A categoria é obrigatória.");
        }
        if (livro.getCategorias().stream()
                .anyMatch(categoriaAtual -> Objects.equals(
                        categoriaAtual.getId(), categoria.getId()))) {
            throw new OperacaoNaoPermitidaException(
                    "A categoria já está relacionada ao livro.");
        }

        final List<Categoria> categorias = new ArrayList<>(livro.getCategorias());
        categorias.add(categoria);
        livro.setCategorias(categorias);
        livro.setDataAtualizacao(LocalDateTime.now());
    }

    @Transactional
    public void excluir(final Long id) {
        final Livro livro = this.obterPorId(id);
        this.livroRepository.delete(livro);
    }

    public Livro obterPorId(final Long id) {
        if (id == null) {
            throw new DadosInvalidosException("O identificador do livro é obrigatório.");
        }
        return this.livroRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException("Livro não encontrado para o identificador %s.".formatted(id)));
    }

    public List<Livro> listar() {
        return List.copyOf(this.livroRepository.findAll());
    }

    public List<Livro> listarAtivos() {
        return this.livroRepository.findByAtivoTrue();
    }

    public List<Livro> buscarPorTitulo(final String titulo) {
        return this.buscarPorTitulo(titulo, ORDENACAO_PADRAO);
    }

    public List<Livro> buscarPorTitulo(final String titulo, final Sort ordenacao) {
        this.validarTextoDeBusca(titulo);
        return this.livroRepository.findByTituloContainingIgnoreCase(titulo, ordenacao);
    }

    public List<Livro> listarOrdenadosPorTitulo() {
        return this.livroRepository.findAll(ORDENACAO_PADRAO);
    }

    public List<String> listarTitulos() {
        return this.livroRepository.findAll().stream()
                .map(Livro::getTitulo)
                .toList();
    }

    private void validarLivro(final Livro livro) {
        if (livro == null) {
            throw new DadosInvalidosException("O livro é obrigatório.");
        }
        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new DadosInvalidosException("O título do livro é obrigatório.");
        }
        if (livro.getIsbn13() == null || livro.getIsbn13().isBlank()) {
            throw new DadosInvalidosException("O ISBN-13 do livro é obrigatório.");
        }

        final boolean isbn13EmUso = livro.getId() == null
                ? this.livroRepository.existsByIsbn13(livro.getIsbn13())
                : this.livroRepository.existsByIsbn13AndIdNot(livro.getIsbn13(), livro.getId());
        if (isbn13EmUso) {
            throw new OperacaoNaoPermitidaException("Já existe um livro com o ISBN-13 informado.");
        }

        if (livro.getIsbn10() == null) {
            return;
        }
        final boolean isbn10EmUso = livro.getId() == null
                ? this.livroRepository.existsByIsbn10(livro.getIsbn10())
                : this.livroRepository.existsByIsbn10AndIdNot(livro.getIsbn10(), livro.getId());
        if (isbn10EmUso) {
            throw new OperacaoNaoPermitidaException("Já existe um livro com o ISBN-10 informado.");
        }
    }

    private void copiarDados(final Livro origem, final Livro destino) {
        destino.setTitulo(origem.getTitulo());
        destino.setIsbn10(origem.getIsbn10());
        destino.setIsbn13(origem.getIsbn13());
        destino.setEditora(origem.getEditora());
        destino.setAnoPublicacao(origem.getAnoPublicacao());
        destino.setEdicao(origem.getEdicao());
        destino.setDescricao(origem.getDescricao());
        destino.setUrlImagemCapa(origem.getUrlImagemCapa());
        destino.setDataAtualizacao(LocalDateTime.now());
    }

    private void validarIsbn(final String isbn10, final String isbn13) {
        if (isbn10 == null && isbn13 == null) {
            throw new DadosInvalidosException(
                    "O ISBN-13 ou o ISBN-10 deve ser informado.");
        }
        if (isbn10 != null && (!isbn10.matches("\\d{9}[\\dX]")
                || this.calcularSomaPonderadaIsbn10(isbn10) % 11 != 0)) {
            throw new DadosInvalidosException("O ISBN-10 informado é inválido.");
        }
        if (isbn13 != null) {
            if (!isbn13.matches("97[89]\\d{10}")) {
                throw new DadosInvalidosException("O ISBN-13 informado é inválido.");
            }
            final int digitoEsperado = this.calcularDigitoVerificadorIsbn13(
                    isbn13.substring(0, 12));
            final int digitoInformado = Character.digit(isbn13.charAt(12), 10);
            if (digitoInformado != digitoEsperado) {
                throw new DadosInvalidosException("O ISBN-13 informado é inválido.");
            }
        }
        if (isbn10 != null && isbn13 != null
                && !this.converterIsbn10ParaIsbn13(isbn10).equals(isbn13)) {
            throw new DadosInvalidosException(
                    "O ISBN-10 e o ISBN-13 informados não são equivalentes.");
        }
    }

    private boolean possuiValor(final String isbn) {
        return isbn != null && !isbn.isBlank();
    }

    private String normalizarIsbn(final String isbn) {
        return isbn.replace("-", "").replace(" ", "").toUpperCase(Locale.ROOT);
    }

    private String converterIsbn10ParaIsbn13(final String isbn10) {
        final String baseIsbn13 = "978" + isbn10.substring(0, 9);
        return baseIsbn13 + this.calcularDigitoVerificadorIsbn13(baseIsbn13);
    }

    private int calcularDigitoVerificadorIsbn13(final String baseIsbn13) {
        int soma = 0;
        for (int indice = 0; indice < 12; indice++) {
            final int digito = Character.digit(baseIsbn13.charAt(indice), 10);
            final int peso = indice % 2 == 0 ? 1 : 3;
            soma += digito * peso;
        }
        return (10 - soma % 10) % 10;
    }

    private int calcularSomaPonderadaIsbn10(final String isbn10) {
        int soma = 0;
        for (int indice = 0; indice < 10; indice++) {
            final char caractere = isbn10.charAt(indice);
            final int digito = caractere == 'X' ? 10 : Character.digit(caractere, 10);
            soma += digito * (10 - indice);
        }
        return soma;
    }

    private void validarTextoDeBusca(final String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new DadosInvalidosException("O título para busca é obrigatório.");
        }
    }
}
