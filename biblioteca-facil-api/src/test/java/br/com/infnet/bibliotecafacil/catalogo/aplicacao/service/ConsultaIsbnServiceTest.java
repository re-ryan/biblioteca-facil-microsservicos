package br.com.infnet.bibliotecafacil.catalogo.aplicacao.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;
import br.com.infnet.bibliotecafacil.catalogo.infraestrutura.integracao.isbn.ConsultaIsbnClient;
import br.com.infnet.bibliotecafacil.catalogo.infraestrutura.integracao.isbn.MetadadosLivroResponseDto;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.ServicoConsultaIsbnIndisponivelException;
import feign.FeignException;
import feign.Request;
import feign.Response;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsultaIsbnServiceTest {

    private ConsultaIsbnClient consultaIsbnClient;
    private ConsultaIsbnService consultaIsbnService;

    @BeforeEach
    void configurar() {
        this.consultaIsbnClient = mock(ConsultaIsbnClient.class);
        this.consultaIsbnService = new ConsultaIsbnService(this.consultaIsbnClient);
    }

    @Test
    void deveCopiarMetadadosEncontradosParaOLivro() {
        final Livro livro = this.criarLivro("9788535902778");
        when(this.consultaIsbnClient.consultar("9788535902778"))
                .thenReturn(new MetadadosLivroResponseDto(
                        "9788535902778",
                        "Dom Casmurro",
                        "Companhia das Letras",
                        "Romance brasileiro.",
                        1999,
                        "https://exemplo.com/capa.jpg"));

        this.consultaIsbnService.consultarApi(livro);

        assertAll(
                () -> assertEquals("Dom Casmurro", livro.getTitulo()),
                () -> assertEquals("Companhia das Letras", livro.getEditora()),
                () -> assertEquals("Romance brasileiro.", livro.getDescricao()),
                () -> assertEquals(1999, livro.getAnoPublicacao()),
                () -> assertEquals("https://exemplo.com/capa.jpg", livro.getUrlImagemCapa()));
    }

    @Test
    void deveIgnorarConsultaQuandoLivroNaoPossuiIsbn() {
        this.consultaIsbnService.consultarApi(new Livro());

        verifyNoInteractions(this.consultaIsbnClient);
    }

    @Test
    void deveManterDadosInformadosQuandoIsbnNaoForEncontrado() {
        final Livro livro = this.criarLivro("9788535902778");
        when(this.consultaIsbnClient.consultar("9788535902778"))
                .thenThrow(this.criarErroFeign(404));

        this.consultaIsbnService.consultarApi(livro);

        assertEquals("Título informado", livro.getTitulo());
    }

    @Test
    void deveSinalizarIndisponibilidadeDoServicoRemoto() {
        final Livro livro = this.criarLivro("9788535902778");
        when(this.consultaIsbnClient.consultar("9788535902778"))
                .thenThrow(this.criarErroFeign(502));

        assertThrows(
                ServicoConsultaIsbnIndisponivelException.class,
                () -> this.consultaIsbnService.consultarApi(livro));
    }

    private Livro criarLivro(final String isbn) {
        final Livro livro = new Livro();
        livro.setTitulo("Título informado");
        livro.setIsbn13(isbn);
        return livro;
    }

    private FeignException criarErroFeign(final int status) {
        final Request request = Request.create(
                Request.HttpMethod.GET,
                "/api/isbn/9788535902778",
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
        return FeignException.errorStatus("ConsultaIsbnClient#consultar", response);
    }
}
