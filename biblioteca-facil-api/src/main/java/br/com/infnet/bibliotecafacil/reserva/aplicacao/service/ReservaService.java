package br.com.infnet.bibliotecafacil.reserva.aplicacao.service;

import br.com.infnet.bibliotecafacil.biblioteca.aplicacao.service.AcervoService;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.DadosInvalidosException;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.ObjetoNaoEncontradoException;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.OperacaoNaoPermitidaException;
import br.com.infnet.bibliotecafacil.biblioteca.dominio.Acervo;
import br.com.infnet.bibliotecafacil.usuario.dominio.Bibliotecario;
import br.com.infnet.bibliotecafacil.usuario.dominio.Leitor;
import br.com.infnet.bibliotecafacil.reserva.dominio.Reserva;
import br.com.infnet.bibliotecafacil.reserva.dominio.StatusReserva;
import br.com.infnet.bibliotecafacil.usuario.dominio.Usuario;
import br.com.infnet.bibliotecafacil.reserva.infraestrutura.repository.ReservaRepository;
import br.com.infnet.bibliotecafacil.usuario.aplicacao.service.UsuarioService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import br.com.infnet.bibliotecafacil.reserva.aplicacao.evento.ReservaSolicitadaEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioService usuarioService;
    private final AcervoService acervoService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ReservaService(
            final ReservaRepository reservaRepository,
            final UsuarioService usuarioService,
            final AcervoService acervoService,
            final ApplicationEventPublisher applicationEventPublisher) {
        this.reservaRepository = reservaRepository;
        this.usuarioService = usuarioService;
        this.acervoService = acervoService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public Reserva solicitar(final Long leitorId, final Long acervoId) {
        final Leitor leitor = this.obterLeitor(leitorId);
        final Acervo acervo = this.acervoService.obterDisponivelParaReserva(acervoId);
        this.validarReservaPendenteDuplicada(leitor, acervo);
        this.acervoService.reservarUnidade(acervo);

        final Reserva reserva = new Reserva();
        reserva.setLeitor(leitor);
        reserva.setAcervo(acervo);
        reserva.setDataReserva(LocalDateTime.now());
        reserva.setStatus(StatusReserva.PENDENTE);
        final Reserva reservaSalva = this.reservaRepository.save(reserva);
        this.applicationEventPublisher.publishEvent(new ReservaSolicitadaEvent(
                UUID.randomUUID(),
                "RESERVA_SOLICITADA",
                reservaSalva.getId(),
                leitor.getId(),
                leitor.getEmail(),
                acervo.getLivro().getTitulo(),
                acervo.getBiblioteca().getNome(),
                LocalDateTime.now()));
        return reservaSalva;
    }

    @Transactional
    public Reserva confirmar(final Long reservaId, final Long bibliotecarioId) {
        final Reserva reserva = this.obterPorId(reservaId);
        this.validarProcessamento(reserva, bibliotecarioId);
        reserva.setStatus(StatusReserva.CONFIRMADA);
        return this.reservaRepository.save(reserva);
    }

    @Transactional
    public Reserva rejeitar(final Long reservaId, final Long bibliotecarioId) {
        final Reserva reserva = this.obterPorId(reservaId);
        this.validarProcessamento(reserva, bibliotecarioId);
        this.acervoService.liberarUnidade(reserva.getAcervo().getId());
        reserva.setStatus(StatusReserva.REJEITADA);
        return this.reservaRepository.save(reserva);
    }

    public Reserva obterPorId(final Long id) {
        if (id == null) {
            throw new DadosInvalidosException("O identificador da reserva é obrigatório.");
        }
        return this.reservaRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        "Reserva não encontrada para o identificador %s.".formatted(id)));
    }

    public List<Reserva> listar() {
        return List.copyOf(this.reservaRepository.findAll());
    }

    private Usuario obterUsuario(final Long id) {
        return this.usuarioService.obterPorId(id);
    }

    private Leitor obterLeitor(final Long id) {
        final Usuario usuario = this.obterUsuario(id);
        if (!(usuario instanceof Leitor leitor)) {
            throw new DadosInvalidosException("O usuário informado não é um leitor.");
        }
        if (!leitor.isAtivo()) {
            throw new OperacaoNaoPermitidaException(
                    "O leitor precisa estar ativo para solicitar uma reserva.");
        }
        return leitor;
    }

    private void validarProcessamento(final Reserva reserva, final Long bibliotecarioId) {
        final Usuario usuario = this.obterUsuario(bibliotecarioId);
        if (!(usuario instanceof Bibliotecario bibliotecario)) {
            throw new DadosInvalidosException("O usuário informado não é um bibliotecário.");
        }
        if (!bibliotecario.isAtivo()) {
            throw new OperacaoNaoPermitidaException(
                    "O bibliotecário precisa estar ativo para processar reservas.");
        }
        if (bibliotecario.getBiblioteca() == null
                || !bibliotecario.getBiblioteca().getId()
                        .equals(reserva.getAcervo().getBiblioteca().getId())) {
            throw new OperacaoNaoPermitidaException(
                    "O bibliotecário não pertence à biblioteca da reserva.");
        }
        if (reserva.getStatus() != StatusReserva.PENDENTE) {
            throw new OperacaoNaoPermitidaException(
                    "Somente uma reserva pendente pode ser processada.");
        }
    }

    private void validarReservaPendenteDuplicada(final Leitor leitor, final Acervo acervo) {
        final boolean reservaPendenteExistente = this.reservaRepository
                .existsByLeitor_IdAndAcervo_IdAndStatus(
                        leitor.getId(),
                        acervo.getId(),
                        StatusReserva.PENDENTE);
        if (reservaPendenteExistente) {
            throw new OperacaoNaoPermitidaException(
                    "O leitor já possui uma reserva pendente para este acervo.");
        }
    }

}
