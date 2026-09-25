package br.com.infnet.bibliotecafacil.catalogo.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CategoriaTest {

    @Test
    public void deveCriarCategoriaAtiva() {
        final Categoria categoria = criarCategoria(1L, "Romance", "Narrativas de ficção.");

        assertEquals("Romance", categoria.getNome());
        assertEquals("Narrativas de ficção.", categoria.getDescricao());
        assertTrue(categoria.isAtiva());
        assertNotNull(categoria.getDataCriacao());
        assertEquals(categoria.getDataCriacao(), categoria.getDataAtualizacao());
    }

    @Test
    public void deveAceitarDescricaoAusente() {
        final Categoria categoria = criarCategoria(1L, "Romance", null);

        assertNull(categoria.getDescricao());
    }

    @Test
    public void deveAlterarEstadoDaCategoria() {
        final Categoria categoria = criarCategoria(1L, "Romance", null);

        categoria.setAtiva(false);
        assertFalse(categoria.isAtiva());

        categoria.setAtiva(true);
        assertTrue(categoria.isAtiva());
    }

    @Test
    public void deveApresentarDatasDeAuditoria() {
        final Categoria categoria = criarCategoria(1L, "Romance", null);

        final String apresentacao = categoria.toString();

        assertTrue(apresentacao.contains("dataCriacao="));
        assertTrue(apresentacao.contains("dataAtualizacao="));
    }
    private Categoria criarCategoria(final Long id, final String nome, final String descricao) {
        final Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNome(nome);
        categoria.setDescricao(descricao);
        return categoria;
    }
}
