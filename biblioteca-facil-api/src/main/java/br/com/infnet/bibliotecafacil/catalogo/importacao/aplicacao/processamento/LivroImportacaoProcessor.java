package br.com.infnet.bibliotecafacil.catalogo.importacao.aplicacao.processamento;

import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;
import br.com.infnet.bibliotecafacil.catalogo.importacao.aplicacao.dto.LivroImportacaoItem;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public final class LivroImportacaoProcessor implements ItemProcessor<LivroImportacaoItem, Livro> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LivroImportacaoProcessor.class);
    private static final int PRIMEIRO_ANO_IMPRESSO = 1450;

    @Override
    public Livro process(final LivroImportacaoItem item) {
        try {
            final String titulo = this.normalizarTextoObrigatorio(item.titulo());
            final String isbn13 = this.normalizarTextoObrigatorio(item.isbn13());
            final int anoPublicacao = Integer.parseInt(
                    this.normalizarTextoObrigatorio(item.anoPublicacao()));
            if (anoPublicacao < PRIMEIRO_ANO_IMPRESSO
                    || anoPublicacao > LocalDate.now().getYear() + 1) {
                throw new IllegalArgumentException("ano de publicação fora do intervalo aceito");
            }

            final Livro livro = new Livro();
            livro.setTitulo(titulo);
            livro.setIsbn13(isbn13);
            livro.setEditora(this.normalizarTextoOpcional(item.editora()));
            livro.setAnoPublicacao(anoPublicacao);
            livro.setEdicao(this.normalizarTextoOpcional(item.edicao()));
            return livro;
        } catch (final IllegalArgumentException exception) {
            LOGGER.warn("Registro de livro ignorado durante a importação: {}. Motivo: {}",
                    item, exception.getMessage());
            return null;
        }
    }

    private String normalizarTextoObrigatorio(final String valor) {
        final String valorNormalizado = this.normalizarTextoOpcional(valor);
        if (valorNormalizado == null) {
            throw new IllegalArgumentException("campo obrigatório ausente");
        }
        return valorNormalizado;
    }

    private String normalizarTextoOpcional(final String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim().replaceAll("\\s+", " ");
    }
}
