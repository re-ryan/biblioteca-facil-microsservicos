package br.com.infnet.bibliotecafacil.catalogo.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.infnet.bibliotecafacil.catalogo.aplicacao.service.CadastroLivroService;
import br.com.infnet.bibliotecafacil.catalogo.aplicacao.service.LivroService;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.ServicoConsultaIsbnIndisponivelException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LivroController.class)
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CadastroLivroService cadastroLivroService;

    @MockitoBean
    private LivroService livroService;

    @Test
    void deveRetornarServicoIndisponivelSemExporDetalhesInternos() throws Exception {
        when(this.cadastroLivroService.incluir(any(Livro.class)))
                .thenThrow(new ServicoConsultaIsbnIndisponivelException(
                        "O serviço de consulta de ISBN está temporariamente indisponível."));

        this.mockMvc.perform(post("/api/livros")
                        .contentType("application/json")
                        .content("""
                                {
                                  "titulo": "Dom Casmurro",
                                  "isbn13": "9788535902778"
                                }
                                """))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503))
                .andExpect(jsonPath("$.mensagem")
                        .value("O serviço de consulta de ISBN está temporariamente indisponível."));
    }
}
