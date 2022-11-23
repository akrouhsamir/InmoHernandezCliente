package com.inmohernandez.cliente.controllers;

import com.inmohernandez.cliente.dao.AlquilerDAO;
import com.inmohernandez.cliente.models.Alquiler;
import com.inmohernandez.cliente.models.Inmueble;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainAquileresController {
    @FXML
    private TableView tv_alquileres;

    @FXML
    private TextField tf_search, tf_seleccionado;

    @FXML
    private DatePicker date_desde, date_hasta;

    private Stage myStage;

    private MainInmueblesController mainController;

    private int idInmueble;

    private List<Alquiler> lastAlquileresRefresh = FXCollections.observableArrayList();

    public void initController(Stage myStage, MainInmueblesController mainController, int idInmueble){
        this.myStage = myStage;
        this.mainController = mainController;
        this.idInmueble = idInmueble;

        initAlquileresTableView();
        updateTableViewAlquileres();
    }

    public void initAlquileresTableView(){
        TableColumn colId, colCliente, colFechaInicio, colFechaFin, colMensualidad;

        colId = new TableColumn("Id");
        colCliente = new TableColumn("Cliente");
        colFechaInicio = new TableColumn("Fecha inicio");
        colFechaFin = new TableColumn("Fecha fin");
        colMensualidad = new TableColumn("Mensualidad");

        colId.setCellValueFactory(new PropertyValueFactory<Alquiler,Long>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<Alquiler,String>("cliente"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<Alquiler,String>("fechaInicio"));
        colFechaFin.setCellValueFactory(new PropertyValueFactory<Alquiler,String>("fechaFin"));
        colMensualidad.setCellValueFactory(new PropertyValueFactory<Alquiler,Float>("mensualidad"));

        tv_alquileres.getColumns().addAll(colId,colCliente,colFechaInicio,colFechaFin,colMensualidad);
    }

    private void updateTableViewAlquileres(){
        ObservableList<Alquiler> alquileres;

        tv_alquileres.getItems().clear();

        try {
            alquileres = getAlquileresFromDAO();
            tv_alquileres.getItems().addAll(alquileres);
            lastAlquileresRefresh = alquileres;
        } catch (IOException e) {
            //showReport("Error al conectar con el servidor",5);
        }
    }

    private ObservableList<Alquiler> getAlquileresFromDAO() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"idInmueble\" : " + idInmueble);
        sb.append("}");
        return AlquilerDAO.getAlquileresFromDB(sb.toString());
    }


}
