package br.com.infnet.bibliotecafacil.catalogo.importacao.infraestrutura.batch;

import br.com.infnet.bibliotecafacil.catalogo.aplicacao.service.LivroService;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public final class LivroImportacaoWriter implements ItemWriter<Livro> {

    private final LivroService livroService;

    public LivroImportacaoWriter(final LivroService livroService) {
        this.livroService = livroService;
    }

    @Override
    public void write(final Chunk<? extends Livro> chunk) {
        chunk.getItems().forEach(this.livroService::incluir);
    }
}
