package com.seminario.siglo21.sistemapeluqueria.persistencia;

import com.seminario.siglo21.sistemapeluqueria.modelo.TurnoCalendar;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de Acceso a Datos (DAO) para la entidad Turno.
 * Utiliza ConexionBD para la gestión de la base de datos.
 */
public class TurnoDAO {

    // (La consulta SQL se mantiene igual, ya que usaba alias en español)
    private static final String SQL_SELECT_RANGE =
            "SELECT " +
                    "t.idTurno, t.fecha, t.hora AS hora_inicio, t.estado AS estado_turno, " +
                    "c.nombre AS nombre_cliente, c.apellido AS apellido_cliente, " +
                    "GROUP_CONCAT(si.nombreServicio SEPARATOR ', ') AS servicios_descripcion, " +
                    "SUM(tsi.cantidad * si.duracionHoras * 60) AS duracion_total_minutos " +
                    "FROM Turno t " +
                    "JOIN Cliente c ON t.idCliente = c.idCliente " +
                    "JOIN TurnoServicioInterno tsi ON t.idTurno = tsi.idTurno " +
                    "JOIN ServicioInterno si ON tsi.idServicioInterno = si.idServicioInterno " +
                    "WHERE t.fecha BETWEEN ? AND ? " +
                    "GROUP BY t.idTurno, c.nombre, c.apellido, t.fecha, t.hora, t.estado " +
                    "ORDER BY t.hora";

    /**
     * Obtiene una lista de turnos mapeados a TurnoCalendar dentro del rango de fechas especificado.
     * CAMBIO: Parámetros traducidos.
     * @param fechaInicio La fecha de inicio del rango (inclusivo).
     * @param fechaFin La fecha de fin del rango (inclusivo).
     * @return Lista de objetos TurnoCalendar.
     */
    public List<TurnoCalendar> obtenerTurnosPorRango(LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {

        List<TurnoCalendar> turnos = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_RANGE)) {

            // CAMBIO: Parámetros traducidos
            stmt.setDate(1, Date.valueOf(fechaInicio));
            stmt.setDate(2, Date.valueOf(fechaFin));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    LocalTime horaInicio = rs.getTime("hora_inicio").toLocalTime();
                    int duracionTotalMinutos = rs.getInt("duracion_total_minutos");
                    LocalTime horaFin = horaInicio.plusMinutes(duracionTotalMinutos);

                    TurnoCalendar turno = new TurnoCalendar(
                            rs.getInt("idTurno"),
                            rs.getDate("fecha").toLocalDate(),
                            horaInicio,
                            rs.getString("estado_turno"),
                            rs.getString("nombre_cliente"),
                            rs.getString("apellido_cliente"),
                            duracionTotalMinutos,
                            rs.getString("servicios_descripcion")
                    );
                    turnos.add(turno);
                }
            }
        }
        return turnos;
    }

    /**
     * Método para actualizar la fecha y hora de un turno (usado por Drag and Drop).
     * CAMBIO: Parámetros traducidos.
     */
    public void moverTurno(int idTurno, LocalDate nuevaFecha, LocalTime nuevaHora) throws SQLException {

        final String SQL = "UPDATE Turno SET fecha = ?, hora = ? WHERE idTurno = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            // CAMBIO: Parámetros traducidos
            stmt.setDate(1, Date.valueOf(nuevaFecha));
            stmt.setTime(2, Time.valueOf(nuevaHora));
            stmt.setInt(3, idTurno);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al mover el turno, ID no encontrado: " + idTurno);
            }
        }
    }
}