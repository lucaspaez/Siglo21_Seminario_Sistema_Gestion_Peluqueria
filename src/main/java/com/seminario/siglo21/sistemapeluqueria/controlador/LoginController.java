package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.App;
import com.seminario.siglo21.sistemapeluqueria.modelo.Cuenta;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasenia;

    private CambiarVista cambiarVista;

    private Connection conexion;
    @FXML
    private Label lblMensaje;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void btnIngresar(ActionEvent event) {

        String usuario = txtUsuario.getText().trim();
        String pass = txtContrasenia.getText().trim();
        try {
            if (usuario.isEmpty() || pass.isEmpty()) {
                lblMensaje.setText("Ingrese usuario y contraseña.");
                return;
            }

            boolean ok = Cuenta.validarCredenciales(usuario, pass);
            if (ok) {
                cambiarVista.setRoot("MenuPrincipal");
            } else {
                lblMensaje.setText("Credenciales inválidas.");
            }
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setTitle("Error");
            a.setContentText(e.getMessage());
            a.showAndWait();

        }

    }

    // Método que cambia la vista actual
    public void setCambiarVista(CambiarVista cambiarVista) {
        this.cambiarVista = cambiarVista;
    }

}
