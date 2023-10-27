package entities;

import com.reservas.reserva_habitaciones.TipoHabitacion;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Habitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numero;
    @Enumerated(EnumType.STRING)
    private TipoHabitacion tipo;
    private BigDecimal precioBase;

    @OneToMany(mappedBy = "habitaciones", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public TipoHabitacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoHabitacion tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(BigDecimal precioBase) {
        this.precioBase = precioBase;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}
