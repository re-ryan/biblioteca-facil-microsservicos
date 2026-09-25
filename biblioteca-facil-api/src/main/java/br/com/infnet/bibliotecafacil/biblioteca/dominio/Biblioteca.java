package br.com.infnet.bibliotecafacil.biblioteca.dominio;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Biblioteca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String nome;
    @Column(nullable = false, unique = true)
    private String cpfCnpj;
    @Column(nullable = false, unique = true)
    private String email;
    private String telefone;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Endereco endereco;
    private LocalDateTime dataCriacao = LocalDateTime.now();
    @OneToMany(mappedBy = "biblioteca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Acervo> acervos = new ArrayList<>();
    private boolean ativa = true;
    private LocalDateTime dataAtualizacao = this.dataCriacao;

    public void setId(final Long id) {
        this.id = id;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public void setCpfCnpj(final String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setTelefone(final String telefone) {
        this.telefone = telefone;
    }

    public void setEndereco(final Endereco endereco) {
        this.endereco = endereco;
    }

    public void setAcervos(final List<Acervo> acervos) {
        this.acervos = new ArrayList<>(acervos);
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

    public String getCpfCnpj() {
        return this.cpfCnpj;
    }

    public String getEmail() {
        return this.email;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Endereco getEndereco() {
        return this.endereco;
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

    public List<Acervo> getAcervos() {
        return List.copyOf(this.acervos);
    }

    @Override
    public String toString() {
        return ("Biblioteca{id=%s, nome='%s', cpfCnpj='%s', email='%s', telefone='%s', ativa=%s, "
                + "dataCriacao=%s, dataAtualizacao=%s, endereco=%s, itensAcervo=%s}")
                .formatted(
                        this.id,
                        this.nome,
                        this.cpfCnpj,
                        this.email,
                        this.telefone,
                        this.ativa,
                        this.dataCriacao,
                        this.dataAtualizacao,
                        this.endereco,
                        this.acervos.size());
    }

}
