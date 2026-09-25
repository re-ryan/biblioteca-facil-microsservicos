package br.com.infnet.bibliotecafacil.biblioteca.dominio;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class BibliotecaTest {

    @Test
    public void deveRepresentarOsDadosDaBiblioteca() {
        final Endereco endereco = new Endereco();
        endereco.setCidade("Rio de Janeiro");
        final Biblioteca biblioteca = new Biblioteca();
        biblioteca.setId(1L);
        biblioteca.setNome("Biblioteca Central");
        biblioteca.setCpfCnpj("12345678000199");
        biblioteca.setEmail("central@biblioteca.com");
        biblioteca.setTelefone("(21) 2222-3333");
        biblioteca.setEndereco(endereco);
        biblioteca.setAtiva(false);

        assertAll(
                () -> assertEquals(1L, biblioteca.getId()),
                () -> assertEquals("Biblioteca Central", biblioteca.getNome()),
                () -> assertEquals("12345678000199", biblioteca.getCpfCnpj()),
                () -> assertEquals("central@biblioteca.com", biblioteca.getEmail()),
                () -> assertEquals("(21) 2222-3333", biblioteca.getTelefone()),
                () -> assertEquals(endereco, biblioteca.getEndereco()),
                () -> assertFalse(biblioteca.isAtiva()));
    }

    @Test
    public void naoDeveExporAListaInternaDeAcervos() {
        final Biblioteca biblioteca = new Biblioteca();
        biblioteca.setAcervos(List.of(new Acervo()));

        assertThrows(UnsupportedOperationException.class,
                () -> biblioteca.getAcervos().add(new Acervo()));
    }
}
