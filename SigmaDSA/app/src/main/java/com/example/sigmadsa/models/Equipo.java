package com.example.sigmadsa.models;

public class Equipo {
    private String id;
    private String nombre;
    private String descripcion;

    public Equipo(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
