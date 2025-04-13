package com.bytesw.soap_app.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class MovimientoParser {

    @Value("${trama.movimiento.cuentaOrigen.length}")
    private int cuentaOrigenLength;

    @Value("${trama.movimiento.cuentaDestino.length}")
    private int cuentaDestinoLength;

    @Value("${trama.movimiento.fecha.length}")
    private int fechaLength;

    @Value("${trama.movimiento.hora.length}")
    private int horaLength;

    @Value("${trama.movimiento.tipo.length}")
    private int tipoLength;

    @Value("${trama.movimiento.monto.length}")
    private int montoLength;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public MovimientoData parse(String trama) {
        int index = 0;

        String cuentaOrigen = trama.substring(index, index + cuentaOrigenLength).trim();
        index += cuentaOrigenLength;

        String cuentaDestino = trama.substring(index, index + cuentaDestinoLength).trim();
        index += cuentaDestinoLength;

        String fechaStr = trama.substring(index, index + fechaLength).trim();
        index += fechaLength;

        String horaStr = trama.substring(index, index + horaLength).trim();
        index += horaLength;

        String tipo = trama.substring(index, index + tipoLength).trim();
        index += tipoLength;

        String montoStr = trama.substring(index, index + montoLength).trim();

        LocalDate fecha = LocalDate.parse(fechaStr, dateFormatter);
        String hora = LocalTime.parse(horaStr, timeFormatter).toString();
        String monto = montoStr;

        return new MovimientoData(cuentaOrigen, cuentaDestino, fecha, hora, tipo, monto);
    }

    public static class MovimientoData {
        public final String cuentaOrigen;
        public final String cuentaDestino;
        public final LocalDate fecha;
        public final String hora;
        public final String tipo;
        public final String monto;

        public MovimientoData(String cuentaOrigen, String cuentaDestino, LocalDate fecha,
        String hora, String tipo, String monto) {
            this.cuentaOrigen = cuentaOrigen;
            this.cuentaDestino = cuentaDestino;
            this.fecha = fecha;
            this.hora = hora;
            this.tipo = tipo;
            this.monto = monto;
        }
    }
}