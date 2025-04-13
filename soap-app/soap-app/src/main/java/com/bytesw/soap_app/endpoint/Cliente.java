package com.bytesw.soap_app.endpoint;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.bytesw.soap_app.request.Cliente.ActualizarClienteRequest;
import com.bytesw.soap_app.request.Cliente.ActualizarClienteResponse;
import com.bytesw.soap_app.request.Cliente.CrearClienteRequest;
import com.bytesw.soap_app.request.Cliente.CrearClienteResponse;
import com.bytesw.soap_app.request.Cliente.EliminarClienteRequest;
import com.bytesw.soap_app.request.Cliente.EliminarClienteResponse;
import com.bytesw.soap_app.service.ClienteService;
import com.bytesw.soap_app.utils.ClienteParser;
import com.bytesw.soap_app.utils.ClienteParser.ClienteData;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Endpoint
public class Cliente {

    private static final String NAMESPACE_URI = "http://bytesw.com/soap/clientes";

    private final ClienteService clienteService;
    private final ClienteParser clienteParser;

    public Cliente(ClienteService clienteService, ClienteParser clienteParser) 
    {
        this.clienteService = clienteService;
        this.clienteParser = clienteParser;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "crearClienteRequest")
    @ResponsePayload
    public CrearClienteResponse crearCliente(@RequestPayload CrearClienteRequest request) 
    {
        ClienteData data = clienteParser.parse(request.getTrama());
        
        String resultado = clienteService.guardarCliente(
                data.nombre,
                data.identificacion,
                data.tipoIdentificacion,
                data.fechaNacimiento
        );

        //String[] partes = resultado.split("\\|", 2);
        CrearClienteResponse response = new CrearClienteResponse();
        response.setTrama(resultado);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "actualizarClienteRequest")
    @ResponsePayload
    public ActualizarClienteResponse actualizarCliente(@RequestPayload ActualizarClienteRequest request) {

        ClienteData datos = clienteParser.parse(request.getTrama());

        String resultado = clienteService.actualizarCliente(
                datos.nombre,
                datos.identificacion,
                datos.tipoIdentificacion,
                datos.fechaNacimiento
        );

        ActualizarClienteResponse response = new ActualizarClienteResponse();
        response.setTrama(resultado);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "eliminarClienteRequest")
    @ResponsePayload
    public EliminarClienteResponse eliminarCliente(@RequestPayload EliminarClienteRequest request) {
       
        String resultado = clienteService.eliminarCliente(request.getTrama());

        EliminarClienteResponse response = new EliminarClienteResponse();
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

   
    // @XmlRootElement(namespace = NAMESPACE_URI)
    // public static class ProcesarClienteRequest 
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
    // public static class ProcesarClienteResponse 
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

