package br.com.infnet.bibliotecafacil.catalogo.api.controller;

import br.com.infnet.bibliotecafacil.catalogo.api.dto.AutorRequestDto;
import br.com.infnet.bibliotecafacil.catalogo.api.dto.AutorResponseDto;
import br.com.infnet.bibliotecafacil.catalogo.aplicacao.service.AutorService;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Autor;
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
@RequestMapping("/api/autores")
@Tag(name = "Autores")
public final class AutorController {

    private final AutorService autorService;

    public AutorController(final AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    public List<AutorResponseDto> listar() {
        return this.autorService.listar().stream()
                .map(AutorResponseDto::de)
                .toList();
    }

    @GetMapping("/busca")
    public List<AutorResponseDto> buscarPorNome(
            final @RequestParam String nome,
            final @ParameterObject @SortDefault(sort = "nome", direction = Sort.Direction.ASC) Sort ordenacao) {
        return this.autorService.buscarPorNome(nome, ordenacao).stream()
                .map(AutorResponseDto::de)
                .toList();
    }

    @GetMapping("/{id}")
    public AutorResponseDto obterPorId(final @PathVariable Long id) {
        return AutorResponseDto.de(this.autorService.obterPorId(id));
    }

    @PostMapping
    public ResponseEntity<AutorResponseDto> incluir(final @Valid @RequestBody AutorRequestDto request) {
        final Autor autor = this.criarAutor(request);
        final Autor autorIncluido = this.autorService.incluir(autor);
        return ResponseEntity.created(URI.create("/api/autores/" + autorIncluido.getId()))
                .body(AutorResponseDto.de(autorIncluido));
    }

    @PutMapping("/{id}")
    public AutorResponseDto alterar(
            final @PathVariable Long id,
            final @Valid @RequestBody AutorRequestDto request) {
        final Autor autor = this.criarAutor(request);
        autor.setId(id);
        return AutorResponseDto.de(this.autorService.alterar(autor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(final @PathVariable Long id) {
        this.autorService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private Autor criarAutor(final AutorRequestDto request) {
        final Autor autor = new Autor();
        autor.setNome(request.nome());
        autor.setNomeCatalogacao(request.nomeCatalogacao());
        return autor;
    }
}
