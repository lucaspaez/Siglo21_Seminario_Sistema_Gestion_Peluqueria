package com.seminario.siglo21.sistemapeluqueria.modelo;

import com.seminario.siglo21.sistemapeluqueria.persistencia.ConexionBD;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Empleado extends Persona{
    
    // Atributos Exclusivos de la clase Empleado
    private int idEmpleado;
    private String especialidad;

    // Métodos Constructores
    public Empleado() {
    }

    public Empleado(int idEmpleado, String especialidad, String nombre, String apellido, int dni, String direccion, int telefono, String email, boolean activo) {
        super(nombre, apellido, dni, direccion, telefono, email, activo);
        this.idEmpleado = idEmpleado;
        this.especialidad = especialidad;
    }

    public static List<Empleado> ListarEmpleados() {
        List<Empleado> lista = new ArrayList<>();

        try {
            // Inicializo variables de conexion
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            String consultaSQL = "SELECT * FROM vista_empleados_detalle WHERE activo = TRUE;";

            // Ejecuto la consulta
            statement = conexion.prepareStatement(consultaSQL);
            resultSet = statement.executeQuery();

            // CArgo la lista con todos los resultados de la query
            while (resultSet.next()) {

                Empleado e = new Empleado();

                e.setIdEmpleado(resultSet.getInt("idEmpleado"));
                e.setNombre(resultSet.getString("nombre"));
                e.setApellido(resultSet.getString("apellido"));
                e.setDni(resultSet.getInt("dni"));
                e.setEspecialidad(resultSet.getString("especialidad"));
                e.setTelefono(resultSet.getInt("telefonos"));
                e.setEmail(resultSet.getString("emails"));
                e.setDireccion(resultSet.getString("direccionCompleta"));

                lista.add(e);
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

    // Métodos Getters y Setters
    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public String toString() {
        return getNombre() + " "+ getApellido(); // Esto es lo que se mostrará en el ComboBox
    }
}
