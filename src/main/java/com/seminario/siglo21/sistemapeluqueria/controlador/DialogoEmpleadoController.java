package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.modelo.Empleado;
import com.seminario.siglo21.sistemapeluqueria.persistencia.EmpleadoDAO;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;

public class DialogoEmpleadoController {

    @FXML private Label lblTitulo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDNI;
    @FXML private CheckBox chkActivo;
    @FXML private Label lblError;
    @FXML private Button btnGuardar;

    private Empleado empleadoAEditar = null;
    private GestionarEmpleadosController mainController;
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    public void setMainController(GestionarEmpleadosController mainController) {
        this.mainController = mainController;
    }

    /**
     * Inicializa el formulario.
     * Si 'empleado' es null, está en modo CREAR.
     * Si 'empleado' no es null, está en modo EDITAR.
     */
    public void initData(Empleado empleado) {
        if (empleado != null) {
            this.empleadoAEditar = empleado;
            lblTitulo.setText("Editar Empleado");
            btnGuardar.setText("Actualizar");

            // Cargar datos existentes
            txtNombre.setText(empleado.getNombre());
            txtApellido.setText(empleado.getApellido());
            txtDNI.setText(String.valueOf(empleado.getDni()));
            chkActivo.setSelected(empleado.isActivo());
        } else {
            // Modo Crear
            lblTitulo.setText("Nuevo Empleado");
            btnGuardar.setText("Guardar");
            chkActivo.setSelected(true); // Por defecto activo
        }
    }

    @FXML
    private void handleGuardar() {
        if (!validarCampos()) {
            return;
        }

        try {
            int dni = Integer.parseInt(txtDNI.getText());

            if (empleadoAEditar == null) {
                // MODO CREAR
                Empleado nuevo = new Empleado();
                nuevo.setNombre(txtNombre.getText());
                nuevo.setApellido(txtApellido.getText());
                nuevo.setDni(dni);
                nuevo.setActivo(chkActivo.isSelected());

                empleadoDAO.crear(nuevo);
                VistaUtil.mostrarAlerta("info", "Empleado creado exitosamente.");

            } else {
                // MODO ACTUALIZAR
                empleadoAEditar.setNombre(txtNombre.getText());
                empleadoAEditar.setApellido(txtApellido.getText());
                empleadoAEditar.setDni(dni);
                empleadoAEditar.setActivo(chkActivo.isSelected());

                empleadoDAO.actualizar(empleadoAEditar);
                VistaUtil.mostrarAlerta("info", "Empleado actualizado exitosamente.");
            }

            // Recargar la tabla principal y cerrar
            mainController.cargarEmpleados();
            cerrarVentana();

        } catch (NumberFormatException e) {
            lblError.setText("Error: El DNI debe ser un número.");
        } catch (SQLException e) {
            lblError.setText("Error de base de datos: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() || txtDNI.getText().isEmpty()) {
            lblError.setText("Error: Todos los campos son obligatorios.");
            return false;
        }
        // Puedes añadir más validaciones (ej. DNI solo números)
        return true;
    }

    @FXML
    private void handleCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }
}