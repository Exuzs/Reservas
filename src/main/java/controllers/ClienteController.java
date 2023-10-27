package controllers;

import dto.ClienteDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteDTO> registrarCliente(@RequestBody @Valid ClienteDTO clienteDTO) {
        ClienteDTO clienteRegistrado = clienteService.registrarCliente(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteRegistrado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerCliente(@PathVariable Long id) {
        ClienteDTO cliente = clienteService.obtenerClientePorId(id);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(@PathVariable Long id, @RequestBody @Valid ClienteDTO clienteDTO) {
        ClienteDTO clienteActualizado = clienteService.actualizarCliente(id, clienteDTO);
        return ResponseEntity.ok(clienteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        List<ClienteDTO> clientes = clienteService.listarClientes();
        return ResponseEntity.ok(clientes);
    }
}
