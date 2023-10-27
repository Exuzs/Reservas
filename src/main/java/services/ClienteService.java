package services;

import dto.ClienteDTO;
import entities.Cliente;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ClienteRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ModelMapper modelMapper;

    public ClienteDTO registrarCliente(ClienteDTO clienteDTO) {
        if (clienteDTO == null || clienteDTO.getId() != null) {
            throw new IllegalArgumentException("ClienteDTO no válido para registro.");
        }

        if (clienteRepository.existsByCedula(clienteDTO.getCedula())) {
            throw new IllegalArgumentException("La cédula ya está registrada.");
        }

        Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);

        Cliente clienteGuardado = clienteRepository.save(cliente);

        return modelMapper.map(clienteGuardado, ClienteDTO.class);
    }

    public ClienteDTO obtenerClientePorId(Long id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            return modelMapper.map(clienteOptional.get(), ClienteDTO.class);
        } else {
            throw new NoSuchElementException("Cliente no encontrado con ID: " + id);
        }
    }

    public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            cliente.setNombre(clienteDTO.getNombre());
            cliente.setApellido(clienteDTO.getApellido());
            cliente.setDireccion(clienteDTO.getDireccion());
            cliente.setEdad(clienteDTO.getEdad());
            cliente.setCorreoElectronico(clienteDTO.getCorreoElectronico());

            Cliente clienteActualizado = clienteRepository.save(cliente);

            return modelMapper.map(clienteActualizado, ClienteDTO.class);
        } else {
            throw new NoSuchElementException("Cliente no encontrado con ID: " + id);
        }
    }

    public void eliminarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Cliente no encontrado con ID: " + id);
        }
    }

    public List<ClienteDTO> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteDTO.class))
                .collect(Collectors.toList());
    }
}
