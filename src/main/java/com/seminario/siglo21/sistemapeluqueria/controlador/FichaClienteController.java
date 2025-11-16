package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.modelo.Cliente; // (Necesitas el modelo Cliente)
import com.seminario.siglo21.sistemapeluqueria.modelo.TurnoHistorialDTO;
import com.seminario.siglo21.sistemapeluqueria.persistencia.TurnoDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class FichaClienteController {

    @FXML private Label lblNombreCliente;
    @FXML private Label lblDetallesCliente; // (Necesitarás cargar el DNI/Teléfono)
    @FXML private TableView<TurnoHistorialDTO> tvHistorial;
    @FXML private TableColumn<TurnoHistorialDTO, LocalDate> colFecha;
    @FXML private TableColumn<TurnoHistorialDTO, String> colEstilista;
    @FXML private TableColumn<TurnoHistorialDTO, String> colServicios;
    @FXML private TableColumn<TurnoHistorialDTO, String> colEstado;
    @FXML private TableColumn<TurnoHistorialDTO, String> colObservaciones;

    private TurnoDAO turnoDAO = new TurnoDAO();

    @FXML
    public void initialize() {
        // Configurar las columnas de la tabla para que coincidan con el DTO
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEstilista.setCellValueFactory(new PropertyValueFactory<>("estilista"));
        colServicios.setCellValueFactory(new PropertyValueFactory<>("serviciosRealizados"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
    }

    // Método de entrada para poblar la vista
    public void initData(Cliente cliente) {
        if (cliente == null) return;

        // 1. Cargar detalles del cliente
        lblNombreCliente.setText("Ficha de Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
        // (Aquí deberías cargar DNI y Teléfono si los tienes en el objeto Cliente)
        lblDetallesCliente.setText("DNI: " + cliente.getDni() + " - Tel: " +  cliente.getTelefono());

        // 2. Cargar el historial desde el DAO
        try {
            List<TurnoHistorialDTO> historial = turnoDAO.getHistorialPorCliente(cliente.getIdCliente());
            tvHistorial.setItems(FXCollections.observableArrayList(historial));
        } catch (SQLException e) {
            e.printStackTrace();
            // Mostrar alerta al usuario
        }
    }
}