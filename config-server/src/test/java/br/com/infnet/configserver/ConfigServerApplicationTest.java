package br.com.infnet.configserver;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConfigServerApplicationTest {

    @Autowired
    private TestRestTemplate clienteHttp;

    @Test
    void devePublicarConfiguracoesDosDoisServicos() {
        final ParameterizedTypeReference<Map<String, Object>> tipoResposta =
                new ParameterizedTypeReference<>() { };
        final ResponseEntity<Map<String, Object>> configuracaoApi = this.clienteHttp.exchange(
                "/biblioteca-facil-api/prod", HttpMethod.GET, null, tipoResposta);
        final ResponseEntity<Map<String, Object>> configuracaoIsbn = this.clienteHttp.exchange(
                "/consulta-isbn-service/dev", HttpMethod.GET, null, tipoResposta);

        assertThat(configuracaoApi.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(configuracaoApi.getBody()).isNotNull();
        assertThat(configuracaoApi.getBody().get("profiles")).isEqualTo(List.of("prod"));
        assertThat((List<?>) configuracaoApi.getBody().get("propertySources")).isNotEmpty();
        assertThat(this.extrairPropriedades(configuracaoApi))
                .containsEntry("spring.jpa.open-in-view", false)
                .containsKeys(
                        "spring.datasource.url",
                        "servicos.consulta-isbn.url",
                        "resiliencia.consulta-isbn.max-tentativas",
                        "resiliencia.consulta-isbn.intervalo-ms");

        assertThat(configuracaoIsbn.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(configuracaoIsbn.getBody()).isNotNull();
        assertThat(configuracaoIsbn.getBody().get("profiles")).isEqualTo(List.of("dev"));
        assertThat((List<?>) configuracaoIsbn.getBody().get("propertySources")).isNotEmpty();
        assertThat(this.extrairPropriedades(configuracaoIsbn))
                .containsKeys(
                        "integracao.brasilapi.url",
                        "resiliencia.brasilapi.max-tentativas",
                        "resiliencia.brasilapi.intervalo-ms");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extrairPropriedades(
            final ResponseEntity<Map<String, Object>> configuracao) {
        final List<Map<String, Object>> fontes =
                (List<Map<String, Object>>) configuracao.getBody().get("propertySources");
        return (Map<String, Object>) fontes.getFirst().get("source");
    }
}
