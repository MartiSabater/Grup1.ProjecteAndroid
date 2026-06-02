package com.example.sigmadsa.api;

public class EventResponse {
    private String id;
    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String imagen;

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public String getImagen() {
        return imagen;
    }
}
