package br.com.infnet.bibliotecafacil.catalogo.importacao.infraestrutura.batch;

import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;
import br.com.infnet.bibliotecafacil.catalogo.importacao.aplicacao.dto.LivroImportacaoItem;
import br.com.infnet.bibliotecafacil.catalogo.importacao.aplicacao.processamento.LivroImportacaoProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ImportacaoLivrosBatchConfiguration {

    @Bean
    @org.springframework.batch.core.configuration.annotation.StepScope
    public FlatFileItemReader<LivroImportacaoItem> livroImportacaoReader(
            @Value("#{jobParameters['arquivo']}") final String arquivo) {
        return new FlatFileItemReaderBuilder<LivroImportacaoItem>()
                .name("livroImportacaoReader")
                .resource(new FileSystemResource(arquivo))
                .encoding("UTF-8")
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names("titulo", "isbn13", "editora", "anoPublicacao", "edicao")
                .fieldSetMapper(fieldSet -> new LivroImportacaoItem(
                        fieldSet.readString("titulo"),
                        fieldSet.readString("isbn13"),
                        fieldSet.readString("editora"),
                        fieldSet.readString("anoPublicacao"),
                        fieldSet.readString("edicao")))
                .build();
    }

    @Bean
    public Step importarLivrosStep(
            final JobRepository jobRepository,
            final PlatformTransactionManager transactionManager,
            final FlatFileItemReader<LivroImportacaoItem> livroImportacaoReader,
            final LivroImportacaoProcessor processor,
            final LivroImportacaoWriter writer,
            @Value("${batch.importacao-livros.tamanho-chunk:10}") final int tamanhoChunk) {
        return new StepBuilder("importarLivrosStep", jobRepository)
                .<LivroImportacaoItem, Livro>chunk(tamanhoChunk, transactionManager)
                .reader(livroImportacaoReader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job importarLivrosJob(
            final JobRepository jobRepository,
            final Step importarLivrosStep) {
        return new JobBuilder("importarLivrosJob", jobRepository)
                .start(importarLivrosStep)
                .build();
    }
}
