package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarcaProducto {

    private int idMarcaProducto;
    private String nombreMarca;
    private String descripcionMarca;

    public MarcaProducto() {
    }
    public MarcaProducto(int idMarcaProducto, String nombreMarca, String descripcionMarca) {
        this.idMarcaProducto = idMarcaProducto;
        this.nombreMarca = nombreMarca;
        this.descripcionMarca = descripcionMarca;
    }

    public int getIdMarcaProducto() {
        return idMarcaProducto;
    }

    public void setIdMarcaProducto(int idMarcaProducto) {
        this.idMarcaProducto = idMarcaProducto;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public String getDescripcionMarca() {
        return descripcionMarca;
    }

    public void setDescripcionMarca(String descripcionMarca) {
        this.descripcionMarca = descripcionMarca;
    }

    public static List<MarcaProducto> listarMarcaProducto() {
        List<MarcaProducto> lista = new ArrayList();

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            String consultaSQL = "SELECT * FROM marcaproducto;";

            // Ejecuto la consulta
            statement = conexion.prepareStatement(consultaSQL);
            resultSet = statement.executeQuery();

            // CArgo la lista con todos los resultados de la query
            while (resultSet.next()) {

                MarcaProducto marcaProducto = new MarcaProducto();

                marcaProducto.setIdMarcaProducto(resultSet.getInt("idMarcaProducto"));
                marcaProducto.setNombreMarca(resultSet.getString("nombreMarca"));
                marcaProducto.setDescripcionMarca(resultSet.getString("DescripcionMarca"));
                lista.add(marcaProducto);
            }

        } catch (SQLException e) {
            VistaUtil.mostrarAlerta("error", e.getMessage());
        }

        return lista;
    }
}
