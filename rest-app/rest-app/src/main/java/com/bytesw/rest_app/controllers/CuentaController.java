package com.bytesw.rest_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bytesw.rest_app.models.Cuenta;
import com.bytesw.rest_app.models.Movimiento;
import com.bytesw.rest_app.repository.CuentaRepository;
import com.bytesw.rest_app.repository.MovimientoRepository;
import com.bytesw.rest_app.request.MovimientoRequest;
import com.bytesw.rest_app.service.CuentaSoap;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaSoap cuentaSoap;

    @PostMapping
    public Map<String, String> crearCuenta(@RequestBody Map<String, String> request) {
        System.out.println("request: " + request);
        try
        {
            //clienteId, fechaApertura, horaApertura, estado, saldo
            String clienteId = request.get("clienteId");
            String fechaApertura = request.get("fechaApertura");
            String horaApertura = request.get("horaApertura");
            String estado = request.get("estado");
            String saldo = request.get("saldo");
    
            Map<String, String> responseXml = cuentaSoap.enviarCrearCuenta(clienteId, fechaApertura, horaApertura, estado, saldo);
    
            //Map<String, String> response = new HashMap<>();
            //response.put("soapResponse", responseXml);
            return responseXml;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @PutMapping
    public Map<String, String> actualizarCuenta(@RequestBody Map<String, String> request) {
        System.out.println("request: " + request);
        try
        {
            String nroCta = request.get("nroCta");
            String clienteId = request.get("clienteId");
            String fechaApertura = request.get("fechaApertura");
            String horaApertura = request.get("horaApertura");
            String estado = request.get("estado");
            String saldo = request.get("saldo");
    
            Map<String, String> responseXml = cuentaSoap.enviarActualizarCuenta(nroCta, clienteId, fechaApertura, horaApertura, estado, saldo);
    
            //Map<String, String> response = new HashMap<>();
            //response.put("soapResponse", responseXml);
            return responseXml;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @GetMapping
    public List<Cuenta> getCuentas(@RequestParam(required = false) Integer page,
                                   @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            return cuentaRepository.findAll(pageable).getContent();
        }
        return cuentaRepository.findAll();
    }

    @DeleteMapping
    public Map<String, String> eliminarCuenta(@RequestBody Map<String, String> request) {
        System.out.println("request: " + request);
        try
        {
            String cuenta = request.get("nroCta");
    
            Map<String, String> responseXml = cuentaSoap.enviarEliminarCuenta(cuenta);
    
            return responseXml;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @GetMapping("/{nroCta}")
    public ResponseEntity<MovimientoRequest> getCuentaPorId(@PathVariable String nroCta) {
        return cuentaRepository.findByNumeroCuenta(nroCta)
        .map(cuenta -> {
                List<Movimiento> movimientos = movimientoRepository.findByCuentaOrigenOrCuentaDestino(nroCta, nroCta);
                return ResponseEntity.ok(new MovimientoRequest(cuenta, movimientos));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Cuenta> getCuentasPorCliente(@PathVariable Long clienteId,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer size) {
    if (page != null && size != null) {
        Pageable pageable = PageRequest.of(page, size);
        return cuentaRepository.findByClienteId(clienteId, pageable).getContent();
    }
    return cuentaRepository.findByClienteId(clienteId);
}
}
