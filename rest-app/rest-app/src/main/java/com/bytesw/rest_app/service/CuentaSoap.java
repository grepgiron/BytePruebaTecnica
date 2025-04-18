package com.bytesw.rest_app.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.xml.transform.StringSource;

import com.bytesw.rest_app.controllers.ClienteController;
import com.bytesw.rest_app.utils.ParserResponse;

@Component
public class CuentaSoap {
    @Value("${trama.cuenta.nroCuenta.length}")
    private int nroCtaLentgh;

    @Value("${trama.cuenta.clienteId.length}")
    private int clienteIdLentgh;

    @Value("${trama.cuenta.fechaApertura.length}")
    private int fechaAperturaLentgh;

    @Value("${trama.cuenta.horaApertura.length}")
    private int horaAperturaLentgh;

    @Value("${trama.cuenta.estado.length}")
    private int estadoLentgh;

    @Value("${trama.cuenta.saldo.length}")
    private int saldoLentgh;


    private static final String SOAP_URI = "http://localhost:8081/ws";

    private static final Logger logger = LoggerFactory.getLogger(CuentaSoap.class);

    @Autowired
    private WebServiceTemplate webServiceTemplate;
    
    public Map<String, String> enviarCrearCuenta(String clienteId, String fechaApertura, String horaApertura, String estado, String saldo) {
        String trama = buildTrama(clienteId, fechaApertura, horaApertura, estado, saldo);
        String xml =
            // "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' " +
            // "xmlns:cli='http://bytesw.com/soap/clientes'>" +
            // "<soapenv:Header/>" +
            // "<soapenv:Body>" +
            "<cli:crearCuentaRequest xmlns:cli='http://bytesw.com/soap/cuentas'>" +
            "<trama>" + trama + "</trama>" +
            "</cli:crearCuentaRequest>" ;
            // "</soapenv:Body>" +
            // "</soapenv:Envelope>";

            Source requestSource = new StringSource(xml);

            try {
                String rawResponse = webServiceTemplate.sendSourceAndReceive(
                    SOAP_URI,
                    requestSource,
                    source -> {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        transformer.transform(source, new StreamResult(out));
                        return out.toString(StandardCharsets.UTF_8);
                    }
                );

                ParserResponse parser = new ParserResponse();
                return parser.parseSoapResponse(rawResponse);

            } catch (Exception e) {
                e.printStackTrace();
                logger.error(xml, e);
                Map<String, String> error = new HashMap<>();
                error.put("codigo", "999");
                error.put("mensaje", "Error al consumir servicio SOAP: " + e.getMessage());
                error.put("referencia", "");
                return error;
            }
    }

    public Map<String, String> enviarActualizarCuenta(String nroCta, String clienteId, String fechaApertura, String horaApertura, String estado, String saldo) {
        String trama = buildTramaActualizar(nroCta, clienteId, fechaApertura, horaApertura, estado, saldo);
        String xml =
            // "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' " +
            // "xmlns:cli='http://bytesw.com/soap/clientes'>" +
            // "<soapenv:Header/>" +
            // "<soapenv:Body>" +
            "<cli:actualizarCuentaRequest xmlns:cli='http://bytesw.com/soap/cuentas'>" +
            "<trama>" + trama + "</trama>" +
            "</cli:actualizarCuentaRequest>" ;
            // "</soapenv:Body>" +
            // "</soapenv:Envelope>";

            Source requestSource = new StringSource(xml);

            try {
                String rawResponse = webServiceTemplate.sendSourceAndReceive(
                    SOAP_URI,
                    requestSource,
                    source -> {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        transformer.transform(source, new StreamResult(out));
                        return out.toString(StandardCharsets.UTF_8);
                    }
                );

                ParserResponse parser = new ParserResponse();
                return parser.parseSoapResponse(rawResponse);

            } catch (Exception e) {
                e.printStackTrace();
                logger.error(xml, e);
                Map<String, String> error = new HashMap<>();
                error.put("codigo", "999");
                error.put("mensaje", "Error al consumir servicio SOAP: " + e.getMessage());
                error.put("referencia", "");
                return error;
            }
    }

    public Map<String, String> enviarEliminarCuenta(String nroCta) {
        String trama = buildTramaEliminar(nroCta.trim());
        String xml =
            // "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' " +
            // "xmlns:cli='http://bytesw.com/soap/clientes'>" +
            // "<soapenv:Header/>" +
            // "<soapenv:Body>" +
            "<cli:eliminarCuentaRequest xmlns:cli='http://bytesw.com/soap/cuentas'>" +
            "<trama>" + trama + "</trama>" +
            "</cli:eliminarCuentaRequest>" ;
            // "</soapenv:Body>" +
            // "</soapenv:Envelope>";

            Source requestSource = new StringSource(xml);

            try {
                String rawResponse = webServiceTemplate.sendSourceAndReceive(
                    SOAP_URI,
                    requestSource,
                    source -> {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        transformer.transform(source, new StreamResult(out));
                        return out.toString(StandardCharsets.UTF_8);
                    }
                );

                ParserResponse parser = new ParserResponse();
                return parser.parseSoapResponse(rawResponse);

            } catch (Exception e) {
                e.printStackTrace();
                logger.error(xml, e);
                Map<String, String> error = new HashMap<>();
                error.put("codigo", "999");
                error.put("mensaje", "Error al consumir servicio SOAP: " + e.getMessage());
                error.put("referencia", "");
                return error;
            }
    }

    private String buildTrama(String clienteId, String fechaApertura, String horaApertura, String estado, String saldo) {
        String fechaFormateada = LocalDate.parse(fechaApertura).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        StringBuilder sb = new StringBuilder();
        sb.append(fixed(clienteId, clienteIdLentgh));
        sb.append(fixed(fechaFormateada, fechaAperturaLentgh));
        sb.append(fixed(horaApertura, horaAperturaLentgh));
        sb.append(fixed(estado, estadoLentgh));
        sb.append(fixed(saldo, saldoLentgh));
        return sb.toString();
    }

    private String buildTramaActualizar(String nroCta, String clienteId, String fechaApertura, String horaApertura, String estado, String saldo) {
        System.out.println(nroCta);
        String fechaFormateada = LocalDate.parse(fechaApertura).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        StringBuilder sb = new StringBuilder();
        sb.append(fixed(nroCta, nroCtaLentgh));
        sb.append(fixed(clienteId, clienteIdLentgh));
        sb.append(fixed(fechaFormateada, fechaAperturaLentgh));
        sb.append(fixed(horaApertura, horaAperturaLentgh));
        sb.append(fixed(estado, estadoLentgh));
        sb.append(fixed(saldo, saldoLentgh));
        return sb.toString();
    }

    private String buildTramaEliminar(String nroCta) {
        StringBuilder sb = new StringBuilder();
        sb.append(fixed(nroCta.trim(), nroCtaLentgh));
        return sb.toString();
    }

    private String fixed(String value, int length) {
        if (value == null) value = "";
        if (value.length() > length) {
            return value.substring(0, length);
        }
        return String.format("%-" + length + "s", value);
    }    
}
