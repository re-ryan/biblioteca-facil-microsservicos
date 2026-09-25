package br.com.infnet.bibliotecafacil.biblioteca.dominio;

import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class AcervoTest {

    @Test
    public void deveRepresentarOsDadosDoAcervo() {
        final Biblioteca biblioteca = new Biblioteca();
        biblioteca.setNome("Biblioteca Central");
        final Livro livro = new Livro();
        livro.setTitulo("Dom Casmurro");
        final Acervo acervo = new Acervo();
        acervo.setId(1L);
        acervo.setBiblioteca(biblioteca);
        acervo.setLivro(livro);
        acervo.setQuantidadeReal(5);
        acervo.setQuantidadeDisponivel(3);
        acervo.setAtivo(false);

        assertAll(
                () -> assertEquals(1L, acervo.getId()),
                () -> assertEquals(biblioteca, acervo.getBiblioteca()),
                () -> assertEquals(livro, acervo.getLivro()),
                () -> assertEquals(5, acervo.getQuantidadeReal()),
                () -> assertEquals(3, acervo.getQuantidadeDisponivel()),
                () -> assertFalse(acervo.isAtivo()));
    }
}
