package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import com.seminario.siglo21.sistemapeluqueria.util.HashUtil;
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

            String consultaSQL = "SELECT * FROM vista_clientes_detalle;";

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

}
