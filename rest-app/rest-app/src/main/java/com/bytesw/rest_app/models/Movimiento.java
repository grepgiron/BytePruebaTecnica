package com.bytesw.rest_app.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;

@Entity
@Table(name = "movimientos")
public class Movimiento {

    @Id
    private String numeroReferencia;

    @ManyToOne
    @JoinColumn(name = "cuenta_origen")
    private Cuenta cuentaOrigen;

    @ManyToOne
    @JoinColumn(name = "cuenta_destino")
    private Cuenta cuentaDestino;

    private LocalDate fechaMovimiento;
    
    private LocalTime horaMovimiento;

    private String tipoMovimiento;

    private BigDecimal monto;

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public void setCuentaOrigen(Cuenta cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public void setCuentaDestino(Cuenta cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public void setFechaMovimiento(LocalDate fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public void setHoraMovimiento(LocalTime horaMovimiento) {
        this.horaMovimiento = horaMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public Cuenta getCuentaOrigen() {
        return cuentaOrigen;
    }

    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    public LocalDate getFechaMovimiento() {
        return fechaMovimiento;
    }

    public LocalTime getHoraMovimiento() {
        return horaMovimiento;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public BigDecimal getMonto() {
        return monto;
    }

}