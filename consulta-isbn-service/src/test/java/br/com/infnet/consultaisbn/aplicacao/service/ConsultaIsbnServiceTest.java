package br.com.infnet.consultaisbn.aplicacao.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.com.infnet.consultaisbn.api.dto.MetadadosLivroResponseDto;
import br.com.infnet.consultaisbn.aplicacao.exception.IsbnNaoEncontradoException;
import br.com.infnet.consultaisbn.aplicacao.exception.ProvedorBibliograficoIndisponivelException;
import br.com.infnet.consultaisbn.infraestrutura.integracao.brasilapi.BrasilApiIsbnClient;
import br.com.infnet.consultaisbn.infraestrutura.integracao.brasilapi.BrasilApiLivroResponseDto;
import feign.FeignException;
import feign.Request;
import feign.Response;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsultaIsbnServiceTest {

    private BrasilApiIsbnClient brasilApiIsbnClient;
    private ConsultaIsbnService consultaIsbnService;

    @BeforeEach
    void configurar() {
        this.brasilApiIsbnClient = mock(BrasilApiIsbnClient.class);
        this.consultaIsbnService = new ConsultaIsbnService(this.brasilApiIsbnClient);
    }

    @Test
    void deveTraduzirRespostaDaBrasilApiParaContratoDoServico() {
        when(this.brasilApiIsbnClient.consultar("9788535902778"))
                .thenReturn(new BrasilApiLivroResponseDto(
                        "9788535902778",
                        "Dom Casmurro",
                        "Companhia das Letras",
                        "Romance brasileiro.",
                        1999,
                        "https://exemplo.com/capa.jpg"));

        final MetadadosLivroResponseDto resposta = this.consultaIsbnService.consultar("9788535902778");

        assertAll(
                () -> assertEquals("9788535902778", resposta.isbn()),
                () -> assertEquals("Dom Casmurro", resposta.titulo()),
                () -> assertEquals("Companhia das Letras", resposta.editora()),
                () -> assertEquals(1999, resposta.anoPublicacao()));
    }

    @Test
    void deveTraduzirIsbnNaoEncontradoSemExporFeign() {
        when(this.brasilApiIsbnClient.consultar("9788535902778"))
                .thenThrow(this.criarErroFeign(404));

        assertThrows(
                IsbnNaoEncontradoException.class,
                () -> this.consultaIsbnService.consultar("9788535902778"));
    }

    @Test
    void deveTraduzirFalhaDoProvedorSemExporFeign() {
        when(this.brasilApiIsbnClient.consultar("9788535902778"))
                .thenThrow(this.criarErroFeign(503));

        assertThrows(
                ProvedorBibliograficoIndisponivelException.class,
                () -> this.consultaIsbnService.consultar("9788535902778"));
    }

    private FeignException criarErroFeign(final int status) {
        final Request request = Request.create(
                Request.HttpMethod.GET,
                "/api/isbn/v1/9788535902778",
                Map.of(),
                null,
                StandardCharsets.UTF_8,
                null);
        final Response response = Response.builder()
                .status(status)
                .reason("Erro de teste")
                .request(request)
                .headers(Map.of())
                .build();
        return FeignException.errorStatus("BrasilApiIsbnClient#consultar", response);
    }
}
