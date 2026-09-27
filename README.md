# Biblioteca Fácil

O Biblioteca Fácil é uma API para consulta de livros e reserva de exemplares em bibliotecas parceiras. O sistema mantém um catálogo central, registra a disponibilidade de cada livro por biblioteca e permite que leitores solicitem reservas para retirada presencial.

Este repositório acompanha a evolução do projeto na disciplina de Arquiteturas Avançadas de Software com Microsserviços e Spring Framework. A primeira etapa partiu de um monólito modular; na segunda, a integração responsável pela consulta de ISBN foi separada em outro serviço.

## Serviços

| Aplicação | Porta | Responsabilidade |
| --- | ---: | --- |
| `biblioteca-facil-api` | 8080 | Catálogo, bibliotecas, usuários, acervo e reservas |
| `consulta-isbn-service` | 8081 | Consulta de metadados bibliográficos na BrasilAPI |

As aplicações possuem projetos Maven e processos independentes. O `pom.xml` da raiz funciona apenas como agregador, permitindo compilar e testar todo o conjunto com um único comando.

## Arquitetura atual

```text
Cliente
   |
   v
Biblioteca Fácil API :8080 ----> H2
   |
   | REST / OpenFeign
   v
Consulta ISBN Service :8081
   |
   | REST / OpenFeign
   v
BrasilAPI
```

O cadastro de um livro pode usar o serviço de ISBN para completar título, editora, autores e outros dados bibliográficos. A aplicação principal não conhece o contrato da BrasilAPI: ela conversa apenas com o contrato publicado por `consulta-isbn-service`.

Quando um ISBN não é encontrado, o cadastro ainda pode continuar com os dados informados pelo usuário. Se o serviço de consulta estiver indisponível, a API principal responde com HTTP 503 antes de persistir o livro.

## Executando localmente

Pré-requisitos:

- Java 21;
- Maven 3.9 ou superior.

Primeiro, compile o projeto e execute os testes:

```bash
mvn clean verify
```

Depois, inicie cada aplicação em um terminal diferente, a partir da raiz do repositório:

```bash
mvn -pl consulta-isbn-service spring-boot:run
```

```bash
mvn -pl biblioteca-facil-api spring-boot:run
```

O serviço de ISBN deve estar disponível antes de testar um cadastro de livro que dependa do enriquecimento bibliográfico.

## Documentação das APIs

| Aplicação | Swagger UI | OpenAPI |
| --- | --- | --- |
| Biblioteca Fácil API | [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) | [localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs) |
| Consulta ISBN Service | [localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) | [localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs) |

A consulta isolada pode ser feita por:

```http
GET http://localhost:8081/api/isbn/9788535902778
```

A coleção [Biblioteca-Facil-Etapa-2.postman_collection.json](postman/Biblioteca-Facil-Etapa-2.postman_collection.json) contém exemplos do serviço isolado, do fluxo integrado e do comportamento quando o serviço fica indisponível.

## Estrutura do repositório

```text
.
|-- biblioteca-facil-api/     # Aplicação principal
|-- consulta-isbn-service/    # Integração bibliográfica
|-- postman/                  # Coleções para testes manuais
`-- pom.xml                   # Agregador Maven
```

## Decisões e limitações atuais

- Os serviços mantêm DTOs próprios para o contrato HTTP e não compartilham entidades JPA.
- Endereços e timeouts das integrações ficam nos arquivos de configuração, fora do código Java.
- A aplicação principal ainda utiliza H2. A troca por um banco persistente faz parte da próxima etapa.
- Retry, circuit breaker e descoberta de serviços ainda não foram adicionados.
- O projeto não controla empréstimos, devoluções, multas ou exemplares individuais.

## Evolução do projeto

O histórico pode ser consultado pelas tags:

- `etapa-1`: organização da aplicação como monólito modular;
- `etapa-2`: extração da consulta ISBN e comunicação entre os serviços.

As próximas etapas incluem configuração centralizada, banco relacional, containers, mensageria e processamento em lote.
