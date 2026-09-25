package br.com.infnet.bibliotecafacil.reserva.infraestrutura.repository;

import br.com.infnet.bibliotecafacil.reserva.dominio.Reserva;
import br.com.infnet.bibliotecafacil.reserva.dominio.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByLeitor_IdAndAcervo_IdAndStatus(
            Long leitorId,
            Long acervoId,
            StatusReserva status);
}
