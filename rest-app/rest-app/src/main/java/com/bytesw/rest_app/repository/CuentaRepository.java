package com.bytesw.rest_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bytesw.rest_app.models.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, String> {}