package com.seminario.siglo21.sistemapeluqueria;

import com.seminario.siglo21.sistemapeluqueria.controlador.CambiarVista;
import com.seminario.siglo21.sistemapeluqueria.controlador.LoginController;
import com.seminario.siglo21.sistemapeluqueria.controlador.MenuPrincipalController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application implements CambiarVista{

private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        setRoot("Login"); // La primera llamada a setRoot()
        stage.show(); // Después de configurar la primera escena, la mostramos.
    }

    @Override
    public void setRoot(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent newRoot = fxmlLoader.load();

        // Si el controlador necesita la interfaz, se la pasas aquí
        Object controller = fxmlLoader.getController();
        if (controller instanceof LoginController) {
            ((LoginController) controller).setCambiarVista(this);
        }
        
        // Se le pasa la interfaz al controlador de MenuPrincipal
        if (controller instanceof MenuPrincipalController) {
            ((MenuPrincipalController) controller).setCambiarVista(this);
        }

        // LÓGICA CORREGIDA AQUÍ
        if (stage.getScene() == null) {
            // Si no hay escena, la creamos y se la asignamos
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);
        } else {
            // Si ya existe una escena, simplemente cambiamos su root
            stage.getScene().setRoot(newRoot);
            // setea el tamaño de la ventana automaticamente en lo que se define
            // por la vista
            stage.sizeToScene();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}