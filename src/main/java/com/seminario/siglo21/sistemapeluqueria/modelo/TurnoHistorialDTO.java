// Archivo: TurnoHistorialDTO.java
package com.seminario.siglo21.sistemapeluqueria.modelo;

import java.time.LocalDate;

// DTO (Data Transfer Object) para la TableView del historial
public class TurnoHistorialDTO {
    private LocalDate fecha;
    private String estilista;
    private String serviciosRealizados;
    private String estado;
    private String observaciones;

    // Constructor
    public TurnoHistorialDTO(LocalDate fecha, String estilista, String serviciosRealizados, String estado, String observaciones) {
        this.fecha = fecha;
        this.estilista = estilista;
        this.serviciosRealizados = serviciosRealizados;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    // Getters (necesarios para la TableView)
    public LocalDate getFecha() { return fecha; }
    public String getEstilista() { return estilista; }
    public String getServiciosRealizados() { return serviciosRealizados; }
    public String getEstado() { return estado; }
    public String getObservaciones() { return observaciones; }
}