package com.bytesw.soap_app.endpoint;

import com.bytesw.soap_app.service.MovimientoService;
import com.bytesw.soap_app.utils.MovimientoParser;
import com.bytesw.soap_app.utils.MovimientoParser.MovimientoData;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@Endpoint
public class Movimiento {

    private static final String NAMESPACE_URI = "http://bytesw.com/soap/movimientos";

    private MovimientoService movimientoService;
    private MovimientoParser movimientoParser;

    public Movimiento(MovimientoService movimientoService, MovimientoParser movimientoParser) 
    {
        this.movimientoService = movimientoService;
        this.movimientoParser = movimientoParser;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "procesarMovimientoRequest")
    @ResponsePayload
    public ProcesarMovimientoResponse procesarMovimiento(@RequestPayload ProcesarMovimientoRequest request) 
    {
        MovimientoData data = movimientoParser.parse(request.getTrama());

        String tramaRespuesta = movimientoService.procesarTrama(data);

        ProcesarMovimientoResponse response = new ProcesarMovimientoResponse();
        response.setTrama(tramaRespuesta);
        return response;
    }

    @XmlRootElement(namespace = NAMESPACE_URI)
    public static class ProcesarMovimientoRequest 
    {
        private String trama;

        @XmlElement
        public String getTrama() 
        {
            return trama;
        }

        public void setTrama(String trama) 
        {
            this.trama = trama;
        }
    }

    @XmlRootElement(namespace = NAMESPACE_URI)
    public static class ProcesarMovimientoResponse 
    {
        private String trama;

        @XmlElement
        public String getTrama() 
        {
            return trama;
        }

        public void setTrama(String trama) 
        {
            this.trama = trama;
        }
    }
}

