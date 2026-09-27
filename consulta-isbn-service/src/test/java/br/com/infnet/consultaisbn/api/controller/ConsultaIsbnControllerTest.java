package br.com.infnet.consultaisbn.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.infnet.consultaisbn.api.dto.MetadadosLivroResponseDto;
import br.com.infnet.consultaisbn.aplicacao.exception.IsbnNaoEncontradoException;
import br.com.infnet.consultaisbn.aplicacao.exception.ProvedorBibliograficoIndisponivelException;
import br.com.infnet.consultaisbn.aplicacao.service.ConsultaIsbnService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ConsultaIsbnController.class)
class ConsultaIsbnControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConsultaIsbnService consultaIsbnService;

    @Test
    void deveRetornarMetadadosEncontrados() throws Exception {
        when(this.consultaIsbnService.consultar("9788535902778"))
                .thenReturn(new MetadadosLivroResponseDto(
                        "9788535902778",
                        "Dom Casmurro",
                        "Companhia das Letras",
                        "Romance brasileiro.",
                        1999,
                        "https://exemplo.com/capa.jpg"));

        this.mockMvc.perform(get("/api/isbn/9788535902778"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("9788535902778"))
                .andExpect(jsonPath("$.titulo").value("Dom Casmurro"));
    }

    @Test
    void deveRejeitarIsbnComFormatoInvalido() throws Exception {
        this.mockMvc.perform(get("/api/isbn/123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.mensagem").value("O ISBN-13 informado é inválido."));
    }

    @Test
    void deveRetornarNaoEncontrado() throws Exception {
        when(this.consultaIsbnService.consultar("9788535902778"))
                .thenThrow(new IsbnNaoEncontradoException(
                        "Nenhum livro foi encontrado para o ISBN informado."));

        this.mockMvc.perform(get("/api/isbn/9788535902778"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveRetornarBadGatewayQuandoProvedorEstiverIndisponivel() throws Exception {
        when(this.consultaIsbnService.consultar("9788535902778"))
                .thenThrow(new ProvedorBibliograficoIndisponivelException(
                        "O provedor de metadados bibliográficos está temporariamente indisponível."));

        this.mockMvc.perform(get("/api/isbn/9788535902778"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.status").value(502))
                .andExpect(jsonPath("$.mensagem")
                        .value("O provedor de metadados bibliográficos está temporariamente indisponível."));
    }
}
