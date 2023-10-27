package entities;

import com.reservas.reserva_habitaciones.TipoHabitacion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Cliente cliente;
    @ManyToOne
    private Habitacion habitacion;
    @NotNull
    private LocalDate fechaReserva;
    private BigDecimal totalAPagar;
    private String codigoReserva;

    public Reserva(Cliente cliente, Habitacion habitacion, LocalDate fechaReserva) {
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaReserva = fechaReserva;

        calcularTotalAPagar();

        generarCodigoReserva();
    }

    private void calcularTotalAPagar() {
        BigDecimal precioBase = habitacion.getPrecioBase();
        LocalDate fechaActual = LocalDate.now();
        long diasDeAnticipacion = ChronoUnit.DAYS.between(fechaActual, fechaReserva);

        if (diasDeAnticipacion > 15) {
            totalAPagar = precioBase.multiply(new BigDecimal("0.80"));
        } else {
            totalAPagar = precioBase;
        }

        if (habitacion.getTipo() == TipoHabitacion.PREMIUM) {
            totalAPagar = totalAPagar.multiply(new BigDecimal("0.95"));
        }
    }

    private void generarCodigoReserva() {
        codigoReserva = UUID.randomUUID().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public BigDecimal getTotalAPagar() {
        return totalAPagar;
    }

    public void setTotalAPagar(BigDecimal totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }
}
