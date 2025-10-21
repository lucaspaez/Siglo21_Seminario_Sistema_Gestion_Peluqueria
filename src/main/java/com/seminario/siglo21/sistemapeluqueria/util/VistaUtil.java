package com.seminario.siglo21.sistemapeluqueria.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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
    public static <T> T abrirVentanaYObtenerControlador(String rutaFXML, String titulo, Class<T> tipoControlador) throws IOException {
        FXMLLoader loader = new FXMLLoader(VistaUtil.class.getResource(rutaFXML));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();

        return loader.getController();
    }

}
