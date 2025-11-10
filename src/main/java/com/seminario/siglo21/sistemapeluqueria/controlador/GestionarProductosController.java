package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.App;
import com.seminario.siglo21.sistemapeluqueria.modelo.Producto;
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

public class GestionarProductosController implements Initializable {

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
    @FXML
    private TableColumn<?, ?> colDescripcionProducto;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colMarca;
    @FXML
    private TableColumn<?, ?> colNombreProducto;
    @FXML
    private TableColumn<?, ?> colPrecioCosto;
    @FXML
    private TableColumn<?, ?> colPrecioVenta;
    @FXML
    private TableColumn<?, ?> colProveedor;
    @FXML
    private TableColumn<?, ?> colSkuProducto;
    @FXML
    private TableView<Producto> tablaProductos;
    @FXML
    private TextField txtFiltroProducto;

    private ObservableList<Producto> listaProductos;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listaProductos = FXCollections.observableArrayList();

        colId.setCellValueFactory(new PropertyValueFactory("idProducto"));
        colSkuProducto.setCellValueFactory(new PropertyValueFactory("skuProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory("nombreProducto"));
        colDescripcionProducto.setCellValueFactory(new PropertyValueFactory("descripcionProducto"));
        colPrecioCosto.setCellValueFactory(new PropertyValueFactory("precioCostoProducto"));
        colPrecioVenta.setCellValueFactory(new PropertyValueFactory("precioVentaProducto"));
        colMarca.setCellValueFactory(new PropertyValueFactory("marcaProducto"));
        colProveedor.setCellValueFactory(new PropertyValueFactory("proveedorProducto"));

        cargarProductos();

    }

    @FXML
    void agregarProducto(ActionEvent event) throws IOException {

        VistaUtil.mostrarVentanaModal(
                "/com/seminario/siglo21/sistemapeluqueria/DialogoProducto.fxml",
                "Gestión de Productos"
        );

    }

    @FXML
    void editarProducto(ActionEvent event) throws IOException {

        // Obtengo el id del producto que quiero editar
        Producto p = (Producto) this.tablaProductos.getSelectionModel().getSelectedItem();

        //Verificio que efectivamente se haya seleccionado un producto
        if (p == null) {
            VistaUtil.mostrarAlerta("info",
                    "Debe seleccionar un producto para editar!");
        } else {

            DialogoProductoController controlador = VistaUtil.abrirVentanaYObtenerControlador(
                    "/com/seminario/siglo21/sistemapeluqueria/DialogoProducto.fxml",
                    "Gestión de Productos - Editar Producto",
                    ctrl -> {
                        ctrl.setIdProductoEditar(p.getIdProducto());
                        ctrl.muestraProductoEnLosCampos();
                    }
            );
        }

    }

    @FXML
    void eliminarProducto(ActionEvent event) {

        // Obtengo el id del producto que quiero eliminar
        Producto p = (Producto) this.tablaProductos.getSelectionModel().getSelectedItem();

        //Verificio que efectivamente se haya seleccionado un proveedor
        if (p == null) {
            VistaUtil.mostrarAlerta("info",
                    "Debe seleccionar un producto para eliminar!");
        } else {
            if (p.eliminarProducto()) {
                VistaUtil.mostrarAlerta("info",
                        "El producto se elimimió correctamente!");
            }
            cargarProductos();
        }
    }

    @FXML
    void refrescarTabla(ActionEvent event) {

        cargarProductos();
    }

    private void cargarProductos() {

        List<Producto> datos = Producto.listarProductos();
        listaProductos.setAll(datos);
        tablaProductos.setItems(listaProductos);

    }

    @FXML
    void volverAtras(ActionEvent event) throws IOException {

        // Sale al menú principal
        VistaUtil.cambiarVista(App.getPrimaryStage(),
                "/com/seminario/siglo21/sistemapeluqueria/MenuPrincipal.fxml",
                "Sistema de Gestión - Principal");

    }

}
