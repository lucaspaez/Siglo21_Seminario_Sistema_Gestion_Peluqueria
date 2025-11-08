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

    public boolean actualizarProveedor() {

        String consultaSql = "UPDATE Proveedor "
                + "SET "
                + "razonSocial = '"+ this.getRazonSocial() +"', "
                + "cuit = '"+ this.getCuit() +"' "
                + "WHERE idProveedor = " + this.getIdProveedor() + ";";

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
                    "Error al intentar actualizar el proveedor: " + e.getMessage());

            return false;
        }

    }

    public void GuardarNuevoProveedor(int idDireccion) {

        String consultaSql = "INSERT INTO Proveedor (razonSocial, cuit, idDireccion) VALUES\n"
                + "(?, ?, ?);";

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            // Preparamos con la consulta e indicamos que queremos el id generado
            PreparedStatement statement = conexion.prepareStatement(consultaSql,
                    PreparedStatement.RETURN_GENERATED_KEYS);

            // Asigno los valores del objeto Proveedor
            statement.setString(1, this.getRazonSocial());
            statement.setString(2, this.getCuit());
            statement.setInt(3, idDireccion);

            int filas = statement.executeUpdate();

            if (filas > 0) {
                // Obtengo el ID Generado por la Base de Datos
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.setIdProveedor(rs.getInt(1));
                        //System.out.println("ID Proveedor guardado: " + this.getIdProveedor());
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

            VistaUtil.mostrarAlerta("error",
                    "Error al guardar Proveedor: " + e.getMessage());
        }

    }

    public int cargarProveedor(int id) {


        String consultaSQL = "SELECT * FROM Proveedor "
                + "WHERE idProveedor = " + id;

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            PreparedStatement statement = null;
            ResultSet resultSet = null;

            // Preparo la query en la conexion
            statement = conexion.prepareStatement(consultaSQL);

            // Ejecuto la Query
            resultSet = statement.executeQuery();

            // Cargo el cliente
            if (resultSet.next()) {
                setIdProveedor(resultSet.getInt("idProveedor"));
                setRazonSocial(resultSet.getString("razonSocial"));
                setCuit(resultSet.getString("cuit"));
                return resultSet.getInt("idDireccion");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            VistaUtil.mostrarAlerta("error",
                    "Error al cargar el proveedor: " + e.getMessage());
        }

        return 0;
    }

    public boolean eliminarProveedor() {
        String consultaSql = "CALL EliminarOInactivarProveedor(" + this.getIdProveedor() + ");";

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            // Preparo la query en la conexion
            PreparedStatement statement = conexion.prepareStatement(consultaSql);

            // Ejecuto la Query
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            VistaUtil.mostrarAlerta("error",
                    "Error al intentar eliminar el Proveedor: " + e.getMessage());
            return false;
        }
        return true;
    }
}
