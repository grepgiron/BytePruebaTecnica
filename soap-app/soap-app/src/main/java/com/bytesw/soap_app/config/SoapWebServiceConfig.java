package com.bytesw.soap_app.config;

import java.util.Map;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class SoapWebServiceConfig {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) 
    {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "movimientos")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema movimientoSchema) 
    {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("MovimientoPort");
        definition.setTargetNamespace("http://bytesw.com/soap/movimientos");
        definition.setLocationUri("/ws");
        definition.setSchema(movimientoSchema);
        return definition;
    }

    @Bean
    public XsdSchema movimientoSchema() 
    {
        return new SimpleXsdSchema(new ClassPathResource("schemas/movimiento.xsd"));
    }

    // --- Cliente ---
    @Bean(name = "clientes")
    public DefaultWsdl11Definition clienteWsdl(XsdSchema clienteSchema) 
    {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("ClientePort");
        definition.setTargetNamespace("http://bytesw.com/soap/clientes");
        definition.setLocationUri("/ws");
        definition.setSchema(clienteSchema);
        definition.setRequestSuffix("Request");
        definition.setResponseSuffix("Response");
        return definition;
    }

    @Bean
    public XsdSchema clienteSchema() 
    {
        return new SimpleXsdSchema(new ClassPathResource("schemas/cliente.xsd"));
    }

    // --- Cuenta ---
    @Bean(name = "cuentas")
    public DefaultWsdl11Definition cuentaWsdl(XsdSchema cuentaSchema) 
    {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("CuentaPort");
        definition.setTargetNamespace("http://bytesw.com/soap/cuentas");
        definition.setLocationUri("/ws");
        definition.setSchema(cuentaSchema);
        return definition;
    }

    @Bean
    public XsdSchema cuentaSchema() 
    {
        return new SimpleXsdSchema(new ClassPathResource("schemas/cuenta.xsd"));
    }

    @Bean
    public Jaxb2Marshaller marshaller() 
    {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.bytesw.soap_app.endpoint");
        marshaller.setMarshallerProperties(Map.of(
            jakarta.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE
        ));
        return marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller) 
    {
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);
        return template;
    }
}
