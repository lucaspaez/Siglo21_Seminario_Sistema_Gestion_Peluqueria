package com.seminario.siglo21.sistemapeluqueria.controlador;

import java.io.IOException;

// Esta interfaz se usa para el cambio entre vistas
public interface CambiarVista {
    
    // Método a implementar
    void setRoot(String fxml) throws IOException;
    
}
