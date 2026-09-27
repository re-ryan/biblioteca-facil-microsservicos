package br.com.infnet.bibliotecafacil.catalogo.aplicacao.service;

import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;
import br.com.infnet.bibliotecafacil.catalogo.infraestrutura.integracao.isbn.ConsultaIsbnClient;
import br.com.infnet.bibliotecafacil.catalogo.infraestrutura.integracao.isbn.MetadadosLivroResponseDto;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.ServicoConsultaIsbnIndisponivelException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public final class ConsultaIsbnService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsultaIsbnService.class);

    private final ConsultaIsbnClient consultaIsbnClient;

    public ConsultaIsbnService(final ConsultaIsbnClient consultaIsbnClient) {
        this.consultaIsbnClient = consultaIsbnClient;
    }

    public void consultarApi(final Livro livro) {
        if (livro == null || livro.getIsbn13() == null || livro.getIsbn13().isBlank()) {
            return;
        }

        try {
            final MetadadosLivroResponseDto dadosExternos = this.consultaIsbnClient.consultar(livro.getIsbn13());
            this.copiarDados(livro, dadosExternos);
        } catch (final FeignException.NotFound exception) {
            LOGGER.info("ISBN {} não encontrado pelo serviço de consulta. "
                    + "O cadastro usará os dados informados.", livro.getIsbn13());
        } catch (final FeignException exception) {
            LOGGER.warn("O serviço de consulta ISBN está indisponível para o ISBN {}. Status remoto: {}.",
                    livro.getIsbn13(), exception.status());
            throw new ServicoConsultaIsbnIndisponivelException(
                    "O serviço de consulta de ISBN está temporariamente indisponível.");
        }
    }

    private void copiarDados(final Livro livro, final MetadadosLivroResponseDto dadosExternos) {
        if (dadosExternos == null) {
            return;
        }
        if (this.possuiTexto(dadosExternos.titulo())) {
            livro.setTitulo(dadosExternos.titulo());
        }
        if (this.possuiTexto(dadosExternos.editora())) {
            livro.setEditora(dadosExternos.editora());
        }
        if (this.possuiTexto(dadosExternos.descricao())) {
            livro.setDescricao(dadosExternos.descricao());
        }
        if (dadosExternos.anoPublicacao() != null && dadosExternos.anoPublicacao() > 0) {
            livro.setAnoPublicacao(dadosExternos.anoPublicacao());
        }
        if (this.possuiTexto(dadosExternos.urlImagemCapa())) {
            livro.setUrlImagemCapa(dadosExternos.urlImagemCapa());
        }
    }

    private boolean possuiTexto(final String valor) {
        return valor != null && !valor.isBlank();
    }
}
