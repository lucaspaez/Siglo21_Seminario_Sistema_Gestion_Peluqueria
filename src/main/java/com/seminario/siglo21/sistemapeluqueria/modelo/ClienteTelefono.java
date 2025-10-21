package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;

public class ClienteTelefono {

    private int idCliente;
    private int idTelefono;

    public ClienteTelefono() {
    }

    public ClienteTelefono(int idCliente, int idTelefono) {
        this.idCliente = idCliente;
        this.idTelefono = idTelefono;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(int idTelefono) {
        this.idTelefono = idTelefono;
    }

    public void conectarClienteTelefono() {

        String consultaSql = "INSERT INTO ClienteTelefono "
                + "VALUES ( ?, ?);";

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            // Preparamos con la consulta
            PreparedStatement statement = conexion.prepareStatement(consultaSql);

            // Asigno los valores del objeto Email
            statement.setInt(1, this.getIdCliente());
            statement.setInt(2, this.getIdTelefono());

            // Ejecuto la consulta
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setTitle("Error");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }

    }

}
