package com.bytesw.soap_app.endpoint;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.bytesw.soap_app.repository.ClienteRepository;
import com.bytesw.soap_app.request.Cliente.EliminarClienteRequest;
import com.bytesw.soap_app.request.Cliente.EliminarClienteResponse;
import com.bytesw.soap_app.request.Cuenta.CrearCuentaRequest;
import com.bytesw.soap_app.request.Cuenta.CrearCuentaResponse;
import com.bytesw.soap_app.request.Cuenta.EliminarCuentaRequest;
import com.bytesw.soap_app.request.Cuenta.EliminarCuentaResponse;
import com.bytesw.soap_app.service.CuentaService;
import com.bytesw.soap_app.utils.CuentaParse;
import com.bytesw.soap_app.utils.CuentaParse.CuentaData;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Endpoint
public class Cuenta {

    private static final String NAMESPACE_URI = "http://bytesw.com/soap/cuentas";

    private final CuentaService cuentaService;
    private final CuentaParse cuentaParse;

    public Cuenta(CuentaService cuentaService, CuentaParse cuentaParse) 
    {
        this.cuentaService = cuentaService;
        this.cuentaParse = cuentaParse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "crearCuentaRequest")
    @ResponsePayload
    public CrearCuentaResponse crearCuenta(@RequestPayload CrearCuentaRequest request) 
    {
        
        CuentaData data = cuentaParse.parse(request.getTrama());

        String resultado = cuentaService.guardarCuenta(
                //data.nroCuenta,
                data.clienteId, 
                data.fechaApertura,
                data.horaApertura, 
                data.estado, 
                data.saldo 
        );
        //String[] partes = resultado.split("\\|", 2);

        CrearCuentaResponse response = new CrearCuentaResponse();
        response.setTrama(resultado);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "eliminarCuentaRequest")
    @ResponsePayload
    public EliminarCuentaResponse eliminarCuenta(@RequestPayload EliminarCuentaRequest request) {
       
        String resultado = cuentaService.eliminarCuenta(request.getTrama());

        EliminarCuentaResponse response = new EliminarCuentaResponse();
        response.setTrama(resultado);
        return response;

    }

    public static class LocalDateAdapter extends XmlAdapter<String, LocalDate> 
    {
        private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

        @Override
        public LocalDate unmarshal(String v) throws Exception 
        {
            return v != null ? LocalDate.parse(v, formatter) : null;
        }

        @Override
        public String marshal(LocalDate v) throws Exception 
        {
            return v != null ? v.format(formatter) : null;
        }
    }

    public static class LocalTimeAdapter extends XmlAdapter<String, LocalTime> 
    {
        private final DateTimeFormatter formatter = DateTimeFormatter.ISO_TIME;

        @Override
        public LocalTime unmarshal(String v) throws Exception 
        {
            return v != null ? LocalTime.parse(v, formatter) : null;
        }

        @Override
        public String marshal(LocalTime v) throws Exception 
        {
            return v != null ? v.format(formatter) : null;
        }
    }

    // @XmlRootElement(name = "crearCuentaRequest", namespace = NAMESPACE_URI)
    // @XmlAccessorType(XmlAccessType.FIELD)
    // public static class CrearCuentaRequest 
    // {
    //     @XmlElement
    //     private String numeroCuenta;
    //     @XmlElement
    //     private Long clienteId;
    //     @XmlElement
    //     @XmlJavaTypeAdapter(LocalDateAdapter.class)
    //     private LocalDate fechaApertura;
    //     @XmlElement
    //     @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    //     private LocalTime horaApertura;
    //     @XmlElement
    //     private String estado;
    //     @XmlElement
    //     private BigDecimal saldo;

    //     public String getNumeroCuenta() { return numeroCuenta; }
    //     public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    //     public Long getClienteId() { return clienteId; }
    //     public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    //     public LocalDate getFechaApertura() { return fechaApertura; }
    //     public void setFechaApertura(LocalDate fechaApertura) { this.fechaApertura = fechaApertura; }
    //     public LocalTime getHoraApertura() { return horaApertura; }
    //     public void setHoraApertura(LocalTime horaApertura) { this.horaApertura = horaApertura; }
    //     public String getEstado() { return estado; }
    //     public void setEstado(String estado) { this.estado = estado; }
    //     public BigDecimal getSaldo() { return saldo; }
    //     public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    // }

    // @XmlRootElement(name = "crearCuentaResponse", namespace = NAMESPACE_URI)
    // @XmlAccessorType(XmlAccessType.FIELD)
    // public static class CrearCuentaResponse 
    // {
    //     @XmlElement
    //     private String codigo;
    //     @XmlElement
    //     private String mensaje;

    //     public String getCodigo() { return codigo; }
    //     public void setCodigo(String codigo) { this.codigo = codigo; }
    //     public String getMensaje() { return mensaje; }
    //     public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    // }

    // @XmlRootElement(namespace = NAMESPACE_URI)
    // public static class ProcesarCuentaRequest 
    // {
    //     private String trama;

    //     @XmlElement
    //     public String getTrama() 
    //     {
    //         return trama;
    //     }

    //     public void setTrama(String trama) 
    //     {
    //         this.trama = trama;
    //     }
    // }

    // @XmlRootElement(namespace = NAMESPACE_URI)
    // public static class ProcesarCuentaResponse 
    // {
    //     private String trama;

    //     @XmlElement
    //     public String getTrama() 
    //     {
    //         return trama;
    //     }

    //     public void setTrama(String trama) 
    //     {
    //         this.trama = trama;
    //     }
    // }
}
