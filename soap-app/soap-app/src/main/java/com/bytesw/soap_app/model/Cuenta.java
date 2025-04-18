package com.bytesw.soap_app.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cuentas")
public class Cuenta 
{
    @Id
    @Column(name = "nro_cta", length = 12)
    private String numeroCuenta;

    @Column(name = "id_cliente")
    private Long clienteId;

    @Column(name = "fecha_apertura")
    private LocalDate fechaApertura;

    @Column(name = "hora_apertura")
    private LocalTime horaApertura;

    @Column(length = 20)
    private String estado;

    @Column(name = "numero_cuenta", length = 20)
    private String cta;

    public void setCta(String cta) {
        this.cta = cta;
    }

    public String getCta() {
        return cta;
    }

    @Column(precision = 15, scale = 2)
    private BigDecimal saldo;

    // Getters y Setters
    public String getNumeroCuenta() 
    {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) 
    {
        this.numeroCuenta = numeroCuenta;
    }

    public Long getClienteId() 
    {
        return clienteId;
    }

    public void setClienteId(Long clienteId) 
    {
        this.clienteId = clienteId;
    }

    public LocalDate getFechaApertura() 
    {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) 
    {
        this.fechaApertura = fechaApertura;
    }

    public LocalTime getHoraApertura() 
    {
        return horaApertura;
    }

    public void setHoraApertura(LocalTime horaApertura) 
    {
        this.horaApertura = horaApertura;
    }

    public String getEstado() 
    {
        return estado;
    }

    public void setEstado(String estado) 
    {
        this.estado = estado;
    }

    public BigDecimal getSaldo() 
    {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) 
    {
        this.saldo = saldo;
    }
}

