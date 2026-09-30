# Consulta ISBN Service

Aplicação Spring Boot independente responsável por consultar metadados bibliográficos por ISBN-13 e traduzir a resposta da BrasilAPI para um contrato controlado pelo Biblioteca Fácil.

## Responsabilidade

O serviço recebe um ISBN-13 pela API REST, consulta o provedor bibliográfico e devolve apenas ISBN, título, editora, descrição, ano de publicação e URL da capa. Ele é stateless: não possui banco de dados e não acessa entidades ou tabelas da aplicação principal.

## Configuração e execução

O endereço do provedor, os timeouts do cliente e a porta são entregues pelo Config Server. O arquivo local seleciona o perfil `dev` por padrão; o Docker Compose usa `prod` e resolve o Config Server pelo nome do container.

Falhas temporárias da BrasilAPI são repetidas até três vezes por padrão. Como a operação é uma consulta GET, a repetição não altera estado. O limite e o intervalo podem ser ajustados por `BRASIL_API_MAX_TENTATIVAS` e `BRASIL_API_INTERVALO_RETRY_MS`; respostas 404 continuam sendo tratadas diretamente, sem retry.

A forma recomendada de iniciar todo o ambiente está no README da raiz. Para executar apenas este módulo pelo Maven, mantenha o Config Server ativo e rode:

```bash
mvn -pl consulta-isbn-service spring-boot:run
```

## API

```http
GET /api/isbn/{isbn}
```

- Swagger UI: `http://localhost:8081/swagger-ui.html`;
- OpenAPI: `http://localhost:8081/v3/api-docs`;
- Saúde: `http://localhost:8081/actuator/health`.

Respostas principais:

- `200`: metadados encontrados;
- `400`: formato de ISBN-13 inválido;
- `404`: ISBN não encontrado;
- `502`: falha ao acessar o provedor bibliográfico;
- `500`: erro interno inesperado, sem exposição de detalhes internos.
