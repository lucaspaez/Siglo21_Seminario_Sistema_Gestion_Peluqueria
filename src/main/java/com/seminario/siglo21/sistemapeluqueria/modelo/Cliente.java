package com.seminario.siglo21.sistemapeluqueria.modelo;

public class Cliente extends Persona{
    
    // Atributos exclusivos de la clase Cliente
    private int idCliente;
    
    // Métodos Constructores
    public Cliente() {
    }

    public Cliente(int idCliente, String nombre, String apellido, int dni, int idDireccion, int idTelefono, int idEmail, boolean activo) {
        super(nombre, apellido, dni, idDireccion, idTelefono, idEmail, activo);
        this.idCliente = idCliente;
    }

    // Métodos Getters y Setters
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    
    
}
