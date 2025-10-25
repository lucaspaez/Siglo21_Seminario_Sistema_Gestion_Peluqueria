package com.seminario.siglo21.sistemapeluqueria.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;
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

}
