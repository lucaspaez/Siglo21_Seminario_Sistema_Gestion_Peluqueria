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
    void agregarProveedor(ActionEvent event) throws IOException {
        VistaUtil.mostrarVentanaModal(
                "/com/seminario/siglo21/sistemapeluqueria/DialogoProveedor.fxml",
                "Gestión de Proveedores"
        );
    }

    @FXML
    void editarProveedor(ActionEvent event) throws IOException {

        // Obtengo el id del proveedor que quiero editar
        Proveedor p = (Proveedor) this.tablaProveedores.getSelectionModel().getSelectedItem();

        //Verificio que efectivamente se haya seleccionado un proveedor
        if (p == null) {
            VistaUtil.mostrarAlerta("info",
                    "Debe seleccionar un proveedor para editar!");
        } else {

            //System.out.println("ID Proveedor a editar: " + p.getIdProveedor());

            DialogoProveedorController controlador = VistaUtil.abrirVentanaYObtenerControlador(
                    "/com/seminario/siglo21/sistemapeluqueria/DialogoProveedor.fxml",
                    "Gestión de Proveedores - Editar Proveedor",
                    ctrl -> {
                        ctrl.setIdProveedorEditar(p.getIdProveedor());
                        ctrl.muestraProveedorEnLosCampos();
                    }
            );
        }

    }

    @FXML
    void eliminarProveedor(ActionEvent event) {

        // Obtengo el id del poveedor que quiero eliminar
        Proveedor p = (Proveedor) this.tablaProveedores.getSelectionModel().getSelectedItem();

        //Verificio que efectivamente se haya seleccionado un proveedor
        if (p == null) {
            VistaUtil.mostrarAlerta("info",
                    "Debe seleccionar un proveedor para eliminar!");
        } else {
            if (p.eliminarProveedor()) {
                VistaUtil.mostrarAlerta("info",
                        "El proveedor se elimimió correctamente!");
            }
            cargarProveedores();
        }

    }

    @FXML
    void refrescarTabla(ActionEvent event) {
        cargarProveedores();
    }

    @FXML
    void volverAtras(ActionEvent event) throws IOException {

        // Sale al menú principal
        VistaUtil.cambiarVista(App.getPrimaryStage(),
                "/com/seminario/siglo21/sistemapeluqueria/MenuPrincipal.fxml",
                "Sistema de Gestión - Principal");

    }


}
