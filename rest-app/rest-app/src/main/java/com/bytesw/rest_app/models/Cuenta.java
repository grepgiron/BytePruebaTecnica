package com.bytesw.rest_app.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;

@Entity
@Table(name = "cuentas")
public class Cuenta {

    @Id
    private String numeroCuenta;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private LocalDate fechaApertura;
    
    private LocalTime horaApertura;

    private String estado;

    private BigDecimal saldo;

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public LocalTime getHoraApertura() {
        return horaApertura;
    }

    public String getEstado() {
        return estado;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public void setHoraApertura(LocalTime horaApertura) {
        this.horaApertura = horaApertura;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    

}
