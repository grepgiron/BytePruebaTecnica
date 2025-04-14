package com.bytesw.soap_app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytesw.soap_app.model.Cuenta;
import com.bytesw.soap_app.model.Movimiento;
import com.bytesw.soap_app.repository.CuentaRepository;
import com.bytesw.soap_app.repository.MovimientoRepository;
import com.bytesw.soap_app.utils.MovimientoParser.MovimientoData;

@Service
public class MovimientoService 
{

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    private static final Pattern NUMERO_CUENTA_PATTERN = Pattern.compile("^[0-9]{12}$");
    private static final Pattern TIPO_PATTERN = Pattern.compile("^[DT]$"); // D: Débito, T: Transferencia
    private static final Pattern MONTO_PATTERN = Pattern.compile("^\\d{1,14}\\.\\d{2}$");
    private static final Pattern FECHA_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final DateTimeFormatter FECHA_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

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

    @Transactional
    public String procesarTrama(MovimientoData trama) 
    {
        try 
        {
            
            //Extraer y validar trama
            String cuentaOrigen = trama.cuentaOrigen;
            String cuentaDestino = trama.cuentaDestino;
            LocalDate fechaStr = LocalDate.now();
            String horaStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String tipo = trama.tipo;
            String montoStr = trama.monto;

            //Validar trama
            List<ValidacionError> errores = validarDatosMovimiento(
                cuentaOrigen, cuentaDestino, horaStr, tipo, montoStr
            );

            if (!errores.isEmpty()) 
            {
                ValidacionError primerError = errores.get(0);
                return String.format("%-3s%-100s%-16s", 
                    primerError.getCodigo(), 
                    primerError.getMensaje(), 
                    String.format("%-16s", ""));
            }

            //Trama de movimiento
            LocalDate fecha = fechaStr;
            LocalTime hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
            BigDecimal monto = new BigDecimal(montoStr);

            //Obtener cuentas
            Cuenta cuentaOrigenObj;
            Cuenta cuentaDestinoObj;
            Optional<Cuenta> cuentaOrigenOpt = cuentaRepository.findByNumeroCuenta(cuentaOrigen);
            Optional<Cuenta> cuentaDestinoOpt = cuentaRepository.findByNumeroCuenta(cuentaDestino);

            if (cuentaOrigenOpt.isEmpty() || cuentaDestinoOpt.isEmpty()) 
            {
                return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Cuenta no existe", 
                    String.format("%-16s", ""));
            } else
            
            {
                cuentaOrigenObj = cuentaOrigenOpt.get();
                cuentaDestinoObj = cuentaDestinoOpt.get();
            }

            //Validar cuentas activas
            if (cuentaOrigenObj.getEstado().equals("INACTIVA") || cuentaDestinoObj.getEstado().equals("INACTIVA")) 
            
            {
                return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Cuenta esta INACTIVA", 
                    String.format("%-16s", ""));
            }
            //Validar saldo suficiente
            if (TIPO_PATTERN.matcher(tipo).matches() && cuentaOrigenObj.getSaldo().compareTo(monto) < 0) 
            {
                return String.format("%-3s%-100s%-16s", 
                    "999", 
                    "Saldo insuficiente en la cuenta", 
                    String.format("%-16s", ""));
            }

            //Actualizar saldos entre cuentas
            if (tipo.equals("D")) 
            {
                // Débito: reducir saldo de cuenta origen
                cuentaOrigenObj.setSaldo(cuentaOrigenObj.getSaldo().subtract(monto));
            } else if (tipo.equals("T")) 
            {
                // Transferencia: reducir saldo de cuenta origen y aumentar saldo de cuenta destino
                cuentaOrigenObj.setSaldo(cuentaOrigenObj.getSaldo().subtract(monto));
                cuentaDestinoObj.setSaldo(cuentaDestinoObj.getSaldo().add(monto));
            }

            //Guardar cambios en las cuentas
            cuentaRepository.save(cuentaOrigenObj);
            if (tipo.equals("T")) 
            {
                cuentaRepository.save(cuentaDestinoObj);
            }

            //registrar movimiento
            Movimiento movimiento = crearMovimiento(
                cuentaOrigen, cuentaDestino, fecha, hora, tipo, monto
            );
            movimientoRepository.save(movimiento);

            
            StringBuilder numeroReferencia = new StringBuilder(movimiento.getNumeroReferencia());
            while (numeroReferencia.length() < 16) 
            {
                numeroReferencia.append(" ");
            }

            return String.format("%-3s%-100s%-16s", 
                "000", 
                "Operación exitosa",
                String.format("%-16s", movimiento.getNumeroReferencia())); 
                
        } catch (Exception e) 
        {
            e.printStackTrace();
            return String.format("%-3s%-100s%-16s", 
                "999", 
                "Error al procesar la trama: " + e.getMessage(), 
                String.format("%-16s", ""));
        }
    }

    private List<ValidacionError> validarDatosMovimiento(String cuentaOrigen, String cuentaDestino,
            String horaStr, String tipo, String montoStr) 
    {
        List<ValidacionError> errores = new ArrayList<>();

        //Validación de cuentas
        if (!NUMERO_CUENTA_PATTERN.matcher(cuentaOrigen).matches()) 
        {
            errores.add(new ValidacionError("999", "El número de cuenta origen debe contener exactamente 12 dígitos numéricos"));
        } else if (!cuentaRepository.existsByNumeroCuenta(cuentaOrigen)) 
        {
            errores.add(new ValidacionError("999", "La cuenta origen no existe"));
        }

        if (!NUMERO_CUENTA_PATTERN.matcher(cuentaDestino).matches()) 
        {
            errores.add(new ValidacionError("999", "El número de cuenta destino debe contener exactamente 12 dígitos numéricos"));
        } else if (!cuentaRepository.existsByNumeroCuenta(cuentaDestino)) 
        {
            errores.add(new ValidacionError("999", "La cuenta destino no existe"));
        }

        //Validación de tipo
        if (!TIPO_PATTERN.matcher(tipo).matches()) 
        {
            errores.add(new ValidacionError("999", "El tipo debe ser D (Débito) o T (Transferencia)"));
        }

        //Validación de monto
        if (!MONTO_PATTERN.matcher(montoStr).matches()) 
        {
            errores.add(new ValidacionError("999", "El monto debe tener formato numérico con 2 decimales"));
        } else 
        {
            BigDecimal monto = new BigDecimal(montoStr);
            if (monto.compareTo(BigDecimal.ZERO) <= 0) 
        {
                errores.add(new ValidacionError("999", "El monto debe ser mayor que cero"));
            }
        }

        return errores;
    }

    private Movimiento crearMovimiento(String cuentaOrigen, String cuentaDestino,
            LocalDate fecha, LocalTime hora, String tipo, BigDecimal monto) 
    {
        Movimiento movimiento = new Movimiento();
        movimiento.setCuentaOrigen(cuentaOrigen);
        movimiento.setCuentaDestino(cuentaDestino);
        movimiento.setFecha(fecha);
        movimiento.setHora(hora);
        movimiento.setTipo(tipo);
        movimiento.setMonto(monto);
        
        //Obtener el último número de referencia y sumar 1 (nuevo Id)
        String ultimoNumero = movimientoRepository.findTopByOrderByNumeroReferenciaDesc()
            .map(Movimiento::getNumeroReferencia)
            .orElse("0");
        long siguienteNumero = Long.parseLong(ultimoNumero) + 1;
        movimiento.setNumeroReferencia(String.valueOf(siguienteNumero));
        
        return movimiento;
    }
}

