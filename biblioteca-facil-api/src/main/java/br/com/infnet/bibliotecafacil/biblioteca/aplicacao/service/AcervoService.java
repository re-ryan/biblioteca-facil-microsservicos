package br.com.infnet.bibliotecafacil.biblioteca.aplicacao.service;

import br.com.infnet.bibliotecafacil.biblioteca.dominio.Acervo;
import br.com.infnet.bibliotecafacil.biblioteca.infraestrutura.repository.AcervoRepository;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.DadosInvalidosException;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.ObjetoNaoEncontradoException;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.OperacaoNaoPermitidaException;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AcervoService {

    private final AcervoRepository acervoRepository;

    public AcervoService(final AcervoRepository acervoRepository) {
        this.acervoRepository = acervoRepository;
    }

    @Transactional
    public Acervo obterDisponivelParaReserva(final Long id) {
        final Acervo acervo = this.obterComBloqueio(id);
        this.validarDisponibilidade(acervo);
        return acervo;
    }

    @Transactional
    public void reservarUnidade(final Acervo acervo) {
        this.validarDisponibilidade(acervo);
        this.atualizarQuantidadeDisponivel(acervo, acervo.getQuantidadeDisponivel() - 1);
    }

    @Transactional
    public void liberarUnidade(final Long id) {
        final Acervo acervo = this.obterComBloqueio(id);
        if (acervo.getQuantidadeDisponivel() >= acervo.getQuantidadeReal()) {
            throw new OperacaoNaoPermitidaException(
                    "Todas as unidades do acervo já estão disponíveis.");
        }
        this.atualizarQuantidadeDisponivel(acervo, acervo.getQuantidadeDisponivel() + 1);
    }

    private Acervo obterComBloqueio(final Long id) {
        if (id == null) {
            throw new DadosInvalidosException("O identificador do acervo é obrigatório.");
        }
        return this.acervoRepository.buscarPorIdComBloqueio(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        "Acervo não encontrado para o identificador %s.".formatted(id)));
    }

    private void validarDisponibilidade(final Acervo acervo) {
        if (acervo == null) {
            throw new DadosInvalidosException("O acervo é obrigatório.");
        }
        if (!acervo.getBiblioteca().isAtiva()) {
            throw new OperacaoNaoPermitidaException(
                    "A biblioteca precisa estar ativa para realizar uma reserva.");
        }
        if (!acervo.isAtivo()) {
            throw new OperacaoNaoPermitidaException(
                    "O acervo precisa estar ativo para realizar uma reserva.");
        }
        if (!acervo.getLivro().isAtivo()) {
            throw new OperacaoNaoPermitidaException(
                    "O livro precisa estar ativo para realizar uma reserva.");
        }
        if (acervo.getQuantidadeDisponivel() <= 0) {
            throw new OperacaoNaoPermitidaException(
                    "Não há unidade disponível para reserva na biblioteca selecionada.");
        }
    }

    private void atualizarQuantidadeDisponivel(final Acervo acervo, final int quantidade) {
        acervo.setQuantidadeDisponivel(quantidade);
        final LocalDateTime agora = LocalDateTime.now();
        final LocalDateTime dataAtualizacao = agora.isAfter(acervo.getDataAtualizacao())
                ? agora
                : acervo.getDataAtualizacao().plusNanos(1);
        acervo.setDataAtualizacao(dataAtualizacao);
    }
}
