package br.com.infnet.bibliotecafacil.catalogo.api.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.infnet.bibliotecafacil.catalogo.aplicacao.dto.LivroCatalogo;
import br.com.infnet.bibliotecafacil.catalogo.aplicacao.dto.LivroCatalogo.DisponibilidadeBiblioteca;
import br.com.infnet.bibliotecafacil.catalogo.aplicacao.service.CatalogoService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CatalogoController.class)
class CatalogoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CatalogoService catalogoService;

    @Test
    void deveConsultarCatalogoComFiltrosOpcionais() throws Exception {
        when(this.catalogoService.buscar("dom", "machado", "romance"))
                .thenReturn(List.of(new LivroCatalogo(
                        1L,
                        "Dom Casmurro",
                        "9788535902778",
                        "Companhia das Letras",
                        1998,
                        null,
                        List.of("Machado de Assis"),
                        List.of("Romance"),
                        List.of(new DisponibilidadeBiblioteca(
                                2L,
                                "Biblioteca Central",
                                3)))));

        this.mockMvc.perform(get("/api/catalogo")
                        .param("titulo", "dom")
                        .param("autor", "machado")
                        .param("categoria", "romance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Dom Casmurro"))
                .andExpect(jsonPath("$[0].autores[0]").value("Machado de Assis"))
                .andExpect(jsonPath("$[0].categorias[0]").value("Romance"))
                .andExpect(jsonPath("$[0].bibliotecas[0].id").value(2))
                .andExpect(jsonPath("$[0].bibliotecas[0].nome")
                        .value("Biblioteca Central"))
                .andExpect(jsonPath("$[0].bibliotecas[0].quantidadeDisponivel")
                        .value(3));

        verify(this.catalogoService).buscar("dom", "machado", "romance");
    }
}
