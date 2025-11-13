package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.modelo.CategoriaServicio;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DialogoCategoriaServicioController {

    @FXML
    private Button Cancelar;
    @FXML
    private Button btnGuardar;
    @FXML
    private Label lblMensaje;
    @FXML
    private TextField txtDescripcionCategoria;
    @FXML
    private TextField txtNombreCategoria;

    private int idCategoriaEditar;
    private CategoriaServicio categoriaServicio = new CategoriaServicio();

    public int getIdCategoriaEditar() {
        return idCategoriaEditar;
    }

    public void setIdCategoriaEditar(int idCategoriaEditar) {
        this.idCategoriaEditar = idCategoriaEditar;
    }

    @FXML
    void Cancelar(ActionEvent event) {
        // Sale de la ventana modal
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    void Guardar(ActionEvent event) {

        if (this.txtNombreCategoria.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar el nombre de la categoría.");
            return;
        }else {
            categoriaServicio.setNombreCategoria(this.txtNombreCategoria.getText());
        }

        if (this.txtDescripcionCategoria.getText().isEmpty()) {
            this.lblMensaje.setText("Debe ingresar una descripción para la categoría.");
            return;
        }else{
            categoriaServicio.setDescripcionCategoria(this.txtDescripcionCategoria.getText());
        }

        if (categoriaServicio.getIdCategoria() != 0) { // Debo actualizar un servicio

            if (categoriaServicio.actualizarCategoria())
                VistaUtil.mostrarAlerta("info",
                        "La categoría se actualizó correctamente. Vuelva a ingresar para ver los cambios.");

        }else{ // Debo Agregar un servicio nuevo

            if (categoriaServicio.creaCategoria());
            VistaUtil.mostrarAlerta("info",
                    "La categoría se creó correctamente. Vuelva a ingresar para ver los cambios.");

        }

        // Sale de la ventana modal
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();



    }

    public void muestraCategoriaEnCampos() {

        categoriaServicio.setearCategoriaId(idCategoriaEditar);

        this.txtNombreCategoria.setText(categoriaServicio.getNombreCategoria());
        this.txtDescripcionCategoria.setText(categoriaServicio.getDescripcionCategoria());
    }
}
