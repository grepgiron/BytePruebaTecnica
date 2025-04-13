package com.bytesw.rest_app.utils;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ParserResponse {
    public Map<String, String> parseSoapResponse(String xmlResponse) {
        Map<String, String> result = new HashMap<>();

        try {
            // 1. Parsear el XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true); // para manejar namespaces como ns3
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlResponse)));

            // 2. Buscar el nodo <trama>
            NodeList list = doc.getElementsByTagNameNS("*", "trama");
            if (list.getLength() > 0) {
                String trama = list.item(0).getTextContent().trim();

                // 3. Separar trama en código y mensaje
                String codigo = trama.length() >= 3 ? trama.substring(0, 3) : "999";
                String mensaje = trama.length() >= 103
                    ? trama.substring(3, 103).trim()
                    : trama.length() > 3 ? trama.substring(3).trim() : "Respuesta incompleta";

                String referencia = trama.length() >= 119
                    ? trama.substring(103, 119).trim()
                    : "";

                result.put("codigo", codigo);
                result.put("mensaje", mensaje);
                result.put("referencia", referencia);
            } else {
                result.put("codigo", "999");
                result.put("mensaje", "Respuesta SOAP no contiene trama");
                result.put("referencia", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("codigo", "999");
            result.put("mensaje", "Error al procesar respuesta SOAP: " + e.getMessage());
            result.put("referencia", "");
        }

        return result;
    }
}
