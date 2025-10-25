package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
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
        String consultaSql = "INSERT INTO Telefono (telefono) VALUES (?);";

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            // Preparamos con la consulta e indicamos que queremos el id generado
            PreparedStatement statement = conexion.prepareStatement(consultaSql,
                    PreparedStatement.RETURN_GENERATED_KEYS);

            System.out.println(consultaSql);

            // Asigno los valores del objeto Telefono
            statement.setInt(1, this.getTelefono());

            int filas = statement.executeUpdate();

            if (filas > 0) {
                // Obtengo el ID Generado por la Base de Datos
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {

                        setIdTelefono(rs.getInt(1));

                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            VistaUtil.mostrarAlerta("Error",
                    "Error al guardar teléfono: " + e.getMessage());
        }
    }

    public void cargarTelefono(int idCliente) {

        String consultaSQL = "SELECT t.idTelefono, t.telefono FROM ClienteTelefono ct " +
                "JOIN Telefono t ON ct.idTelefono = t.idTelefono " +
                "WHERE ct.idCliente = "+ idCliente +";";

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            // Preparamos con la consulta e indicamos que queremos el id generado
            PreparedStatement statement = conexion.prepareStatement(consultaSQL);

            ResultSet resultSet = null;

            // Preparo la query en la conexion
            statement = conexion.prepareStatement(consultaSQL);

            // Ejecuto la Query
            resultSet = statement.executeQuery();

            // Cargo el cliente
            if (resultSet.next()) {
                this.setIdTelefono(resultSet.getInt("idTelefono"));
                this.setTelefono(resultSet.getInt("telefono"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            VistaUtil.mostrarAlerta("Error",
                    "Error al cargar teléfono: " + e.getMessage());
        }

    }
    
    public boolean actualizarTelefono(){
    
        String consultaSql = "UPDATE Telefono "
                + "SET "
                    + "telefono = "+ this.getTelefono() +" "
                + "WHERE idTelefono = " + this.getIdTelefono()+";";

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            // Preparo la query en la conexion
            PreparedStatement statement = conexion.prepareStatement(consultaSql);

            // Ejecuto la Query
            statement.executeUpdate();
                        
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            VistaUtil.mostrarAlerta("error",
                    "Error al intentar actualizar el teléfono: " + e.getMessage());
            
            return false;
        }
        
    }

}
