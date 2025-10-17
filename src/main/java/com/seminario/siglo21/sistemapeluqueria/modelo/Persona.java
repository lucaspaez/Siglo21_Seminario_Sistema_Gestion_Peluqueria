package com.seminario.siglo21.sistemapeluqueria.modelo;

public class Persona {
    
    // Atributos de la clase Persona
    private String nombre;
    private String apellido;
    private int dni;
    private int idDireccion;
    private int idTelefono;
    private int idEmail;
    private boolean activo;
    
    // Métodos Constructores
    public Persona() {
    }

    public Persona(String nombre, String apellido, int dni, int idDireccion, int idTelefono, int idEmail, boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.idDireccion = idDireccion;
        this.idTelefono = idTelefono;
        this.idEmail = idEmail;
        this.activo = activo;
    }

    // Métodos Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public int getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

    public int getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(int idTelefono) {
        this.idTelefono = idTelefono;
    }

    public int getIdEmail() {
        return idEmail;
    }

    public void setIdEmail(int idEmail) {
        this.idEmail = idEmail;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
}
