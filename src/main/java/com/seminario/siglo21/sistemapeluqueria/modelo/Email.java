package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;

public class Email {
    
    // Atributos
    private int idEmail;
    private String email;

    public Email() {
    }

    public Email(int idEmail, String email) {
        this.idEmail = idEmail;
        this.email = email;
    }

    public int getIdEmail() {
        return idEmail;
    }

    public void setIdEmail(int idEmail) {
        this.idEmail = idEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void GuardarEmailNuevo() {
        
        String consultaSql = "INSERT INTO Email (email) VALUES (?);";
        
        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            // Preparamos con la consulta e indicamos que queremos el id generado
            PreparedStatement statement = conexion.prepareStatement(consultaSql,
                    PreparedStatement.RETURN_GENERATED_KEYS);

            // Asigno los valores del objeto Email
            statement.setString(1, this.getEmail());

            int filas = statement.executeUpdate();

            if (filas > 0) {
                // Obtengo el ID Generado por la Base de Datos
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {

                        setIdEmail(rs.getInt(1));

                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setTitle("Error");
            a.setContentText("Error al guardar email: " + e.getMessage());
            a.showAndWait();
        }
    }


    
    public boolean actualizarEmail(){
    
        String consultaSql = "UPDATE Email "
                + "SET "
                    + "email = '"+ this.getEmail() +"' "
                + "WHERE idEmail = " + this.getIdEmail()+";";

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
                    "Error al intentar actualizar el Email: " + e.getMessage());
            
            return false;
        }
        
    }
    
    
}
