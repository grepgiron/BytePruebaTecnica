package com.bytesw.rest_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bytesw.rest_app.models.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, String> 
{
    List<Movimiento> findByCuentaOrigenOrCuentaDestino(String cuentaOrigen, String cuentaDestino);
    Optional<Movimiento> findByCuentaOrigen(String cuentaOrigen);
}
