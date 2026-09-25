package br.com.infnet.bibliotecafacil.biblioteca.dominio;

import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Acervo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "biblioteca_id")
    private Biblioteca biblioteca;
    @ManyToOne(optional = false)
    @JoinColumn(name = "livro_id")
    private Livro livro;
    private int quantidadeReal;
    private LocalDateTime dataCriacao = LocalDateTime.now();
    private int quantidadeDisponivel;
    private boolean ativo = true;
    private LocalDateTime dataAtualizacao = this.dataCriacao;

    public void setId(final Long id) {
        this.id = id;
    }

    public void setBiblioteca(final Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
    }

    public void setLivro(final Livro livro) {
        this.livro = livro;
    }

    public void setQuantidadeReal(final int quantidadeReal) {
        this.quantidadeReal = quantidadeReal;
    }

    public void setQuantidadeDisponivel(final int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public void setDataAtualizacao(final LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public void setAtivo(final boolean ativo) {
        this.ativo = ativo;
    }

    public Long getId() {
        return this.id;
    }

    @JsonIgnore
    public Biblioteca getBiblioteca() {
        return this.biblioteca;
    }

    public Livro getLivro() {
        return this.livro;
    }

    public int getQuantidadeReal() {
        return this.quantidadeReal;
    }

    public int getQuantidadeDisponivel() {
        return this.quantidadeDisponivel;
    }

    public boolean isAtivo() {
        return this.ativo;
    }

    public LocalDateTime getDataCriacao() {
        return this.dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return this.dataAtualizacao;
    }

    @Override
    public String toString() {
        return ("Acervo{id=%s, biblioteca='%s', livro='%s', quantidadeReal=%s, "
                + "quantidadeDisponivel=%s, ativo=%s, dataCriacao=%s, dataAtualizacao=%s}")
                .formatted(this.id,
                        this.biblioteca.getNome(),
                        this.livro.getTitulo(),
                        this.quantidadeReal,
                        this.quantidadeDisponivel,
                        this.ativo,
                        this.dataCriacao,
                        this.dataAtualizacao);
    }

}
