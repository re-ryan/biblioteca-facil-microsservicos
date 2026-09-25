package br.com.infnet.bibliotecafacil.biblioteca.infraestrutura.repository;

import br.com.infnet.bibliotecafacil.biblioteca.dominio.Acervo;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AcervoRepository extends JpaRepository<Acervo, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select acervo from Acervo acervo where acervo.id = :id")
    Optional<Acervo> buscarPorIdComBloqueio(@Param("id") Long id);
}
