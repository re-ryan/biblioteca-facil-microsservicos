package br.com.infnet.bibliotecafacil;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.infnet.bibliotecafacil.catalogo.aplicacao.service.ConsultaIsbnService;
import br.com.infnet.bibliotecafacil.biblioteca.dominio.Acervo;
import br.com.infnet.bibliotecafacil.biblioteca.dominio.Biblioteca;
import br.com.infnet.bibliotecafacil.biblioteca.infraestrutura.repository.AcervoRepository;
import br.com.infnet.bibliotecafacil.biblioteca.infraestrutura.repository.BibliotecaRepository;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;
import br.com.infnet.bibliotecafacil.catalogo.infraestrutura.integracao.isbn.ConsultaIsbnClient;
import br.com.infnet.bibliotecafacil.catalogo.infraestrutura.integracao.isbn.MetadadosLivroResponseDto;
import br.com.infnet.bibliotecafacil.catalogo.infraestrutura.repository.LivroRepository;
import br.com.infnet.bibliotecafacil.reserva.dominio.Reserva;
import br.com.infnet.bibliotecafacil.reserva.dominio.StatusReserva;
import br.com.infnet.bibliotecafacil.reserva.infraestrutura.repository.ReservaRepository;
import br.com.infnet.bibliotecafacil.usuario.dominio.Leitor;
import br.com.infnet.bibliotecafacil.usuario.dominio.Bibliotecario;
import br.com.infnet.bibliotecafacil.usuario.dominio.TipoUsuario;
import br.com.infnet.bibliotecafacil.usuario.infraestrutura.repository.UsuarioRepository;
import feign.FeignException;
import feign.Request;
import feign.Response;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "resiliencia.consulta-isbn.max-tentativas=3",
        "resiliencia.consulta-isbn.intervalo-ms=1"
})
@AutoConfigureMockMvc
class Etapa3IntegrationTest {

    private static final String ISBN = "9788535902778";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    @Autowired
    private AcervoRepository acervoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ConsultaIsbnService consultaIsbnService;

    @MockitoBean
    private ConsultaIsbnClient consultaIsbnClient;

    @AfterEach
    void limparDados() {
        this.reservaRepository.deleteAll();
        this.acervoRepository.deleteAll();
        this.usuarioRepository.deleteAll();
        this.bibliotecaRepository.deleteAll();
        this.livroRepository.deleteAll();
    }

    @Test
    void deveSerializarLivroPersistidoComOpenInViewDesativado() throws Exception {
        final Livro livro = new Livro();
        livro.setTitulo("Dom Casmurro");
        livro.setIsbn13(ISBN);
        this.livroRepository.saveAndFlush(livro);

        this.mockMvc.perform(get("/api/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Dom Casmurro"))
                .andExpect(jsonPath("$[0].isbn13").value(ISBN))
                .andExpect(jsonPath("$[0].autorias").doesNotExist())
                .andExpect(jsonPath("$[0].categorias").doesNotExist());
    }

    @Test
    void deveRepetirConsultaIsbnAteUmaRespostaBemSucedida() {
        final Livro livro = new Livro();
        livro.setTitulo("Título informado");
        livro.setIsbn13(ISBN);
        final FeignException indisponibilidade = this.criarErroFeign(503);
        when(this.consultaIsbnClient.consultar(ISBN))
                .thenThrow(indisponibilidade)
                .thenThrow(indisponibilidade)
                .thenReturn(new MetadadosLivroResponseDto(
                        ISBN,
                        "Título encontrado",
                        "Editora",
                        null,
                        2020,
                        null));

        this.consultaIsbnService.consultarApi(livro);

        verify(this.consultaIsbnClient, times(3)).consultar(ISBN);
        org.assertj.core.api.Assertions.assertThat(livro.getTitulo())
                .isEqualTo("Título encontrado");
    }

    @Test
    void deveSerializarReservaComRelacionamentosCarregadosExplicitamente() throws Exception {
        final Livro livro = new Livro();
        livro.setTitulo("Dom Casmurro");
        livro.setIsbn13(ISBN);
        this.livroRepository.saveAndFlush(livro);

        final Biblioteca biblioteca = new Biblioteca();
        biblioteca.setNome("Biblioteca Central");
        biblioteca.setCpfCnpj("12.345.678/0001-90");
        biblioteca.setEmail("contato@biblioteca.com");
        this.bibliotecaRepository.saveAndFlush(biblioteca);

        final Acervo acervo = new Acervo();
        acervo.setBiblioteca(biblioteca);
        acervo.setLivro(livro);
        acervo.setQuantidadeReal(2);
        acervo.setQuantidadeDisponivel(1);
        this.acervoRepository.saveAndFlush(acervo);

        final Leitor leitor = new Leitor();
        leitor.setNomeCompleto("Ana Souza");
        leitor.setLogin("ana.souza");
        leitor.setEmail("ana@email.com");
        leitor.setSenhaHash("hash-seguro");
        leitor.setTipoUsuario(TipoUsuario.LEITOR);
        this.usuarioRepository.saveAndFlush(leitor);

        final Reserva reserva = new Reserva();
        reserva.setLeitor(leitor);
        reserva.setAcervo(acervo);
        reserva.setDataReserva(LocalDateTime.now());
        reserva.setStatus(StatusReserva.PENDENTE);
        this.reservaRepository.saveAndFlush(reserva);

        this.mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].leitorNome").value("Ana Souza"))
                .andExpect(jsonPath("$[0].bibliotecaNome").value("Biblioteca Central"))
                .andExpect(jsonPath("$[0].livroTitulo").value("Dom Casmurro"));
    }

    @Test
    void deveSerializarBibliotecaDoBibliotecarioSemSessaoAberta() throws Exception {
        final Biblioteca biblioteca = new Biblioteca();
        biblioteca.setNome("Biblioteca Central");
        biblioteca.setCpfCnpj("12.345.678/0001-90");
        biblioteca.setEmail("contato@biblioteca.com");
        this.bibliotecaRepository.saveAndFlush(biblioteca);

        final Bibliotecario bibliotecario = new Bibliotecario();
        bibliotecario.setNomeCompleto("Carlos Lima");
        bibliotecario.setLogin("carlos.lima");
        bibliotecario.setEmail("carlos@email.com");
        bibliotecario.setSenhaHash("hash-seguro");
        bibliotecario.setTipoUsuario(TipoUsuario.BIBLIOTECARIO);
        bibliotecario.setBiblioteca(biblioteca);
        this.usuarioRepository.saveAndFlush(bibliotecario);

        this.mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeCompleto").value("Carlos Lima"))
                .andExpect(jsonPath("$[0].bibliotecaId").value(biblioteca.getId()))
                .andExpect(jsonPath("$[0].bibliotecaNome").value("Biblioteca Central"));
    }

    private FeignException criarErroFeign(final int status) {
        final Request request = Request.create(
                Request.HttpMethod.GET,
                "/api/isbn/" + ISBN,
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
        return FeignException.errorStatus("ConsultaIsbnClient#consultar", response);
    }
}
