# Biblioteca Fácil API

Aplicação principal do Biblioteca Fácil. Ela mantém catálogo, bibliotecas, usuários, acervo e reservas, organizada internamente por capacidade de negócio. A consulta de metadados por ISBN é feita pelo projeto independente `consulta-isbn-service`.

## Persistência

Em execução normal, a aplicação usa PostgreSQL. O Flyway aplica as migrações de `src/main/resources/db/migration` e o Hibernate valida o esquema com `ddl-auto=validate`. O H2 permanece somente no escopo de testes, para manter os testes automatizados rápidos e isolados.

Este é o único serviço que acessa esse banco. O serviço ISBN não compartilha entidades, repositories ou tabelas com a aplicação principal.

Os controllers retornam DTOs de saída em vez de entidades JPA. Por isso, `spring.jpa.open-in-view` fica desativado e a serialização HTTP não mantém a sessão do Hibernate aberta durante toda a requisição.

## Configuração

O arquivo local `application.yml` informa o nome da aplicação, o perfil ativo e o endereço do Config Server. Portas, URL do serviço ISBN, timeouts e parâmetros de persistência são obtidos centralmente. URL, usuário e senha do banco podem ser sobrescritos por:

- `SPRING_DATASOURCE_URL`;
- `SPRING_DATASOURCE_USERNAME`;
- `SPRING_DATASOURCE_PASSWORD`.

O perfil padrão é `dev`; o Docker Compose ativa `prod`.

A consulta ao serviço ISBN possui até três tentativas por padrão porque é uma operação GET idempotente. O limite e o intervalo são configurados por `CONSULTA_ISBN_MAX_TENTATIVAS` e `CONSULTA_ISBN_INTERVALO_RETRY_MS`. ISBN não encontrado não é repetido.

## Execução

A forma recomendada é iniciar o conjunto pela raiz do repositório:

```bash
docker compose up --build -d
```

Para executar pelo Maven, inicie antes o Config Server e disponibilize um PostgreSQL compatível com as variáveis de ambiente:

```bash
mvn -pl biblioteca-facil-api spring-boot:run
```

## API

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI: `http://localhost:8080/v3/api-docs`
- Saúde: `http://localhost:8080/actuator/health`

Recursos principais:

- `/api/autores`
- `/api/categorias`
- `/api/livros`
- `/api/bibliotecas`
- `/api/usuarios`
- `/api/reservas`

## Testes

Na raiz do repositório ou deste módulo:

```bash
mvn test
```
