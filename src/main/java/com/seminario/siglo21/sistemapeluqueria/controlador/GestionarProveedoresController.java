package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.App;
import com.seminario.siglo21.sistemapeluqueria.modelo.Cliente;
import com.seminario.siglo21.sistemapeluqueria.modelo.Proveedor;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GestionarProveedoresController implements Initializable {

    //Campos de texto
    @FXML
    public TextField txtFiltroRazonSocial;

    // Tabla y columnas
    @FXML
    public TableView tablaProveedores;
    @FXML
    public TableColumn colRazonSocial;
    @FXML
    public TableColumn colCuit;
    @FXML
    private TableColumn<?, ?> colDireccion;
    @FXML
    private TableColumn<?, ?> colEmail;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colTelefono;

    //Botones
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnRefrescar;
    @FXML
    private Button btnVolver;

    // Atributos
    private ObservableList<Proveedor> listaProveedores;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listaProveedores = FXCollections.observableArrayList();

        colId.setCellValueFactory(new PropertyValueFactory("idProveedor"));
        colRazonSocial.setCellValueFactory(new PropertyValueFactory("razonSocial"));
        colCuit.setCellValueFactory(new PropertyValueFactory("Cuit"));
        colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));

        cargarProveedores();

    }

    private void cargarProveedores() {
        List<Proveedor> datos = Proveedor.listarProveedores();
        listaProveedores.setAll(datos);
        tablaProveedores.setItems(listaProveedores);
    }

    @FXML
    void agregarProveedor(ActionEvent event) {

    }

    @FXML
    void editarProveedor(ActionEvent event) {

    }

    @FXML
    void eliminarProveedor(ActionEvent event) {

    }

    @FXML
    void refrescarTabla(ActionEvent event) {
        cargarProveedores();
    }

    @FXML
    void volverAtras(ActionEvent event) throws IOException {

        // Sale al login
        VistaUtil.cambiarVista(App.getPrimaryStage(),
                "/com/seminario/siglo21/sistemapeluqueria/MenuPrincipal.fxml",
                "Sistema de Gestión - Principal");

    }


}
