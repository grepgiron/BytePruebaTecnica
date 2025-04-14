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
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.xml.transform.StringSource;

import com.bytesw.rest_app.controllers.ClienteController;
import com.bytesw.rest_app.utils.ParserResponse;

@Component
public class MovimientosSoap {
    @Value("${trama.movimiento.cuentaOrigen.length}")
    private int cuentaOrigenLentgh;

    @Value("${trama.movimiento.cuentaDestino.length}")
    private int cuentaDestinoLentgh;

    @Value("${trama.movimiento.fecha.length}")
    private int fechaLentgh;

    @Value("${trama.movimiento.hora.length}")
    private int horaLentgh;

    @Value("${trama.movimiento.tipo.length}")
    private int tipoLentgh;

    @Value("${trama.movimiento.monto.length}")
    private int montoLentgh;


    private static final String SOAP_URI = "http://localhost:8081/ws";

    private static final Logger logger = LoggerFactory.getLogger(MovimientosSoap.class);

    @Autowired
    private WebServiceTemplate webServiceTemplate;
    
    public Map<String, String> enviarCrearMovimiento(String cuentaOrigen, String cuentaDestino, String fechaStr, String horaStr, String tipo, String montoStr) {
        String trama = buildTrama(cuentaOrigen, cuentaDestino, fechaStr, horaStr, tipo, montoStr);
        String xml =
            // "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' " +
            // "xmlns:cli='http://bytesw.com/soap/clientes'>" +
            // "<soapenv:Header/>" +
            // "<soapenv:Body>" +
            "<cli:procesarMovimientoRequest xmlns:cli='http://bytesw.com/soap/movimientos'>" +
            "<trama>" + trama + "</trama>" +
            "</cli:procesarMovimientoRequest>" ;
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
                logger.info(rawResponse);
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
    
    private String buildTrama(String cuentaOrigen, String cuentaDestino, String fechaStr, String horaStr, String tipo, String montoStr) {
        String fechaFormateada = LocalDate.parse(fechaStr).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        StringBuilder sb = new StringBuilder();
        sb.append(fixed(cuentaOrigen, cuentaOrigenLentgh));
        sb.append(fixed(cuentaDestino, cuentaDestinoLentgh));
        sb.append(fixed(fechaFormateada, fechaLentgh));
        sb.append(fixed(horaStr, horaLentgh));
        sb.append(fixed(tipo, tipoLentgh));
        sb.append(fixed(montoStr, montoLentgh));
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
