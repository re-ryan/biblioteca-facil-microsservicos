package br.com.infnet.bibliotecafacil.biblioteca.aplicacao.service;

import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.DadosInvalidosException;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.ObjetoNaoEncontradoException;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.OperacaoNaoPermitidaException;
import br.com.infnet.bibliotecafacil.biblioteca.dominio.Acervo;
import br.com.infnet.bibliotecafacil.biblioteca.dominio.Biblioteca;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;
import br.com.infnet.bibliotecafacil.biblioteca.infraestrutura.repository.BibliotecaRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BibliotecaService {

    private static final Sort ORDENACAO_PADRAO = Sort.by(Sort.Direction.ASC, "nome");

    private final BibliotecaRepository bibliotecaRepository;

    public BibliotecaService(final BibliotecaRepository bibliotecaRepository) {
        this.bibliotecaRepository = bibliotecaRepository;
    }

    @Transactional
    public Biblioteca incluir(final Biblioteca biblioteca) {
        this.validarBiblioteca(biblioteca);
        return this.bibliotecaRepository.save(biblioteca);
    }

    @Transactional
    public Biblioteca alterar(final Biblioteca biblioteca) {
        this.validarBiblioteca(biblioteca);
        final Biblioteca bibliotecaPersistida = this.obterPorId(biblioteca.getId());
        bibliotecaPersistida.setNome(biblioteca.getNome());
        bibliotecaPersistida.setCpfCnpj(biblioteca.getCpfCnpj());
        bibliotecaPersistida.setEmail(biblioteca.getEmail());
        bibliotecaPersistida.setTelefone(biblioteca.getTelefone());
        bibliotecaPersistida.setEndereco(biblioteca.getEndereco());
        bibliotecaPersistida.setDataAtualizacao(LocalDateTime.now());
        return this.bibliotecaRepository.save(bibliotecaPersistida);
    }

    public Acervo adicionarLivro(
            final Biblioteca biblioteca,
            final Long idAcervo,
            final Livro livro,
            final int quantidadeReal) {
        return this.adicionarLivro(biblioteca, idAcervo, livro, quantidadeReal, quantidadeReal);
    }

    public Acervo adicionarLivro(
            final Biblioteca biblioteca,
            final Long idAcervo,
            final Livro livro,
            final int quantidadeReal,
            final int quantidadeDisponivel) {
        this.validarInclusaoNoAcervo(
                biblioteca, idAcervo, livro, quantidadeReal, quantidadeDisponivel);

        final Acervo acervo = new Acervo();
        acervo.setId(idAcervo);
        acervo.setBiblioteca(biblioteca);
        acervo.setLivro(livro);
        acervo.setQuantidadeReal(quantidadeReal);
        acervo.setQuantidadeDisponivel(quantidadeDisponivel);

        final List<Acervo> acervos = new ArrayList<>(biblioteca.getAcervos());
        acervos.add(acervo);
        biblioteca.setAcervos(acervos);
        biblioteca.setDataAtualizacao(LocalDateTime.now());
        return acervo;
    }

    @Transactional
    public void excluir(final Long id) {
        final Biblioteca biblioteca = this.obterPorId(id);
        this.bibliotecaRepository.delete(biblioteca);
    }

    public Biblioteca obterPorId(final Long id) {
        if (id == null) {
            throw new DadosInvalidosException("O identificador da biblioteca é obrigatório.");
        }
        return this.bibliotecaRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException("Biblioteca não encontrada para o identificador %s.".formatted(id)));
    }

    public List<Biblioteca> listar() {
        return List.copyOf(this.bibliotecaRepository.findAll());
    }

    public List<Biblioteca> listarAtivas() {
        return this.bibliotecaRepository.findByAtivaTrue();
    }

    public List<Biblioteca> buscarPorNome(final String nome) {
        return this.buscarPorNome(nome, ORDENACAO_PADRAO);
    }

    public List<Biblioteca> buscarPorNome(final String nome, final Sort ordenacao) {
        this.validarTextoDeBusca(nome);
        return this.bibliotecaRepository.findByNomeContainingIgnoreCase(nome, ordenacao);
    }

    public List<Biblioteca> listarOrdenadasPorNome() {
        return this.bibliotecaRepository.findAll(ORDENACAO_PADRAO);
    }

    public List<String> listarNomes() {
        return this.bibliotecaRepository.findAll().stream()
                .map(Biblioteca::getNome)
                .toList();
    }

    private void validarBiblioteca(final Biblioteca biblioteca) {
        if (biblioteca == null) {
            throw new DadosInvalidosException("A biblioteca é obrigatória.");
        }

        if (biblioteca.getNome() == null || biblioteca.getNome().isBlank()) {
            throw new DadosInvalidosException("O nome da biblioteca é obrigatório.");
        }

        if (biblioteca.getCpfCnpj() == null || biblioteca.getCpfCnpj().isBlank()) {
            throw new DadosInvalidosException("O CPF ou CNPJ da biblioteca é obrigatório.");
        }

        if (biblioteca.getEmail() == null || biblioteca.getEmail().isBlank()) {
            throw new DadosInvalidosException("O e-mail da biblioteca é obrigatório.");
        }

        final boolean nomeEmUso = biblioteca.getId() == null
                ? this.bibliotecaRepository.existsByNomeIgnoreCase(biblioteca.getNome())
                : this.bibliotecaRepository.existsByNomeIgnoreCaseAndIdNot(
                        biblioteca.getNome(), biblioteca.getId());

        if (nomeEmUso) {
            throw new OperacaoNaoPermitidaException("Já existe uma biblioteca com o nome informado.");
        }

        final boolean cpfCnpjEmUso = biblioteca.getId() == null
                ? this.bibliotecaRepository.existsByCpfCnpj(biblioteca.getCpfCnpj())
                : this.bibliotecaRepository.existsByCpfCnpjAndIdNot(
                        biblioteca.getCpfCnpj(), biblioteca.getId());
        if (cpfCnpjEmUso) {
            throw new OperacaoNaoPermitidaException("Já existe uma biblioteca com o CPF ou CNPJ informado.");
        }

        final boolean emailEmUso = biblioteca.getId() == null
                ? this.bibliotecaRepository.existsByEmailIgnoreCase(biblioteca.getEmail())
                : this.bibliotecaRepository.existsByEmailIgnoreCaseAndIdNot(
                        biblioteca.getEmail(), biblioteca.getId());
        if (emailEmUso) {
            throw new OperacaoNaoPermitidaException("Já existe uma biblioteca com o e-mail informado.");
        }
    }

    private void validarTextoDeBusca(final String nome) {
        if (nome == null || nome.isBlank()) {
            throw new DadosInvalidosException("O nome para busca é obrigatório.");
        }
    }

    private void validarInclusaoNoAcervo(
            final Biblioteca biblioteca,
            final Long idAcervo,
            final Livro livro,
            final int quantidadeReal,
            final int quantidadeDisponivel) {
        if (biblioteca == null) {
            throw new DadosInvalidosException("A biblioteca é obrigatória.");
        }
        if (!biblioteca.isAtiva()) {
            throw new OperacaoNaoPermitidaException(
                    "A biblioteca precisa estar ativa para realizar esta operação.");
        }
        if (livro == null) {
            throw new DadosInvalidosException("O livro é obrigatório.");
        }
        if (quantidadeReal <= 0) {
            throw new DadosInvalidosException(
                    "A quantidade real do acervo deve ser positiva.");
        }
        if (quantidadeDisponivel < 0 || quantidadeDisponivel > quantidadeReal) {
            throw new DadosInvalidosException(
                    "A quantidade disponível deve estar entre zero e a quantidade real.");
        }
        if (idAcervo != null && biblioteca.getAcervos().stream()
                .anyMatch(acervo -> idAcervo.equals(acervo.getId()))) {
            throw new OperacaoNaoPermitidaException(
                    "O identificador do acervo já está em uso nesta biblioteca.");
        }
        if (biblioteca.getAcervos().stream()
                .anyMatch(acervo -> Objects.equals(
                        livro.getId(), acervo.getLivro().getId()))) {
            throw new OperacaoNaoPermitidaException(
                    "O livro já pertence ao acervo desta biblioteca.");
        }
    }

}
