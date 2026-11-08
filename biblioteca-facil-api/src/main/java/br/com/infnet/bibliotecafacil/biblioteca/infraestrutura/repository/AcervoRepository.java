package br.com.infnet.bibliotecafacil.biblioteca.infraestrutura.repository;

import br.com.infnet.bibliotecafacil.biblioteca.dominio.Acervo;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AcervoRepository extends JpaRepository<Acervo, Long> {

    @Query("""
            select distinct acervo
            from Acervo acervo
            join acervo.livro livro
            join acervo.biblioteca biblioteca
            left join livro.autorias autoria
            left join autoria.autor autor
            left join livro.categorias categoria
            where acervo.ativo = true
              and livro.ativo = true
              and biblioteca.ativa = true
              and (:titulo = ''
                   or lower(livro.titulo) like lower(concat('%', :titulo, '%')))
              and (:autor = ''
                   or (autor.ativo = true
                       and lower(autor.nome) like lower(concat('%', :autor, '%'))))
              and (:categoria = ''
                   or (categoria.ativa = true
                       and lower(categoria.nome) like lower(concat('%', :categoria, '%'))))
            """)
    List<Acervo> buscarNoCatalogo(
            @Param("titulo") String titulo,
            @Param("autor") String autor,
            @Param("categoria") String categoria);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select acervo from Acervo acervo where acervo.id = :id")
    Optional<Acervo> buscarPorIdComBloqueio(@Param("id") Long id);
}
