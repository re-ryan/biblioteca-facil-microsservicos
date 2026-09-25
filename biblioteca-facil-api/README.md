# Biblioteca Facil API

Aplicacao Spring Boot que compoe integralmente a Etapa 1. Ela mantem o catalogo, as bibliotecas, os usuarios, o acervo e as reservas em um unico processo, organizado internamente por capacidade de negocio.

## Estrutura interna

```text
br.com.infnet.bibliotecafacil
|-- catalogo/
|   |-- api/
|   |-- aplicacao/
|   |-- dominio/
|   `-- infraestrutura/
|-- biblioteca/
|   |-- api/
|   |-- aplicacao/
|   |-- dominio/
|   `-- infraestrutura/
|-- usuario/
|   |-- api/
|   |-- aplicacao/
|   |-- dominio/
|   `-- infraestrutura/
|-- reserva/
|   |-- api/
|   |-- aplicacao/
|   |-- dominio/
|   `-- infraestrutura/
|-- compartilhado/
`-- bootstrap/
```

As pastas internas sao criadas conforme a necessidade de cada modulo:

- `api`: controllers e contratos HTTP;
- `aplicacao`: casos de uso, coordenacao e regras da aplicacao;
- `dominio`: entidades, enums e valores do dominio;
- `infraestrutura`: repositories JPA e clientes externos.

## Fronteiras relevantes

- `ReservaService` consulta usuarios por `UsuarioService` e altera disponibilidade por `AcervoService`.
- `CadastroUsuarioService` concentra a criacao dos tipos de usuario e o vinculo de bibliotecarios.
- Controllers mapeiam requisicoes e respostas, sem acesso direto a repositories.
- A consulta ISBN permanece interna ao modulo Catalogo e usa OpenFeign para acessar a BrasilAPI.

## Persistencia, validacao e erros

- H2 e Spring Data JPA compoem a persistencia da Etapa 1.
- Os DTOs de entrada usam Bean Validation e os controllers ativam a validacao com `@Valid`.
- O tratamento centralizado converte validacoes, recursos inexistentes e violacoes de regras de negocio em respostas HTTP sem expor detalhes internos.
- Os repositories incluem consultas derivadas e JPQL voltadas a buscas por nome, ISBN, disponibilidade, associacoes e status de reserva.

## Execucao

Na raiz do monorepositorio:

```bash
mvn -pl biblioteca-facil-api spring-boot:run
```

Ou neste diretorio:

```bash
mvn spring-boot:run
```

## Documentacao da API

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI: `http://localhost:8080/v3/api-docs`

Recursos principais:

- `/api/autores`
- `/api/categorias`
- `/api/livros`
- `/api/bibliotecas`
- `/api/usuarios`
- `/api/reservas`

## Testes

Na raiz do monorepositorio ou deste modulo:

```bash
mvn test
```

A colecao de validacao manual esta em `postman/Biblioteca-Facil-Baseline.postman_collection.json`.
