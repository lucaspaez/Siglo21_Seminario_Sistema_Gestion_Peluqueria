package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
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
            VistaUtil.mostrarAlerta("error", e.getMessage());
        }

    }

    public void cargarEmail(int id, Email email) {
        String consultaSQL = "SELECT e.idEmail, e.email FROM ClienteEmail ce " +
                "JOIN Email e ON ce.idEmail = e.idEmail " +
                "WHERE ce.idCliente = "+ id +";";

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            // Preparamos con la consulta e indicamos que queremos el id generado
            PreparedStatement statement = conexion.prepareStatement(consultaSQL);

            // Preparo la query en la conexion
            statement = conexion.prepareStatement(consultaSQL);

            // Ejecuto la Query
            ResultSet resultSet = statement.executeQuery();

            // Cargo el cliente
            if (resultSet.next()) {
                email.setIdEmail(resultSet.getInt("idEmail"));
                email.setEmail(resultSet.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            VistaUtil.mostrarAlerta("Error",
                    "Error al cargar email: " + e.getMessage());
        }
    }
}
