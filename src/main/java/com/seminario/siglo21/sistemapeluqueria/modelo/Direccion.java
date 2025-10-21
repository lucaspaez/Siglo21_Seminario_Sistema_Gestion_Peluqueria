package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;

public class Direccion {

    // Atributos de la clase Direccion
    private int idDireccion;
    private String calle;
    private int numero;
    private int piso;
    private String ciudad;
    private String provincia;
    private String pais;
    private int codigoPostal;

    // Métodos constructores
    public Direccion() {
    }

    public Direccion(int idDireccion, String calle, int numero, int piso, String ciudad, String provincia, String pais, int codigoPostal) {
        this.idDireccion = idDireccion;
        this.calle = calle;
        this.numero = numero;
        this.piso = piso;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.pais = pais;
        this.codigoPostal = codigoPostal;
    }

    // Métodos getters y setters
    public int getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public void GuardarDirecionNueva() {

        String consultaSql = "INSERT INTO Direccion (calle, numero, piso, ciudad, provincia, pais, codigoPostal) VALUES\n"
                + "(?, ?, ?, ?, ?, ?, ?);";

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            // Preparamos con la consulta e indicamos que queremos el id generado
            PreparedStatement statement = conexion.prepareStatement(consultaSql,
                    PreparedStatement.RETURN_GENERATED_KEYS);

            // Asigno los valores del objeto Direccion
            statement.setString(1, this.getCalle());
            statement.setInt(2, this.getNumero());
            statement.setInt(3, this.getPiso());
            statement.setString(4, this.getCiudad());
            statement.setString(5, this.getProvincia());
            statement.setString(6, this.getPais());
            statement.setInt(7, this.getCodigoPostal());

            int filas = statement.executeUpdate();

            if (filas > 0) {
                // Obtengo el ID Generado por la Base de Datos
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {

                        this.idDireccion = rs.getInt("idCliente");

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
