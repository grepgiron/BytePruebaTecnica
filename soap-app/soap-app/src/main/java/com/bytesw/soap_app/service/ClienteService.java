package com.bytesw.soap_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bytesw.soap_app.model.Cliente;
import com.bytesw.soap_app.repository.ClienteRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    private static final Pattern P_NOMBRE = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]{2,50}$");
    private static final Pattern P_IDENTIFICACION = Pattern.compile("^\\d{13,14}$");
    private static final Pattern P_TIPO_IDENTIFICACION = Pattern.compile("^[A-Za-z]{2,10}$");

    public String guardarCliente(String nombre, String identificacion, String tipoIdentificacion, LocalDate fechaNacimiento) 
    {

        try {
            
            //Validar si cliente ya existe
            if (clienteRepository.existsByIdentificacion(identificacion)) 
            {
                //return "999|Ya existe un cliente con esta identificación";
                return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Ya existe un cliente con esta identificación", 
                    String.format("%-16s", ""));
            }

            //Validar campos erroneos
            if (!isValid(nombre, identificacion, tipoIdentificacion, fechaNacimiento)) 
            {
                //return "999|Datos de cliente inválidos";
                return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Datos de cliente inválidos", 
                    String.format("%-16s", ""));
            }

            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setIdentificacion(identificacion);
            cliente.setTipoIdentificacion(tipoIdentificacion);
            cliente.setFechaNacimiento(fechaNacimiento);
            clienteRepository.save(cliente);

            //return "000|Cliente creado exitosamente";
            return String.format("%-3s%-100s%-16s", 
                    "000", 
                    "Cliente creado exitosamente", 
                    String.format("%-16s", ""));
        } catch (Exception e) 
        {
            //e.printStackTrace();
            //return "999|Error al guardar el cliente";
            return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Error al guardar el cliente", 
                    String.format("%-16s", e.getMessage()));
        }
    }

    public String actualizarCliente(String nombre, String identificacion, String tipoIdentificacion, LocalDate fechaNacimiento) 
    {

        try {
            //Validar campos erroneos
            if (!isValid(nombre, identificacion, tipoIdentificacion, fechaNacimiento)) 
            {
                //return "999|Datos de cliente inválidos";
                return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Datos de cliente inválidos", 
                    String.format("%-16s", ""));
            }
            System.out.println(identificacion);
            //Validar si cliente existe
            Optional<Cliente> clienteOpt = clienteRepository.findByIdentificacion(identificacion);
            if (clienteOpt.isEmpty()) {
                return "999|Cliente no encontrado";
            }

            Cliente cliente = clienteOpt.get();
            cliente.setNombre(nombre);
            cliente.setIdentificacion(identificacion);
            cliente.setTipoIdentificacion(tipoIdentificacion);
            cliente.setFechaNacimiento(fechaNacimiento);
            clienteRepository.save(cliente);

            //return "000|Cliente creado exitosamente";
            return String.format("%-3s%-100s%-16s", 
                    "000", 
                    "Cliente actualizado exitosamente", 
                    String.format("%-16s", ""));
        } catch (Exception e) 
        {
            //e.printStackTrace();
            //return "999|Error al guardar el cliente";
            return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Error al actualizar el cliente", 
                    String.format("%-16s", e.getMessage()));
        }
    }

    public String eliminarCliente(String identificacion) {
        try {
            Optional<Cliente> clienteOpt = clienteRepository.findByIdentificacion(identificacion);
            if (clienteOpt.isEmpty()) {
                return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Cliente no encontrado", 
                    String.format("%-16s", ""));
            }

            clienteRepository.delete(clienteOpt.get());
            return String.format("%-3s%-100s%-16s", 
                    "000", 
                    "Cliente eliminado correctamente", 
                    String.format("%-16s", ""));
        } catch (Exception e) {
            return String.format("%-3s%-100s%-16s", 
                "999", 
                "Error al actualizar el cliente", 
                String.format("%-16s", e.getMessage()));
        }
    }

    //Validacion general de campos erroneos
    private boolean isValid(String nombre, String identificacion, String tipoIdentificacion, LocalDate fechaNacimiento) 
    {
        return nombre != null && P_NOMBRE.matcher(nombre).matches() &&
               identificacion != null && P_IDENTIFICACION.matcher(identificacion).matches() &&
               tipoIdentificacion != null && P_TIPO_IDENTIFICACION.matcher(tipoIdentificacion).matches() &&
               fechaNacimiento != null && !fechaNacimiento.isAfter(LocalDate.now());
    }
}