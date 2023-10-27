package services;

import com.reservas.reserva_habitaciones.TipoHabitacion;
import dto.HabitacionDTO;
import dto.ReservaDTO;
import entities.Cliente;
import entities.Habitacion;
import entities.Reserva;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ClienteRepository;
import repositories.HabitacionRepository;
import repositories.ReservaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private HabitacionRepository habitacionRepository;
    @Autowired
    private ModelMapper modelMapper;

    public ReservaDTO realizarReserva(ReservaDTO reservaDTO) {
        Cliente cliente = clienteRepository.findById(reservaDTO.getCliente().getId())
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado"));

        if (!habitacionDisponible(reservaDTO.getHabitacion(), reservaDTO.getFechaReserva())) {
            throw new NoSuchElementException("La habitación no está disponible para la fecha seleccionada.");
        }

        calcularPrecioYGenerarCodigo(reservaDTO);

        Reserva reserva = modelMapper.map(reservaDTO, Reserva.class);
        reserva.setCliente(cliente);

        Reserva reservaGuardada = reservaRepository.save(reserva);

        return modelMapper.map(reservaGuardada, ReservaDTO.class);
    }

    public List<ReservaDTO> obtenerReservasPorCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado"));

        List<Reserva> reservas = reservaRepository.findByCliente(cliente);

        return reservas.stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .collect(Collectors.toList());
    }

    private boolean habitacionDisponible(HabitacionDTO habitacionDTO, LocalDate fechaReserva) {
        Habitacion habitacion = habitacionRepository.findById(habitacionDTO.getId())
                .orElseThrow(() -> new NoSuchElementException("Habitación no encontrada"));

        List<Reserva> reservasEnFecha = reservaRepository.findByFechaReservaAndHabitacion(fechaReserva, habitacion);

        return reservasEnFecha.isEmpty();
    }

    private void calcularPrecioYGenerarCodigo(ReservaDTO reservaDTO) {
        Habitacion habitacion = habitacionRepository.findById(reservaDTO.getHabitacion().getId())
                .orElseThrow(() -> new NoSuchElementException("Habitación no encontrada"));

        BigDecimal precioBase = habitacion.getPrecioBase();
        LocalDate fechaReserva = reservaDTO.getFechaReserva();

        long diasEntreReservaYHoy = ChronoUnit.DAYS.between(LocalDate.now(), fechaReserva);

        if (diasEntreReservaYHoy > 15) {
            precioBase = precioBase.multiply(new BigDecimal("0.8"));
        }

        if (habitacion.getTipo() == TipoHabitacion.PREMIUM) {
            precioBase = precioBase.multiply(new BigDecimal("0.95"));
        }

        BigDecimal totalAPagar = precioBase;
        reservaDTO.setTotalAPagar(totalAPagar);

        String codigoReserva = generarCodigoReserva();
        reservaDTO.setCodigoReserva(codigoReserva);
    }

    private String generarCodigoReserva() {
        return UUID.randomUUID().toString();
    }

    public ReservaDTO obtenerReservaPorId(Long id) {
        Optional<Reserva> reservaOptional = reservaRepository.findById(id);
        if (reservaOptional.isPresent()) {
            return modelMapper.map(reservaOptional.get(), ReservaDTO.class);
        } else {
            throw new NoSuchElementException("Reserva no encontrada con ID: " + id);
        }
    }

    public ReservaDTO modificarReserva(Long id, ReservaDTO reservaDTO) {
        // Validar si la reserva existe
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reserva no encontrada con ID: " + id));

        // Validar si la habitación está disponible para la nueva fecha de reserva (si es aplicable)
        if (!habitacionDisponible(reservaDTO.getHabitacion(), reservaDTO.getFechaReserva())) {
            throw new NoSuchElementException("La habitación no está disponible para la nueva fecha seleccionada.");
        }

        reservaExistente.setFechaReserva(reservaDTO.getFechaReserva());
        reservaExistente.setHabitacion(modelMapper.map(reservaDTO.getHabitacion(), Habitacion.class));
        reservaExistente.setTotalAPagar(reservaDTO.getTotalAPagar());

        Reserva reservaModificada = reservaRepository.save(reservaExistente);

        return modelMapper.map(reservaModificada, ReservaDTO.class);
    }

    public void cancelarReserva(Long id) {
        // Validar si la reserva existe
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reserva no encontrada con ID: " + id));

        reservaRepository.delete(reservaExistente);
    }
}
