package com.bytesw.soap_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bytesw.soap_app.model.Cuenta;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, String> 
{
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    Optional<Cuenta> findTopByOrderByNumeroCuentaDesc();
    boolean existsByNumeroCuenta(String numeroCuenta);
    boolean existsByClienteIdAndEstado(Long clienteId, String estado);
    boolean existsByClienteId(Long clienteId);
}
