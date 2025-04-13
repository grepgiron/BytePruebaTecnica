package com.bytesw.rest_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bytesw.rest_app.models.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, String> {}
