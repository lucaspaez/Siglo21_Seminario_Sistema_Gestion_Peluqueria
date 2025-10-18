package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import com.seminario.siglo21.sistemapeluqueria.util.HashUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;

public class Cuenta {

    // Atributos de la clases Cuenta
    private int idCuenta;
    private int idEmpleado;
    private String usuario;
    private String password_hash;
    private String fechaUltimoAcceso;
    private int idRol;

    // Métodos constructores
    public Cuenta() {
    }

    public Cuenta(int idCuenta, int idEmpleado, String usuario, String password_hash, String fechaUltimoAcceso, int idRol) {
        this.idCuenta = idCuenta;
        this.idEmpleado = idEmpleado;
        this.usuario = usuario;
        this.password_hash = password_hash;
        this.fechaUltimoAcceso = fechaUltimoAcceso;
        this.idRol = idRol;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getFechaUltimoAcceso() {
        return fechaUltimoAcceso;
    }

    public void setFechaUltimoAcceso(String fechaUltimoAcceso) {
        this.fechaUltimoAcceso = fechaUltimoAcceso;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public static boolean validarCredenciales(String usuario, String contraseniaPlana) throws SQLException {

        // Inicializo variables de conexion
        Connection conexion = ConexionBD.getConexion();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String consultaSQL = "SELECT password_hash FROM Cuenta"
                + " WHERE usuario = '" + usuario + "';";

        try {
            // Ejecuto la consulta
            statement = conexion.prepareStatement(consultaSQL);
            resultSet = statement.executeQuery();

            // Verifico que sean iguales y retorno true si lo son
            if (resultSet.next()) {
                String storedHash = resultSet.getString("password_hash");
                String inputHash = HashUtil.sha256(contraseniaPlana);
                return storedHash.equalsIgnoreCase(inputHash);
            }

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setTitle("Error");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }

        return false;
    }

}
