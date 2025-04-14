package com.bytesw.soap_app.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CuentaParserActualiza {
    @Value("${trama.cuenta.nroCuenta.length}")
    private int nroCuentaLength;

    @Value("${trama.cuenta.clienteId.length}")
    private int clienteIdLength;

    @Value("${trama.cuenta.fechaApertura.length}")
    private int fechaAperturaLength;

    @Value("${trama.cuenta.horaApertura.length}")
    private int horaAperturaLength;

    @Value("${trama.cuenta.estado.length}")
    private int estadoLength;

    @Value("${trama.cuenta.saldo.length}")
    private int saldoLength;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public CuentaData parse(String trama) {
        int index = 0;

        String nroCuenta = trama.substring(index, index + nroCuentaLength).trim();
        index += nroCuentaLength;

        String clienteIdStr = trama.substring(index, index + clienteIdLength).trim();
        index += clienteIdLength;

        String fechaAperturaStr = trama.substring(index, index + fechaAperturaLength).trim();
        index += fechaAperturaLength;

        String horaAperturaStr = trama.substring(index, index + horaAperturaLength).trim();
        index += horaAperturaLength;

        String estado = trama.substring(index, index + estadoLength).trim();
        index += estadoLength;

        String saldoStr = trama.substring(index, index + saldoLength).trim();

        Long clienteId = Long.parseLong(clienteIdStr);
        LocalDate fechaApertura = LocalDate.parse(fechaAperturaStr, dateFormatter);
        LocalTime horaApertura = LocalTime.parse(horaAperturaStr, timeFormatter);
        BigDecimal saldo = new BigDecimal(saldoStr);

        return new CuentaData(nroCuenta, clienteId, fechaApertura, horaApertura, estado, saldo);
    }

    public static class CuentaData {
        public final String nroCuenta;
        public final Long clienteId;
        public final LocalDate fechaApertura;
        public final LocalTime horaApertura;
        public final String estado;
        public final BigDecimal saldo;

        public CuentaData(String nroCuenta, Long clienteId, LocalDate fechaApertura, LocalTime horaApertura, String estado, BigDecimal saldo) {
            this.nroCuenta = nroCuenta;
            this.clienteId = clienteId;
            this.fechaApertura = fechaApertura;
            this.horaApertura = horaApertura;
            this.estado = estado;
            this.saldo = saldo;
        }
    }
}
