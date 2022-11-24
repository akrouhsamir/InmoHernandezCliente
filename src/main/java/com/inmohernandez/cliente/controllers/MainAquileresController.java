package com.inmohernandez.cliente.controllers;

import com.inmohernandez.cliente.dao.AlquilerDAO;
import com.inmohernandez.cliente.models.Alquiler;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @FXML
    private Label report;
    private SimpleStringProperty search,fechaDesde, fechaHasta;

    private List<Alquiler> lastAlquileresRefresh = FXCollections.observableArrayList();

    public void initController(Stage myStage, MainInmueblesController mainController, int idInmueble){
        this.myStage = myStage;
        this.mainController = mainController;
        this.idInmueble = idInmueble;

        search = new SimpleStringProperty();
        fechaDesde = new SimpleStringProperty();
        fechaHasta = new SimpleStringProperty();

        search.bind(tf_search.textProperty());
        fechaDesde.bind(date_desde.valueProperty().asString());
        fechaHasta.bind(date_hasta.valueProperty().asString());

        initAlquileresTableView();
        updateTableViewAlquileres();
        eventoDobleClickTV();
        initFiltroFecha();
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
            System.out.println(alquileres);
            tv_alquileres.getItems().addAll(alquileres);
            lastAlquileresRefresh = alquileres;
        } catch (IOException e) {
            showReport("Error al conectar con el servidor",5);
        }
    }

    private ObservableList<Alquiler> getAlquileresFromDAO() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"idInmueble\" : " + idInmueble + ", ");
        sb.append("\"fecha_desde\" : \"" + (fechaDesde.get().equals("null") ? "1970-01-01": fechaDesde.get())+ "\", ");
        sb.append("\"fecha_hasta\" : \"" + (fechaHasta.get().equals("null") ? "2299-01-01": fechaHasta.get())+ "\"");
        sb.append("}");
        return AlquilerDAO.getAlquileresFromDB(sb.toString());
    }

    public void showReport(String msg,int dur){
        report.setText(msg);
        PauseTransition pause = new PauseTransition(Duration.seconds(dur));
        pause.setOnFinished(event ->
                report.setText(""));
        pause.play();
    }

    @FXML
    public void keyTypedSearch(){
        String b = search.get().trim().toLowerCase();
        String cliente,id;

        tv_alquileres.getItems().clear();
        for(Alquiler a : lastAlquileresRefresh){
            id = String.valueOf(a.getId()).trim().toLowerCase();
            cliente = a.getCliente().trim().toLowerCase();
            if(id.startsWith(b) || cliente.startsWith(b)){
                tv_alquileres.getItems().add(a);
            }
        }
    }

    private void eventoDobleClickTV(){
        tv_alquileres.setRowFactory(tv -> {
            TableRow<Alquiler> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount()==2 && (!row.isEmpty())){
                    tf_seleccionado.setText(row.getItem().getId()+"");
                }
            });
            return row;
        });
    }

    public void initFiltroFecha() {
        final String fechaPattern = "([1-9]|[0-2][0-9]|3[0-1])/(0[1-9]|1[0-2])/(\\d{4})";

        date_desde.getEditor().textProperty().addListener(new ChangeListener<String>() {
            Date bef, aft;
            String hasta;

            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches(fechaPattern)) {
                    if(!newValue.isBlank()){
                        showReport("Requerido (dd/MM/YYYY)", 2);
                    }
                    Platform.runLater(() -> {
                        date_desde.getEditor().clear();
                    });
                } else {
                    hasta = date_hasta.getEditor().getText();
                    if (!hasta.isBlank()) {
                        try {
                            aft = new SimpleDateFormat("dd/MM/yyyy").parse(hasta);
                            bef = new SimpleDateFormat("dd/MM/yyyy").parse(newValue);
                            if (aft.before(bef)) {
                                Platform.runLater(() -> {
                                    date_desde.getEditor().setText(hasta);
                                });
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        date_hasta.getEditor().textProperty().addListener(new ChangeListener<String>() {
            Date bef, aft;
            String desde;

            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches(fechaPattern)) {
                    if(!newValue.isBlank()){
                        showReport("Requerido (dd/MM/YYYY)", 2);
                    }
                    Platform.runLater(() -> {
                        date_hasta.getEditor().clear();
                    });
                } else {
                    desde = date_desde.getEditor().getText();
                    if (!desde.isBlank()) {
                        try {
                            bef = new SimpleDateFormat("dd/MM/yyyy").parse(desde);
                            aft = new SimpleDateFormat("dd/MM/yyyy").parse(newValue);
                            if (aft.before(bef)) {
                                Platform.runLater(() -> {
                                    date_hasta.getEditor().setText(desde);
                                });
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    @FXML
    public void clickLimpiarFiltros(){
        date_desde.setValue(null);
        date_hasta.setValue(null);
        updateTableViewAlquileres();
    }

    @FXML
    public void clickAplicarFiltros(){
        updateTableViewAlquileres();
    }
}
