package br.com.infnet.bibliotecafacil.catalogo.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AutorTest {

    @Test
    public void deveCriarAutorAtivo() {
        final Autor autor = criarAutor(15L, "Machado de Assis", "ASSIS, Machado de");

        assertEquals(15L, autor.getId());
        assertEquals("Machado de Assis", autor.getNome());
        assertEquals("ASSIS, Machado de", autor.getNomeCatalogacao());
        assertTrue(autor.isAtivo());
        assertNotNull(autor.getDataCriacao());
        assertEquals(autor.getDataCriacao(), autor.getDataAtualizacao());
    }

    @Test
    public void deveAlterarEstadoDoAutor() {
        final Autor autor = criarAutor(1L, "Machado de Assis", "ASSIS, Machado de");

        autor.setAtivo(false);
        assertFalse(autor.isAtivo());

        autor.setAtivo(true);
        assertTrue(autor.isAtivo());
    }

    @Test
    public void deveApresentarDatasDeAuditoria() {
        final Autor autor = criarAutor(1L, "Machado de Assis", "ASSIS, Machado de");

        final String apresentacao = autor.toString();

        assertTrue(apresentacao.contains("dataCriacao="));
        assertTrue(apresentacao.contains("dataAtualizacao="));
    }
    private Autor criarAutor(final Long id, final String nome, final String nomeCatalogacao) {
        final Autor autor = new Autor();
        autor.setId(id);
        autor.setNome(nome);
        autor.setNomeCatalogacao(nomeCatalogacao);
        return autor;
    }
}
