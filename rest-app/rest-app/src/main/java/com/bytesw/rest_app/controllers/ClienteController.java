package com.bytesw.rest_app.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bytesw.rest_app.models.Cliente;
import com.bytesw.rest_app.repository.ClienteRepository;
import com.bytesw.rest_app.service.ClientSoap;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClientSoap soapClient;

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @PostMapping
    public Map<String, String> crearCliente(@RequestBody Map<String, String> request) {
        System.out.println("request: " + request);
        try
        {
            String nombre = request.get("nombre");
            String identificacion = request.get("identificacion");
            String tipoIdentificacion = request.get("tipoIdentificacion");
            String fechaNacimiento = request.get("fechaNacimiento");
    
            Map<String, String> responseXml = soapClient.enviarCrearCliente(nombre, identificacion, tipoIdentificacion, fechaNacimiento);
    
            //Map<String, String> response = new HashMap<>();
            //response.put("soapResponse", responseXml);
            return responseXml;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @PutMapping
    public Map<String, String> actualizarCliente(@RequestBody Map<String, String> request) {
        System.out.println("request: " + request);
        try
        {
            String nombre = request.get("nombre");
            String identificacion = request.get("identificacion");
            String tipoIdentificacion = request.get("tipoIdentificacion");
            String fechaNacimiento = request.get("fechaNacimiento");
    
            Map<String, String> responseXml = soapClient.enviarActualizarCliente(nombre, identificacion, tipoIdentificacion, fechaNacimiento);
    
            //Map<String, String> response = new HashMap<>();
            //response.put("soapResponse", responseXml);
            return responseXml;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @DeleteMapping
    public Map<String, String> eliminarCliente(@RequestBody Map<String, String> request) {
        System.out.println("request: " + request);
        try
        {
            String identificacion = request.get("identificacion");
    
            Map<String, String> responseXml = soapClient.enviarEliminarCliente(identificacion);
    
            return responseXml;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClientePorId(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Cliente> getClientes(@RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            return clienteRepository.findAll(pageable).getContent();
        }
        return clienteRepository.findAll();
    }
}
