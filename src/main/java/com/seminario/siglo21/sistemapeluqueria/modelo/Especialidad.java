package com.seminario.siglo21.sistemapeluqueria.modelo;

public class Especialidad {
    
    // Atributos
    private int idEspecialidad;
    private String especialidad;
    private String descripcionEspecialidad;

    // Constructores
    public Especialidad() {
    }

    public Especialidad(int idEspecialidad, String especialidad, String descripcionEspecialidad) {
        this.idEspecialidad = idEspecialidad;
        this.especialidad = especialidad;
        this.descripcionEspecialidad = descripcionEspecialidad;
    }

    // Getters y Setters
    public int getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(int idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getDescripcionEspecialidad() {
        return descripcionEspecialidad;
    }

    public void setDescripcionEspecialidad(String descripcionEspecialidad) {
        this.descripcionEspecialidad = descripcionEspecialidad;
    }
    
    
}
