package com.bytesw.rest_app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.xml.transform.StringSource;

import com.bytesw.rest_app.utils.ParserResponse;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

@Component
public class ClientSoap {

    @Value("${trama.cliente.nombre.length}")
    private int nombreLength;

    @Value("${trama.cliente.identificacion.length}")
    private int identificacionLength;

    @Value("${trama.cliente.tipoIdentificacion.length}")
    private int tipoIdentificacionLength;

    @Value("${trama.cliente.fechaNacimiento.length}")
    private int fechaNacimientoLength;


    private static final String SOAP_URI = "http://localhost:8081/ws";

    private final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    public Map<String, String> enviarCrearCliente(String nombre, String identificacion, String tipoIdentificacion, String fechaNacimiento) {
        String trama = buildTrama(nombre, identificacion, tipoIdentificacion, fechaNacimiento);
        String xml =
            // "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' " +
            // "xmlns:cli='http://bytesw.com/soap/clientes'>" +
            // "<soapenv:Header/>" +
            // "<soapenv:Body>" +
            "<cli:crearClienteRequest xmlns:cli='http://bytesw.com/soap/clientes'>" +
            "<trama>" + trama + "</trama>" +
            "</cli:crearClienteRequest>" ;
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
                Map<String, String> error = new HashMap<>();
                error.put("codigo", "999");
                error.put("mensaje", "Error al consumir servicio SOAP: " + e.getMessage());
                error.put("referencia", "");
                return error;
            }
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

    private String buildTrama(String nombre, String identificacion, String tipoIdentificacion, String fechaNacimiento) {
        String fechaFormateada = LocalDate.parse(fechaNacimiento).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        StringBuilder sb = new StringBuilder();
        sb.append(fixed(nombre, nombreLength));
        sb.append(fixed(identificacion, identificacionLength));
        sb.append(fixed(tipoIdentificacion, tipoIdentificacionLength));
        sb.append(fixed(fechaFormateada, fechaNacimientoLength));
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
