package br.com.infnet.notificacao.mensageria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.infnet.notificacao.aplicacao.service.NotificacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

class ReservaSolicitadaConsumerTest {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private final NotificacaoService notificacaoService = new NotificacaoService();
    private final ReservaSolicitadaConsumer consumer = new ReservaSolicitadaConsumer(
            this.objectMapper,
            this.notificacaoService);

    @Test
    void deveProcessarMensagemDeReservaSolicitada() throws Exception {
        final ReservaSolicitadaMessage evento = new ReservaSolicitadaMessage(
                UUID.randomUUID(),
                "RESERVA_SOLICITADA",
                10L,
                20L,
                "leitor@exemplo.com",
                "Arquitetura Limpa",
                "Biblioteca Central",
                LocalDateTime.now());

        this.consumer.consumir(this.objectMapper.writeValueAsBytes(evento));

        assertEquals(1, this.notificacaoService.listar().size());
        assertEquals(10L, this.notificacaoService.listar().getFirst().reservaId());
        assertEquals("leitor@exemplo.com", this.notificacaoService.listar().getFirst().emailLeitor());
    }

    @Test
    void naoDeveReenfileirarMensagemComJsonInvalido() {
        final byte[] payloadInvalido = "{invalido".getBytes();

        assertThrows(
                AmqpRejectAndDontRequeueException.class,
                () -> this.consumer.consumir(payloadInvalido));
    }
}
