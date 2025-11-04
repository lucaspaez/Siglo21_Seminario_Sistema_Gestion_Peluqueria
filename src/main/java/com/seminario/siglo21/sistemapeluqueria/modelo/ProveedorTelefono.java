package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProveedorTelefono {
    private int idTelefono;
    private int idProveedor;

    public ProveedorTelefono() {
    }

    public ProveedorTelefono(int idTelefono, int idProveedor) {
        this.idTelefono = idTelefono;
        this.idProveedor = idProveedor;
    }

    public int getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(int idTelefono) {
        this.idTelefono = idTelefono;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public void conectarProveedorTelefono() {

        String consultaSql = "INSERT INTO ProveedorTelefono "
                + "VALUES ( ?, ?);";

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            // Preparamos con la consulta
            PreparedStatement statement = conexion.prepareStatement(consultaSql);

            System.out.println("se conectaran:");
            System.out.println("Id Proveedor: " + this.getIdProveedor());
            System.out.println("Id Telefono: " + this.getIdTelefono());

            // Asigno los valores del objeto Telefono
            statement.setInt(1, this.getIdProveedor());
            statement.setInt(2, this.getIdTelefono());

            // Ejecuto la consulta
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            VistaUtil.mostrarAlerta("error", e.getMessage());
        }

    }
}
