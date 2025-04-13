package com.bytesw.soap_app.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ClienteParser {
    @Value("${trama.cliente.nombre.length}")
    private int nombreLength;

    @Value("${trama.cliente.identificacion.length}")
    private int identificacionLength;

    @Value("${trama.cliente.tipoIdentificacion.length}")
    private int tipoIdentificacionLength;

    @Value("${trama.cliente.fechaNacimiento.length}")
    private int fechaNacimientoLength;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ClienteData parse(String trama) {
        int index = 0;

        String nombre = trama.substring(index, index + nombreLength).trim();
        index += nombreLength;

        String identificacion = trama.substring(index, index + identificacionLength).trim();
        index += identificacionLength;

        String tipoIdentificacion = trama.substring(index, index + tipoIdentificacionLength).trim();
        index += tipoIdentificacionLength;

        String fechaNacimientoStr = trama.substring(index, index + fechaNacimientoLength).trim();
        LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr, dateFormatter);

        return new ClienteData(nombre, identificacion, tipoIdentificacion, fechaNacimiento);
    }

    public static class ClienteData {
        public final String nombre;
        public final String identificacion;
        public final String tipoIdentificacion;
        public final LocalDate fechaNacimiento;

        public ClienteData(String nombre, String identificacion, String tipoIdentificacion, LocalDate fechaNacimiento) {
            this.nombre = nombre;
            this.identificacion = identificacion;
            this.tipoIdentificacion = tipoIdentificacion;
            this.fechaNacimiento = fechaNacimiento;
        }
    }
    
}
