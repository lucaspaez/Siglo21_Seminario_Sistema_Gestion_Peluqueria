package com.seminario.siglo21.sistemapeluqueria.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DialogoClienteController implements Initializable {

    @FXML
    private TextField txtNombreCliente;
    @FXML
    private TextField txtApellidoCliente;
    @FXML
    private TextField txtDniCliente;
    @FXML
    private TextField txtCalleCliente;
    @FXML
    private TextField txtNumeroCliente;
    @FXML
    private TextField txtPisoCliente;
    @FXML
    private TextField txtCiudadCliente;
    @FXML
    private TextField txtProvinciaCliente;
    @FXML
    private TextField txtPaisCliente;
    @FXML
    private TextField txtCpCliente;
    @FXML
    private TextField txtTelefonoCliente;
    @FXML
    private TextField txtEmailCliente;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button Cancelar;
    @FXML
    private Label txtMensaje;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void Guardar(ActionEvent event) {
    }

    @FXML
    private void Cancelar(ActionEvent event) {
    }
    
}
