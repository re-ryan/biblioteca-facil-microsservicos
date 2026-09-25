package br.com.infnet.bibliotecafacil.usuario.dominio;

import br.com.infnet.bibliotecafacil.reserva.dominio.Reserva;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("LEITOR")
public class Leitor extends Usuario {

    @OneToMany(mappedBy = "leitor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas = new ArrayList<>();

    public List<Reserva> getReservas() {
        return List.copyOf(this.reservas);
    }

    @Override
    public String toString() {
        return "Leitor{%s, reservas=%s}"
                .formatted(this.descreverUsuario(), this.reservas.size());
    }

}
