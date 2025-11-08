package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.modelo.*;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DialogoProveedorController {
    public TextField txtRazonSocial;
    public TextField txtCuit;

    public Label lblMensaje;
    public Button btnGuardar;
    public Button Cancelar;

    public TextField txtEmailProveedor;
    public TextField txtTelefonoProveedor;
    public TextField txtCpProveedor;
    public TextField txtPaisProveedor;
    public TextField txtProvinciaProveedor;
    public TextField txtCiudadProveedor;
    public TextField txtPisoProveedor;
    public TextField txtNumeroProveedor;
    public TextField txtCalleProveedor;

    Proveedor p = new Proveedor();
    Email e = new Email();
    Telefono t = new Telefono();
    Direccion d = new Direccion();

    private int idProveedorEditar;

    public int getIdProveedorEditar() {
        return idProveedorEditar;
    }

    public void setIdProveedorEditar(int idProveedorEditar) {
        this.idProveedorEditar = idProveedorEditar;
    }

    public void Guardar(ActionEvent actionEvent) {

        // Valido datos personales
        if (validarDatosProveedor()) {
            p.setRazonSocial(this.txtRazonSocial.getText());
            p.setCuit(this.txtCuit.getText());
        } else {
            return;
        }

        // Valido datos de dirección
        if (VistaUtil.validarCamposDireccion(this.txtCalleProveedor, this.txtNumeroProveedor,
                this.txtPisoProveedor, this.txtCiudadProveedor, this.txtProvinciaProveedor,
                this.txtPaisProveedor, this.txtCpProveedor, this.lblMensaje)) {
            d.setCalle(this.txtCalleProveedor.getText());
            d.setNumero(Integer.parseInt(this.txtNumeroProveedor.getText()));

            // Piso opcional
            if (!this.txtPisoProveedor.getText().isEmpty()) {
                d.setPiso(this.txtPisoProveedor.getText());
            } else {
                d.setPiso("");
            }

            d.setCiudad(this.txtCiudadProveedor.getText());
            d.setProvincia(this.txtProvinciaProveedor.getText());
            d.setPais(this.txtPaisProveedor.getText());

            // Código postal opcional
            if (!this.txtCpProveedor.getText().isEmpty()) {
                d.setCodigoPostal(Integer.parseInt(this.txtCpProveedor.getText()));
            } else {
                d.setCodigoPostal(0);
            }
        } else {
            return;
        }

        // Valído telefono
        if (VistaUtil.validarCampoTelefono(this.txtTelefonoProveedor, this.lblMensaje)) {
            t.setTelefono(Integer.parseInt(this.txtTelefonoProveedor.getText()));
        } else {
            return;
        }

        // Valído Email
        if (VistaUtil.validarCampoEmail(this.txtEmailProveedor, this.lblMensaje)) {
            e.setEmail(this.txtEmailProveedor.getText());
        } else {
            return;
        }

        if (p.getIdProveedor() != 0) { // Debo actualizar un cliente

            if (p.actualizarProveedor() && d.actualizarDireccion() && t.actualizarTelefono() && e.actualizarEmail()){

                VistaUtil.mostrarAlerta("info",
                        "El proveedor se actualizó correctamente, refresque la tabla para ver los cambios.");

            }


        } else { // Debo ingresar un proveedor nuevo

            // Guardo la direccion
            d.GuardarDirecionNueva();
            //System.out.println("ID de direccion: " + d.getIdDireccion());

            // Guardo el proveedor
            p.GuardarNuevoProveedor(d.getIdDireccion());
            //System.out.println("ID de Proveedor es: " + p.getIdProveedor());

            // Guardo el telefono nuevo
            t.GuardarTelefonoNuevo();

            // Guardo email nuevo
            e.GuardarEmailNuevo();

            // Conecto el telefono y el mail con el Proveedor
            //System.out.println("Id a pasar: " + p.getIdProveedor());
            ProveedorTelefono proveedorTelefono = new ProveedorTelefono(p.getIdProveedor(), t.getIdTelefono());
            proveedorTelefono.conectarProveedorTelefono();

            ProveedorEmail proveedorEmail = new ProveedorEmail(p.getIdProveedor(), e.getIdEmail());
            proveedorEmail.conectaProveedorEmail();

            VistaUtil.mostrarAlerta("info",
                    "El nuevo cliente se guardo correctamente, refresque la tabla para visualizarlo.");

        }

        // Sale de la ventana modal
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        
    }

    private boolean validarDatosProveedor() {

        if (this.txtRazonSocial.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar la Razón Social.");
            return false;
        }
        if (this.txtCuit.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar el Cuit.");
            return false;
        }
        return true;
    }

    public void Cancelar(ActionEvent actionEvent) {
        // Sale de la ventana modal
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void muestraProveedorEnLosCampos() {


        // Cargo el cliente y obtengo el ID de la Dirección
        int idDireccion = p.cargarProveedor(this.getIdProveedorEditar());

        // Cargo lo caplos en la vista
        this.txtRazonSocial.setText(p.getRazonSocial());
        this.txtCuit.setText(p.getCuit());

        // Obtengo la dirección
        d.cargarDireccion(idDireccion);

        // Cargo los compos de la dirección
        this.txtCalleProveedor.setText(d.getCalle());
        this.txtNumeroProveedor.setText(d.getNumero() + "");
        this.txtPisoProveedor.setText(d.getPiso() + "");
        this.txtCiudadProveedor.setText(d.getCiudad());
        this.txtProvinciaProveedor.setText(d.getProvincia());
        this.txtPaisProveedor.setText(d.getPais());
        this.txtCpProveedor.setText(d.getCodigoPostal() + "");

        // Obtengo el teléfono
        t.cargarTelefono(p.getIdProveedor());

        // cargo el campo del teléfono
        this.txtTelefonoProveedor.setText(t.getTelefono() + "");

        // Obtengo el Email
        ProveedorEmail pmail = new ProveedorEmail();
        pmail.cargarEmail(p.getIdProveedor(), this.e);

        // Cargo el campo del Email
        this.txtEmailProveedor.setText(e.getEmail());
    }
}
