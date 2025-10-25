package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import com.seminario.siglo21.sistemapeluqueria.util.HashUtil;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;

public class Cliente extends Persona {

    // Atributos exclusivos de la clase Cliente
    private int idCliente;

    // Métodos Constructores
    public Cliente() {
    }

    public Cliente(int idCliente, String nombre, String apellido, int dni, String direccion, int telefono, String email, boolean activo) {
        super(nombre, apellido, dni, direccion, telefono, email, activo);
        this.idCliente = idCliente;
    }

    // Métodos Getters y Setters
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public static List<Cliente> listarClientes() {

        List<Cliente> lista = new ArrayList<>();

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            String consultaSQL = "SELECT * FROM vista_clientes_detalle "
                    + "WHERE activo = TRUE;";

            // Ejecuto la consulta
            statement = conexion.prepareStatement(consultaSQL);
            resultSet = statement.executeQuery();

            // CArgo la lista con todos los resultados de la query
            while (resultSet.next()) {

                Cliente c = new Cliente();

                c.setIdCliente(resultSet.getInt("idCliente"));
                c.setNombre(resultSet.getString("nombre"));
                c.setApellido(resultSet.getString("apellido"));
                c.setDni(resultSet.getInt("dni"));
                c.setTelefono(resultSet.getInt("telefonos"));
                c.setEmail(resultSet.getString("emails"));
                c.setDireccion(resultSet.getString("direccionCompleta"));

                lista.add(c);
            }

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setTitle("Error");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }

        return lista;
    }

    public void GuardarNuevoCliente(int idDireccion) {

        String consultaSql = "INSERT INTO Cliente (nombre, apellido, dni, idDireccion) VALUES\n"
                + "(?, ?, ?, ?);";

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();

            // Preparamos con la consulta e indicamos que queremos el id generado
            PreparedStatement statement = conexion.prepareStatement(consultaSql,
                    PreparedStatement.RETURN_GENERATED_KEYS);

            // Asigno los valores del objeto Cliente
            statement.setString(1, this.getNombre());
            statement.setString(2, this.getApellido());
            statement.setInt(3, this.getDni());
            statement.setInt(4, idDireccion);

            int filas = statement.executeUpdate();

            if (filas > 0) {
                // Obtengo el ID Generado por la Base de Datos
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {

                        setIdCliente(rs.getInt(1));
                        System.out.println("ID de cliente es: " + getIdCliente());

                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

            VistaUtil.mostrarAlerta("error",
                    "Error al guardar cliente: " + e.getMessage());
        }
    }

    public boolean desactivaCliente() {

        String consultaSql = "UPDATE Cliente SET activo = FALSE "
                + "WHERE idCliente = " + this.getIdCliente();

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
                    "Error al intentar eliminar el cliente: " + e.getMessage());
            return false;
        }

        return true;
    }

    public boolean actualizarCliente() {

        String consultaSql = "UPDATE Cliente "
                + "SET "
                    + "nombre = '"+ this.getNombre() +"', "
                    + "apellido = '"+ this.getApellido() +"', "
                    + "dni = "+ this.getDni() +" "
                + "WHERE idCliente = " + this.getIdCliente() + ";";

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
                    "Error al intentar actualizar el cliente: " + e.getMessage());
            
            return false;
        }
    }

    public int cargarCliente(int id) {

        String consultaSQL = "SELECT * FROM Cliente "
                + "WHERE idCliente = " + id;

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
                setIdCliente(resultSet.getInt("idCliente"));
                setNombre(resultSet.getString("nombre"));
                setApellido(resultSet.getString("apellido"));
                setDni(resultSet.getInt("dni"));
                return resultSet.getInt("idDireccion");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            VistaUtil.mostrarAlerta("error",
                    "Error al cargar el cliente: " + e.getMessage());
        }

        return 0;
    }

}
