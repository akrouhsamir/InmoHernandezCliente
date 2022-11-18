package com.inmohernandez.cliente.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class MoldInmuebleController {
    @FXML
    private TextField tf_titulo,tf_precio,tf_ubicacion;
    @FXML
    private Spinner spinner_habitaciones,spinner_bannos;


    @FXML
    private Button btn_mod,btn_cancelar;

    @FXML
    private ComboBox cbox_zona;

    @FXML
    public void initialize(){
        initTableviewInmuebles();
    }

    public void initTableviewInmuebles(){

    }
}
