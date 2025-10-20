package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.modelo.Cliente;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    private CambiarVista cambiarVista;

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

        // Cago el archivo FXML
        FXMLLoader loaderCli = new FXMLLoader(
                getClass().getClassLoader().getResource("com/seminario/siglo21/sistemapeluqueria/DialogoCliente.fxml")
        );

        Parent rootCli = loaderCli.load();

        // Creo una nueva ventana (Stage) y le asigno la vista
        Stage nuevaStage = new Stage();
        nuevaStage.setTitle("Gestión de clientes");

        Scene sceneCli = new Scene(rootCli);

        nuevaStage.setScene(sceneCli);
        nuevaStage.initModality(Modality.APPLICATION_MODAL);

        nuevaStage.show();

    }

}
