package services;

import com.reservas.reserva_habitaciones.TipoHabitacion;
import dto.HabitacionDTO;
import entities.Habitacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.HabitacionRepository;
import org.modelmapper.ModelMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HabitacionService {
    @Autowired
    private HabitacionRepository habitacionRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<HabitacionDTO> obtenerHabitacionesDisponibles(LocalDate fecha, TipoHabitacion tipo) {
        List<Habitacion> habitacionesDisponibles;

        if (tipo != null) {
            habitacionesDisponibles = habitacionRepository.findByTipo(tipo);
        } else {
            habitacionesDisponibles = habitacionRepository.findNotReservedOnDate(fecha);
        }

        return habitacionesDisponibles.stream()
                .map(habitacion -> modelMapper.map(habitacion, HabitacionDTO.class))
                .collect(Collectors.toList());
    }

    public HabitacionDTO obtenerHabitacionPorId(Long id) {
        Optional<Habitacion> habitacionOptional = habitacionRepository.findById(id);
        if (habitacionOptional.isPresent()) {
            return modelMapper.map(habitacionOptional.get(), HabitacionDTO.class);
        } else {
            throw new NoSuchElementException("Habitación no encontrada con ID: " + id);
        }
    }

    public HabitacionDTO registrarHabitacion(HabitacionDTO habitacionDTO) {
        Habitacion habitacion = modelMapper.map(habitacionDTO, Habitacion.class);
        Habitacion habitacionRegistrada = habitacionRepository.save(habitacion);
        return modelMapper.map(habitacionRegistrada, HabitacionDTO.class);
    }

    public HabitacionDTO actualizarHabitacion(Long id, HabitacionDTO habitacionDTO) {
        Optional<Habitacion> habitacionOptional = habitacionRepository.findById(id);
        if (habitacionOptional.isPresent()) {
            Habitacion habitacion = habitacionOptional.get();
            habitacion.setNumero(habitacionDTO.getNumero());
            habitacion.setTipo(habitacionDTO.getTipo());
            habitacion.setPrecioBase(habitacionDTO.getPrecioBase());

            Habitacion habitacionActualizada = habitacionRepository.save(habitacion);

            return modelMapper.map(habitacionActualizada, HabitacionDTO.class);
        } else {
            throw new NoSuchElementException("Habitación no encontrada con ID: " + id);
        }
    }

    public void eliminarHabitacion(Long id) {
        if (habitacionRepository.existsById(id)) {
            habitacionRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Habitación no encontrada con ID: " + id);
        }
    }
}
