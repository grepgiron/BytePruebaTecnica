package com.bytesw.rest_app.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bytesw.rest_app.models.Cliente;
import com.bytesw.rest_app.models.Movimiento;
import com.bytesw.rest_app.repository.MovimientoRepository;
import com.bytesw.rest_app.request.MovimientoRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @GetMapping
    public List<Movimiento> getMovimientos(@RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            return movimientoRepository.findAll(pageable).getContent();
        }
        return movimientoRepository.findAll();
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
