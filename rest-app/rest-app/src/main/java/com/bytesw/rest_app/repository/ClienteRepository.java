package com.bytesw.rest_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bytesw.rest_app.models.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {}