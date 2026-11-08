package br.com.infnet.bibliotecafacil.catalogo.api.controller;

import br.com.infnet.bibliotecafacil.catalogo.api.dto.LivroCatalogoResponseDto;
import br.com.infnet.bibliotecafacil.catalogo.aplicacao.service.CatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalogo")
@Tag(name = "Catálogo")
public final class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(final CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping
    @Operation(summary = "Consulta o catálogo disponível para leitores")
    public List<LivroCatalogoResponseDto> buscar(
            final @RequestParam(required = false) String titulo,
            final @RequestParam(required = false) String autor,
            final @RequestParam(required = false) String categoria) {
        return this.catalogoService.buscar(titulo, autor, categoria).stream()
                .map(LivroCatalogoResponseDto::de)
                .toList();
    }
}
