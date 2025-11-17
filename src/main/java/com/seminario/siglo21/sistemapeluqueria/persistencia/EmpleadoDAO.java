package com.seminario.siglo21.sistemapeluqueria.persistencia;

import com.seminario.siglo21.sistemapeluqueria.modelo.Empleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    /**
     * Obtiene TODOS los empleados (activos e inactivos).
     * Usado por la pantalla de gestión.
     */
    public List<Empleado> obtenerTodos() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT idEmpleado, nombre, apellido, dni, activo FROM Empleado";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }
        }
        return empleados;
    }

    /**
     * Obtiene solo empleados ACTIVOS.
     * Usado para ComboBoxes (como en el formulario de turnos).
     */
    public List<Empleado> obtenerActivos() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT idEmpleado, nombre, apellido, dni, activo FROM Empleado WHERE activo = TRUE";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }
        }
        return empleados;
    }

    // Crea nuevo empleado
    public Empleado crear(Empleado empleado) throws SQLException {
        String sql = "INSERT INTO Empleado (nombre, apellido, dni, activo) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellido());
            stmt.setInt(3, empleado.getDni());
            stmt.setBoolean(4, empleado.isActivo());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Fallo al crear el empleado, 0 filas afectadas.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    empleado.setIdEmpleado(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Fallo al crear el empleado, no se obtuvo ID.");
                }
            }
        }
        return empleado;
    }

    // Avtualiza Empleado
    public boolean actualizar(Empleado empleado) throws SQLException {
        String sql = "UPDATE Empleado SET nombre = ?, apellido = ?, dni = ?, activo = ? WHERE idEmpleado = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellido());
            stmt.setInt(3, empleado.getDni());
            stmt.setBoolean(4, empleado.isActivo());
            stmt.setInt(5, empleado.getIdEmpleado());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Inactiva un empleado (Soft Delete).
     * No voy a usar el Stored Procedure por simplicidad,
     * pero llamaria con "CALL EliminarOInactivarEmpleado(?)".
     */
    public boolean inactivar(int idEmpleado) throws SQLException {
        // En lugar de llamar al SP, hacemos un UPDATE directo por simplicidad del CRUD
        // Si tienes un SP "EliminarOInactivarEmpleado", es mejor llamarlo.
        String sql = "UPDATE Empleado SET activo = FALSE WHERE idEmpleado = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEmpleado);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Metodo auxiliar para mapear un empleado
    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        return new Empleado(
                rs.getInt("idEmpleado"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getInt("dni"),
                rs.getBoolean("activo")
        );
    }
}