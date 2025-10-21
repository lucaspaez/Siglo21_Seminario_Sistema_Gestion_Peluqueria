package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;

public class Telefono {

    // Atributos
    private int idTelefono;
    private int telefono;

    // Constriuctores
    public Telefono() {
    }

    public Telefono(int idTelefono, int telefono) {
        this.idTelefono = idTelefono;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(int idTelefono) {
        this.idTelefono = idTelefono;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public void GuardarTelefonoNuevo() {
        String consultaSql = "INSERT INTO Telefono (telefono) VALUES ( ?);";

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            // Preparamos con la consulta e indicamos que queremos el id generado
            PreparedStatement statement = conexion.prepareStatement(consultaSql,
                    PreparedStatement.RETURN_GENERATED_KEYS);

            // Asigno los valores del objeto Telefono
            statement.setInt(1, this.getTelefono());

            int filas = statement.executeUpdate();

            if (filas > 0) {
                // Obtengo el ID Generado por la Base de Datos
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {

                        this.idTelefono = rs.getInt("idTelefono");

                    }
                }
            }

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setTitle("Error");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }

}
