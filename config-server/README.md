# Config Server

Servidor central de configurações do Biblioteca Fácil, baseado em Spring Cloud Config e no backend nativo em classpath.

O diretório `src/main/resources/config` possui configurações `dev` e `prod` para a API principal e para o serviço ISBN. Esse repositório é adequado ao escopo local e acadêmico da etapa; ele pode ser trocado por um repositório Git externo quando houver necessidade operacional real.

Além de portas e URLs, as configurações centralizam timeouts e limites de retry das integrações HTTP. Credenciais e ajustes de implantação continuam chegando por variáveis de ambiente dos clientes.

## Execução

```bash
mvn -pl config-server spring-boot:run
```

Por padrão, o servidor responde na porta `8888`.

Exemplos de consulta:

```http
GET http://localhost:8888/biblioteca-facil-api/dev
GET http://localhost:8888/consulta-isbn-service/prod
GET http://localhost:8888/actuator/health
```
