package br.com.infnet.bibliotecafacil;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.infnet.bibliotecafacil.catalogo.infraestrutura.repository.LivroRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Etapa4BatchIntegrationTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importarLivrosJob;

    @Autowired
    private LivroRepository livroRepository;

    @TempDir
    private Path diretorioTemporario;

    @AfterEach
    void limparDados() {
        this.livroRepository.deleteAll();
    }

    @Test
    void deveLerProcessarEPersistirCsvEmChunks() throws Exception {
        final Path arquivo = this.diretorioTemporario.resolve("livros.csv");
        Files.writeString(arquivo, """
                titulo,isbn13,editora,anoPublicacao,edicao
                  Clean   Architecture ,9780134494166,Prentice Hall,2017,1
                Registro inválido,9780321125217,Editora,9999,1
                Refactoring,9780134757599,Addison-Wesley,2018,2
                """);

        final JobExecution execucao = this.jobLauncher.run(
                this.importarLivrosJob,
                new JobParametersBuilder()
                        .addString("arquivo", arquivo.toString())
                        .addLong("solicitacao", System.nanoTime())
                        .toJobParameters());

        assertThat(execucao.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(execucao.getStepExecutions()).singleElement().satisfies(etapa -> {
            assertThat(etapa.getReadCount()).isEqualTo(3);
            assertThat(etapa.getFilterCount()).isEqualTo(1);
            assertThat(etapa.getWriteCount()).isEqualTo(2);
            assertThat(etapa.getCommitCount()).isEqualTo(2);
        });
        assertThat(this.livroRepository.findAll())
                .extracting(livro -> livro.getTitulo())
                .containsExactlyInAnyOrder("Clean Architecture", "Refactoring");
    }
}
