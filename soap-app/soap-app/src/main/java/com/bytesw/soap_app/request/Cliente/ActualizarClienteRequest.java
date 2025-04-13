package com.bytesw.soap_app.request.Cliente;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "actualizarClienteRequest", namespace = "http://bytesw.com/soap/clientes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActualizarClienteRequest {
    private String trama;

    public String getTrama() {
        return trama;
    }

    public void setTrama(String trama) {
        this.trama = trama;
    }
}
