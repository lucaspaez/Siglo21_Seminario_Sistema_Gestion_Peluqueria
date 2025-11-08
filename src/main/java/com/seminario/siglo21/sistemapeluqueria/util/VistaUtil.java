package com.seminario.siglo21.sistemapeluqueria.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import javafx.scene.control.Alert;

public class VistaUtil {

    // Abre una nueva ventana modal con recibiendo la vista FXML.
    public static void mostrarVentanaModal(String rutaFXML, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(VistaUtil.class.getResource(rutaFXML));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    // Cambia la vista principal (por ejemplo, para cambiar de escena en el
    // mismo Stage)
    public static void cambiarVista(Stage stage, String rutaFXML, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(VistaUtil.class.getResource(rutaFXML));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setTitle(titulo);
        stage.setScene(scene);
        stage.show();
    }

    // La misma funcion que el método mostrarVentanaModal pero este obtiene
    // el controladore de la vista
    public static <T> T abrirVentanaYObtenerControlador(String rutaFXML, String titulo, Consumer<T> inicializador) throws IOException {
        FXMLLoader loader = new FXMLLoader(VistaUtil.class.getResource(rutaFXML));
        Parent root = loader.load();

        // Obtengo el controlador del FXML
        T controller = loader.getController();

        // Si se pasa un inicializador, lo aplico antes de mostrar la ventana
        if (inicializador != null) {
            inicializador.accept(controller);
        }

        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();

        return controller;
    }

    public static void mostrarAlerta(String tipo, String mensaje) {

        switch (tipo) {
            case "info":
                Alert i = new Alert(Alert.AlertType.INFORMATION);
                i.setHeaderText(null);
                i.setTitle("Info");
                i.setContentText(mensaje);
                i.showAndWait();
                break;
            case "error":
                Alert e = new Alert(Alert.AlertType.ERROR);
                e.setHeaderText(null);
                e.setTitle("Error");
                e.setContentText(mensaje);
                e.showAndWait();
                break;
        }

    }

    public static boolean validarCamposDireccion(TextField calle, TextField numero, TextField piso,
                                           TextField ciudad, TextField provincia, TextField pais,
                                           TextField codigoPostal, Label lblMensaje) {

        // CALLE
        if (calle.getText().isEmpty()) {
            lblMensaje.setText("Debe ingresar una calle en la dirección.");
            return false;
        }
        // NUMERO
        if (numero.getText().isEmpty()) {
            lblMensaje.setText("Debe ingresar un número en la dirección.");
            return false;
        } else {
            try {
                int n = Integer.parseInt(numero.getText());
            } catch (NumberFormatException e) {
                lblMensaje.setText("El numero en la dirección debe ser un número.");
                return false;
            }
        }

        // CIUDAD
        if (ciudad.getText().isEmpty()) {
            lblMensaje.setText("Debe ingresar la ciudad en la dirección.");
            return false;
        }
        // PROVINCIA
        if (provincia.getText().isEmpty()) {
            lblMensaje.setText("Debe ingresar la provincia en la dirección.");
            return false;
        }
        // PAIS
        if (pais.getText().isEmpty()) {
            lblMensaje.setText("Debe ingresar el país en la dirección.");
            return false;
        }
        // CÓDIGO POSTAL (opcional)
        if (!codigoPostal.getText().isEmpty()) {
            try {
                int cp = Integer.parseInt(codigoPostal.getText());
            } catch (NumberFormatException e) {
                lblMensaje.setText("El codigo postal en la dirección debe ser un número.");
                return false;
            }
        }
        return true;

    }

    public static boolean validarCampoTelefono(TextField txtTelefono, Label lblMensaje) {

        if (txtTelefono.getText().isEmpty()) {
            lblMensaje.setText("Debe ingresar un número de teléfono.");
            return false;
        } else {
            try {
                int tel = Integer.parseInt(txtTelefono.getText());
            } catch (NumberFormatException e) {
                lblMensaje.setText("El numero de teléfono debe ser un número.");
                return false;
            }
        }
        return true;
    }

    public static boolean validarCampoEmail(TextField txtEmail, Label lblMensaje) {

        if (txtEmail.getText().isEmpty()) {
            lblMensaje.setText("Debe ingresar un email.");
            return false;
        } else {
            String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9+_.-]+\\.[A-Za-z]{2,}$";
            if (!Pattern.matches(regex, txtEmail.getText())) {
                lblMensaje.setText("Debe ingresar un email válido.");
                return false;
            }
        }
        return true;
    }

}
