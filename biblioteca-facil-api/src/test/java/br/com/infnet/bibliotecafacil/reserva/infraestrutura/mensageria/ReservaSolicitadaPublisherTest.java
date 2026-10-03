package br.com.infnet.bibliotecafacil.reserva.infraestrutura.mensageria;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import br.com.infnet.bibliotecafacil.reserva.aplicacao.evento.ReservaSolicitadaEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

class ReservaSolicitadaPublisherTest {

    @Test
    void devePublicarJsonPersistenteComIdentificadorDoEvento() throws Exception {
        final RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        final ReservaSolicitadaPublisher publisher = new ReservaSolicitadaPublisher(
                rabbitTemplate, objectMapper, "biblioteca.reservas", "reserva.solicitada");
        final ReservaSolicitadaEvent evento = new ReservaSolicitadaEvent(
                UUID.randomUUID(),
                "RESERVA_SOLICITADA",
                10L,
                20L,
                "leitor@exemplo.com",
                "Clean Architecture",
                "Biblioteca Central",
                LocalDateTime.now());
        final ArgumentCaptor<Message> mensagemCaptor = ArgumentCaptor.forClass(Message.class);

        publisher.publicar(evento);

        verify(rabbitTemplate).send(
                eq("biblioteca.reservas"),
                eq("reserva.solicitada"),
                mensagemCaptor.capture());
        final Message mensagem = mensagemCaptor.getValue();
        assertThat(mensagem.getMessageProperties().getDeliveryMode())
                .isEqualTo(MessageDeliveryMode.PERSISTENT);
        assertThat(mensagem.getMessageProperties().getMessageId())
                .isEqualTo(evento.eventoId().toString());
        assertThat(objectMapper.readTree(mensagem.getBody()).get("reservaId").asLong())
                .isEqualTo(10L);
    }
}
