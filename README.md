# Biblioteca Fácil

O Biblioteca Fácil é uma API para consulta de livros e reserva de exemplares em bibliotecas parceiras. O sistema mantém um catálogo central, registra a disponibilidade de cada livro por biblioteca e permite que leitores solicitem reservas para retirada presencial.

Este repositório acompanha a evolução do projeto na disciplina de Arquiteturas Avançadas de Software com Microsserviços e Spring Framework. A solução começou como um monólito modular, separou a consulta de ISBN na segunda etapa e agora pode ser executada de forma reproduzível com configuração externa, PostgreSQL e containers.

## Componentes

| Componente | Porta | Responsabilidade |
| --- | ---: | --- |
| `biblioteca-facil-api` | 8080 | Catálogo, bibliotecas, usuários, acervo e reservas |
| `consulta-isbn-service` | 8081 | Consulta de metadados bibliográficos na BrasilAPI |
| `config-server` | 8888 | Configurações de ambiente das duas aplicações |
| PostgreSQL | 5432 (rede interna) | Dados pertencentes à aplicação principal |

O serviço de ISBN é stateless. Ele não possui banco próprio porque apenas consulta e traduz dados do provedor externo; também não acessa as tabelas da aplicação principal.

## Arquitetura

```text
                              +-------------------+
                              |   Config Server   |
                              |       :8888       |
                              +---------+---------+
                                        |
                         configuração   |   configuração
                    +-------------------+-------------------+
                    |                                       |
                    v                                       v
+---------+   +--------------------------+   HTTP   +-----------------------+
| Cliente |-->| Biblioteca Fácil API     |--------->| Consulta ISBN Service |
+---------+   | :8080                    |          | :8081                 |
              +------------+-------------+          +-----------+-----------+
                           |                                    |
                           v                                    v
                    +-------------+                         BrasilAPI
                    | PostgreSQL  |
                    | :5432       |
                    +-------------+
```

O cadastro de um livro pode chamar o serviço de ISBN para completar título, editora e outros dados. A aplicação principal conhece apenas o contrato publicado pelo serviço, nunca o contrato da BrasilAPI. Se o ISBN não for encontrado, o cadastro pode continuar com os dados informados; se o serviço estiver indisponível, a API responde com HTTP 503 antes de persistir o livro.

## Executando com Docker Compose

Pré-requisito: Docker Desktop ou Docker Engine com Docker Compose.

Crie a configuração local a partir do exemplo e defina uma senha para o PostgreSQL:

```bash
cp .env.example .env
```

No PowerShell, o mesmo passo pode ser feito com:

```powershell
Copy-Item .env.example .env
```

Depois, inicie todo o ambiente:

```bash
docker compose up --build -d
```

O Compose cria a rede interna, inicia os componentes na ordem indicada pelos health checks e preserva os dados no volume `postgres-data`. Para acompanhar a inicialização e encerrar o ambiente:

```bash
docker compose ps
docker compose logs -f
docker compose down
```

`docker compose down` mantém o volume. Use `docker compose down -v` somente quando quiser apagar também os dados locais.

## Configuração por ambiente

As aplicações usam os perfis `dev` e `prod`. O perfil `dev` aponta, por padrão, para serviços em `localhost`; o perfil `prod`, usado no Compose, utiliza os nomes DNS dos containers. As configurações ficam no repositório nativo do `config-server`, enquanto os arquivos locais das aplicações contêm apenas seu nome, perfil ativo e endereço do servidor de configuração.

As principais variáveis aceitas são:

| Variável | Uso |
| --- | --- |
| `SPRING_PROFILES_ACTIVE` | Seleciona `dev` ou `prod` |
| `CONFIG_SERVER_URL` | Endereço do Config Server |
| `SPRING_DATASOURCE_URL` | Conexão JDBC da aplicação principal |
| `SPRING_DATASOURCE_USERNAME` | Usuário do PostgreSQL |
| `SPRING_DATASOURCE_PASSWORD` | Senha do PostgreSQL |
| `CONSULTA_ISBN_URL` | Endereço do serviço ISBN |
| `BRASIL_API_URL` | Endereço do provedor bibliográfico |
| `CONSULTA_ISBN_MAX_TENTATIVAS` | Limite de tentativas da API ao consultar o serviço ISBN |
| `BRASIL_API_MAX_TENTATIVAS` | Limite de tentativas do serviço ISBN ao consultar a BrasilAPI |
| `*_CONNECT_TIMEOUT` e `*_READ_TIMEOUT` | Limites de conexão e leitura das integrações HTTP |
| `*_INTERVALO_RETRY_MS` | Intervalo entre tentativas de uma consulta remota |

Portas e timeouts dos clientes também podem ser alterados sem recompilar o código. Senhas reais ficam no arquivo `.env`, ignorado pelo Git; o repositório mantém somente `.env.example`.

## Persistência

A aplicação principal usa PostgreSQL. O Flyway aplica a migração `V1__criar_estrutura_inicial.sql` e o Hibernate apenas valida se as entidades continuam compatíveis com o esquema. Assim, a estrutura do banco possui histórico versionado e não é recriada a cada inicialização.

O banco pertence exclusivamente à `biblioteca-facil-api`. Compartilhar tabelas com outro serviço criaria acoplamento no modelo, nas migrações e no ciclo de publicação; por isso, qualquer troca entre responsabilidades separadas ocorre por uma interface do próprio serviço.

As respostas HTTP são DTOs próprios da API, e não entidades JPA. Com isso, a serialização não depende de uma sessão de persistência aberta e `spring.jpa.open-in-view` permanece desativado. Relacionamentos necessários para as respostas de reserva são carregados explicitamente pelo repository.

## Falhas temporárias nas integrações

As duas consultas remotas são operações GET e podem ser repetidas com segurança. Quando o serviço ISBN ou a BrasilAPI apresenta uma falha temporária, a chamada é repetida até três vezes, com intervalo configurável. ISBN não encontrado continua sendo uma resposta funcional e não dispara retry. Depois de esgotar as tentativas, a API mantém os códigos já documentados: 503 quando o serviço ISBN não responde e 502 quando a BrasilAPI está indisponível.

## Reflexão sobre a separação da consulta ISBN

A funcionalidade separada da aplicação principal foi a consulta de metadados bibliográficos por ISBN. Ela foi escolhida porque encapsula uma integração externa, não mantém estado próprio e não precisa acessar o banco do catálogo. Essa fronteira também impede que a API principal dependa diretamente do contrato da BrasilAPI.

A separação trouxe custos que não existiam na chamada interna: dois processos precisam ser configurados e executados, o contrato HTTP passou a exigir DTOs próprios e a comunicação agora está sujeita a timeout, indisponibilidade e falhas de rede. Por isso, a solução também precisa de configuração externa, tratamento de erros e testes do fluxo distribuído.

Se o serviço ISBN estiver indisponível, somente o cadastro que depende do enriquecimento bibliográfico é interrompido e responde com HTTP 503 antes de persistir o livro; as demais funcionalidades da aplicação principal continuam disponíveis. Um ISBN apenas não encontrado tem tratamento diferente: o cadastro pode prosseguir com os dados informados pelo usuário.

Essa funcionalidade poderia continuar dentro da aplicação em uma solução menor, evitando o custo operacional de outro processo e de uma chamada de rede. Neste projeto, mantê-la independente é uma decisão coerente porque isola a dependência da BrasilAPI e permite evoluir, escalar e tratar as falhas dessa integração sem acoplar seu ciclo de execução ao restante do domínio. A separação, portanto, é justificável, mas não é uma obrigação tecnológica.

## Documentação e verificação

| Aplicação | Swagger UI | Saúde |
| --- | --- | --- |
| Biblioteca Fácil API | [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) | [localhost:8080/actuator/health](http://localhost:8080/actuator/health) |
| Consulta ISBN Service | [localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) | [localhost:8081/actuator/health](http://localhost:8081/actuator/health) |
| Config Server | — | [localhost:8888/actuator/health](http://localhost:8888/actuator/health) |

As configurações entregues para cada perfil podem ser conferidas, por exemplo, em:

```http
GET http://localhost:8888/biblioteca-facil-api/prod
GET http://localhost:8888/consulta-isbn-service/prod
```

Para validar código e testes sem iniciar os containers:

```bash
mvn clean verify
```

## Por que essa estrutura

Configurações como portas, URLs, timeouts e conexão com o banco variam conforme o ambiente. Externalizá-las permite usar o mesmo artefato em desenvolvimento e produção. O Config Server oferece um ponto central para esses valores e reduz divergências entre aplicações, enquanto variáveis de ambiente mantêm credenciais e ajustes de implantação fora do código.

O Docker empacota cada aplicação com seu runtime, eliminando diferenças da máquina onde ela é executada. O Docker Compose descreve como imagens, rede, dependências, banco e volume formam um único ambiente local; por isso, todo o conjunto pode ser iniciado pelo mesmo comando sem usar `localhost` na comunicação interna.

## Estrutura do repositório

```text
.
|-- biblioteca-facil-api/     # Aplicação principal e migrações do seu banco
|-- consulta-isbn-service/    # Integração bibliográfica stateless
|-- config-server/            # Configurações centralizadas dev e prod
|-- postman/                  # Coleções para testes manuais
|-- docker-compose.yml        # Ambiente integrado
`-- pom.xml                   # Agregador Maven
```

## Evolução do projeto

O histórico pode ser consultado pelas tags:

- `etapa-1`: organização da aplicação como monólito modular;
- `etapa-2`: extração da consulta ISBN e comunicação entre os serviços;
- `etapa-3`: configuração externa, PostgreSQL e execução integrada em containers.

Mensageria e processamento em lote pertencem à Etapa 4 e ainda não fazem parte desta versão.
