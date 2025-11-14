package com.seminario.siglo21.sistemapeluqueria.controlador;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.ResourceBundle;

// Nota: Asumimos que existen los DAO y Modelos para Cliente, Empleado y Servicio.

public class DialogoTurnoController implements Initializable {

    @FXML
    private ComboBox<String> cmbCliente;
    @FXML
    private ComboBox<String> cmbEmpleado;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private ComboBox<LocalTime> cmbHora;
    @FXML
    private ListView<String> lvServicios;
    @FXML
    private Button btnGuardar;

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
        // 1. Cargar datos de prueba (Reemplazar con DAO en producción)
        cmbCliente.getItems().addAll("Juan Pérez", "María López", "Carlos Ruiz");
        cmbEmpleado.getItems().addAll("Ana García", "Pedro Gómez");

        // 2. Llenar ComboBox de Horas (Cada 30 minutos, de 08:00 a 19:30)
        LocalTime hora = LocalTime.of(8, 0);
        while (hora.isBefore(LocalTime.of(20, 0))) {
            cmbHora.getItems().add(hora);
            hora = hora.plusMinutes(30);
        }

        // 3. Cargar servicios (Reemplazar con DAO en producción)
        lvServicios.getItems().addAll("Corte Hombre", "Tintura", "Alisado", "Peinado");
        lvServicios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // 4. Establecer la fecha predeterminada (hoy)
        dpFecha.setValue(LocalDate.now());

        // 5. Configurar listener para deshabilitar botón si faltan campos
        // Implementar un listener más robusto aquí...
    }

    @FXML
    private void handleSave() {
        // ⚠️ VALIDACIONES: Asegurar que todos los campos requeridos estén seleccionados.

        if (cmbCliente.getValue() == null || cmbEmpleado.getValue() == null ||
                dpFecha.getValue() == null || cmbHora.getValue() == null ||
                lvServicios.getSelectionModel().isEmpty()) {

            // Mostrar una alerta de error (TODO: implementar AlertaUtil)
            System.err.println("Debe completar todos los campos.");
            return;
        }

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