package br.com.infnet.bibliotecafacil.catalogo.importacao.aplicacao.processamento;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;
import br.com.infnet.bibliotecafacil.catalogo.importacao.aplicacao.dto.LivroImportacaoItem;
import org.junit.jupiter.api.Test;

class LivroImportacaoProcessorTest {

    private final LivroImportacaoProcessor processor = new LivroImportacaoProcessor();

    @Test
    void deveNormalizarRegistroValido() throws Exception {
        final LivroImportacaoItem item = new LivroImportacaoItem(
                "  Clean   Architecture ",
                " 9780134494166 ",
                "  Prentice   Hall ",
                "2017",
                " 1 ");

        final Livro livro = this.processor.process(item);

        assertThat(livro).isNotNull();
        assertThat(livro.getTitulo()).isEqualTo("Clean Architecture");
        assertThat(livro.getIsbn13()).isEqualTo("9780134494166");
        assertThat(livro.getEditora()).isEqualTo("Prentice Hall");
        assertThat(livro.getAnoPublicacao()).isEqualTo(2017);
        assertThat(livro.getEdicao()).isEqualTo("1");
    }

    @Test
    void deveFiltrarRegistroComAnoInvalido() throws Exception {
        final LivroImportacaoItem item = new LivroImportacaoItem(
                "Livro futuro", "9780134494166", "Editora", "9999", "1");

        assertThat(this.processor.process(item)).isNull();
    }

    @Test
    void deveFiltrarRegistroSemTitulo() throws Exception {
        final LivroImportacaoItem item = new LivroImportacaoItem(
                " ", "9780134494166", "Editora", "2017", "1");

        assertThat(this.processor.process(item)).isNull();
    }
}
