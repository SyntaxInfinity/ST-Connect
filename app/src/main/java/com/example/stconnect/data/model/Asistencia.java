package com.example.stconnect.data.model;

public class Asistencia {
    private String nombre;
    private String rut;
    private String estado;

    public Asistencia(String nombre, String rut, String estado) {
        this.nombre = nombre;
        this.rut = rut;
        this.estado = estado;
    }

    public String getNombre() { return nombre; }
    public String getRut() { return rut; }
    public String getEstado() { return estado; }
}
