package com.example.stconnect.data.model;

public class User {
    private String uid;
    private String nombre;
    private String email;
    private String rut;
    private String carrera;

    public User() {
        // Constructor vacío requerido para Firebase
    }

    public User(String uid, String nombre, String email, String rut, String carrera) {
        this.uid = uid;
        this.nombre = nombre;
        this.email = email;
        this.rut = rut;
        this.carrera = carrera;
    }

    // Getters y Setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
}

