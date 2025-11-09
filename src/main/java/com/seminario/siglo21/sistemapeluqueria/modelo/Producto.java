package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Producto {

    // Atributos
    private int idProducto;
    private String skuProducto;
    private String nombreProducto;
    private String descripcionProducto;
    private double precioCostoProducto;
    private double precioVentaProducto;
    private String marcaProducto;
    private String proveedorProducto;
    private boolean activo;

    // Constructores
    public Producto() {

    }
    public Producto(int idProducto, String skuProducto, String nombreProducto, boolean activo, String proveedorProducto, String descripcionProducto, double precioCostoProducto, double precioVentaProducto, String marcaProducto) {
        this.idProducto = idProducto;
        this.skuProducto = skuProducto;
        this.nombreProducto = nombreProducto;
        this.activo = activo;
        this.proveedorProducto = proveedorProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioCostoProducto = precioCostoProducto;
        this.precioVentaProducto = precioVentaProducto;
        this.marcaProducto = marcaProducto;
    }

    //Getters y Setters
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getSkuProducto() {
        return skuProducto;
    }

    public void setSkuProducto(String skuProducto) {
        this.skuProducto = skuProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public double getPrecioCostoProducto() {
        return precioCostoProducto;
    }

    public void setPrecioCostoProducto(double precioCostoProducto) {
        this.precioCostoProducto = precioCostoProducto;
    }

    public double getPrecioVentaProducto() {
        return precioVentaProducto;
    }

    public void setPrecioVentaProducto(double precioVentaProducto) {
        this.precioVentaProducto = precioVentaProducto;
    }

    public String getMarcaProducto() {
        return marcaProducto;
    }

    public void setMarcaProducto(String marcaProducto) {
        this.marcaProducto = marcaProducto;
    }

    public String getProveedorProducto() {
        return proveedorProducto;
    }

    public void setProveedorProducto(String proveedorProducto) {
        this.proveedorProducto = proveedorProducto;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Funciones
    public static List<Producto> listarProductos() {

        List<Producto> lista = new ArrayList();

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            String consultaSQL = "SELECT * FROM vista_productos_detalle "
                    + "WHERE activo = TRUE;";

            // Ejecuto la consulta
            statement = conexion.prepareStatement(consultaSQL);
            resultSet = statement.executeQuery();

            // CArgo la lista con todos los resultados de la query
            while (resultSet.next()) {

                Producto p = new Producto();

                p.setIdProducto(resultSet.getInt("idProducto"));
                p.setSkuProducto(resultSet.getString("skuProducto"));
                p.setNombreProducto(resultSet.getString("nombreProducto"));
                p.setDescripcionProducto(resultSet.getString("descripcionProducto"));
                p.setPrecioCostoProducto(resultSet.getDouble("precioCostoProducto"));
                p.setPrecioVentaProducto(resultSet.getDouble("precioVentaProducto"));
                p.setMarcaProducto(resultSet.getString("marcaProducto"));
                p.setProveedorProducto(resultSet.getString("proveedorProducto"));

                lista.add(p);
            }

        } catch (SQLException e) {
            VistaUtil.mostrarAlerta("error", e.getMessage());
        }

        return lista;

    }

    public boolean actualizarProducto(String marca, String proveedor) {
        return true;
    }

    public boolean agregarProductoNuevo(String marca, String proveedor) {
        return true;
    }
}
