package com.bytesw.rest_app.service;

import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.Source;

@Component
public class ClientSoap {

    private static final String SOAP_URI = "http://localhost:8081/ws";

    private final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    public String enviarCrearCliente(String nombre, String identificacion, String tipoIdentificacion, String fechaNacimiento) {
        String xml = "<crearClienteRequest xmlns='http://bytesw.com/soap/clientes'>" +
                "<nombre>" + nombre + "</nombre>" +
                "<identificacion>" + identificacion + "</identificacion>" +
                "<tipoIdentificacion>" + tipoIdentificacion + "</tipoIdentificacion>" +
                "<fechaNacimiento>" + fechaNacimiento + "</fechaNacimiento>" +
                "</crearClienteRequest>";
        System.out.print(xml);
        Source source = new StringSource(xml);
        Source response = webServiceTemplate.sendSourceAndReceive(SOAP_URI, source, null);
        return response.toString();
    }

    public String enviarCrearCuenta(String numeroCuenta, Long clienteId, String fechaApertura, String horaApertura, String estado, String saldo) {
        String xml = "<crearCuentaRequest xmlns='http://bytesw.com/soap/cuentas'>" +
                "<numeroCuenta>" + numeroCuenta + "</numeroCuenta>" +
                "<clienteId>" + clienteId + "</clienteId>" +
                "<fechaApertura>" + fechaApertura + "</fechaApertura>" +
                "<horaApertura>" + horaApertura + "</horaApertura>" +
                "<estado>" + estado + "</estado>" +
                "<saldo>" + saldo + "</saldo>" +
                "</crearCuentaRequest>";

        Source source = new StringSource(xml);
        Source response = webServiceTemplate.sendSourceAndReceive(SOAP_URI, source, null);
        return response.toString();
    }
}
