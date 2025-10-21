package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.scene.control.Alert;

public class ClienteEmail {

    private int idCliente;
    private int idEmail;

    public ClienteEmail(int idCliente, int idEmail) {
        this.idCliente = idCliente;
        this.idEmail = idEmail;
    }

    public ClienteEmail() {
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEmail() {
        return idEmail;
    }

    public void setIdEmail(int idEmail) {
        this.idEmail = idEmail;
    }

    public void conectaClienteEmail() {

        String consultaSql = "INSERT INTO ClienteEmail "
                + "VALUES ( ?, ?);";

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            // Preparamos con la consulta
            PreparedStatement statement = conexion.prepareStatement(consultaSql);

            // Asigno los valores del objeto Email
            statement.setInt(1, this.getIdCliente());
            statement.setInt(2, this.getIdEmail());

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
