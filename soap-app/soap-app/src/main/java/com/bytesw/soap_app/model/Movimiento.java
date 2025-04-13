package com.bytesw.soap_app.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movimientos")
public class Movimiento 
{
    
    @Id
    @Column(name = "numero_referencia", length = 20)
    private String numeroReferencia;
    @Column(name = "cuenta_origen")
    private String cuentaOrigen;
    @Column(name = "cuenta_destino")
    private String cuentaDestino;
    @Column(name = "fecha_movimiento")
    private LocalDate fecha;
    @Column(name = "hora_movimiento")
    private LocalTime hora;
    @Column(name = "tipo_movimiento")
    private String tipo;
    @Column(name = "monto")
    private BigDecimal monto;
    

    // Getters y Setters

    public String getCuentaOrigen() 
    {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(String cuentaOrigen) 
    {
        this.cuentaOrigen = cuentaOrigen;
    }

    public String getCuentaDestino() 
    {
        return cuentaDestino;
    }

    public void setCuentaDestino(String cuentaDestino) 
    {
        this.cuentaDestino = cuentaDestino;
    }

    public LocalDate getFecha() 
    {
        return fecha;
    }

    public void setFecha(LocalDate fecha) 
    {
        this.fecha = fecha;
    }

    public LocalTime getHora() 
    {
        return hora;
    }

    public void setHora(LocalTime hora) 
    {
        this.hora = hora;
    }

    public String getTipo() 
    {
        return tipo;
    }

    public void setTipo(String tipo) 
    {
        this.tipo = tipo;
    }

    public BigDecimal getMonto() 
    {
        return monto;
    }

    public void setMonto(BigDecimal monto) 
    {
        this.monto = monto;
    }

    public String getNumeroReferencia() 
    {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) 
    {
        this.numeroReferencia = numeroReferencia;
    }
} 