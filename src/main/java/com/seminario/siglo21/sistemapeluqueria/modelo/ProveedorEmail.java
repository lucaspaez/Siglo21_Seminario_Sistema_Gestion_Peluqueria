package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProveedorEmail {
    private int idProveedor;
    private int idEmail;

    public ProveedorEmail() {
    }

    public ProveedorEmail(int idProveedor, int idEmail) {
        this.idProveedor = idProveedor;
        this.idEmail = idEmail;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public int getIdEmail() {
        return idEmail;
    }

    public void setIdEmail(int idEmail) {
        this.idEmail = idEmail;
    }

    public void conectaProveedorEmail() {

        String consultaSql = "INSERT INTO ProveedorEmail "
                + "VALUES ( ?, ?);";

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            // Preparamos con la consulta
            PreparedStatement statement = conexion.prepareStatement(consultaSql);

            // Asigno los valores del objeto Email
            statement.setInt(1, this.getIdProveedor());
            statement.setInt(2, this.getIdEmail());

            // Ejecuto la consulta
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            VistaUtil.mostrarAlerta("error", e.getMessage());
        }

    }
}
