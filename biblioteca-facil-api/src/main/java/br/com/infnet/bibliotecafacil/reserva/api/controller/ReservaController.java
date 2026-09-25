package br.com.infnet.bibliotecafacil.reserva.api.controller;

import br.com.infnet.bibliotecafacil.reserva.api.dto.ProcessamentoReservaRequestDto;
import br.com.infnet.bibliotecafacil.reserva.api.dto.ReservaRequestDto;
import br.com.infnet.bibliotecafacil.reserva.api.dto.ReservaResponseDto;
import br.com.infnet.bibliotecafacil.reserva.aplicacao.service.ReservaService;
import br.com.infnet.bibliotecafacil.reserva.dominio.Reserva;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservas")
@Tag(name = "Reservas")
public final class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(final ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public List<ReservaResponseDto> listar() {
        return this.reservaService.listar().stream()
                .map(this::converterParaResposta)
                .toList();
    }

    @GetMapping("/{id}")
    public ReservaResponseDto obterPorId(final @PathVariable Long id) {
        return this.converterParaResposta(this.reservaService.obterPorId(id));
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDto> solicitar(final @Valid @RequestBody ReservaRequestDto request) {
        final Reserva reserva = this.reservaService.solicitar(request.leitorId(), request.acervoId());
        return ResponseEntity.created(URI.create("/api/reservas/" + reserva.getId()))
                .body(this.converterParaResposta(reserva));
    }

    @PutMapping("/{id}/confirmacao")
    public ReservaResponseDto confirmar(
            final @PathVariable Long id,
            final @Valid @RequestBody ProcessamentoReservaRequestDto request) {
        return this.converterParaResposta(
                this.reservaService.confirmar(id, request.bibliotecarioId()));
    }

    @PutMapping("/{id}/rejeicao")
    public ReservaResponseDto rejeitar(
            final @PathVariable Long id,
            final @Valid @RequestBody ProcessamentoReservaRequestDto request) {
        return this.converterParaResposta(this.reservaService.rejeitar(id, request.bibliotecarioId()));
    }

    private ReservaResponseDto converterParaResposta(final Reserva reserva) {
        return new ReservaResponseDto(
                reserva.getId(),
                reserva.getLeitor().getId(),
                reserva.getLeitor().getNomeCompleto(),
                reserva.getAcervo().getId(),
                reserva.getAcervo().getBiblioteca().getId(),
                reserva.getAcervo().getBiblioteca().getNome(),
                reserva.getAcervo().getLivro().getId(),
                reserva.getAcervo().getLivro().getTitulo(),
                reserva.getDataReserva(),
                reserva.getStatus());
    }
}
