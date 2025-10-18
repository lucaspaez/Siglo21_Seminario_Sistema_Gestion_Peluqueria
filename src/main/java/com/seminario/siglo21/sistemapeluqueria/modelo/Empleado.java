package com.seminario.siglo21.sistemapeluqueria.modelo;

public class Empleado extends Persona{
    
    // Atributos Exclusivos de la clase Empleado
    private int idEmpleado;
    private String especialidad;

    // Métodos Constructores
    public Empleado() {
    }

    public Empleado(int idEmpleado, String especialidad, String nombre, String apellido, int dni, String direccion, int telefono, String email, boolean activo) {
        super(nombre, apellido, dni, direccion, telefono, email, activo);
        this.idEmpleado = idEmpleado;
        this.especialidad = especialidad;
    }

    // Métodos Getters y Setters
    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    
}
