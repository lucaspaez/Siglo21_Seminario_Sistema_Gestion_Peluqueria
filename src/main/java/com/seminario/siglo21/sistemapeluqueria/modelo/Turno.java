package com.seminario.siglo21.sistemapeluqueria.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Entidad de la tabla Turno.
 * Utilizada para las operaciones CRUD directas con la base de datos.
 */
public class Turno {

    private int idTurno;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;

    // Claves Foráneas (FK)
    private int idCliente;
    private int idEmpleado;
    private String observaciones;

    private List<Integer> idServiciosAsociados;

    // --- CONSTRUCTOR ---

    public Turno(int idTurno, LocalDate fecha, LocalTime hora, String estado, int idCliente, int idEmpleado, String observaciones) {
        this.idTurno = idTurno;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.observaciones = observaciones;
    }

    // Constructor sin id (para la creación de nuevos turnos)
    public Turno(LocalDate fecha, LocalTime hora, String estado, int idCliente, int idEmpleado, String observaciones) {
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.observaciones = observaciones;
    }

    // Constructor vacío
    public Turno() {}

    // --- GETTERS Y SETTERS ---

    public int getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<Integer> getIdServiciosAsociados() {
        return idServiciosAsociados;
    }

    public void setIdServiciosAsociados(List<Integer> idServiciosAsociados) {
        this.idServiciosAsociados = idServiciosAsociados;
    }
}
