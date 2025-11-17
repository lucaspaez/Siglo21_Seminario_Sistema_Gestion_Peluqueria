package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.App;
import com.seminario.siglo21.sistemapeluqueria.modelo.Empleado;
import com.seminario.siglo21.sistemapeluqueria.persistencia.EmpleadoDAO;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class GestionarEmpleadosController implements Initializable {

    @FXML private TableView<Empleado> tvEmpleados;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colApellido;
    @FXML private TableColumn<Empleado, Integer> colDNI;
    @FXML private TableColumn<Empleado, Boolean> colActivo;

    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private ObservableList<Empleado> listaEmpleados = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Configurar las columnas de la tabla
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

        // Formato visual para la columna "Activo" (muestra Sí/No)
        colActivo.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : (item ? "Sí" : "No"));
            }
        });

        // 2. Cargar los datos
        cargarEmpleados();
    }

    @FXML
    public void cargarEmpleados() {
        try {
            listaEmpleados.clear();
            List<Empleado> empleados = empleadoDAO.obtenerTodos();
            listaEmpleados.addAll(empleados);
            tvEmpleados.setItems(listaEmpleados);
        } catch (SQLException e) {
            VistaUtil.mostrarAlerta("error", "Error al cargar empleados: " + e.getMessage());
        }
    }

    @FXML
    private void handleNuevo() {
        try {
            // Abrir el formulario en modo "Creación"
            DialogoEmpleadoController formController = VistaUtil.abrirVentanaYObtenerControlador(
                    "/com/seminario/siglo21/sistemapeluqueria/DialogoEmpleado.fxml",
                    "Nuevo Empleado",
                    (DialogoEmpleadoController controller) -> {
                        controller.setMainController(this); // Pasa la referencia de este controlador
                        controller.initData(null); // Pasa null para indicar que es "Nuevo"
                    }
            );
        } catch (IOException e) {
            VistaUtil.mostrarAlerta("error", "No se pudo abrir el formulario: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditar() {
        Empleado seleccionado = tvEmpleados.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            VistaUtil.mostrarAlerta("info", "Debe seleccionar un empleado para editar.");
            return;
        }

        try {
            // Abrir el formulario en modo "Edición"
            DialogoEmpleadoController formController = VistaUtil.abrirVentanaYObtenerControlador(
                    "/com/seminario/siglo21/sistemapeluqueria/DialogoEmpleado.fxml",
                    "Editar Empleado",
                    (DialogoEmpleadoController controller) -> {
                        controller.setMainController(this);
                        controller.initData(seleccionado); // Pasa el empleado seleccionado
                    }
            );
        } catch (IOException e) {
            VistaUtil.mostrarAlerta("error", "No se pudo abrir el formulario: " + e.getMessage());
        }
    }

    @FXML
    private void handleEliminar() {
        Empleado seleccionado = tvEmpleados.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            VistaUtil.mostrarAlerta("info", "Debe seleccionar un empleado para inactivar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Inactivación");
        confirmacion.setHeaderText("¿Está seguro de que desea inactivar a " + seleccionado.getNombre() + " " + seleccionado.getApellido() + "?");
        confirmacion.setContentText("Esta acción cambiará el estado del empleado a 'Inactivo'.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                if (empleadoDAO.inactivar(seleccionado.getIdEmpleado())) {
                    VistaUtil.mostrarAlerta("info", "Empleado inactivado correctamente.");
                    cargarEmpleados(); // Recargar la tabla
                } else {
                    VistaUtil.mostrarAlerta("error", "No se pudo inactivar el empleado.");
                }
            } catch (SQLException e) {
                VistaUtil.mostrarAlerta("error", "Error de base de datos: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleVolver() throws IOException {
        VistaUtil.cambiarVista(App.getPrimaryStage(),
                "/com/seminario/siglo21/sistemapeluqueria/MenuPrincipal.fxml",
                "Sistema de Gestión - Principal");
    }
}