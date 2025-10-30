package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Proveedor {

    // Atributos
    private int idProveedor;
    private String RazonSocial;
    private String Cuit;
    private String Direccion;
    private int Telefono;
    private String email;
    private boolean activo;

    // Constructores
    public Proveedor() {
    }

    public Proveedor(int id, String razonSocial, String cuit, String direccion, int telefono, String email, boolean activo) {
        this.idProveedor = id;
        RazonSocial = razonSocial;
        Cuit = cuit;
        Direccion = direccion;
        Telefono = telefono;
        this.email = email;
        this.activo = activo;
    }

    // Getters y Setters
    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int id) {
        this.idProveedor = id;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        RazonSocial = razonSocial;
    }

    public String getCuit() {
        return Cuit;
    }

    public void setCuit(String cuit) {
        Cuit = cuit;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public int getTelefono() {
        return Telefono;
    }

    public void setTelefono(int telefono) {
        Telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public static List<Proveedor> listarProveedores(){
        List<Proveedor> lista = new ArrayList();

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            String consultaSQL = "SELECT * FROM vista_proveedores_detalle "
                    + "WHERE activo = TRUE;";

            // Ejecuto la consulta
            statement = conexion.prepareStatement(consultaSQL);
            resultSet = statement.executeQuery();

            // CArgo la lista con todos los resultados de la query
            while (resultSet.next()) {

                Proveedor p = new Proveedor();

                p.setIdProveedor(resultSet.getInt("idProveedor"));
                p.setRazonSocial(resultSet.getString("razonSocial"));
                p.setCuit(resultSet.getString("cuit"));
                p.setTelefono(resultSet.getInt("telefonos"));
                p.setEmail(resultSet.getString("emails"));
                p.setDireccion(resultSet.getString("direccionCompleta"));

                lista.add(p);
            }

        } catch (SQLException e) {
            VistaUtil.mostrarAlerta("error", e.getMessage());
        }

        return lista;
    }
}
