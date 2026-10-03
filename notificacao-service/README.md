# Serviço de Notificações

O `notificacao-service` consome eventos de reservas publicados pela aplicação principal no RabbitMQ. O processamento simula a comunicação enviada ao leitor e mantém uma visão temporária das notificações processadas para facilitar a demonstração acadêmica.

O serviço não acessa o PostgreSQL da aplicação principal. Todos os dados necessários para sua responsabilidade chegam no evento `RESERVA_SOLICITADA`.

Endpoints:

- `GET /api/notificacoes`: lista as notificações processadas nesta execução;
- `GET /actuator/health`: informa a saúde do serviço e da conexão com o RabbitMQ.

As configurações de porta, broker, exchange, fila e routing key são fornecidas pelo Config Server.
