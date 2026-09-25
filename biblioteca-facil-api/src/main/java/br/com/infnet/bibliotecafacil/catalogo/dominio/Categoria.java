package br.com.infnet.bibliotecafacil.catalogo.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String nome;
    private String descricao;
    private boolean ativa = true;
    private LocalDateTime dataCriacao = LocalDateTime.now();
    private LocalDateTime dataAtualizacao = this.dataCriacao;

    public void setId(final Long id) {
        this.id = id;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public void setDescricao(final String descricao) {
        this.descricao = descricao;
    }

    public void setAtiva(final boolean ativa) {
        this.ativa = ativa;
    }

    public void setDataAtualizacao(final LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Long getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public boolean isAtiva() {
        return this.ativa;
    }

    public LocalDateTime getDataCriacao() {
        return this.dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return this.dataAtualizacao;
    }

    @Override
    public String toString() {
        return ("Categoria{id=%s, nome='%s', descricao='%s', ativa=%s, "
                + "dataCriacao=%s, dataAtualizacao=%s}")
                .formatted(this.id, this.nome, this.descricao, this.ativa,
                        this.dataCriacao, this.dataAtualizacao);
    }

}
