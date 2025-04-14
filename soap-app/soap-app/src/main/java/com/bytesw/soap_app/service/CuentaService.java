package com.bytesw.soap_app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bytesw.soap_app.model.Cliente;
import com.bytesw.soap_app.model.Cuenta;
import com.bytesw.soap_app.repository.ClienteRepository;
import com.bytesw.soap_app.repository.CuentaRepository;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ClienteRepository clienteRepository;


    private static final Pattern NUMERO_CUENTA_PATTERN = Pattern.compile("^[0-9]{12}$");

    private static class ValidacionError 
    {
        private final String codigo;
        private final String mensaje;

        public ValidacionError(String codigo, String mensaje) 
        {
            this.codigo = codigo;
            this.mensaje = mensaje;
        }

        public String getCodigo() { return codigo; }
        public String getMensaje() { return mensaje; }
    }

    public String guardarCuenta(Long clienteId, LocalDate fechaApertura, 
                              LocalTime horaApertura, String estado, BigDecimal saldo) 
    {
        try {
            String numeroCuenta;
            Cuenta cuenta = crearCuenta(
                clienteId, fechaApertura, horaApertura, estado, saldo
            );
            numeroCuenta = cuenta.getNumeroCuenta();
            
            List<ValidacionError> errores = validarDatosCuenta(
                numeroCuenta, clienteId, fechaApertura, horaApertura, estado, saldo
            );

            if (!clienteRepository.existsById(clienteId)) 
            {
                System.out.println(clienteId);
                //return "999|Cliente no existe";
                return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Cliente no existe", 
                    String.format("%-16s", ""));
            }

            if (!errores.isEmpty()) {
                ValidacionError primerError = errores.get(0);
                //return primerError.getCodigo() + "|" + primerError.getMensaje();
                return String.format("%-3s%-100s%-16s", 
                    primerError.getCodigo(), 
                    primerError.getMensaje(), 
                    String.format("%-16s", ""));
            }

            cuentaRepository.save(cuenta);

            //return "000|Cuenta creada exitosamente";
            return String.format("%-3s%-100s%-16s", 
                    "000", 
                    "Cuenta creada exitosamente", 
                    String.format("%-16s", cuenta.getNumeroCuenta()));

        } catch (Exception e) 
        {
            e.printStackTrace();
            return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Error al crear la cuenta", 
                    String.format("%-16s", e.getMessage()));
        }
    }

    public String actualizarCuenta(String nroCta, Long clienteId, LocalDate fechaApertura, LocalTime horaApertura, String estado, BigDecimal saldo) {
        try {
            Optional<Cuenta> cuentaOpt = cuentaRepository.findByNumeroCuenta(nroCta);

            if (cuentaOpt.isEmpty()) {
                return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Cuenta no encontrada", 
                    String.format("%-16s", ""));
            }

            Cuenta cuenta = cuentaOpt.get();

            List<ValidacionError> errores = validarDatosCuentaExistente(clienteId, fechaApertura, horaApertura, estado, saldo);
            if (!clienteRepository.existsById(clienteId)) {
                return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Cliente no existe", 
                    String.format("%-16s", nroCta));
            }

            if (!errores.isEmpty()) {
                ValidacionError error = errores.get(0);
                return String.format("%-3s%-100s%-16s", 
                    error.getCodigo(), 
                    error.getMensaje(), 
                    String.format("%-16s", nroCta));
            }

            // Actualizar los datos de la cuenta
            cuenta.setClienteId(clienteId);
            cuenta.setFechaApertura(fechaApertura);
            cuenta.setHoraApertura(horaApertura);
            cuenta.setEstado(estado);
            cuenta.setSaldo(saldo);

            cuentaRepository.save(cuenta);

            return String.format("%-3s%-100s%-16s", 
                    "000", 
                    "Cuenta actualizada exitosamente", 
                    String.format("%-16s", nroCta));
        } catch (Exception e) {
            e.printStackTrace();
            return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Error al actualizar la cuenta", 
                    String.format("%-16s", nroCta));
        }
    }
    

    public String eliminarCuenta(String cuenta) {
        try {
            Optional<Cuenta> cuentaOpt = cuentaRepository.findByNumeroCuenta(cuenta.trim());
            if (cuentaOpt.isEmpty()) {
                return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Cuenta no encontrada", 
                    String.format("%-16s", "")); 
            }

            
            Cuenta cuentaTp = cuentaOpt.get();

            if (cuentaTp.getEstado().equals("INACTIVA")) {
                return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Cuenta se encuentra INACTIVA", 
                    String.format("%-16s", "")); 
            }


            cuentaTp.setEstado("INACTIVA");
            cuentaRepository.save(cuentaTp);

            return String.format("%-3s%-100s%-16s", 
                    "000", 
                    "Cuenta paso a inactiva correctamente", 
                    String.format("%-16s", ""));
        } catch (Exception e) {
            return String.format("%-3s%-100s%-16s", 
                "999", 
                "Error al inactivar el cuenta", 
                String.format("%-16s", e.getMessage()));
        }
    }


    private List<ValidacionError> validarDatosCuenta(String numeroCuenta, Long clienteId, 
            LocalDate fechaApertura, LocalTime horaApertura, String estado, BigDecimal saldo) 
    {
        List<ValidacionError> errores = new ArrayList<>();

        // Validación del número de cuenta
        if (numeroCuenta == null) 
        {
            errores.add(new ValidacionError("999", "El número de cuenta es requerido"));
        } else if (!NUMERO_CUENTA_PATTERN.matcher(numeroCuenta).matches()) 
        {
            errores.add(new ValidacionError("999", "El número de cuenta debe contener exactamente 12 dígitos numéricos"));
        } else if (cuentaRepository.existsByNumeroCuenta(numeroCuenta)) 
        {
            errores.add(new ValidacionError("999", "Ya existe una cuenta con ese número"));
        }

        // Validación del cliente
        if (clienteId == null) 
        {
            errores.add(new ValidacionError("999", "El ID del cliente es requerido"));
        }

        // Validación de fechas
        if (fechaApertura == null) 
        {
            errores.add(new ValidacionError("999", "La fecha de apertura es requerida"));
        }
        if (horaApertura == null) 
        {
            errores.add(new ValidacionError("999", "La hora de apertura es requerida"));
        }

        // Validación del estado
        if (estado == null || estado.isEmpty()) 
        {
            errores.add(new ValidacionError("999", "El estado es requerido"));
        }

        // Validación del saldo
        if (saldo == null) 
        {
            errores.add(new ValidacionError("999", "El saldo es requerido"));
        } else if (saldo.compareTo(BigDecimal.ZERO) < 0) {
            errores.add(new ValidacionError("999", "El saldo debe ser mayor o igual a cero"));
        }

        return errores;
    }

    private Cuenta crearCuenta(Long clienteId, LocalDate fechaApertura,
            LocalTime horaApertura, String estado, BigDecimal saldo) 
    {
        Cuenta cuenta = new Cuenta();
        //cuenta.setNumeroCuenta(numeroCuenta);
        cuenta.setClienteId(clienteId);
        cuenta.setFechaApertura(fechaApertura);
        cuenta.setHoraApertura(horaApertura);
        cuenta.setEstado(estado);
        //cuenta.setCta(estado);
        cuenta.setSaldo(saldo);

        String ultimoNumero = cuentaRepository.findTopByOrderByNumeroCuentaDesc() 
            .map(Cuenta::getNumeroCuenta)
            .orElse("0");
        long siguienteNumero = Long.parseLong(ultimoNumero) + 1;
        cuenta.setNumeroCuenta(String.valueOf(siguienteNumero));
        cuenta.setCta(String.valueOf(siguienteNumero));

        return cuenta;
    }

    private List<ValidacionError> validarDatosCuentaExistente(Long clienteId, LocalDate fechaApertura, LocalTime horaApertura, String estado, BigDecimal saldo) 
{
        List<ValidacionError> errores = new ArrayList<>();

        if (clienteId == null) {
            errores.add(new ValidacionError("999", "El ID del cliente es requerido"));
        }

        if (fechaApertura == null) {
            errores.add(new ValidacionError("999", "La fecha de apertura es requerida"));
        }

        if (horaApertura == null) {
            errores.add(new ValidacionError("999", "La hora de apertura es requerida"));
        }

        if (estado == null || estado.isEmpty()) {
            errores.add(new ValidacionError("999", "El estado es requerido"));
        }

        if (saldo == null) {
            errores.add(new ValidacionError("999", "El saldo es requerido"));
        } else if (saldo.compareTo(BigDecimal.ZERO) < 0) {
            errores.add(new ValidacionError("999", "El saldo debe ser mayor o igual a cero"));
        }

        return errores;
    }
}

