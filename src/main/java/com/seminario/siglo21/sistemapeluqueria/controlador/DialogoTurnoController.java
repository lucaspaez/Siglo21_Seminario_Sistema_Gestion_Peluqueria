package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.modelo.Cliente;
import com.seminario.siglo21.sistemapeluqueria.modelo.Empleado;
import com.seminario.siglo21.sistemapeluqueria.modelo.ServicioInterno;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

// Nota: Asumimos que existen los DAO y Modelos para Cliente, Empleado y Servicio.

public class DialogoTurnoController implements Initializable {

    public Label lblMensaje;
    @FXML
    private ComboBox<Cliente> cmbCliente;
    @FXML
    private ComboBox<Empleado> cmbEmpleado;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private ComboBox<LocalTime> cmbHora;
    @FXML
    private ListView<ServicioInterno> lvServicios;
    @FXML
    private Button btnGuardar;

    private int idClienteSeleccionado;
    private int idEmpleadoSeleccionado;
    private int idServicioSeleccionado;
    private ObservableList<ServicioInterno> serviciosSeleccionados;

    // Referencia al controlador principal (para recargar la vista después de guardar)
    private TurnoController mainController;

    /**
     * Setea la referencia al controlador principal para poder recargar el calendario.
     * @param controller La instancia de TurnoController.
     */
    public void setMainController(TurnoController controller) {
        this.mainController = controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // 1. Cargamos los combobox de Clientes y Empleados
        // Cargamos los clientes en su combobox
        ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
        List<Cliente> datos = Cliente.listarClientes();
        listaClientes.setAll(datos);
        cmbCliente.setItems(listaClientes);

        // Cargamos los empleados en su combobox
        ObservableList<Empleado> listaEmpleados = FXCollections.observableArrayList();
        List<Empleado> datosEmpleados = Empleado.ListarEmpleados();
        listaEmpleados.setAll(datosEmpleados);
        cmbEmpleado.setItems(listaEmpleados);

        // 2. Llenamos ComboBox de Horas (Cada 30 minutos, de 08:00 a 19:30)
        LocalTime hora = LocalTime.of(8, 0);
        while (hora.isBefore(LocalTime.of(20, 0))) {
            cmbHora.getItems().add(hora);
            hora = hora.plusMinutes(30);
        }

        // 3. Cargamos los servicios
        ObservableList<ServicioInterno> listaServicios = FXCollections.observableArrayList();
        List<ServicioInterno> datosServicios = ServicioInterno.listarServicios();
        listaServicios.setAll(datosServicios);
        lvServicios.setItems(listaServicios);
        lvServicios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // 4. Establecemos la fecha predeterminada (hoy)
        dpFecha.setValue(LocalDate.now());

        // 5. Configuramos los listener de los combobox para recuperar los IDs
        cmbCliente.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                idClienteSeleccionado = newValue.getIdCliente();
                System.out.println("ID Cliente seleccionado: " + idClienteSeleccionado);
            }
        });
        cmbEmpleado.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                idEmpleadoSeleccionado = newValue.getIdEmpleado();
                System.out.println("ID Empleado seleccionado: " + idEmpleadoSeleccionado);
            }
        });
    }

    @FXML
    private void handleSave() {
        // ⚠️ VALIDACIONES: Asegurar que todos los campos requeridos estén seleccionados.

        if (cmbCliente.getValue() == null || cmbEmpleado.getValue() == null ||
                dpFecha.getValue() == null || cmbHora.getValue() == null ||
                lvServicios.getSelectionModel().isEmpty()) {

            // Mostrar una alerta de error (TODO: implementar AlertaUtil)
            this.lblMensaje.setText("¡Debes completar todos los campos!");
            //System.err.println("Debe completar todos los campos.");
            return;
        }

        serviciosSeleccionados = lvServicios.getSelectionModel().getSelectedItems();

        // Extraigo los IDs de los servicios seleccionados
        List<Integer> idServiciosSeleccionados = serviciosSeleccionados.stream()
                .map(ServicioInterno::getIdServicioInterno)
                .collect(Collectors.toList());

        System.out.println("IDs de Servicios seleccionados: " + idServiciosSeleccionados);

        System.out.println("Guardando nuevo turno...");
        System.out.println("Cliente: " + cmbCliente.getValue());
        // Aquí iría la lógica de persistencia (TurnoDAO.crearTurno(nuevoTurno)).

        // 1. Persistir el nuevo turno en la DB (Usando TurnoDAO)
        // 2. Si es exitoso:

        // Cierra la ventana modal
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();

        // Recarga el calendario principal
        if (mainController != null) {
            mainController.loadAppointments();
        }
    }

    @FXML
    private void handleCancel() {
        // Simplemente cierra la ventana modal
        Stage stage = (Stage) cmbCliente.getScene().getWindow();
        stage.close();
    }
}