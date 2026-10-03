package br.com.infnet.bibliotecafacil.reserva.infraestrutura.mensageria;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    @Bean
    DirectExchange reservasExchange(final @Value("${mensageria.exchange}") String nomeExchange) {
        return new DirectExchange(nomeExchange, true, false);
    }

    @Bean
    Queue reservaSolicitadaQueue(
            final @Value("${mensageria.filas.reserva-solicitada}") String nomeFila) {
        return QueueBuilder.durable(nomeFila).build();
    }

    @Bean
    Binding reservaSolicitadaBinding(
            final Queue reservaSolicitadaQueue,
            final DirectExchange reservasExchange,
            final @Value("${mensageria.routing-keys.reserva-solicitada}") String routingKey) {
        return BindingBuilder.bind(reservaSolicitadaQueue)
                .to(reservasExchange)
                .with(routingKey);
    }
}
