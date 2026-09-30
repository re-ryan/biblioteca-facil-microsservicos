package br.com.infnet.bibliotecafacil.reserva.infraestrutura.repository;

import br.com.infnet.bibliotecafacil.reserva.dominio.Reserva;
import br.com.infnet.bibliotecafacil.reserva.dominio.StatusReserva;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Override
    @EntityGraph(attributePaths = {"leitor", "acervo", "acervo.biblioteca", "acervo.livro"})
    List<Reserva> findAll();

    @Override
    @EntityGraph(attributePaths = {"leitor", "acervo", "acervo.biblioteca", "acervo.livro"})
    Optional<Reserva> findById(Long id);

    boolean existsByLeitor_IdAndAcervo_IdAndStatus(
            Long leitorId,
            Long acervoId,
            StatusReserva status);
}
