package com.bytesw.soap_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bytesw.soap_app.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> 
{
    boolean existsById(Long Id);
    boolean existsByIdentificacion(String identificacion);
    Optional<Cliente> findByIdentificacion(String identificacion);
}