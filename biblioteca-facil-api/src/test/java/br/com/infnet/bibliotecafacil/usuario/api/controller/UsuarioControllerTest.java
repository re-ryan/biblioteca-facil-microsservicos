package br.com.infnet.bibliotecafacil.usuario.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.ObjetoNaoEncontradoException;
import br.com.infnet.bibliotecafacil.usuario.aplicacao.service.CadastroUsuarioService;
import br.com.infnet.bibliotecafacil.usuario.aplicacao.service.UsuarioService;
import br.com.infnet.bibliotecafacil.usuario.dominio.Leitor;
import br.com.infnet.bibliotecafacil.usuario.dominio.TipoUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private CadastroUsuarioService cadastroUsuarioService;

    @Test
    void deveRejeitarRequisicaoComDadosInvalidos() throws Exception {
        this.mockMvc.perform(post("/api/usuarios")
                        .contentType("application/json")
                        .content("""
                                {
                                  "nomeCompleto": " ",
                                  "login": "ana.souza",
                                  "email": "email-invalido",
                                  "senhaHash": "hash-seguro",
                                  "tipoUsuario": "LEITOR"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void naoDeveExporHashDaSenhaNaResposta() throws Exception {
        final Leitor leitor = new Leitor();
        leitor.setId(1L);
        leitor.setNomeCompleto("Ana Souza");
        leitor.setLogin("ana.souza");
        leitor.setEmail("ana@email.com");
        leitor.setSenhaHash("hash-que-nao-pode-vazar");
        leitor.setTipoUsuario(TipoUsuario.LEITOR);
        when(this.usuarioService.obterPorId(1L)).thenReturn(leitor);

        this.mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeCompleto").value("Ana Souza"))
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString("senhaHash"))))
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString("hash-que-nao-pode-vazar"))));
    }

    @Test
    void deveRetornarNaoEncontradoSemExporDetalhesInternos() throws Exception {
        when(this.usuarioService.obterPorId(99L))
                .thenThrow(new ObjetoNaoEncontradoException(
                        "Usuário não encontrado para o identificador 99."));

        this.mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem")
                        .value("Usuário não encontrado para o identificador 99."));
    }

    @Test
    void deveRejeitarJsonMalformadoSemExporDetalhesInternos() throws Exception {
        this.mockMvc.perform(post("/api/usuarios")
                        .contentType("application/json")
                        .content("{json-invalido"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.mensagem")
                        .value("O corpo da requisição está ausente ou possui formato inválido."))
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString("JsonParseException"))));
    }

    @Test
    void deveRejeitarIdentificadorMalformadoSemExporDetalhesInternos() throws Exception {
        this.mockMvc.perform(get("/api/usuarios/invalido"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.mensagem")
                        .value("O parâmetro 'id' possui formato inválido."))
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString("NumberFormatException"))));
    }
}
