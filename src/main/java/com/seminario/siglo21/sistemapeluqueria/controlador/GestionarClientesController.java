package com.seminario.siglo21.sistemapeluqueria.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GestionarClientesController implements Initializable {

    @FXML
    private TableView<?> tablaClientes;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colDni;
    @FXML
    private TableColumn<?, ?> colDireccion;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDni;
    @FXML
    private TextField txtDireccion;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnRefrescar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
