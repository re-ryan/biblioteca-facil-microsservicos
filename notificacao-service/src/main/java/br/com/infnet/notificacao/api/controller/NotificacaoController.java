package br.com.infnet.notificacao.api.controller;

import br.com.infnet.notificacao.api.dto.NotificacaoResponseDto;
import br.com.infnet.notificacao.aplicacao.service.NotificacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notificacoes")
@Tag(name = "Notificações")
public final class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(final NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @GetMapping
    public List<NotificacaoResponseDto> listar() {
        return this.notificacaoService.listar();
    }
}
