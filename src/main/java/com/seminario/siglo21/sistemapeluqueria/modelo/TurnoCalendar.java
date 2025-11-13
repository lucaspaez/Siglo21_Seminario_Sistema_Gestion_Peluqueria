package com.seminario.siglo21.sistemapeluqueria.modelo;

import java.time.LocalDate;
import java.time.LocalTime;


/**
 * Modelo de Vista (DTO) utilizado específicamente para el calendario.
 * Consolida información de Turno, Cliente y Duración de Servicios.
 */
public class TurnoCalendar {

    private int idTurno;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado; // Ejemplo: PENDIENTE, CONFIRMADO, REALIZADO, CANCELADO

    // Datos consolidados del Cliente
    private String nombreCliente;
    private String apellidoCliente;

    // Datos críticos para la UI del calendario
    private int duracionTotalMinutos; // Para calcular la altura del bloque en el GridPane
    private String serviciosDescripcion; // Lista de servicios separados por coma (ej: "Corte, Tintura")

    // --- CONSTRUCTOR ---

    // --- CONSTRUCTOR PARA EL DAO ---
    public TurnoCalendar(int idTurno, LocalDate fecha, LocalTime hora, String estado,
                         String nombreCliente, String apellidoCliente,
                         int duracionTotalMinutos, String serviciosDescripcion) {
        this.idTurno = idTurno;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.duracionTotalMinutos = duracionTotalMinutos;
        this.serviciosDescripcion = serviciosDescripcion;
    }

    // Constructor vacío (útil para mapeo JDBC)
    public TurnoCalendar() {}

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

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public int getDuracionTotalMinutos() {
        return duracionTotalMinutos;
    }

    public void setDuracionTotalMinutos(int duracionTotalMinutos) {
        this.duracionTotalMinutos = duracionTotalMinutos;
    }

    public String getServiciosDescripcion() {
        return serviciosDescripcion;
    }

    public void setServiciosDescripcion(String serviciosDescripcion) {
        this.serviciosDescripcion = serviciosDescripcion;
    }

    // --- Método para la vista ---
    public String getNombreCompletoCliente() {
        return this.nombreCliente + " " + this.apellidoCliente;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%d min)",
                hora.toString(),
                getNombreCompletoCliente(),
                duracionTotalMinutos);
    }
}