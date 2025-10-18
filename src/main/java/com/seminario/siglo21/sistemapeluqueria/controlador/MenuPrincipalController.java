package com.seminario.siglo21.sistemapeluqueria.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class MenuPrincipalController implements Initializable {

    @FXML
    private Button btnClientes;
    @FXML
    private Button btnTurnos;
    @FXML
    private Button btnProductos;
    @FXML
    private Button btnProveedores;
    @FXML
    private Button btnVentas;
    @FXML
    private Button btnReportes;
    
    private CambiarVista cambiarVista;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void gestionarCliente(ActionEvent event) throws IOException {
        cambiarVista.setRoot("GestionarClientes");
    }
    
    // Método que cambia la vista actual
    public void setCambiarVista(CambiarVista cambiarVista) {
        this.cambiarVista = cambiarVista;
    }
}
