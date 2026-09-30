package br.com.infnet.bibliotecafacil.usuario.api.controller;

import br.com.infnet.bibliotecafacil.usuario.api.dto.UsuarioRequestDto;
import br.com.infnet.bibliotecafacil.usuario.api.dto.UsuarioResponseDto;
import br.com.infnet.bibliotecafacil.usuario.aplicacao.command.UsuarioCommand;
import br.com.infnet.bibliotecafacil.usuario.aplicacao.service.CadastroUsuarioService;
import br.com.infnet.bibliotecafacil.usuario.aplicacao.service.UsuarioService;
import br.com.infnet.bibliotecafacil.usuario.dominio.Usuario;
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
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios")
public final class UsuarioController {

    private final UsuarioService usuarioService;
    private final CadastroUsuarioService cadastroUsuarioService;

    public UsuarioController(
            final UsuarioService usuarioService,
            final CadastroUsuarioService cadastroUsuarioService) {
        this.usuarioService = usuarioService;
        this.cadastroUsuarioService = cadastroUsuarioService;
    }

    @GetMapping
    public List<UsuarioResponseDto> listar() {
        return this.usuarioService.listar().stream()
                .map(UsuarioResponseDto::de)
                .toList();
    }

    @GetMapping("/busca")
    public List<UsuarioResponseDto> buscarPorNome(
            final @RequestParam String nome,
            final @ParameterObject @SortDefault(sort = "nomeCompleto", direction = Sort.Direction.ASC) Sort ordenacao) {
        return this.usuarioService.buscarPorNome(nome, ordenacao).stream()
                .map(UsuarioResponseDto::de)
                .toList();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDto obterPorId(final @PathVariable Long id) {
        return UsuarioResponseDto.de(this.usuarioService.obterPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> incluir(
            final @Valid @RequestBody UsuarioRequestDto request) {
        final Usuario usuarioIncluido = this.cadastroUsuarioService.incluir(this.criarCommand(request));
        return ResponseEntity.created(URI.create("/api/usuarios/" + usuarioIncluido.getId()))
                .body(UsuarioResponseDto.de(usuarioIncluido));
    }

    @PutMapping("/{id}")
    public UsuarioResponseDto alterar(
            final @PathVariable Long id,
            final @Valid @RequestBody UsuarioRequestDto request) {
        return UsuarioResponseDto.de(
                this.cadastroUsuarioService.alterar(id, this.criarCommand(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(final @PathVariable Long id) {
        this.usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private UsuarioCommand criarCommand(final UsuarioRequestDto request) {
        return new UsuarioCommand(
                request.nomeCompleto(),
                request.dataNascimento(),
                request.login(),
                request.email(),
                request.senhaHash(),
                request.tipoUsuario(),
                request.bibliotecaId());
    }
}
