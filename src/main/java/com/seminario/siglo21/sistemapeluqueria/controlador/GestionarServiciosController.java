package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.App;
import com.seminario.siglo21.sistemapeluqueria.modelo.Producto;
import com.seminario.siglo21.sistemapeluqueria.modelo.Proveedor;
import com.seminario.siglo21.sistemapeluqueria.modelo.ServicioInterno;
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

public class GestionarServiciosController implements Initializable {

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
    private TableColumn<?, ?> colDescripcionServicio;
    @FXML
    private TableColumn<?, ?> colDuracionServicio;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colNombreServicio;
    @FXML
    private TableColumn<?, ?> colPrecioServicio;
    @FXML
    private TableView<ServicioInterno> tablaServicios;
    @FXML
    private TextField txtFiltroServicios;

    private ObservableList<ServicioInterno> listaServicios;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listaServicios = FXCollections.observableArrayList();

        colId.setCellValueFactory(new PropertyValueFactory("idServicioInterno"));
        colNombreServicio.setCellValueFactory(new PropertyValueFactory("nombreServicio"));
        colDescripcionServicio.setCellValueFactory(new PropertyValueFactory("descripcion"));
        colDuracionServicio.setCellValueFactory(new PropertyValueFactory("duracionHoras"));
        colPrecioServicio.setCellValueFactory(new PropertyValueFactory("precio"));

        cargarServicios();

    }

    private void cargarServicios() {

        List<ServicioInterno> datos = ServicioInterno.listarServicios();
        listaServicios.setAll(datos);
        this.tablaServicios.setItems(listaServicios);

    }

    @FXML
    void agregarServicio(ActionEvent event) throws IOException {
        VistaUtil.mostrarVentanaModal(
                "/com/seminario/siglo21/sistemapeluqueria/DialogoServicio.fxml",
                "Gestión de Servicios"
        );
    }

    @FXML
    void editarServicio(ActionEvent event) throws IOException {

        // Obtengo el id del servicio que quiero editar
        ServicioInterno s = (ServicioInterno) this.tablaServicios.getSelectionModel().getSelectedItem();

        //Verificio que efectivamente se haya seleccionado un servicio
        if (s == null) {
            VistaUtil.mostrarAlerta("info",
                    "Debe seleccionar un Servicio para editar!");
        } else {

            DialogoServicioController controlador = VistaUtil.abrirVentanaYObtenerControlador(
                    "/com/seminario/siglo21/sistemapeluqueria/DialogoServicio.fxml",
                    "Gestión de Servicios - Editar Servicio",
                    ctrl -> {
                        ctrl.setIdServicioEditar(s.getIdServicioInterno());
                        ctrl.muestraServicioEnCampos();
                    }
            );
        }

    }

    @FXML
    void eliminarServicio(ActionEvent event) {
        // Obtengo el id del producto que quiero eliminar
        ServicioInterno s = (ServicioInterno) this.tablaServicios.getSelectionModel().getSelectedItem();

        //Verificio que efectivamente se haya seleccionado un servicio
        if (s == null) {
            VistaUtil.mostrarAlerta("info",
                    "Debe seleccionar un Servicio para eliminar!");
        } else {
            if (s.eliminarServicio()) {
                VistaUtil.mostrarAlerta("info",
                        "El Servicio se elimimió correctamente!");
            }
            cargarServicios();
        }
    }

    @FXML
    void refrescarTabla(ActionEvent event) {
        cargarServicios();
    }

    @FXML
    void volverAtras(ActionEvent event) throws IOException {
        // Sale al menú principal
        VistaUtil.cambiarVista(App.getPrimaryStage(),
                "/com/seminario/siglo21/sistemapeluqueria/MenuPrincipal.fxml",
                "Sistema de Gestión - Principal");
    }
}
