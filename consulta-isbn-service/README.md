# Consulta ISBN Service

Aplicacao Spring Boot independente responsavel por consultar metadados bibliograficos por ISBN-13 e traduzir a resposta da BrasilAPI para um contrato controlado pelo Biblioteca Facil.

## Responsabilidade

O servico recebe um ISBN-13 pela API REST, consulta o provedor bibliografico e devolve apenas ISBN, titulo, editora, descricao, ano de publicacao e URL da capa. Ele nao possui banco de dados e nao acessa entidades ou tabelas da aplicacao principal.

## Execucao

Na raiz do monorepositorio:

```bash
mvn -pl consulta-isbn-service spring-boot:run
```

O servico usa a porta `8081`.

## API

```http
GET /api/isbn/{isbn}
```

- Swagger UI: `http://localhost:8081/swagger-ui.html`;
- OpenAPI: `http://localhost:8081/v3/api-docs`.

## Respostas

- `200`: metadados encontrados;
- `400`: formato de ISBN-13 invalido;
- `404`: ISBN nao encontrado;
- `502`: falha ao acessar o provedor bibliografico;
- `500`: erro interno inesperado, sem exposicao de detalhes internos.

## Configuracao

O endereco do provedor fica em `integracao.brasilapi.url`. Os timeouts do cliente Feign tambem sao configurados externamente em `application.properties`.
