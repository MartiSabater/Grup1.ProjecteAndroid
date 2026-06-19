package com.example.sigmadsa.models;

public class Group {
    private String id;
    private String nombre;
    private String descripcion;
    private int miembros;

    public Group() {
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

    public int getMiembros() {
        return miembros;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setMiembros(int miembros) {
        this.miembros = miembros;
    }
}
