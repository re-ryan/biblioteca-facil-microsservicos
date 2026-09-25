package br.com.infnet.bibliotecafacil.catalogo.dominio;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class LivroTest {

    @Test
    public void deveRepresentarOsDadosDoLivro() {
        final Livro livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Dom Casmurro");
        livro.setIsbn10("8535902775");
        livro.setIsbn13("9788535902778");
        livro.setEditora("Companhia das Letras");
        livro.setAnoPublicacao(1899);
        livro.setEdicao("1ª edição");
        livro.setDescricao("Romance de Machado de Assis.");
        livro.setUrlImagemCapa("https://exemplo.com/capa.jpg");
        livro.setAtivo(false);

        assertAll(
                () -> assertEquals(1L, livro.getId()),
                () -> assertEquals("Dom Casmurro", livro.getTitulo()),
                () -> assertEquals("8535902775", livro.getIsbn10()),
                () -> assertEquals("9788535902778", livro.getIsbn13()),
                () -> assertEquals("Companhia das Letras", livro.getEditora()),
                () -> assertEquals(1899, livro.getAnoPublicacao()),
                () -> assertEquals("1ª edição", livro.getEdicao()),
                () -> assertEquals("Romance de Machado de Assis.", livro.getDescricao()),
                () -> assertEquals("https://exemplo.com/capa.jpg", livro.getUrlImagemCapa()),
                () -> assertFalse(livro.isAtivo()));
    }

    @Test
    public void naoDeveExporAsListasInternas() {
        final Livro livro = new Livro();
        livro.setAutorias(List.of(new Autoria()));
        livro.setCategorias(List.of(new Categoria()));

        assertAll(
                () -> assertThrows(UnsupportedOperationException.class,
                        () -> livro.getAutorias().add(new Autoria())),
                () -> assertThrows(UnsupportedOperationException.class,
                        () -> livro.getCategorias().add(new Categoria())));
    }
}
