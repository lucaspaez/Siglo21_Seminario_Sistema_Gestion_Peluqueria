package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.modelo.Cliente;
import com.seminario.siglo21.sistemapeluqueria.modelo.Empleado;
import com.seminario.siglo21.sistemapeluqueria.modelo.ServicioInterno;
import com.seminario.siglo21.sistemapeluqueria.modelo.Turno;
import com.seminario.siglo21.sistemapeluqueria.persistencia.TurnoDAO;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

// Nota: Asumimos que existen los DAO y Modelos para Cliente, Empleado y Servicio.

public class DialogoTurnoController implements Initializable {
    @FXML
    public Label lblMensaje;
    @FXML
    public Label lblTitulo;
    @FXML
    public ComboBox<String> cmbEstado;
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
    private ObservableList<ServicioInterno> serviciosSeleccionados;

    private Turno turnoAEditar = null;

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

        // 5. Llenamos el ComboBox de Estados
        cmbEstado.setItems(FXCollections.observableArrayList(
                "PENDIENTE",
                "CONFIRMADO",
                "REALIZADO",
                "CANCELADO"
        ));

        // 6. Configuramos los listener de los combobox para recuperar los IDs
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
    private void handleSave() throws SQLException {
        // VALIDACIONES: Nos aseguramos que todos los campos requeridos estén seleccionados.
        if (cmbEstado.getValue() == null) {
            VistaUtil.mostrarAlerta("error", "Debe seleccionar un estado para el turno.");
            return;
        }

        if (cmbCliente.getValue() == null || cmbEmpleado.getValue() == null ||
                dpFecha.getValue() == null || cmbHora.getValue() == null ||
                lvServicios.getSelectionModel().isEmpty()) {

            // Mostrar una alerta de error (TODO: implementar AlertaUtil)
            this.lblMensaje.setText("¡Debes completar todos los campos!");
            //System.err.println("Debe completar todos los campos.");
            return;
        }

        //Preparar el objeto Turno y los IDs de servicios
        Turno turnoFinal;
        if (turnoAEditar != null) {
            // Modo Edición: Usar el objeto existente y actualizar sus campos
            turnoFinal = turnoAEditar;
        } else {
            // Modo Creación: Crear un nuevo objeto Turno (estado por defecto: PENDIENTE)
            turnoFinal = new Turno();
        }

        turnoFinal.setEstado(cmbEstado.getValue());
        turnoFinal.setFecha(dpFecha.getValue());
        turnoFinal.setHora(cmbHora.getValue());
        turnoFinal.setIdCliente(cmbCliente.getValue().getIdCliente());
        turnoFinal.setIdEmpleado(cmbEmpleado.getValue().getIdEmpleado());

        // Extraigo los IDs de los servicios seleccionados
        serviciosSeleccionados = lvServicios.getSelectionModel().getSelectedItems();
        List<Integer> idServiciosSeleccionados = serviciosSeleccionados.stream()
                .map(ServicioInterno::getIdServicioInterno)
                .collect(Collectors.toList());

        List<Integer> idServicios = serviciosSeleccionados.stream()
                .map(ServicioInterno::getIdServicioInterno)
                .collect(Collectors.toList());

        //System.out.println("IDs de Servicios seleccionados: " + idServiciosSeleccionados);

        //System.out.println("Guardando nuevo turno...");
        //System.out.println("Cliente: " + cmbCliente.getValue());

        // Guradamos el turno en la DB
        TurnoDAO turnoDAO = new TurnoDAO();

        boolean resultado = false;

        // Persistencia (Llamada al DAO)
        try {
            if (turnoAEditar != null) {
                // Lógica de Actualización
                resultado = turnoDAO.actualizarTurno(turnoFinal, idServicios);
                if (resultado) {
                    VistaUtil.mostrarAlerta("info", "Turno actualizado exitosamente.");
                }
            } else {
                // Lógica de Creación (asumo que ya tienes agendarTurno)
                // Usamos el método que desarrollaste en pasos anteriores.
                resultado = turnoDAO.agendarTurno(turnoFinal, idServicios);
                if (resultado) {
                    VistaUtil.mostrarAlerta("info", "Turno agendado exitosamente.");
                }
            }

        } catch (SQLException e) {
            VistaUtil.mostrarAlerta("error", "Error de base de datos: " + e.getMessage());
            System.err.println("Error SQL: " + e.getMessage());
            return;
        }

        if (resultado) {
            // Cierra la ventana modal
            Stage stage = (Stage) btnGuardar.getScene().getWindow();
            stage.close();

            // Recarga el calendario principal
            if (mainController != null) {
                mainController.loadAppointments();
            }
        }
    }

    @FXML
    private void handleCancel() {
        // Simplemente cierra la ventana modal
        Stage stage = (Stage) cmbCliente.getScene().getWindow();
        stage.close();
    }

    /**
     * Inicializa el formulario en modo EDICIÓN con los datos de un Turno existente.
     * Este método también requiere que cmbCliente, cmbEmpleado y lvServicios
     * ya estén cargados con datos (lo que se hace en initialize).
     * * @param turno El objeto Turno completo obtenido desde el DAO.
     */
    public void initData(Turno turno) {
        this.turnoAEditar = turno;

        // 1. Cambiar el título y el texto del botón
        btnGuardar.setText("Actualizar Turno");

        lblTitulo.setText("Editar Turno Existente");

        // 2. Poblar los campos con los datos del turno
        dpFecha.setValue(turno.getFecha());
        cmbHora.setValue(turno.getHora());

        // 3. Seleccionamos Cliente y Empleado
        // Buscamos en la lista de items cargada el objeto cuyo ID coincida
        cmbCliente.getItems().stream()
                .filter(c -> c.getIdCliente() == turno.getIdCliente())
                .findFirst()
                .ifPresent(cmbCliente.getSelectionModel()::select);

        cmbEmpleado.getItems().stream()
                .filter(e -> e.getIdEmpleado() == turno.getIdEmpleado())
                .findFirst()
                .ifPresent(cmbEmpleado.getSelectionModel()::select);

        // 4. Seleccionamos el estado actual en el ComboBox
        cmbEstado.setValue(turno.getEstado());

        // 5. Seleccionar los Servicios
        if (turno.getIdServiciosAsociados() != null) {
            MultipleSelectionModel<ServicioInterno> selectionModel = lvServicios.getSelectionModel();

            for (ServicioInterno servicio : lvServicios.getItems()) {
                if (turno.getIdServiciosAsociados().contains(servicio.getIdServicioInterno())) {
                    selectionModel.select(servicio);
                }
            }
        }
    }
}