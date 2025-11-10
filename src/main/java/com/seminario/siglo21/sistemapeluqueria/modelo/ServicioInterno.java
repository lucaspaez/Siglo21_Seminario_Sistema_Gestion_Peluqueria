package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioInterno {
    private int idServicioInterno;
    private String nombreServicio;
    private String descripcion;
    private int duracionHoras;
    private double precio;
    private boolean activo;

    public ServicioInterno() {
    }

    public ServicioInterno(int idServicioInterno, String nombreServicio, String descripcion, int duracionHoras, double precio, boolean activo) {
        this.idServicioInterno = idServicioInterno;
        this.nombreServicio = nombreServicio;
        this.descripcion = descripcion;
        this.duracionHoras = duracionHoras;
        this.precio = precio;
        this.activo = activo;
    }

    public int getDuracionHoras() {
        return duracionHoras;
    }

    public void setDuracionHoras(int duracionHoras) {
        this.duracionHoras = duracionHoras;
    }

    public int getIdServicioInterno() {
        return idServicioInterno;
    }

    public void setIdServicioInterno(int idServicioInterno) {
        this.idServicioInterno = idServicioInterno;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Funciones
    public static List<ServicioInterno> listarServicios() {
        List<ServicioInterno> lista = new ArrayList();

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            String consultaSQL = "SELECT * FROM serviciointerno "
                    + "WHERE activo = TRUE;";

            // Ejecuto la consulta
            statement = conexion.prepareStatement(consultaSQL);
            resultSet = statement.executeQuery();

            // Cargo la lista con todos los resultados de la query
            while (resultSet.next()) {

                ServicioInterno s = new ServicioInterno();

                s.setIdServicioInterno(resultSet.getInt("idServicioInterno"));
                s.setNombreServicio(resultSet.getString("nombreServicio"));
                s.setDescripcion(resultSet.getString("descripcion"));
                s.setDuracionHoras(resultSet.getInt("duracionHoras"));
                s.setPrecio(resultSet.getDouble("precio"));

                lista.add(s);
            }

        } catch (SQLException e) {
            VistaUtil.mostrarAlerta("error", e.getMessage());
        }

        return lista;
    }

    public boolean eliminarServicio() {

        // Recupero el id de la marca:
        String consultaSql = "CALL EliminarOInactivarServicioInterno(" + this.getIdServicioInterno() + ");";

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            // Preparamos con la consulta e indicamos que queremos el id generado
            PreparedStatement statement = conexion.prepareStatement(consultaSql);

            // Ejecutamos la query
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

            VistaUtil.mostrarAlerta("error",
                    "Error al eliminar el Servicio: " + e.getMessage());
            return false;
        }

        return true;

    }

    public boolean agregarServicio() {

        String consultaSql = "INSERT INTO ServicioInterno (nombreServicio, descripcion, duracionHoras, precio, activo) " +
                "VALUES ( ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement statement = conexion.prepareStatement(consultaSql)) {

            statement.setString(1, this.getNombreServicio());
            statement.setString(2, this.getDescripcion());
            statement.setInt(3, this.getDuracionHoras());
            statement.setDouble(4, this.getPrecio());
            statement.setBoolean(5, true);

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();

            VistaUtil.mostrarAlerta("error",
                    "Error al crear el Servicio: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarServicio() {

        String consultaSql = "UPDATE ServicioInterno " +
                "SET " +
                "nombreServicio = ?, " +
                "descripcion = ?, " +
                "duracionHoras = ?, " +
                "precio = ?, " +
                "activo = ? " +
                "WHERE " +
                "idServicioInterno = ?";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement statement = conexion.prepareStatement(consultaSql)) {

            statement.setString(1, this.getNombreServicio());
            statement.setString(2, this.getDescripcion());
            statement.setInt(3, this.getDuracionHoras());
            statement.setDouble(4, this.getPrecio());
            statement.setBoolean(5, true);
            statement.setInt(6, this.getIdServicioInterno());

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();

            VistaUtil.mostrarAlerta("error",
                    "Error al actualizar el Servicio: " + e.getMessage());
            return false;
        }

    }

    public void cargarServicio(int id) {

        String consultaSQL = "SELECT * FROM serviciointerno WHERE idServicioInterno = "+ id +";";

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            PreparedStatement statement = null;
            ResultSet resultSet = null;

            // Preparo la query en la conexion
            statement = conexion.prepareStatement(consultaSQL);

            // Ejecuto la Query
            resultSet = statement.executeQuery();

            // Cargo el Servicio
            if (resultSet.next()) {
                this.setIdServicioInterno(resultSet.getInt("idServicioInterno"));
                this.setNombreServicio(resultSet.getString("nombreServicio"));
                this.setDescripcion(resultSet.getString("descripcion"));
                this.setDuracionHoras(resultSet.getInt("duracionHoras"));
                this.setPrecio(resultSet.getDouble("precio"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            VistaUtil.mostrarAlerta("error",
                    "Error al cargar el Servicio: " + e.getMessage());
        }

    }


}

