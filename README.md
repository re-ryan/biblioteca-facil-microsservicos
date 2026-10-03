# Biblioteca Fácil

O Biblioteca Fácil é uma API para consulta de livros e reserva de exemplares em bibliotecas parceiras. O sistema mantém um catálogo central, registra a disponibilidade de cada livro por biblioteca e permite que leitores solicitem reservas para retirada presencial.

Este repositório acompanha a evolução do projeto na disciplina de Arquiteturas Avançadas de Software com Microsserviços e Spring Framework. A solução começou como um monólito modular, separou a consulta de ISBN, ganhou configuração externa e containers e, na quarta etapa, passou a combinar comunicação REST, mensageria e processamento em lote.

## Componentes

| Componente | Porta | Responsabilidade |
| --- | ---: | --- |
| `biblioteca-facil-api` | 8080 | Catálogo, bibliotecas, usuários, acervo e reservas |
| `consulta-isbn-service` | 8081 | Consulta de metadados bibliográficos na BrasilAPI |
| `notificacao-service` | 8082 | Consome eventos de reserva e simula notificações aos leitores |
| `config-server` | 8888 | Configurações de ambiente das três aplicações |
| PostgreSQL | 5432 (rede interna) | Dados pertencentes à aplicação principal |
| RabbitMQ | 5672 / 15672 | Broker AMQP e painel local de acompanhamento das filas |

O serviço de ISBN é stateless. Ele não possui banco próprio porque apenas consulta e traduz dados do provedor externo; também não acessa as tabelas da aplicação principal.

## Arquitetura

```text
                                      +-------------------+
                                      |   Config Server   |
                                      |       :8888       |
                                      +---------+---------+
                                                |
          +--------------------------+----------+--------------------------+
          |                          |                                     |
          v                          v                                     v
+---------+   +----------------------+---+   HTTP   +-----------------------+
| Cliente |-->| Biblioteca Fácil API    |--------->| Consulta ISBN Service |
+---------+   | :8080                   |          | :8081                 |
              +------+-------------+----+          +-----------+-----------+
                     |             |                           |
            JPA/Batch|             | evento                    v
                     v             v                       BrasilAPI
              +------------+   +----------+   AMQP   +--------------------+
              | PostgreSQL |   | RabbitMQ |--------->| Notificação Service|
              +------------+   +----------+          | :8082              |
                                                    +--------------------+
```

O cadastro de um livro pode chamar o serviço de ISBN para completar título, editora e outros dados. A aplicação principal conhece apenas o contrato publicado pelo serviço, nunca o contrato da BrasilAPI. Se o ISBN não for encontrado, o cadastro pode continuar com os dados informados; se o serviço estiver indisponível, a API responde com HTTP 503 antes de persistir o livro.

Uma reserva continua sendo concluída pela API e salva no PostgreSQL. Depois do commit, a API publica o evento `RESERVA_SOLICITADA`; o serviço de notificação o consome em outro momento, sem participar da transação original. A importação de catálogo permanece dentro da aplicação principal porque ela é a dona dos livros e do respectivo banco.

## Executando com Docker Compose

Pré-requisito: Docker Desktop ou Docker Engine com Docker Compose.

Crie a configuração local a partir do exemplo e defina senhas para o PostgreSQL e o RabbitMQ:

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

O Compose cria a rede interna, inicia os componentes na ordem indicada pelos health checks e preserva PostgreSQL e RabbitMQ nos volumes `postgres-data` e `rabbitmq-data`. Para acompanhar a inicialização e encerrar o ambiente:

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
| `RABBITMQ_USERNAME` e `RABBITMQ_PASSWORD` | Credenciais locais do broker |
| `RESERVAS_EXCHANGE`, `RESERVA_SOLICITADA_QUEUE` e `RESERVA_SOLICITADA_ROUTING_KEY` | Topologia AMQP do evento de reserva |
| `IMPORTACAO_LIVROS_CHUNK` | Quantidade de registros lidos por transação do Batch |

Portas e timeouts dos clientes também podem ser alterados sem recompilar o código. Senhas reais ficam no arquivo `.env`, ignorado pelo Git; o repositório mantém somente `.env.example`.

## Persistência

A aplicação principal usa PostgreSQL. O Flyway aplica a estrutura funcional em `V1__criar_estrutura_inicial.sql` e as tabelas de controle do Spring Batch em `V2__criar_metadados_spring_batch.sql`; o Hibernate apenas valida se as entidades continuam compatíveis com o esquema. Assim, a estrutura do banco possui histórico versionado e não é recriada a cada inicialização.

O banco pertence exclusivamente à `biblioteca-facil-api`. Compartilhar tabelas com outro serviço criaria acoplamento no modelo, nas migrações e no ciclo de publicação; por isso, qualquer troca entre responsabilidades separadas ocorre por uma interface do próprio serviço.

As respostas HTTP são DTOs próprios da API, e não entidades JPA. Com isso, a serialização não depende de uma sessão de persistência aberta e `spring.jpa.open-in-view` permanece desativado. Relacionamentos necessários para as respostas de reserva são carregados explicitamente pelo repository.

## Falhas temporárias nas integrações

As duas consultas remotas são operações GET e podem ser repetidas com segurança. Quando o serviço ISBN ou a BrasilAPI apresenta uma falha temporária, a chamada é repetida até três vezes, com intervalo configurável. ISBN não encontrado continua sendo uma resposta funcional e não dispara retry. Depois de esgotar as tentativas, a API mantém os códigos já documentados: 503 quando o serviço ISBN não responde e 502 quando a BrasilAPI está indisponível.

## Reflexão sobre a separação da consulta ISBN

Escolhemos separar a consulta de dados bibliográficos por ISBN. Essa funcionalidade apenas consulta uma API externa, não mantém dados próprios e não precisa acessar o banco do catálogo. Com isso, a API principal também deixa de depender diretamente do contrato da BrasilAPI.

Depois da separação, o projeto ficou um pouco mais complexo. Agora existem dois processos para configurar e executar, DTOs para o contrato HTTP e a possibilidade de timeout ou falha de rede. Por isso, foi necessário adicionar configuração externa, tratamento de erros e testes da comunicação entre as aplicações.

Se o serviço ISBN estiver indisponível, apenas o cadastro que precisa desses dados é interrompido. Nesse caso, a API responde com HTTP 503 antes de salvar o livro, enquanto as outras funcionalidades continuam disponíveis. Quando o ISBN apenas não é encontrado, o cadastro pode continuar com os dados informados pelo usuário.

Em um projeto menor, essa consulta poderia continuar dentro da aplicação e evitar uma chamada pela rede. Aqui, mantê-la separada faz sentido porque a integração com a BrasilAPI pode evoluir e tratar suas falhas sem afetar diretamente o restante do sistema.

## Comunicação assíncrona de reservas

Quando uma reserva é solicitada, a API salva os dados e publica uma mensagem JSON persistente no exchange durável `biblioteca.reservas`. A fila durável `biblioteca.reserva-solicitada` recebe o evento e o entrega ao `notificacao-service`, que simula o envio da notificação ao leitor.

A mensagem contém somente os dados usados na notificação: identificadores do evento, da reserva e do leitor, tipo do evento, e-mail do leitor, título do livro, nome da biblioteca e horário da ocorrência. O consumidor usa o identificador do evento para não processar a mesma mensagem duas vezes durante uma execução. As notificações processadas podem ser consultadas em `GET /api/notificacoes`.

Se o consumidor estiver parado, a reserva continua sendo criada e a mensagem fica aguardando no RabbitMQ. Quando o serviço volta, ele recebe e processa o evento. Ainda existe uma limitação: se o RabbitMQ falhar logo depois do commit da reserva, o evento pode não ser publicado. Em um ambiente de produção, esse risco poderia ser tratado com o padrão transactional outbox.

### Demonstração com consumidor indisponível

Com o ambiente iniciado e uma reserva válida preparada:

```bash
docker compose stop notificacao-service
# execute POST /api/reservas pela coleção Postman
docker compose exec rabbitmq rabbitmqctl list_queues name messages_ready consumers
docker compose start notificacao-service
docker compose exec rabbitmq rabbitmqctl list_queues name messages_ready consumers
```

Na primeira consulta, `biblioteca.reserva-solicitada` deve apresentar uma mensagem pronta e nenhum consumidor. Depois da retomada, `messages_ready` volta a zero e a notificação aparece em:

```http
GET http://localhost:8082/api/notificacoes
```

O mesmo estado pode ser acompanhado no painel [localhost:15672](http://localhost:15672), usando as credenciais definidas no `.env`.

## Importação de livros com Spring Batch

O processamento em lote importa os livros do arquivo `biblioteca-facil-api/dados/livros.csv` para o catálogo.

O Job `importarLivrosJob` executa o Step `importarLivrosStep`:

```text
livros.csv
   |
   v
FlatFileItemReader -> LivroImportacaoProcessor -> LivroImportacaoWriter -> PostgreSQL
```

O reader lê as linhas do CSV. O processor remove espaços excedentes, converte o ano e ignora registros sem campos obrigatórios ou com ano incoerente. Por fim, o writer utiliza o `LivroService`, que valida o ISBN e verifica duplicidades antes de salvar cada livro. O tamanho do chunk é configurável e vale 10 por padrão. A importação é iniciada por:

```http
POST http://localhost:8080/api/importacoes/livros
```

A resposta informa o estado da execução e as quantidades lida, importada e filtrada. O arquivo fica montado como somente leitura no container, enquanto os dados e os metadados de execução pertencem ao PostgreSQL da API.

## Reflexão arquitetural da Etapa 4

1. **Qual operação foi escolhida para comunicação assíncrona?** A notificação de uma nova reserva.

2. **Por que essa operação não precisa ser concluída durante a requisição original?** Para responder ao leitor, a API precisa validar e salvar a reserva. A notificação pode ser enviada depois, sem aumentar o tempo da requisição.

3. **O que acontece com a mensagem se o consumidor estiver temporariamente indisponível?** A mensagem permanece na fila do RabbitMQ. Quando o `notificacao-service` volta a funcionar, ele recebe e processa o evento pendente.

4. **Qual funcionalidade foi escolhida para processamento em lote?** A importação de livros para o catálogo a partir de um arquivo CSV.

5. **Por que essa funcionalidade é adequada para Batch?** O arquivo possui vários registros que passam pelo mesmo fluxo de leitura, validação e gravação. O processamento em chunks também permite controlar quantos itens são tratados em cada transação.

6. **Em quais situações da aplicação seria mais adequado utilizar REST, mensageria ou Batch?**

   - REST é usado quando o cliente precisa de uma resposta imediata, como consultar um ISBN, listar o catálogo ou saber se a reserva foi aceita.
   - Mensageria é usada quando uma tarefa pode acontecer depois, como notificar o leitor sobre a reserva.
   - Batch é usado para processar um conjunto de registros, como importar os livros de um arquivo CSV.

## Documentação e verificação

| Aplicação | Swagger UI | Saúde |
| --- | --- | --- |
| Biblioteca Fácil API | [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) | [localhost:8080/actuator/health](http://localhost:8080/actuator/health) |
| Consulta ISBN Service | [localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) | [localhost:8081/actuator/health](http://localhost:8081/actuator/health) |
| Notificação Service | [localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html) | [localhost:8082/actuator/health](http://localhost:8082/actuator/health) |
| Config Server | — | [localhost:8888/actuator/health](http://localhost:8888/actuator/health) |

As configurações entregues para cada perfil podem ser conferidas, por exemplo, em:

```http
GET http://localhost:8888/biblioteca-facil-api/prod
GET http://localhost:8888/consulta-isbn-service/prod
GET http://localhost:8888/notificacao-service/prod
```

Para validar código e testes sem iniciar os containers:

```bash
mvn clean verify
```

## Por que essa estrutura

Portas, URLs, timeouts e dados de conexão com o banco podem mudar de um ambiente para outro. Por isso, essas configurações ficam fora do código. O Config Server centraliza os valores usados pelas aplicações, enquanto as variáveis de ambiente mantêm credenciais e outros ajustes locais fora do repositório.

O Docker coloca cada aplicação junto com o ambiente necessário para executá-la. O Docker Compose reúne as imagens, a rede, as dependências, o banco e os volumes em um único ambiente local. Assim, toda a solução pode ser iniciada com o mesmo comando, e os containers se comunicam pelos nomes dos serviços em vez de usar `localhost`.

## Estrutura do repositório

```text
.
|-- biblioteca-facil-api/     # Aplicação principal e migrações do seu banco
|-- consulta-isbn-service/    # Integração bibliográfica stateless
|-- notificacao-service/      # Consumidor assíncrono de reservas
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
- `etapa-4`: comunicação assíncrona de reservas e importação de livros com Spring Batch.
