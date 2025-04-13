package com.bytesw.soap_app.request.Cuenta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "eliminarCuentaResponse", namespace = "http://bytesw.com/soap/cuentas")
@XmlAccessorType(XmlAccessType.FIELD)
public class EliminarCuentaResponse 
{
    private String trama;

    public String getTrama() 
    {
        return trama;
    }

    public void setTrama(String trama) 
    {
        this.trama = trama;
    }
}
