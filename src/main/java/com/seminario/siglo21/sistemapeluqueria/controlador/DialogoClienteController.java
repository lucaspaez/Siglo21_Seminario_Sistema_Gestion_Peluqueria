package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.modelo.*;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DialogoClienteController implements Initializable {

    @FXML
    private TextField txtNombreCliente;
    @FXML
    private TextField txtApellidoCliente;
    @FXML
    private TextField txtDniCliente;
    @FXML
    private TextField txtCalleCliente;
    @FXML
    private TextField txtNumeroCliente;
    @FXML
    private TextField txtPisoCliente;
    @FXML
    private TextField txtCiudadCliente;
    @FXML
    private TextField txtProvinciaCliente;
    @FXML
    private TextField txtPaisCliente;
    @FXML
    private TextField txtCpCliente;
    @FXML
    private TextField txtTelefonoCliente;
    @FXML
    private TextField txtEmailCliente;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button Cancelar;
    @FXML
    private Label lblMensaje;

    int idClienteEditar;

    Cliente c = new Cliente();
    Email e = new Email();
    Telefono t = new Telefono();
    Direccion d = new Direccion();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void muestraClienteEnLosCampos() {

        // Cargo el cliente y obtengo el ID de la Dirección
        int idDireccion = c.cargarCliente(this.getIdClienteEditar());

        // Cargo lo caplos en la vista
        this.txtNombreCliente.setText(c.getNombre());
        this.txtApellidoCliente.setText(c.getApellido());
        this.txtDniCliente.setText(c.getDni() + "");

        // Obtengo la dirección
        d.cargarDireccion(idDireccion);

        // Cargo los compos de la dirección
        this.txtCalleCliente.setText(d.getCalle());
        this.txtNumeroCliente.setText(d.getNumero() + "");
        this.txtPisoCliente.setText(d.getPiso() + "");
        this.txtCiudadCliente.setText(d.getCiudad());
        this.txtProvinciaCliente.setText(d.getProvincia());
        this.txtPaisCliente.setText(d.getPais());
        this.txtCpCliente.setText(d.getCodigoPostal() + "");

        // Obtengo el teléfono
        t.cargarTelefono(c.getIdCliente());

        // cargo el campo del teléfono
        this.txtTelefonoCliente.setText(t.getTelefono() + "");

        // Obtengo el Email
        ClienteEmail cmail = new ClienteEmail();
        cmail.cargarEmail(c.getIdCliente(), this.e);

        // Cargo el campo del Email
        this.txtEmailCliente.setText(e.getEmail());

    }

    public int getIdClienteEditar() {
        return idClienteEditar;
    }

    public void setIdClienteEditar(int idClienteEditar) {
        this.idClienteEditar = idClienteEditar;
    }

    @FXML
    private void Guardar(ActionEvent event) {

        // Valido datos personales
        //Cliente c = new Cliente();

        if (validarDatosPersonales()) {
            c.setNombre(this.txtNombreCliente.getText());
            c.setApellido(this.txtApellidoCliente.getText());
            c.setDni(Integer.parseInt(this.txtDniCliente.getText()));
        } else {
            return;
        }

        // Valido datos de dirección
        //Direccion d = new Direccion();

        if (validarDireccion()) {
            d.setCalle(this.txtCalleCliente.getText());
            d.setNumero(Integer.parseInt(this.txtNumeroCliente.getText()));

            // Piso opcional
            if (!this.txtPisoCliente.getText().isEmpty()) {
                d.setPiso(this.txtPisoCliente.getText());
            } else {
                d.setPiso("");
            }

            d.setCiudad(this.txtCiudadCliente.getText());
            d.setProvincia(this.txtProvinciaCliente.getText());
            d.setPais(this.txtPaisCliente.getText());

            // Código postal opcional
            if (!this.txtCpCliente.getText().isEmpty()) {
                d.setCodigoPostal(Integer.parseInt(this.txtCpCliente.getText()));
            } else {
                d.setCodigoPostal(0);
            }
        } else {
            return;
        }

        // Valido telefono
        //Telefono t = new Telefono();

        if (validarTelefono()) {
            t.setTelefono(Integer.parseInt(this.txtTelefonoCliente.getText()));
        } else {
            return;
        }

        // Valido Email
        //Email e = new Email();
        if (validarEmail()) {
            e.setEmail(this.txtEmailCliente.getText());
        } else {
            return;
        }

        if (c.getIdCliente() != 0) { // Debo actualizar un cliente
            
            if (c.actualizarCliente() && d.actualizarDireccion() && t.actualizarTelefono() && e.actualizarEmail()){
            
                VistaUtil.mostrarAlerta("info",
                    "El cliente se actualizo correctamente, refresque la tabla para ver los cambios.");
                
            }
            

        } else { // Debo ingresar un cliente nuevo

            // Guardo la direccion del cliente
            d.GuardarDirecionNueva();
            //System.out.println("ID de direccion: " + d.getIdDireccion());

            // Guardo el cliente
            c.GuardarNuevoCliente(d.getIdDireccion());
            //System.out.println("ID de cliente es: " + c.getIdCliente());

            // Guardo el telefono nuevo
            t.GuardarTelefonoNuevo();

            // Guardo email nuevo
            e.GuardarEmailNuevo();

            // Conecto el telefono y el mail con el Cliente
            ClienteTelefono clienteTelefono = new ClienteTelefono(c.getIdCliente(), t.getIdTelefono());
            clienteTelefono.conectarClienteTelefono();

            ClienteEmail clienteEmail = new ClienteEmail(c.getIdCliente(), e.getIdEmail());
            clienteEmail.conectaClienteEmail();

            VistaUtil.mostrarAlerta("info",
                    "El nuevo cliente se guardo correctamente, refresque la tabla para visualizarlo.");

        }

        // Sale de la ventana modal
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void Cancelar(ActionEvent event) {

        // Sale de la ventana modal
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    private boolean validarDatosPersonales() {

        if (this.txtNombreCliente.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar un nombre.");
            return false;
        }
        if (this.txtApellidoCliente.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar un Apellido.");
            return false;
        }
        if (this.txtDniCliente.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar un DNI.");
            return false;
        } else {
            try {
                int aux = Integer.parseInt(this.txtDniCliente.getText());
            } catch (NumberFormatException e) {
                this.lblMensaje.setText("El DNI debe ser numérico!");
                return false;
            }
        }
        return true;
    }

    private boolean validarDireccion() {

        // CALLE
        if (this.txtCalleCliente.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar una calle en la dirección.");
            return false;
        }
        // NUMERO
        if (this.txtNumeroCliente.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar un número en la dirección.");
            return false;
        } else {
            try {
                int numero = Integer.parseInt(this.txtNumeroCliente.getText());
            } catch (NumberFormatException e) {
                this.lblMensaje.setText("El numero en la dirección debe ser un número.");
                return false;
            }
        }
        // PISO (opcional)

        // CIUDAD
        if (this.txtCiudadCliente.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar la ciudad en la dirección.");
            return false;
        }
        // PROVINCIA
        if (this.txtProvinciaCliente.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar la provincia en la dirección.");
            return false;
        }
        // PAIS
        if (this.txtPaisCliente.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar el país en la dirección.");
            return false;
        }
        // CÓDIGO POSTAL (opcional)
        if (!this.txtCpCliente.getText().isEmpty()) {
            try {
                int cp = Integer.parseInt(this.txtCpCliente.getText());
            } catch (NumberFormatException e) {
                this.lblMensaje.setText("El codigo postal en la dirección debe ser un número.");
                return false;
            }
        }
        return true;

    }

    private boolean validarTelefono() {

        // TELÉFONO
        if (this.txtTelefonoCliente.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar un número de teléfono.");
            return false;
        } else {
            try {
                int tel = Integer.parseInt(this.txtTelefonoCliente.getText());
            } catch (NumberFormatException e) {
                this.lblMensaje.setText("El numero de teléfono debe ser un número.");
                return false;
            }
        }
        return true;

    }

    private boolean validarEmail() {

        if (this.txtEmailCliente.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar un email.");
            return false;
        } else {
            String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9+_.-]+\\.[A-Za-z]{2,}$";
            if (!Pattern.matches(regex, this.txtEmailCliente.getText())) {
                this.lblMensaje.setText("Debe ingresar un email válido.");
                return false;
            }
        }
        return true;
    }

}
