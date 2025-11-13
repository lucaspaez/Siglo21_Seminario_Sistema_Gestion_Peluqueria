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
    private String categoria;

    public ServicioInterno() {
    }

    public ServicioInterno(int idServicioInterno, String nombreServicio, String descripcion, int duracionHoras, double precio, boolean activo, String categoria) {
        this.idServicioInterno = idServicioInterno;
        this.nombreServicio = nombreServicio;
        this.descripcion = descripcion;
        this.duracionHoras = duracionHoras;
        this.precio = precio;
        this.activo = activo;
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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

            String consultaSQL = "SELECT " +
                    "si.idServicioInterno, " +
                    "si.nombreServicio, " +
                    "si.descripcion, " +
                    "si.duracionHoras, " +
                    "si.precio, " +
                    "si.activo, " +
                    "cs.nombreCategoria AS categoria " +
                    "FROM ServicioInterno " +
                    "si JOIN CategoriaServicio cs " +
                        "ON si.idCategoria = cs.idCategoria " +
                    "WHERE si.activo = TRUE;";

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
                s.setCategoria(resultSet.getString("categoria"));

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

        // 1. La consulta ahora es una llamada al Stored Procedure
        String consultaSql = "{CALL InsertarServicioInterno(?, ?, ?, ?, ?, ?)}";

        try (Connection conexion = ConexionBD.getConexion();
             // 2. Usamos CallableStatement en lugar de PreparedStatement
             CallableStatement statement = conexion.prepareCall(consultaSql)) {

            // Parámetros del SP:
            // IN p_nombreServicio, IN p_descripcion, IN p_duracion_minutos,
            // IN p_precio, IN p_activo, IN p_nombreCategoria

            statement.setString(1, this.getNombreServicio());
            statement.setString(2, this.getDescripcion());
            // 🚨 Asumo que este método devuelve la duración en Horas para coincidir con el SP
            statement.setInt(3, this.getDuracionHoras());
            statement.setDouble(4, this.getPrecio());
            statement.setBoolean(5, true);
            // 💡 Nuevo parámetro para la Categoría (pasamos el nombre)
            statement.setString(6, this.getCategoria());

            statement.execute(); // Usamos execute() en lugar de executeUpdate() para SPs

            return true;

        } catch (SQLException e) {
            e.printStackTrace();

            // ⚠️ El error ahora puede ser el de la base de datos (por ejemplo, categoría no encontrada)
            VistaUtil.mostrarAlerta("error",
                    "Error al crear el Servicio: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarServicio() {

        // 1. La consulta ahora es una llamada al Stored Procedure
        String consultaSql = "{CALL ActualizarServicioInterno(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conexion = ConexionBD.getConexion();
             // 2. Usamos CallableStatement
             CallableStatement statement = conexion.prepareCall(consultaSql)) {

            // Parámetros del SP:
            // IN p_idServicioInterno, IN p_nombreServicio, IN p_descripcion,
            // IN p_duracion_minutos, IN p_precio, IN p_activo, IN p_nombreCategoria

            statement.setInt(1, this.getIdServicioInterno()); // 1. ID del servicio a actualizar
            statement.setString(2, this.getNombreServicio());
            statement.setString(3, this.getDescripcion());
            // 🚨 Asumo que este método devuelve la duración en Horas
            statement.setInt(4, this.getDuracionHoras());
            statement.setDouble(5, this.getPrecio());
            statement.setBoolean(6, this.isActivo()); // Es mejor usar el estado real del objeto
            // 💡 Nuevo parámetro para la Categoría (pasamos el nombre)
            statement.setString(7, this.getCategoria());

            statement.execute(); // Usamos execute()

            return true;

        } catch (SQLException e) {
            e.printStackTrace();

            // El error puede incluir la razón del fallo (ej. categoría no existe)
            VistaUtil.mostrarAlerta("error",
                    "Error al actualizar el Servicio: " + e.getMessage());
            return false;
        }

    }

    public void cargarServicio(int id) {

        String consultaSQL = "SELECT " +
                "si.idServicioInterno, " +
                "si.nombreServicio, " +
                "si.descripcion, " +
                "si.duracionHoras, " +
                "si.precio, " +
                "si.activo, " +
                "cs.nombreCategoria AS categoria " +
                "FROM ServicioInterno " +
                "si JOIN CategoriaServicio cs " +
                "ON si.idCategoria = cs.idCategoria " +
                "WHERE idServicioInterno = "+ id +";";

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
                this.setCategoria(resultSet.getString("categoria"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            VistaUtil.mostrarAlerta("error",
                    "Error al cargar el Servicio: " + e.getMessage());
        }

    }

}

