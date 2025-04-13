package com.bytesw.rest_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bytesw.rest_app.models.Cuenta;
import com.bytesw.rest_app.repository.CuentaRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaRepository cuentaRepository;

    @GetMapping
    public List<Cuenta> getCuentas(@RequestParam(required = false) Integer page,
                                   @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            return cuentaRepository.findAll(pageable).getContent();
        }
        return cuentaRepository.findAll();
    }
}
