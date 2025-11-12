package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.modelo.CategoriaServicio;
import com.seminario.siglo21.sistemapeluqueria.modelo.MarcaProducto;
import com.seminario.siglo21.sistemapeluqueria.modelo.ServicioInterno;
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

public class DialogoServicioController implements Initializable {

    public ComboBox<String> cmbCategoriaServicio;
    @FXML
    private Button Cancelar;
    @FXML
    private Button btnGuardar;
    @FXML
    private Label lblMensaje;
    @FXML
    private TextField txtDescripcionServicio;
    @FXML
    private TextField txtDuracionHoras;
    @FXML
    private TextField txtNombreServicio;
    @FXML
    private TextField txtPrecio;

    private ServicioInterno servicio = new ServicioInterno();
    private int idServicioEditar;

    private ObservableList<String> listaCategorias;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaCategorias = FXCollections.observableArrayList();
        cargarCartegorias();
    }

    private void cargarCartegorias() {
        List<CategoriaServicio> datos = CategoriaServicio.listarCategoriasServicios();

        List<String> nombres = datos.stream()
                .map(CategoriaServicio::getNombreCategoria)
                .collect(Collectors.toList());
        listaCategorias.setAll(nombres);
        cmbCategoriaServicio.setItems(listaCategorias);
    }

    public int getIdServicioEditar() {
        return idServicioEditar;
    }

    public void setIdServicioEditar(int idServicioEditar) {
        this.idServicioEditar = idServicioEditar;
    }

    @FXML
    void Cancelar(ActionEvent event) {
        // Sale de la ventana modal
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void Guardar(ActionEvent event) {

        // Valido campos de los datos del Servicio
        if (validarCamposServicios()) {
            servicio.setNombreServicio(this.txtNombreServicio.getText());
            servicio.setDescripcion(this.txtDescripcionServicio.getText());
            try {
                servicio.setPrecio(Double.parseDouble(txtPrecio.getText()));
            } catch (NumberFormatException e) {
                lblMensaje.setText("El precio debe ser un número, use el punto como separador decimal.");
                return;
            }
            try {
                servicio.setDuracionHoras(Integer.parseInt(txtDuracionHoras.getText()));
            } catch (NumberFormatException e) {
                lblMensaje.setText("La ducracion debe ser un número entero de horas.");
                return;
            }
        } else {
            return;
        }

        // Valido campo de la Categoria
        if (cmbCategoriaServicio.getValue() != null) {
            servicio.setCategoria(this.cmbCategoriaServicio.getValue());
        } else {
            VistaUtil.mostrarAlerta(
                    "error",
                    "Debe Seleccionar la categoria del producto, si no existe la categoria buscada debe crearla antes " +
                            "de crear el producto."
            );
            return;
        }

        if (servicio.getIdServicioInterno() != 0) { // Debo actualizar un servicio

            if (servicio.actualizarServicio())
                VistaUtil.mostrarAlerta("info",
                        "El Servicio se actualizó correctamente, refresque la tabla para ver los cambios.");

        }else{ // Debo Agregar un servicio nuevo

            if (servicio.agregarServicio());
            VistaUtil.mostrarAlerta("info",
                    "El Servicio se creó correctamente, refresque la tabla para ver los cambios.");

        }

        // Sale de la ventana modal
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    private boolean validarCamposServicios() {
        if (this.txtNombreServicio.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar el nombre del servicio.");
            return false;
        }
        if (this.txtDescripcionServicio.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar una descripción para el servicio.");
            return false;
        }
        if (this.txtDuracionHoras.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar el numero de horas que tarda en completar el servicio.");
            return false;
        }
        if (this.txtPrecio.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar el precio del servicio.");
            return false;
        }
        return true;
    }

    public void muestraServicioEnCampos() {
        servicio.cargarServicio(this.getIdServicioEditar());

        this.txtNombreServicio.setText(servicio.getNombreServicio());
        this.txtDescripcionServicio.setText(servicio.getDescripcion());
        this.txtDuracionHoras.setText(String.valueOf(servicio.getDuracionHoras()));
        this.txtPrecio.setText(String.valueOf(servicio.getPrecio()));

        cargarCartegorias();
        this.cmbCategoriaServicio.setValue(servicio.getCategoria());

    }
}

