package br.com.infnet.bibliotecafacil.catalogo.importacao.aplicacao.service;

import br.com.infnet.bibliotecafacil.catalogo.importacao.api.dto.ImportacaoLivrosResponseDto;
import br.com.infnet.bibliotecafacil.compartilhado.aplicacao.exception.DadosInvalidosException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public final class ImportacaoLivrosService {

    private final JobLauncher jobLauncher;
    private final Job importarLivrosJob;
    private final String arquivo;

    public ImportacaoLivrosService(
            final JobLauncher jobLauncher,
            final Job importarLivrosJob,
            @Value("${batch.importacao-livros.arquivo:./dados/livros.csv}") final String arquivo) {
        this.jobLauncher = jobLauncher;
        this.importarLivrosJob = importarLivrosJob;
        this.arquivo = arquivo;
    }

    public ImportacaoLivrosResponseDto importar() {
        final Path caminho = Path.of(this.arquivo).toAbsolutePath().normalize();
        if (!Files.isRegularFile(caminho) || !Files.isReadable(caminho)) {
            throw new DadosInvalidosException(
                    "O arquivo CSV configurado para importação não está disponível: " + caminho);
        }

        try {
            final JobExecution execucao = this.jobLauncher.run(
                    this.importarLivrosJob,
                    new JobParametersBuilder()
                            .addString("arquivo", caminho.toString())
                            .addString("solicitacao", UUID.randomUUID().toString())
                            .toJobParameters());
            final StepExecution etapa = execucao.getStepExecutions().stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "A execução do Batch terminou sem registrar a etapa de importação."));
            return new ImportacaoLivrosResponseDto(
                    execucao.getId(),
                    execucao.getStatus().name(),
                    etapa.getReadCount(),
                    etapa.getWriteCount(),
                    etapa.getFilterCount());
        } catch (final RuntimeException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new IllegalStateException("Não foi possível executar a importação de livros.", exception);
        }
    }
}
