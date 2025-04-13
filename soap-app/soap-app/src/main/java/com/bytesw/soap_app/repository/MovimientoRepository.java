package com.bytesw.soap_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bytesw.soap_app.model.Movimiento;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, String> 
{
    Optional<Movimiento> findTopByOrderByNumeroReferenciaDesc();
} 