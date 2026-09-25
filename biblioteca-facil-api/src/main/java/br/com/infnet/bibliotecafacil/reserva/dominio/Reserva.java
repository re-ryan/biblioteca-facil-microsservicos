package br.com.infnet.bibliotecafacil.reserva.dominio;

import br.com.infnet.bibliotecafacil.biblioteca.dominio.Acervo;
import br.com.infnet.bibliotecafacil.usuario.dominio.Leitor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "leitor_id")
    private Leitor leitor;
    @ManyToOne(optional = false)
    @JoinColumn(name = "acervo_id")
    private Acervo acervo;
    private LocalDateTime dataReserva;
    @Enumerated(EnumType.STRING)
    private StatusReserva status;

    public void setLeitor(final Leitor leitor) {
        this.leitor = leitor;
    }

    public void setAcervo(final Acervo acervo) {
        this.acervo = acervo;
    }

    public void setDataReserva(final LocalDateTime dataReserva) {
        this.dataReserva = dataReserva;
    }

    public void setStatus(final StatusReserva status) {
        this.status = status;
    }

    public Long getId() {
        return this.id;
    }

    @JsonIgnore
    public Leitor getLeitor() {
        return this.leitor;
    }

    public Acervo getAcervo() {
        return this.acervo;
    }

    public LocalDateTime getDataReserva() {
        return this.dataReserva;
    }

    public StatusReserva getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "Reserva{id=%s, leitor='%s', biblioteca='%s', livro='%s', data=%s, status=%s}"
                .formatted(this.getId(), this.leitor.getNomeCompleto(),
                        this.acervo.getBiblioteca().getNome(),
                        this.acervo.getLivro().getTitulo(), this.dataReserva, this.status);
    }

}
