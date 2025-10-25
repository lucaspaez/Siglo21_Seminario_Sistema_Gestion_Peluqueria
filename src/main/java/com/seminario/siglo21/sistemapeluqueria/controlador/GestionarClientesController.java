package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.modelo.Cliente;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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

public class GestionarClientesController implements Initializable {

    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colDni;
    @FXML
    private TableColumn<?, ?> colDireccion;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnRefrescar;
    @FXML
    private TableColumn<?, ?> colApellido;
    @FXML
    private TableColumn<?, ?> colTelefono;
    @FXML
    private TableColumn<?, ?> colEmail;

    private ObservableList<Cliente> listaClientes;

    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TextField txtFiltroNombre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        listaClientes = FXCollections.observableArrayList();

        colId.setCellValueFactory(new PropertyValueFactory("idCliente"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory("apellido"));
        colDni.setCellValueFactory(new PropertyValueFactory("dni"));
        colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));

        cargarClientes();

    }

    private void cargarClientes() {
        List<Cliente> datos = Cliente.listarClientes();
        listaClientes.setAll(datos);
        tablaClientes.setItems(listaClientes);
    }

    @FXML
    private void agregarCliente(ActionEvent event) throws IOException {

        VistaUtil.mostrarVentanaModal(
                "/com/seminario/siglo21/sistemapeluqueria/DialogoCliente.fxml",
                "Gestión de clientes"
        );
    }

    @FXML
    private void refrescarTabla(ActionEvent event) {
        cargarClientes();
    }

    @FXML
    private void editarCliente(ActionEvent event) throws IOException {

        // Obtengo el id del cliente que quiero eliminar
        Cliente c = this.tablaClientes.getSelectionModel().getSelectedItem();

        //Verificio que efectivamente se haya seleccionado un cliente
        if (c == null) {
            VistaUtil.mostrarAlerta("info",
                    "Debe seleccionar un cliente para editar!");
        } else {

            DialogoClienteController controlador = VistaUtil.abrirVentanaYObtenerControlador(
                    "/com/seminario/siglo21/sistemapeluqueria/DialogoCliente.fxml",
                    "Gestión de clientes - Editar Cliente",
                    ctrl -> {
                        ctrl.setIdClienteEditar(c.getIdCliente());
                        ctrl.muestraClienteEnLosCampos();
                    }
            );
        }

    }

    @FXML
    private void eliminarCliente(ActionEvent event) {

        // Obtengo el id del cliente que quiero eliminar
        Cliente c = this.tablaClientes.getSelectionModel().getSelectedItem();

        //Verificio que efectivamente se haya seleccionado un cliente
        if (c == null) {
            VistaUtil.mostrarAlerta("info",
                    "Debe seleccionar un cliente para eliminar!");
        } else {
            if (c.desactivaCliente()) {
                VistaUtil.mostrarAlerta("info",
                        "El Cliente quedó inacivo, esto quiere decir que no se verá "
                        + "en ninguna lista, pero si en los servicios "
                        + "históricos que haya realizado, como turnos, "
                        + "compras de producos, etc.");
            }
            cargarClientes();
        }
    }

}
