package controllers;

import com.reservas.reserva_habitaciones.TipoHabitacion;
import dto.HabitacionDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.HabitacionService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/habitaciones")
public class HabitacionController {
    @Autowired
    private HabitacionService habitacionService;

    @GetMapping
    public ResponseEntity<List<HabitacionDTO>> obtenerHabitacionesDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) TipoHabitacion tipo) {
        List<HabitacionDTO> habitaciones = habitacionService.obtenerHabitacionesDisponibles(fecha, tipo);
        return ResponseEntity.ok(habitaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionDTO> obtenerHabitacion(@PathVariable Long id) {
        HabitacionDTO habitacion = habitacionService.obtenerHabitacionPorId(id);
        return ResponseEntity.ok(habitacion);
    }

    @PostMapping
    public ResponseEntity<HabitacionDTO> registrarHabitacion(@RequestBody @Valid HabitacionDTO habitacionDTO) {
        HabitacionDTO habitacionRegistrada = habitacionService.registrarHabitacion(habitacionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(habitacionRegistrada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitacionDTO> actualizarHabitacion(@PathVariable Long id, @RequestBody @Valid HabitacionDTO habitacionDTO) {
        HabitacionDTO habitacionActualizada = habitacionService.actualizarHabitacion(id, habitacionDTO);
        return ResponseEntity.ok(habitacionActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHabitacion(@PathVariable Long id) {
        habitacionService.eliminarHabitacion(id);
        return ResponseEntity.noContent().build();
    }
}
