# Orientacoes para agentes de IA

Este arquivo governa todo o repositorio. Antes de alterar qualquer coisa, leia tambem os arquivos de `.ai/docs/ai/`.

## Objetivo do repositorio

Este repositorio abriga o projeto da disciplina **Arquiteturas Avancadas de Software com Microsservicos e Spring Framework**. O contexto funcional ainda deve ser registrado em `.ai/docs/ai/project-context.md` assim que o enunciado ou a ideia do projeto estiver disponivel.

## Principios de trabalho

- Trate o projeto como software real e como entrega academica: decisoes devem ser tecnicamente justificaveis e faceis de apresentar.
- Preserve alteracoes do usuario e de outros agentes. Nunca descarte trabalho sem autorizacao explicita.
- Antes de implementar, descubra o contexto, as restricoes e os criterios de aceite.
- Prefira a menor solucao que atenda ao caso de uso. Nao crie abstracoes ou microsservicos sem uma fronteira de negocio clara.
- Registre decisoes arquiteturais relevantes em `.ai/docs/adr/`.
- Mantenha `.ai/docs/ai/project-context.md` atualizado quando fatos duraveis do projeto forem confirmados.
- Mantenha `.ai/docs/ai/current-work.md` curto e atualizado durante trabalhos longos; remova itens concluidos ou mova o resultado para a documentacao definitiva.
- Nunca versione segredos, tokens, senhas, chaves privadas ou dados pessoais.

## Arquitetura Java e Spring

- Use a linguagem ubiqua do dominio em nomes de pacotes, classes, metodos e testes.
- Organize cada microsservico por capacidade de negocio, com dependencias apontando para o nucleo da aplicacao.
- Separe apresentacao/API, aplicacao/casos de uso, dominio e infraestrutura.
- Mantenha regras de negocio fora de controllers, listeners, repositories e classes de configuracao.
- Use injecao por construtor e interfaces apenas em fronteiras que realmente possam variar.
- Evite acoplamento direto entre bancos de dados de servicos distintos.
- Para comunicacao remota, considere falhas, timeout, retry com limite, idempotencia e observabilidade.
- Nao adicione um padrao de projeto apenas para cumprir uma rubrica; documente o problema que ele resolve.

## Qualidade e validacao

- Escreva testes para regras de negocio, contratos, validacoes e cenarios de falha relevantes.
- Rode os testes e verificacoes do modulo afetado antes de concluir uma alteracao.
- Se alguma verificacao nao puder ser executada, informe claramente o motivo e o risco residual.
- Nao considere uma tarefa concluida com codigo que nao compila ou documentacao que contradiz a implementacao.

## Encerramento de cada acao

- Ao final de cada acao concluida, explique ao usuario o que foi feito em linguagem clara.
- Informe por que a alteracao foi necessaria e quais arquivos ou componentes foram afetados.
- Apresente as verificacoes executadas e seus resultados, incluindo testes, build ou validacoes manuais.
- Declare explicitamente qualquer pendencia, risco residual ou proximo passo recomendado.
- Nao encerre uma acao apenas com uma lista de arquivos ou comandos; traduza o resultado tecnico para o contexto do projeto e do aprendizado.

## Convencoes de alteracao

- Faca mudancas pequenas, coesas e rastreaveis.
- Nao reformate arquivos sem relacao com a tarefa.
- Comentarios devem explicar restricoes ou decisoes nao obvias; o codigo deve explicar o fluxo normal.
- Ao introduzir dependencia, explique a necessidade e prefira versoes centralizadas pelo build.
- Mensagens de commit, quando solicitadas, devem ser imperativas e descrever uma unica intencao.

## Commits e tags das etapas

- Nao crie commits intermediarios durante o desenvolvimento de uma etapa.
- Mantenha as alteracoes no working tree ate que todos os requisitos da etapa estejam concluidos e validados.
- Ao final de cada etapa, crie ou atualize um relatorio explicativo local em `.ai/relatorios/etapa-N.md` e sua versao visual em `.ai/relatorios/etapa-N.html`.
- Os relatorios devem apresentar objetivo, requisitos atendidos, arquitetura resultante, alteracoes realizadas, evidencias de testes e validacoes, decisoes relevantes, riscos residuais e proximos passos.
- A versao HTML deve ser autocontida, funcionar sem dependencias externas e utilizar graficos ou diagramas que facilitem a apresentacao das evidencias da etapa.
- Mantenha os dois relatorios sincronizados com o estado final do codigo, mas nunca os inclua em commits ou tags; todo o diretorio `.ai/` permanece local e ignorado pelo Git.
- Ao concluir uma etapa, revise o diff completo, execute as verificacoes aplicaveis e crie um unico commit de marco.
- Crie a tag correspondente (`etapa-1`, `etapa-2`, `etapa-3` ou `etapa-4`) apontando para esse commit.
- O primeiro commit deste repositorio sera o marco da `etapa-1`.
- Nao crie commit nem tag sem solicitacao ou autorizacao explicita do usuario.

## Ordem de leitura recomendada

1. `README.md`
2. `.ai/docs/ai/project-context.md`
3. `.ai/docs/ai/architecture.md`
4. `.ai/docs/ai/quality-gates.md`
5. ADRs relacionados em `.ai/docs/adr/`
6. Codigo e testes do modulo afetado
