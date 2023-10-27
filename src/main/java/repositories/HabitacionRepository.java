package repositories;

import com.reservas.reserva_habitaciones.TipoHabitacion;
import entities.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    List<Habitacion> findByTipo(TipoHabitacion tipo);

    @Query("SELECT h FROM Habitacion h WHERE h.id NOT IN (SELECT r.habitacion.id FROM Reserva r WHERE r.fechaReserva = :fecha)")
    List<Habitacion> findNotReservedOnDate(@Param("fecha") LocalDate fecha);
}
