package com.bytesw.rest_app.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bytesw.rest_app.models.Cliente;
import com.bytesw.rest_app.models.Movimiento;
import com.bytesw.rest_app.repository.MovimientoRepository;
import com.bytesw.rest_app.request.MovimientoRequest;
import com.bytesw.rest_app.service.MovimientosSoap;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private MovimientosSoap movimientosSoap;


    private static final Logger logger = LoggerFactory.getLogger(MovimientoController.class);

    @GetMapping
    public List<Movimiento> getMovimientos(@RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            return movimientoRepository.findAll(pageable).getContent();
        }
        return movimientoRepository.findAll();
    }

    @PostMapping
    public Map<String, String> crearMovimiento(@RequestBody Map<String, String> request) {
        System.out.println("request: " + request);
        try
        {
            //cuentaOrigen, cuentaDestino, fechaStr, horaStr, tipo, montoStr
            String cuentaOrigen = request.get("cuentaOrigen");
            String cuentaDestino = request.get("cuentaDestino");
            String fechaStr = request.get("fechaMovimiento");
            String horaStr = request.get("horaMovimiento");
            String tipo = request.get("tipoMovimiento");
            String montoStr = request.get("monto");
    
            Map<String, String> responseXml = movimientosSoap.enviarCrearMovimiento(cuentaOrigen, cuentaDestino, fechaStr, horaStr, tipo, montoStr);
    
            //Map<String, String> response = new HashMap<>();
            //response.put("soapResponse", responseXml);
            return responseXml;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @GetMapping("/cuenta/{nroCta}")
    public ResponseEntity<List<Movimiento>> getMovimientosCuenta(@PathVariable String nroCta) {
        List<Movimiento> movimientos = movimientoRepository.findByCuentaOrigenOrCuentaDestino(nroCta, nroCta);
        if (movimientos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movimientos);
    }
}
