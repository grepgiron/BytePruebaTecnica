package com.bytesw.rest_app.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;

    private String nombre;
    private String identificacion;
    private String tipoIdentificacion;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public void setNombre(String nombre) 
    {
        this.nombre = nombre;
    }

    public void setIdentificacion(String identificacion) 
    {
        this.identificacion = identificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) 
    {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) 
    {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Long getId() 
    {
        return id;
    }

    public String getNombre() 
    {
        return nombre;
    }

    public String getIdentificacion() 
    {
        return identificacion;
    }

    public String getTipoIdentificacion() 
    {
        return tipoIdentificacion;
    }

    public LocalDate getFechaNacimiento() 
    {
        return fechaNacimiento;
    }

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    
}
