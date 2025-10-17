package com.seminario.siglo21.sistemapeluqueria.modelo;

public class Empleado extends Persona{
    
    // Atributos Exclusivos de la clase Empleado
    private int idEmpleado;
    private int idEspecialidad;

    // Métodos Constructores
    public Empleado() {
    }

    public Empleado(int idEmpleado, int idEspecialidad, String nombre, String apellido, int dni, int idDireccion, int idTelefono, int idEmail, boolean activo) {
        super(nombre, apellido, dni, idDireccion, idTelefono, idEmail, activo);
        this.idEmpleado = idEmpleado;
        this.idEspecialidad = idEspecialidad;
    }

    // Métodos Getters y Setters
    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(int idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }
    
}
