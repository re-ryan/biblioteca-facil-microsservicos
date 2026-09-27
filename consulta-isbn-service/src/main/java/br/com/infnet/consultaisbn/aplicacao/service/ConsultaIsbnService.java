package br.com.infnet.consultaisbn.aplicacao.service;

import br.com.infnet.consultaisbn.api.dto.MetadadosLivroResponseDto;
import br.com.infnet.consultaisbn.aplicacao.exception.IsbnNaoEncontradoException;
import br.com.infnet.consultaisbn.aplicacao.exception.ProvedorBibliograficoIndisponivelException;
import br.com.infnet.consultaisbn.infraestrutura.integracao.brasilapi.BrasilApiIsbnClient;
import br.com.infnet.consultaisbn.infraestrutura.integracao.brasilapi.BrasilApiLivroResponseDto;
import feign.FeignException;
import org.springframework.stereotype.Service;

@Service
public final class ConsultaIsbnService {

    private final BrasilApiIsbnClient brasilApiIsbnClient;

    public ConsultaIsbnService(final BrasilApiIsbnClient brasilApiIsbnClient) {
        this.brasilApiIsbnClient = brasilApiIsbnClient;
    }

    public MetadadosLivroResponseDto consultar(final String isbn) {
        try {
            final BrasilApiLivroResponseDto resposta = this.brasilApiIsbnClient.consultar(isbn);
            return this.converter(isbn, resposta);
        } catch (final FeignException.NotFound exception) {
            throw new IsbnNaoEncontradoException("Nenhum livro foi encontrado para o ISBN informado.");
        } catch (final FeignException exception) {
            throw new ProvedorBibliograficoIndisponivelException(
                    "O provedor de metadados bibliográficos está temporariamente indisponível.");
        }
    }

    private MetadadosLivroResponseDto converter(
            final String isbn,
            final BrasilApiLivroResponseDto resposta) {
        if (resposta == null) {
            throw new IsbnNaoEncontradoException("Nenhum livro foi encontrado para o ISBN informado.");
        }
        return new MetadadosLivroResponseDto(
                isbn,
                resposta.titulo(),
                resposta.editora(),
                resposta.descricao(),
                resposta.anoPublicacao(),
                resposta.urlImagemCapa());
    }
}
