package com.seminario.siglo21.sistemapeluqueria;

import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        VistaUtil.cambiarVista(stage, "/com/seminario/siglo21/sistemapeluqueria/Login.fxml",
                "Login");
    }
    
    public static Stage getPrimaryStage(){
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }

}