package br.com.infnet.bibliotecafacil.catalogo.importacao.api.controller;

import br.com.infnet.bibliotecafacil.catalogo.importacao.api.dto.ImportacaoLivrosResponseDto;
import br.com.infnet.bibliotecafacil.catalogo.importacao.aplicacao.service.ImportacaoLivrosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/importacoes/livros")
@Tag(name = "Importação de livros")
public final class ImportacaoLivrosController {

    private final ImportacaoLivrosService importacaoLivrosService;

    public ImportacaoLivrosController(final ImportacaoLivrosService importacaoLivrosService) {
        this.importacaoLivrosService = importacaoLivrosService;
    }

    @PostMapping
    @Operation(summary = "Importa o catálogo a partir do CSV configurado")
    public ResponseEntity<ImportacaoLivrosResponseDto> importar() {
        return ResponseEntity.ok(this.importacaoLivrosService.importar());
    }
}
