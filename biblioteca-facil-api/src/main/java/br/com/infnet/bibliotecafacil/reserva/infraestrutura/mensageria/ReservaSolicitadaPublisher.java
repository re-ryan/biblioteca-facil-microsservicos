package br.com.infnet.bibliotecafacil.reserva.infraestrutura.mensageria;

import br.com.infnet.bibliotecafacil.reserva.aplicacao.evento.ReservaSolicitadaEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@ConditionalOnProperty(
        name = "mensageria.publicacao-habilitada",
        havingValue = "true",
        matchIfMissing = true)
public final class ReservaSolicitadaPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final String exchange;
    private final String routingKey;

    public ReservaSolicitadaPublisher(
            final RabbitTemplate rabbitTemplate,
            final ObjectMapper objectMapper,
            final @Value("${mensageria.exchange}") String exchange,
            final @Value("${mensageria.routing-keys.reserva-solicitada}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publicar(final ReservaSolicitadaEvent evento) {
        try {
            final Message mensagem = MessageBuilder
                    .withBody(this.objectMapper.writeValueAsBytes(evento))
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                    .setMessageId(evento.eventoId().toString())
                    .build();
            this.rabbitTemplate.send(this.exchange, this.routingKey, mensagem);
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Não foi possível serializar o evento de reserva solicitada.",
                    exception);
        }
    }
}
