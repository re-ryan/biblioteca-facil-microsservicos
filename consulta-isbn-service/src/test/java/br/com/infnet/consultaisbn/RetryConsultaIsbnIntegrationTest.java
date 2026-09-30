package br.com.infnet.consultaisbn;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.infnet.consultaisbn.api.dto.MetadadosLivroResponseDto;
import br.com.infnet.consultaisbn.aplicacao.service.ConsultaIsbnService;
import br.com.infnet.consultaisbn.infraestrutura.integracao.brasilapi.BrasilApiIsbnClient;
import br.com.infnet.consultaisbn.infraestrutura.integracao.brasilapi.BrasilApiLivroResponseDto;
import feign.FeignException;
import feign.Request;
import feign.Response;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "resiliencia.brasilapi.max-tentativas=3",
                "resiliencia.brasilapi.intervalo-ms=1"
        })
class RetryConsultaIsbnIntegrationTest {

    private static final String ISBN = "9788535902778";

    @Autowired
    private ConsultaIsbnService consultaIsbnService;

    @MockitoBean
    private BrasilApiIsbnClient brasilApiIsbnClient;

    @Test
    void deveRepetirConsultaAoProvedorAteUmaRespostaBemSucedida() {
        final FeignException indisponibilidade = this.criarErroFeign(503);
        when(this.brasilApiIsbnClient.consultar(ISBN))
                .thenThrow(indisponibilidade)
                .thenThrow(indisponibilidade)
                .thenReturn(new BrasilApiLivroResponseDto(
                        ISBN,
                        "Dom Casmurro",
                        "Editora",
                        null,
                        2020,
                        null));

        final MetadadosLivroResponseDto resposta = this.consultaIsbnService.consultar(ISBN);

        verify(this.brasilApiIsbnClient, times(3)).consultar(ISBN);
        assertThat(resposta.titulo()).isEqualTo("Dom Casmurro");
    }

    private FeignException criarErroFeign(final int status) {
        final Request request = Request.create(
                Request.HttpMethod.GET,
                "/api/isbn/v1/" + ISBN,
                Map.of(),
                null,
                StandardCharsets.UTF_8,
                null);
        final Response response = Response.builder()
                .status(status)
                .reason("indisponível")
                .request(request)
                .headers(Map.of())
                .build();
        return FeignException.errorStatus("BrasilApiIsbnClient#consultar", response);
    }
}
