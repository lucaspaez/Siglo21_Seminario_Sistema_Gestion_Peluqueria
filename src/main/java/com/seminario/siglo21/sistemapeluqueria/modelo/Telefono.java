package com.seminario.siglo21.sistemapeluqueria.modelo;

public class Telefono {
    
    // Atributos
    private int idTelefono;
    private int telefono;

    // Constriuctores
    public Telefono() {
    }

    public Telefono(int idTelefono, int telefono) {
        this.idTelefono = idTelefono;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(int idTelefono) {
        this.idTelefono = idTelefono;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
    
    
}
