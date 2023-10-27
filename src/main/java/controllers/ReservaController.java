package controllers;

import dto.ReservaDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ReservaService;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaDTO> realizarReserva(@RequestBody @Valid ReservaDTO reservaDTO) {
        ReservaDTO reservaRealizada = reservaService.realizarReserva(reservaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaRealizada);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ReservaDTO>>obtenerReservasPorCliente(@PathVariable Long clienteId) {
        List<ReservaDTO> reservas = reservaService.obtenerReservasPorCliente(clienteId);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> obtenerReserva(@PathVariable Long id) {
        ReservaDTO reserva = reservaService.obtenerReservaPorId(id);
        return ResponseEntity.ok(reserva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> modificarReserva(@PathVariable Long id, @RequestBody @Valid ReservaDTO reservaDTO) {
        ReservaDTO reservaModificada = reservaService.modificarReserva(id, reservaDTO);
        return ResponseEntity.ok(reservaModificada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }
}
