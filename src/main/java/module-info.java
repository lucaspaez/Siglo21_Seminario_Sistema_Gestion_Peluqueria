module com.seminario.siglo21.sistemapeluqueria {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql; // Para el API de JDBC
    requires mysql.connector.j; // Módulo del conector de MySQL
    requires javafx.graphics;
    requires javafx.base;

    opens com.seminario.siglo21.sistemapeluqueria to javafx.fxml;
    opens com.seminario.siglo21.sistemapeluqueria.controlador to javafx.fxml;
    
    exports com.seminario.siglo21.sistemapeluqueria;
    exports com.seminario.siglo21.sistemapeluqueria.controlador;
    exports com.seminario.siglo21.sistemapeluqueria.persistencia;
    exports com.seminario.siglo21.sistemapeluqueria.modelo;
    exports com.seminario.siglo21.sistemapeluqueria.util;
    opens com.seminario.siglo21.sistemapeluqueria.modelo to javafx.fxml;
}
