package br.com.infnet.bibliotecafacil.biblioteca.api.controller;

import br.com.infnet.bibliotecafacil.biblioteca.api.dto.BibliotecaRequestDto;
import br.com.infnet.bibliotecafacil.biblioteca.api.dto.BibliotecaResponseDto;
import br.com.infnet.bibliotecafacil.biblioteca.aplicacao.service.BibliotecaService;
import br.com.infnet.bibliotecafacil.biblioteca.dominio.Biblioteca;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bibliotecas")
@Tag(name = "Bibliotecas")
public final class BibliotecaController {

    private final BibliotecaService bibliotecaService;

    public BibliotecaController(final BibliotecaService bibliotecaService) {
        this.bibliotecaService = bibliotecaService;
    }

    @GetMapping
    public List<BibliotecaResponseDto> listar() {
        return this.bibliotecaService.listar().stream()
                .map(BibliotecaResponseDto::de)
                .toList();
    }

    @GetMapping("/busca")
    public List<BibliotecaResponseDto> buscarPorNome(
            final @RequestParam String nome,
            final @ParameterObject @SortDefault(sort = "nome", direction = Sort.Direction.ASC) Sort ordenacao) {
        return this.bibliotecaService.buscarPorNome(nome, ordenacao).stream()
                .map(BibliotecaResponseDto::de)
                .toList();
    }

    @GetMapping("/{id}")
    public BibliotecaResponseDto obterPorId(final @PathVariable Long id) {
        return BibliotecaResponseDto.de(this.bibliotecaService.obterPorId(id));
    }

    @PostMapping
    public ResponseEntity<BibliotecaResponseDto> incluir(
            final @Valid @RequestBody BibliotecaRequestDto request) {
        final Biblioteca biblioteca = this.criarBiblioteca(request);
        final Biblioteca bibliotecaIncluida = this.bibliotecaService.incluir(biblioteca);
        return ResponseEntity.created(URI.create("/api/bibliotecas/" + bibliotecaIncluida.getId()))
                .body(BibliotecaResponseDto.de(bibliotecaIncluida));
    }

    @PutMapping("/{id}")
    public BibliotecaResponseDto alterar(
            final @PathVariable Long id,
            final @Valid @RequestBody BibliotecaRequestDto request) {
        final Biblioteca biblioteca = this.criarBiblioteca(request);
        biblioteca.setId(id);
        return BibliotecaResponseDto.de(this.bibliotecaService.alterar(biblioteca));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(final @PathVariable Long id) {
        this.bibliotecaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private Biblioteca criarBiblioteca(final BibliotecaRequestDto request) {
        final Biblioteca biblioteca = new Biblioteca();
        biblioteca.setNome(request.nome());
        biblioteca.setCpfCnpj(request.cpfCnpj());
        biblioteca.setEmail(request.email());
        biblioteca.setTelefone(request.telefone());
        if (request.endereco() != null) {
            biblioteca.setEndereco(request.endereco());
        }
        return biblioteca;
    }
}
