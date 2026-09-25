# Biblioteca Facil - Arquiteturas Avancadas

Projeto da disciplina **Arquiteturas Avancadas de Software com Microsservicos e Spring Framework**. A solucao evolui o Biblioteca Facil de um monolito modular para uma arquitetura distribuida ao longo de quatro etapas verificaveis no Git.

## Estado atual

**Etapa 1 implementada e em validacao final.** O sistema permanece como uma unica aplicacao Spring Boot, agora organizada por capacidades de negocio. Nenhum microsservico foi extraido nesta etapa.

## Problema de negocio

O Biblioteca Facil permite que leitores encontrem livros fisicos em bibliotecas parceiras e solicitem reservas para retirada presencial. Bibliotecarios mantem a disponibilidade agregada do acervo e processam as solicitacoes recebidas.

## Modulos de negocio

| Modulo | Responsabilidade |
| --- | --- |
| Catalogo | Mantem livros, autores, categorias, autoria e a consulta de metadados bibliograficos por ISBN. |
| Bibliotecas e acervo | Mantem bibliotecas, enderecos e a quantidade disponivel de cada livro por biblioteca. |
| Usuarios | Mantem leitores, bibliotecarios e administradores, incluindo o vinculo do bibliotecario com uma biblioteca. |
| Reservas | Solicita, confirma e rejeita reservas, preservando as regras de disponibilidade e responsabilidade da biblioteca. |

Cada modulo possui internamente suas camadas de API, aplicacao, dominio e infraestrutura, conforme a necessidade. As regras de negocio ficam nos servicos de aplicacao; controllers tratam HTTP e repositories tratam persistencia.

## Dependencias entre modulos

- `Reservas -> Usuarios`: o fluxo de reserva consulta leitores e bibliotecarios por meio de `UsuarioService`.
- `Reservas -> Bibliotecas e acervo`: o fluxo reserva ou libera uma unidade por meio de `AcervoService`.
- `Bibliotecas e acervo -> Catalogo`: um item de acervo associa uma biblioteca a um livro do catalogo.
- `Cadastro de livro -> Consulta ISBN`: `CadastroLivroService` usa a consulta de metadados quando recebe somente o ISBN.

Essas dependencias passam pelas fronteiras de aplicacao dos modulos. Controllers nao acessam repositories, e o modulo de reservas nao acessa diretamente a persistencia de usuarios ou de acervo.

## Candidato a servico independente

A funcionalidade escolhida para a Etapa 2 e a **consulta de metadados bibliograficos por ISBN**.

- Responsabilidade: receber um ISBN, consultar a BrasilAPI e devolver dados bibliograficos normalizados.
- Motivo para separacao: e uma integracao externa, sem estado e sem necessidade de acesso ao banco da aplicacao principal; pode evoluir, escalar e tratar falhas independentemente.
- Dependente atual: o cadastro de livros, implementado por `CadastroLivroService`.
- Situacao na Etapa 1: continua interna ao `biblioteca-facil-api`; a comunicacao remota entre aplicacoes so sera criada na Etapa 2.

## Arquitetura da Etapa 1

```text
Cliente HTTP
    |
    v
Biblioteca Facil API (unica aplicacao Spring Boot)
    |-- Catalogo
    |-- Bibliotecas e acervo
    |-- Usuarios
    `-- Reservas
          |
          v
          H2

Catalogo -- OpenFeign --> BrasilAPI
```

## Projetos

| Projeto | Responsabilidade | Etapa de introducao |
| --- | --- | --- |
| `biblioteca-facil-api` | Aplicacao principal: catalogo, bibliotecas, usuarios, acervo e reservas | Etapa 1 |
| Servico de consulta ISBN | Sera extraido da aplicacao principal depois do marco da Etapa 1 | Etapa 2 |
| Config Server | Centralizara configuracoes das aplicacoes | Etapa 3 |

## Estrutura do repositorio

```text
.
|-- biblioteca-facil-api/     # Aplicacao principal Spring Boot
`-- pom.xml                   # Agregador Maven do monorepositorio
```

## Requisitos e execucao

- Java 21;
- Maven 3.9 ou superior.

Na raiz do repositorio:

```bash
mvn test
mvn -pl biblioteca-facil-api spring-boot:run
```

Com a aplicacao ativa:

- API: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI: `http://localhost:8080/v3/api-docs`

A colecao de apoio para validacao manual esta em `biblioteca-facil-api/postman/Biblioteca-Facil-Baseline.postman_collection.json`.

## Evolucao por etapas

1. Organizar a aplicacao principal por funcionalidades e registrar `etapa-1`.
2. Extrair a consulta ISBN, integrar por REST/OpenFeign e registrar `etapa-2`.
3. Adicionar configuracao centralizada, banco relacional e execucao com containers e registrar `etapa-3`.
4. Adicionar mensageria e Spring Batch e registrar `etapa-4`.

As tags deste repositorio pertencerao exclusivamente a esta disciplina. O historico e as tags do projeto anterior nao foram importados.
