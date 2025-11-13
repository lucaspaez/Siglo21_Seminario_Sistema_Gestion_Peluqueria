package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaServicio {

    private int idCategoria;
    private String nombreCategoria;
    private String descripcionCategoria;

    public CategoriaServicio() {
    }

    public CategoriaServicio(int idCategoria, String nombreCategoria, String descripcionCategoria) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.descripcionCategoria = descripcionCategoria;
    }

    public static List<CategoriaServicio> listarCategoriasServicios() {

        List<CategoriaServicio> lista = new ArrayList<>();

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            String consultaSQL = "SELECT * FROM CategoriaServicio WHERE activo = TRUE;";

            // Ejecuto la consulta
            statement = conexion.prepareStatement(consultaSQL);
            resultSet = statement.executeQuery();

            // CArgo la lista con todos los resultados de la query
            while (resultSet.next()) {

                CategoriaServicio catServ = new CategoriaServicio();

                catServ.setIdCategoria(resultSet.getInt("idCategoria"));
                catServ.setNombreCategoria(resultSet.getString("nombreCategoria"));
                catServ.setDescripcionCategoria(resultSet.getString("descripcionCategoria"));
                lista.add(catServ);
            }

        } catch (SQLException e) {
            VistaUtil.mostrarAlerta("error", e.getMessage());
        }

        return lista;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }

    public void setearCategoriaNombre(String nombreCategoria){
        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            String consultaSQL = "SELECT * FROM CategoriaServicio " +
                    "WHERE nombreCategoria = '"+ nombreCategoria +"';";

            // Ejecuto la consulta
            statement = conexion.prepareStatement(consultaSQL);
            resultSet = statement.executeQuery();

            // Cargo la lista con todos los resultados de la query
            while (resultSet.next()) {

                this.setIdCategoria((resultSet.getInt("idCategoria")));
                this.setNombreCategoria((resultSet.getString("nombreCategoria")));
                this.setDescripcionCategoria((resultSet.getString("descripcionCategoria")));

            }

        } catch (SQLException e) {
            VistaUtil.mostrarAlerta("error", e.getMessage());
        }
    }

    public void setearCategoriaId(int idCategoriaEditar) {

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            String consultaSQL = "SELECT * FROM CategoriaServicio " +
                    "WHERE idCategoria = '"+ idCategoriaEditar +"';";

            // Ejecuto la consulta
            statement = conexion.prepareStatement(consultaSQL);
            resultSet = statement.executeQuery();

            // Cargo la lista con todos los resultados de la query
            while (resultSet.next()) {

                this.setIdCategoria((resultSet.getInt("idCategoria")));
                this.setNombreCategoria((resultSet.getString("nombreCategoria")));
                this.setDescripcionCategoria((resultSet.getString("descripcionCategoria")));

            }

        } catch (SQLException e) {
            VistaUtil.mostrarAlerta("error", e.getMessage());
        }

    }


    public boolean actualizarCategoria() {

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement statement = null;

            String consultaSQL = "UPDATE CategoriaServicio SET " +
                    "nombreCategoria = '"+ this.getNombreCategoria() +"', " +
                    "descripcionCategoria = '"+ this.getDescripcionCategoria() +"' " +
                    "WHERE idCategoria = "+ this.getIdCategoria() +";";

            // Ejecuto la consulta
            statement = conexion.prepareStatement(consultaSQL);
            statement.execute();

            return true;
        } catch (SQLException e) {
            VistaUtil.mostrarAlerta("error", e.getMessage());
            return false;
        }
    }

    public boolean creaCategoria() {

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement statement = null;

            String consultaSQL = "INSERT INTO CategoriaServicio (nombreCategoria, descripcionCategoria) " +
                    "VALUES ('"+ this.getNombreCategoria() +"', '"+ this.getDescripcionCategoria() +"');";

            // Ejecuto la consulta
            statement = conexion.prepareStatement(consultaSQL);
            statement.execute();
            return true;

        } catch (SQLException e) {
            VistaUtil.mostrarAlerta("error", e.getMessage());
            return false;
        }
    }

    public boolean eliminarCategoria(){

        // Recupero el id de la marca:
        String consultaSql = "CALL EliminarOInactivarCategoria(" + this.getIdCategoria() + ");";

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
                    "Error al eliminar el Categoría: " + e.getMessage());
            return false;
        }

        return true;

    }
}
