package com.bytesw.rest_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bytesw.rest_app.models.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, String> 
{
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    List<Cuenta> findByClienteId(Long clienteId);
    Page<Cuenta> findByClienteId(Long clienteId, Pageable pageable);

}