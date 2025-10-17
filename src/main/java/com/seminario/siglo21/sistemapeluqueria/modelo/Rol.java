package com.seminario.siglo21.sistemapeluqueria.modelo;

public class Rol {
    
    // Atributos
    private int idRol;
    private String Rol;
    private String descripcionRol;

    // Constructores
    public Rol() {
    }

    public Rol(int idRol, String Rol, String descripcionRol) {
        this.idRol = idRol;
        this.Rol = Rol;
        this.descripcionRol = descripcionRol;
    }

    // Getters y Setters
    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getRol() {
        return Rol;
    }

    public void setRol(String Rol) {
        this.Rol = Rol;
    }

    public String getDescripcionRol() {
        return descripcionRol;
    }

    public void setDescripcionRol(String descripcionRol) {
        this.descripcionRol = descripcionRol;
    }
    
    
}
