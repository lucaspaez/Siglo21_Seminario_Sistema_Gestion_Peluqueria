package com.seminario.siglo21.sistemapeluqueria.persistencia;

import com.seminario.siglo21.sistemapeluqueria.modelo.Turno;
import com.seminario.siglo21.sistemapeluqueria.modelo.TurnoCalendar;
import com.seminario.siglo21.sistemapeluqueria.modelo.TurnoHistorialDTO;

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
                    "WHERE t.fecha BETWEEN ? AND ? AND t.estado IN ('PENDIENTE', 'CONFIRMADO', 'REALIZADO', 'CANCELADO') " +
                    "GROUP BY t.idTurno, c.nombre, c.apellido, t.fecha, t.hora, t.estado " +
                    "ORDER BY t.hora";

    // Constantes SQL
    private static final String SQL_INSERT_TURNO =
            "INSERT INTO Turno (fecha, hora, estado, idCliente, idEmpleado, observaciones) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_SERVICIO_TURNO =
            "INSERT INTO TurnoServicioInterno (idTurno, idServicioInterno, cantidad) VALUES (?, ?, 1)"; // Cantidad siempre 1 por defecto

    private static final String SQL_SELECT_BY_ID =
            "SELECT idTurno, fecha, hora, estado, idCliente, idEmpleado, observaciones FROM Turno WHERE idTurno = ?";
    private static final String SQL_SELECT_SERVICIOS_IDS =
            "SELECT idServicioInterno FROM TurnoServicioInterno WHERE idTurno = ?";
    // Nuevo SQL para la actualización del turno principal
    private static final String SQL_UPDATE_TURNO =
            "UPDATE Turno SET fecha = ?, hora = ?, estado = ?, idCliente = ?, idEmpleado = ?, observaciones = ? WHERE idTurno = ?";
    // SQL para eliminar servicios antiguos antes de insertar los nuevos
    private static final String SQL_DELETE_SERVICIO_TURNO =
            "DELETE FROM TurnoServicioInterno WHERE idTurno = ?";

    /**
     * Obtiene una lista de turnos mapeados a TurnoCalendar dentro del rango de fechas especificado.
     * CAMBIO: Parámetros traducidos.
     * @param fechaInicio La fecha de inicio del rango (inclusivo).
     * @param fechaFin La fecha de fin del rango (inclusivo).
     * @return Lista de objetos TurnoCalendar.
     */
    public List<TurnoCalendar> obtenerTurnosPorRango(LocalDate fechaInicio,
                                                     LocalDate fechaFin,
                                                     boolean includeCompleted,
                                                     boolean includeCancelled,
                                                     boolean includeConfirmed) throws SQLException {

        List<TurnoCalendar> turnos = new ArrayList<>();

        // La consulta base se mantiene igual (se omite para brevedad)
        String baseSql =
                "SELECT " +
                        "t.idTurno, t.fecha, t.hora AS hora_inicio, t.estado AS estado_turno, " +
                        "c.nombre AS nombre_cliente, c.apellido AS apellido_cliente, " +
                        "GROUP_CONCAT(si.nombreServicio SEPARATOR ', ') AS servicios_descripcion, " +
                        "SUM(tsi.cantidad * si.duracionHoras * 60) AS duracion_total_minutos " +
                        "FROM Turno t " +
                        "JOIN Cliente c ON t.idCliente = c.idCliente " +
                        "JOIN TurnoServicioInterno tsi ON t.idTurno = tsi.idTurno " +
                        "JOIN ServicioInterno si ON tsi.idServicioInterno = si.idServicioInterno ";


        List<String> estados = new ArrayList<>();

        // 1. Estados que siempre se incluyen por defecto (Pendientes y Confirmados)
        // Asumiendo que estos son los estados operativos que siempre deben mostrarse
        estados.add("'PENDIENTE'");
        estados.add("'CONFIRMADO'");

        // 2. Filtro para REALIZADOS (Debe ser un IF INDEPENDIENTE)
        if (includeCompleted) {
            estados.add("'REALIZADO'");
        }

        // 3. Filtro para CANCELADOS (Debe ser un IF INDEPENDIENTE)
        if (includeCancelled) {
            // Asegúrate de que este String coincida EXACTAMENTE con el estado en la base de datos
            estados.add("'CANCELADO'");
        }

        // 4. NUEVO FILTRO PARA CONFIRMADOS
        if (includeConfirmed) { // <-- Usando el nuevo parámetro
            estados.add("'CONFIRMADO'");
        }

        // 5. Construir la cláusula WHERE
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("WHERE t.fecha BETWEEN ? AND ? ");

        // Añadir la condición de estado
        if (!estados.isEmpty()) {
            // Esto genera la sub-cláusula: AND t.estado IN ('PENDIENTE', 'CONFIRMADO', ...)
            whereClause.append("AND UPPER(t.estado) IN (").append(String.join(",", estados)).append(") ");
        } else {
            // Esto solo debería ocurrir si no hay estados por defecto, lo cual es inusual.
            // Si esto ocurre, podría ser un error, pero lo dejamos como seguro.
            System.err.println("Advertencia: No hay estados para filtrar turnos.");
            return turnos; // Devolver lista vacía
        }

        // Cierre de la consulta
        String finalSql = baseSql + whereClause.toString() +
                "GROUP BY t.idTurno, t.fecha, t.hora, t.estado, c.nombre, c.apellido " +
                "ORDER BY t.fecha, t.hora";

        // ⭐️ ¡DEBUG CRÍTICO! IMPRIME LA CONSULTA ANTES DE EJECUTARLA
        System.out.println("DEBUG SQL FINAL: " + finalSql);

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(finalSql)) {

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
                    System.out.println("ESTADO DEL TURNO DESDE LA CONSULTA: "+turno.getEstado());
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

    /**
     * Inserta un nuevo Turno y asocia los servicios seleccionados dentro de una transacción.
     * * @param turno Objeto Turno con fecha, hora, idCliente, idEmpleado.
     * @param idServicios Lista de IDs de los servicios internos a asociar al turno.
     * @return true si la operación fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    public boolean agendarTurno(Turno turno, List<Integer> idServicios) throws SQLException {

        // 1. Validaciones básicas
        if (turno.getIdCliente() == 0 || turno.getIdEmpleado() == 0 || idServicios == null || idServicios.isEmpty()) {
            // En un entorno real, lanzaríamos una excepción más específica o registraríamos el error.
            System.err.println("Datos incompletos para agendar el turno.");
            System.err.println("ID Cliente: "  + turno.getIdCliente());
            System.err.println("ID Empleado: "   + turno.getIdEmpleado());
            System.err.println("IDs Servicios: "  + idServicios);
            return false;
        }

        // Usamos Connection fuera del try-with-resources principal para manejar el commit/rollback
        Connection conn = null;
        try {
            conn = ConexionBD.getConexion();

            // ⭐️ INICIO DE LA TRANSACCIÓN
            conn.setAutoCommit(false);

            // -----------------------------------------------------
            // PASO 1: Insertar el Turno principal y obtener su ID
            // -----------------------------------------------------

            // Usamos Statement.RETURN_GENERATED_KEYS para obtener el ID generado (idTurno)
            try (PreparedStatement stmtTurno = conn.prepareStatement(SQL_INSERT_TURNO, Statement.RETURN_GENERATED_KEYS)) {

                stmtTurno.setDate(1, Date.valueOf(turno.getFecha()));
                stmtTurno.setTime(2, Time.valueOf(turno.getHora()));
                stmtTurno.setString(3, turno.getEstado());
                stmtTurno.setInt(4, turno.getIdCliente());
                stmtTurno.setInt(5, turno.getIdEmpleado());
                stmtTurno.setString(6, turno.getObservaciones());

                int affectedRows = stmtTurno.executeUpdate();

                if (affectedRows == 0) {
                    // Si la inserción del turno falla, salimos y hacemos rollback
                    conn.rollback();
                    throw new SQLException("Fallo al insertar el turno, 0 filas afectadas.");
                }

                // Obtener el ID generado por la base de datos
                int idTurnoGenerado = -1;
                try (ResultSet rs = stmtTurno.getGeneratedKeys()) {
                    if (rs.next()) {
                        idTurnoGenerado = rs.getInt(1); // La primera columna es el ID
                        turno.setIdTurno(idTurnoGenerado); // Actualizamos el objeto Turno
                    } else {
                        conn.rollback();
                        throw new SQLException("Fallo al obtener el ID generado del turno.");
                    }
                }

                // -----------------------------------------------------
                // PASO 2: Insertar la asociación en TurnoServicioInterno
                // -----------------------------------------------------

                try (PreparedStatement stmtServicio = conn.prepareStatement(SQL_INSERT_SERVICIO_TURNO)) {

                    // Usamos un batch para enviar múltiples inserciones a la vez
                    for (int idServicio : idServicios) {
                        stmtServicio.setInt(1, idTurnoGenerado);
                        stmtServicio.setInt(2, idServicio);
                        stmtServicio.addBatch(); // Agrega la sentencia al lote
                    }

                    int[] batchResults = stmtServicio.executeBatch();

                    // Opcional: Verificar que todas las inserciones del batch hayan tenido éxito
                    for (int result : batchResults) {
                        if (result == Statement.EXECUTE_FAILED) {
                            conn.rollback();
                            throw new SQLException("Fallo en la inserción de un servicio asociado.");
                        }
                    }
                }
            }

            // ⭐️ FIN DE LA TRANSACCIÓN
            conn.commit(); // Si todo fue bien, hacemos el commit.
            return true;

        } catch (SQLException e) {
            // En caso de error, hacemos un rollback
            if (conn != null) {
                try {
                    System.err.println("Rollback de la transacción debido a un error: " + e.getMessage());
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error en el rollback: " + rollbackEx.getMessage());
                }
            }
            // Propagamos la excepción al controlador para que pueda informar al usuario
            throw e;

        } finally {
            // Siempre restaurar el autoCommit al estado por defecto (true)
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error al restaurar AutoCommit: " + e.getMessage());
                }
            }
        }
    }

    public Turno getTurnoById(int idTurno) throws SQLException {
        Turno turno = null;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            stmt.setInt(1, idTurno);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // 1. Mapear los datos básicos del turno
                    turno = new Turno(
                            rs.getInt("idTurno"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getTime("hora").toLocalTime(),
                            rs.getString("estado"),
                            rs.getInt("idCliente"),
                            rs.getInt("idEmpleado"),
                            rs.getString("observaciones")
                    );

                    // 2. Obtener los IDs de servicios asociados
                    List<Integer> idServicios = getServiciosIdByTurnoId(conn, idTurno);
                    turno.setIdServiciosAsociados(idServicios); // Asume que agregamos este setter/campo al modelo Turno
                }
            }
        }
        return turno;
    }

    /**
     * Método auxiliar para obtener la lista de IDs de servicios asociados a un turno.
     */
    private List<Integer> getServiciosIdByTurnoId(Connection conn, int idTurno) throws SQLException {
        List<Integer> idServicios = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_SERVICIOS_IDS)) {
            stmt.setInt(1, idTurno);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    idServicios.add(rs.getInt("idServicioInterno"));
                }
            }
        }
        return idServicios;
    }

    public boolean actualizarTurno(Turno turno, List<Integer> idServiciosActualizados) throws SQLException {

        // 1. Validaciones
        if (turno.getIdTurno() == 0 || turno.getIdCliente() == 0 || turno.getIdEmpleado() == 0 || idServiciosActualizados == null) {
            System.err.println("Datos incompletos para actualizar el turno.");
            return false;
        }

        Connection conn = null;
        try {
            conn = ConexionBD.getConexion();

            // ⭐️ INICIO DE LA TRANSACCIÓN
            conn.setAutoCommit(false);

            // -----------------------------------------------------
            // PASO 1: Actualizar los datos principales del Turno
            // -----------------------------------------------------
            try (PreparedStatement stmtTurno = conn.prepareStatement(SQL_UPDATE_TURNO)) {

                stmtTurno.setDate(1, Date.valueOf(turno.getFecha()));
                stmtTurno.setTime(2, Time.valueOf(turno.getHora()));
                stmtTurno.setString(3, turno.getEstado()); // Permitimos actualizar el estado (ej: a REALIZADO)
                stmtTurno.setInt(4, turno.getIdCliente());
                stmtTurno.setInt(5, turno.getIdEmpleado());
                stmtTurno.setString(6, turno.getObservaciones());
                stmtTurno.setInt(7, turno.getIdTurno()); // WHERE clause

                int affectedRows = stmtTurno.executeUpdate();

                if (affectedRows == 0) {
                    conn.rollback();
                    throw new SQLException("Fallo al actualizar el turno, ID no encontrado: " + turno.getIdTurno());
                }
            }

            // -----------------------------------------------------
            // PASO 2: Eliminar las asociaciones de servicios antiguas
            // -----------------------------------------------------
            try (PreparedStatement stmtDelete = conn.prepareStatement(SQL_DELETE_SERVICIO_TURNO)) {
                stmtDelete.setInt(1, turno.getIdTurno());
                stmtDelete.executeUpdate(); // No importa si elimina 0 o N filas
            }

            // -----------------------------------------------------
            // PASO 3: Insertar las nuevas asociaciones de servicios
            // -----------------------------------------------------
            if (!idServiciosActualizados.isEmpty()) {
                try (PreparedStatement stmtInsert = conn.prepareStatement(SQL_INSERT_SERVICIO_TURNO)) {

                    for (int idServicio : idServiciosActualizados) {
                        stmtInsert.setInt(1, turno.getIdTurno());
                        stmtInsert.setInt(2, idServicio);
                        stmtInsert.addBatch();
                    }

                    stmtInsert.executeBatch();
                }
            }

            // ⭐️ FIN DE LA TRANSACCIÓN
            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    System.err.println("Rollback de la transacción de actualización.");
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error en el rollback: " + rollbackEx.getMessage());
                }
            }
            throw e; // Propagar la excepción

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error al restaurar AutoCommit: " + e.getMessage());
                }
            }
        }
    }

    public List<TurnoHistorialDTO> getHistorialPorCliente(int idCliente) throws SQLException {
        List<TurnoHistorialDTO> historial = new ArrayList<>();
        String sql = "SELECT fecha, estilista, serviciosRealizados, estado, observaciones " +
                "FROM vista_historial_cliente WHERE idCliente = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    historial.add(new TurnoHistorialDTO(
                            rs.getDate("fecha").toLocalDate(),
                            rs.getString("estilista"),
                            rs.getString("serviciosRealizados"),
                            rs.getString("estado"),
                            rs.getString("observaciones")
                    ));
                }
            }
        }
        return historial;
    }
}