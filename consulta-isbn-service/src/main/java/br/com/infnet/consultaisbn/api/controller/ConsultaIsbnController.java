package br.com.infnet.consultaisbn.api.controller;

import br.com.infnet.consultaisbn.api.dto.MetadadosLivroResponseDto;
import br.com.infnet.consultaisbn.aplicacao.service.ConsultaIsbnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/isbn")
@Tag(name = "Consulta ISBN", description = "Consulta metadados bibliográficos por ISBN-13.")
public class ConsultaIsbnController {

    private final ConsultaIsbnService consultaIsbnService;

    public ConsultaIsbnController(final ConsultaIsbnService consultaIsbnService) {
        this.consultaIsbnService = consultaIsbnService;
    }

    @GetMapping("/{isbn}")
    @Operation(summary = "Consulta metadados de um livro pelo ISBN-13")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Metadados encontrados"),
            @ApiResponse(responseCode = "400", description = "ISBN inválido"),
            @ApiResponse(responseCode = "404", description = "ISBN não encontrado"),
            @ApiResponse(responseCode = "502", description = "Provedor bibliográfico indisponível")
    })
    public MetadadosLivroResponseDto consultar(
            final @PathVariable
            @Pattern(regexp = "97[89]\\d{10}", message = "O ISBN-13 deve conter 13 dígitos válidos.")
            String isbn) {
        return this.consultaIsbnService.consultar(isbn);
    }
}
