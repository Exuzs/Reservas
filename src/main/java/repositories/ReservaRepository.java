package repositories;

import entities.Cliente;
import entities.Habitacion;
import entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByCliente(Cliente cliente);
    List<Reserva> findByFechaReservaAndHabitacion(LocalDate fechaReserva, Habitacion habitacion);

}
