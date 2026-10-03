package br.com.infnet.notificacao.mensageria;

import br.com.infnet.notificacao.aplicacao.service.NotificacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public final class ReservaSolicitadaConsumer {

    private final ObjectMapper objectMapper;
    private final NotificacaoService notificacaoService;

    public ReservaSolicitadaConsumer(
            final ObjectMapper objectMapper,
            final NotificacaoService notificacaoService) {
        this.objectMapper = objectMapper;
        this.notificacaoService = notificacaoService;
    }

    @RabbitListener(queues = "${mensageria.filas.reserva-solicitada}")
    public void consumir(final byte[] payload) {
        try {
            final ReservaSolicitadaMessage evento = this.objectMapper.readValue(
                    payload,
                    ReservaSolicitadaMessage.class);
            this.notificacaoService.registrar(evento);
        } catch (final IOException exception) {
            throw new AmqpRejectAndDontRequeueException(
                    "A mensagem de reserva solicitada possui formato inválido.",
                    exception);
        }
    }
}
