package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.App;
import com.seminario.siglo21.sistemapeluqueria.modelo.Cliente;
import com.seminario.siglo21.sistemapeluqueria.modelo.ClienteEmail;
import com.seminario.siglo21.sistemapeluqueria.modelo.ClienteTelefono;
import com.seminario.siglo21.sistemapeluqueria.modelo.Direccion;
import com.seminario.siglo21.sistemapeluqueria.modelo.Email;
import com.seminario.siglo21.sistemapeluqueria.modelo.Telefono;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void Guardar(ActionEvent event) {

        // Valido datos personales
        Cliente c = new Cliente();

        if (validarDatosPersonales()) {
            c.setNombre(this.txtNombreCliente.getText());
            c.setApellido(this.txtApellidoCliente.getText());
            c.setDni(Integer.parseInt(this.txtDniCliente.getText()));
        } else {
            return;
        }

        // Valido datos de dirección
        Direccion d = new Direccion();

        if (validarDireccion()) {
            d.setCalle(this.txtCalleCliente.getText());
            d.setNumero(Integer.parseInt(this.txtNumeroCliente.getText()));
            d.setPiso(Integer.parseInt(this.txtPisoCliente.getText()));
            d.setCiudad(this.txtCiudadCliente.getText());
            d.setProvincia(this.txtProvinciaCliente.getText());
            d.setPais(this.txtPaisCliente.getText());
            d.setCodigoPostal(Integer.parseInt(txtCpCliente.getText()));
        } else {
            return;
        }

        // Valido telefono
        Telefono t = new Telefono();

        if (validarTelefono()) {
            t.setTelefono(Integer.parseInt(this.txtTelefonoCliente.getText()));
        } else {
            return;
        }

        // Valido Email
        Email e = new Email();
        if (validarEmail()) {
            e.setEmail(this.txtEmailCliente.getText());
        } else {
            return;
        }
        /*
        // Guardo la direccion del cliente
        d.GuardarDirecionNueva();

        // Guardo el cliente
        c.GuardarNuevoCliente(d.getIdDireccion());

        // Guardo el telefono nuevo
        t.GuardarTelefonoNuevo();

        // Guardo email nuevo
        e.GuardarEmailNuevo();

        // Conecto el telefono y el mail con el Cliente
        ClienteTelefono clienteTelefono = new ClienteTelefono(c.getIdCliente(), t.getIdTelefono());
        clienteTelefono.conectarClienteTelefono();

        ClienteEmail clienteEmail = new ClienteEmail(c.getIdCliente(), e.getIdEmail());
        clienteEmail.conectaClienteEmail();*/
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
        if (!this.txtPisoCliente.getText().isEmpty()) {
            try {
                int piso = Integer.parseInt(this.txtPisoCliente.getText());
            } catch (NumberFormatException e) {
                this.lblMensaje.setText("El piso en la dirección debe ser un número.");
                return false;
            }
        }
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
