package br.com.infnet.bibliotecafacil.usuario.dominio;

import br.com.infnet.bibliotecafacil.biblioteca.dominio.Biblioteca;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("BIBLIOTECARIO")
public class Bibliotecario extends Usuario {

    @ManyToOne
    @JoinColumn(name = "biblioteca_id")
    private Biblioteca biblioteca;

    public void setBiblioteca(final Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
    }

    public Biblioteca getBiblioteca() {
        return this.biblioteca;
    }

    @Override
    public String toString() {
        return "Bibliotecario{%s, biblioteca='%s'}"
                .formatted(this.descreverUsuario(), this.biblioteca.getNome());
    }

}
