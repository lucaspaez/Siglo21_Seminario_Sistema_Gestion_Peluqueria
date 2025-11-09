package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.App;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
    @FXML
    private Button btnCerrarSesion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void gestionarCliente(ActionEvent event) throws IOException {
        // Obtengo el stage actual
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        //Cambio la vista
        VistaUtil.cambiarVista(App.getPrimaryStage(), "/com/seminario/siglo21/sistemapeluqueria/GestionarClientes.fxml",
                "Sistema de Gestión - Gestionar Clientes");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) throws IOException {
        
        // Sale al login
        VistaUtil.cambiarVista(App.getPrimaryStage(),
                "/com/seminario/siglo21/sistemapeluqueria/Login.fxml",
                "Login");
    }

    @FXML
    public void gestionarProveedores(ActionEvent actionEvent) throws IOException {

        // Obtengo el stage actual
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        //Cambio la vista
        VistaUtil.cambiarVista(App.getPrimaryStage(), "/com/seminario/siglo21/sistemapeluqueria/GestionarProveedores.fxml",
                "Sistema de Gestión - Gestionar Proveedores");

    }

    public void gestionarProducto(ActionEvent actionEvent) throws IOException {

        // Obtengo el stage actual
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        //Cambio la vista
        VistaUtil.cambiarVista(App.getPrimaryStage(), "/com/seminario/siglo21/sistemapeluqueria/GestionarProductos.fxml",
                "Sistema de Gestión - Gestionar Productos");

    }
}
