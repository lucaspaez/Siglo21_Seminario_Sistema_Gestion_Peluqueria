package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.modelo.MarcaProducto;
import com.seminario.siglo21.sistemapeluqueria.modelo.Producto;
import com.seminario.siglo21.sistemapeluqueria.modelo.Proveedor;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DialogoProductoController implements Initializable {

    @FXML
    public ComboBox<String> cmbMarca;
    @FXML
    public ComboBox<String> cmbProveedor;
    @FXML
    private Button Cancelar;
    @FXML
    private Button btnGuardar;
    @FXML
    private Label lblMensaje;
    @FXML
    private TextField txtDescripcionProducto;
    @FXML
    private TextField txtNombreProducto;
    @FXML
    private TextField txtPrecioCosto;
    @FXML
    private TextField txtPrecioVenta;
    @FXML
    private TextField txtSku;

    private ObservableList<String> listaProveedores;
    private ObservableList<String> listaMarcas;


    private Producto producto = new Producto();

    private int idProductoEditar;

    public int getIdProductoEditar() {
        return idProductoEditar;
    }

    public void setIdProductoEditar(int idProductoEditar) {
        this.idProductoEditar = idProductoEditar;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listaProveedores = FXCollections.observableArrayList();
        listaMarcas = FXCollections.observableArrayList();

        cargarProveedores();
        cargarMarcas();
    }

    private void cargarMarcas() {
        List<MarcaProducto> datos = MarcaProducto.listarMarcaProducto();

        List<String> nombres = datos.stream()
                .map(MarcaProducto::getNombreMarca)
                .collect(Collectors.toList());
        listaMarcas.setAll(nombres);
        cmbMarca.setItems(listaMarcas);

    }

    private void cargarProveedores() {
        List<Proveedor> datos = Proveedor.listarProveedores();

        List<String> nombres = datos.stream()
                .map(Proveedor::getRazonSocial)
                .collect(Collectors.toList());
        listaProveedores.setAll(nombres);
        cmbProveedor.setItems(listaProveedores);
    }

    @FXML
    void Cancelar(ActionEvent event) {

        // Sale de la ventana modal
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void Guardar(ActionEvent event) {

        // Valido campos de los datos del Producto
        if (validarCamposProducto()) {
            producto.setSkuProducto(this.txtSku.getText());
            producto.setNombreProducto(this.txtNombreProducto.getText());
            producto.setDescripcionProducto(this.txtDescripcionProducto.getText());
            try {
                producto.setPrecioCostoProducto(Double.parseDouble(txtPrecioCosto.getText()));
            } catch (NumberFormatException e) {
                lblMensaje.setText("El precio de costo debe ser un número.");
                return;
            }
            try {
                producto.setPrecioVentaProducto(Double.parseDouble(txtPrecioVenta.getText()));
            } catch (NumberFormatException e) {
                lblMensaje.setText("El precio de venta debe ser un número.");
                return;
            }
        } else {
            return;
        }

        // Valido campo de la Marca
        if (cmbMarca.getValue() != null) {
            producto.setMarcaProducto(this.cmbMarca.getValue());
        } else {
            VistaUtil.mostrarAlerta(
                    "error",
                    "Debe Seleccionar la marca del producto, si no existe la marca buscada debe crearla antes " +
                            "de crear el producto."
            );
            return;
        }

        // Valido campo del Proveedor
        if (cmbProveedor.getValue() != null) {
            producto.setProveedorProducto(this.cmbProveedor.getValue());
        } else {
            VistaUtil.mostrarAlerta(
                    "error",
                    "Debe Seleccionar el proveedor del producto, si no existe el proveedor buscado debe crearlo " +
                            "antes de crear el producto."
            );
            return;
        }

        if (producto.getIdProducto() != 0) { // Debo actualizar un cliente

            if (producto.actualizarProducto())
                VistaUtil.mostrarAlerta("info",
                    "El producto se actualizó correctamente, refresque la tabla para ver los cambios.");

        }else{ // Debo Agregar un producto nuevo

            if (producto.agregarProductoNuevo());
                VistaUtil.mostrarAlerta("info",
                    "El producto se creó correctamente, refresque la tabla para ver los cambios.");

        }
        // Sale de la ventana modal
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    private boolean validarCamposProducto() {

        if (this.txtNombreProducto.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar el nombre del producto.");
            return false;
        }
        if (this.txtDescripcionProducto.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar la descripcion del producto.");
            return false;
        }
        if (this.txtPrecioCosto.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar la precio de costo del producto.");
            return false;
        }
        if (this.txtPrecioVenta.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar la precio de venta del producto.");
            return false;
        }

        return true;
    }

    public void muestraProductoEnLosCampos() {

        producto.cargarProducto(idProductoEditar);

        this.txtSku.setText(producto.getSkuProducto());
        this.txtNombreProducto.setText(producto.getNombreProducto());
        this.txtDescripcionProducto.setText(producto.getDescripcionProducto());
        this.txtPrecioCosto.setText(String.valueOf(producto.getPrecioCostoProducto()));
        this.txtPrecioVenta.setText(String.valueOf(producto.getPrecioVentaProducto()));
        cargarMarcas();
        cargarProveedores();
        this.cmbMarca.setValue(producto.getMarcaProducto());
        this.cmbProveedor.setValue(producto.getProveedorProducto());
    }
}
