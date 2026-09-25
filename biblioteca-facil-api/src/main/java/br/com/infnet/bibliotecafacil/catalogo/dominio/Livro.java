package br.com.infnet.bibliotecafacil.catalogo.dominio;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String titulo;
    @Column(unique = true)
    private String isbn10;
    @Column(nullable = false, unique = true)
    private String isbn13;
    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordem ASC")
    private List<Autoria> autorias = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "livro_categoria",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id"))
    private List<Categoria> categorias = new ArrayList<>();
    private String editora;
    private Integer anoPublicacao;
    private String edicao;
    @Column(length = 4000)
    private String descricao;
    @Column(length = 1000)
    private String urlImagemCapa;
    private boolean ativo = true;
    private LocalDateTime dataCriacao = LocalDateTime.now();
    private LocalDateTime dataAtualizacao = this.dataCriacao;

    public void setId(final Long id) {
        this.id = id;
    }

    public void setTitulo(final String titulo) {
        this.titulo = titulo;
    }

    public void setIsbn10(final String isbn10) {
        this.isbn10 = isbn10;
    }

    public void setIsbn13(final String isbn13) {
        this.isbn13 = isbn13;
    }

    public void setEditora(final String editora) {
        this.editora = editora;
    }

    public void setAnoPublicacao(final Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public void setEdicao(final String edicao) {
        this.edicao = edicao;
    }

    public void setDescricao(final String descricao) {
        this.descricao = descricao;
    }

    public void setUrlImagemCapa(final String urlImagemCapa) {
        this.urlImagemCapa = urlImagemCapa;
    }

    public void setAtivo(final boolean ativo) {
        this.ativo = ativo;
    }

    public void setDataAtualizacao(final LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public void setAutorias(final List<Autoria> autorias) {
        this.autorias = new ArrayList<>(autorias);
    }

    public void setCategorias(final List<Categoria> categorias) {
        this.categorias = new ArrayList<>(categorias);
    }

    public Long getId() {
        return this.id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getIsbn10() {
        return this.isbn10;
    }

    public String getIsbn13() {
        return this.isbn13;
    }

    public String getEditora() {
        return this.editora;
    }

    public Integer getAnoPublicacao() {
        return this.anoPublicacao;
    }

    public String getEdicao() {
        return this.edicao;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public String getUrlImagemCapa() {
        return this.urlImagemCapa;
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

    public List<Autoria> getAutorias() {
        return List.copyOf(this.autorias);
    }

    public List<Categoria> getCategorias() {
        return List.copyOf(this.categorias);
    }

    @Override
    public String toString() {
        final List<String> nomesDosAutores = new ArrayList<>();
        for (final Autoria autoria : this.autorias) {
            nomesDosAutores.add(autoria.getAutor().getNome());
        }
        final List<String> nomesDasCategorias = new ArrayList<>();
        for (final Categoria categoria : this.categorias) {
            nomesDasCategorias.add(categoria.getNome());
        }

        return ("Livro{id=%s, titulo='%s', isbn10='%s', isbn13='%s', editora='%s', "
                + "anoPublicacao=%s, edicao='%s', descricao='%s', urlImagemCapa='%s', ativo=%s, "
                + "dataCriacao=%s, dataAtualizacao=%s, autores=%s, categorias=%s}")
                .formatted(this.id, this.titulo, this.isbn10, this.isbn13,
                        this.editora, this.anoPublicacao, this.edicao, this.descricao, this.urlImagemCapa,
                        this.ativo, this.dataCriacao, this.dataAtualizacao,
                        nomesDosAutores, nomesDasCategorias);
    }

}
