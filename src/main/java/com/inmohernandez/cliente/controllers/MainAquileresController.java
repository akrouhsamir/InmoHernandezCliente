package com.inmohernandez.cliente.controllers;

import com.inmohernandez.cliente.MainApp;
import com.inmohernandez.cliente.dao.AlquilerDAO;
import com.inmohernandez.cliente.models.Alquiler;
import com.inmohernandez.cliente.models.Inmueble;
import com.inmohernandez.cliente.utils.Utils;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    private String msgFromMoldAlquiler;

    @FXML
    private Label report;

    private Executor exec;
    private SimpleStringProperty search,fechaDesde, fechaHasta, seleccionado;

    private List<Alquiler> lastAlquileresRefresh = FXCollections.observableArrayList();

    public void setMsgFromMoldAlquiler(String msgFromMoldAlquiler) {
        this.msgFromMoldAlquiler = msgFromMoldAlquiler;
    }


    public void initController(Stage myStage, MainInmueblesController mainController, int idInmueble){
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });
        this.myStage = myStage;
        this.mainController = mainController;
        this.idInmueble = idInmueble;

        search = new SimpleStringProperty();
        fechaDesde = new SimpleStringProperty();
        fechaHasta = new SimpleStringProperty();
        seleccionado = new SimpleStringProperty();

        search.bind(tf_search.textProperty());
        fechaDesde.bind(date_desde.getEditor().textProperty());
        fechaHasta.bind(date_hasta.getEditor().textProperty());
        seleccionado.bind(tf_seleccionado.textProperty());

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

        colId.setMaxWidth(60);
        colCliente.setMinWidth(140);
        colFechaInicio.setMinWidth(130);
        colFechaFin.setMinWidth(130);
        colMensualidad.setMinWidth(140);

        colId.setCellValueFactory(new PropertyValueFactory<Alquiler,Long>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<Alquiler,String>("cliente"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<Alquiler,String>("fechaInicio"));
        colFechaFin.setCellValueFactory(new PropertyValueFactory<Alquiler,String>("fechaFin"));
        colMensualidad.setCellValueFactory(new PropertyValueFactory<Alquiler,Float>("mensualidad"));
        colMensualidad.setCellFactory(tc -> new TableCell<Inmueble, Float>() {
            @Override
            protected void updateItem(Float mensualidad, boolean empty) {
                super.updateItem(mensualidad, empty);
                if (empty) {
                    setText(null);
                } else {
                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                    setText(formatter.format(mensualidad)+" €");
                }
            }
        });
        tv_alquileres.getColumns().addAll(colId,colCliente,colFechaInicio,colFechaFin,colMensualidad);
    }

    private void updateTableViewAlquileres(){
        tv_alquileres.getItems().clear();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"idInmueble\" : " + idInmueble + ", ");
        sb.append("\"fecha_desde\" : \"" + (fechaDesde.get().isBlank() ? "1970-01-01": Utils.strToSQLDate(fechaDesde.get())) + "\", ");
        sb.append("\"fecha_hasta\" : \"" + (fechaHasta.get().isBlank() ? "2299-01-01": Utils.strToSQLDate(fechaHasta.get()))+ "\"");
        sb.append("}");

        Task<ObservableList<Alquiler>> task = new Task<ObservableList<Alquiler>>() {
            @Override
            protected ObservableList<Alquiler> call() throws Exception {
                return AlquilerDAO.getAlquileresFromDB(sb.toString());
            }
        };

        task.setOnSucceeded(e->{
            tv_alquileres.getItems().addAll(task.getValue());
            lastAlquileresRefresh = task.getValue();
        });

        task.setOnFailed(e->{
            showReport("Error al conectar con el servidor",5);
        });

        exec.execute(task);
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
        final String fechaPattern = "([1-9]|[0-2][0-9]|3[0-1])/(0[1-9]|1[0-2]|[1-9])/(\\d{4})";

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

    @FXML
    public void crearAlquiler(){
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("mold-alquiler-view.fxml"));
        MoldAlquilerController moldController;
        stage.setTitle("Crear inmueble");
        stage.getIcons().add(new Image(MainApp.class.getResource("imgs/rhinoapp.png").toExternalForm()));
        stage.setResizable(false);
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        moldController = fxmlLoader.getController();
        moldController.initController("Crear",null,stage,this,String.valueOf(idInmueble));
        stage.showAndWait();
        showReport(msgFromMoldAlquiler,2);
        updateTableViewAlquileres();
    }

    @FXML
    public void editarAlquiler(){
        Task<Alquiler> task = new Task<Alquiler>() {
            @Override
            protected Alquiler call() throws Exception {
                return AlquilerDAO.getAlquilerByIdFromDB(seleccionado.get());
            }
        };

        task.setOnSucceeded(ee->{
            Stage stage;
            FXMLLoader fxmlLoader;
            MoldAlquilerController moldController;
            Alquiler alquiler;
            alquiler = task.getValue();
            if(alquiler == null){
                showReport("Alquiler no encontrado.",2);
            }else{
                stage = new Stage();
                stage.getIcons().add(new Image(MainApp.class.getResource("imgs/rhinoapp.png").toExternalForm()));
                fxmlLoader = new FXMLLoader(MainApp.class.getResource("mold-alquiler-view.fxml"));
                stage.setTitle("Editar alquiler");
                stage.setResizable(false);
                try {
                    stage.setScene(new Scene(fxmlLoader.load()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.initModality(Modality.APPLICATION_MODAL);
                moldController = fxmlLoader.getController();
                moldController.initController("Editar",alquiler,stage,this,String.valueOf(idInmueble));
                stage.showAndWait();
                showReport(msgFromMoldAlquiler,2);
                updateTableViewAlquileres();
            }
        });


        if(seleccionado.get().isBlank()){
            showReport("Selecciona un alquiler.",2);
        }else{
            exec.execute(task);
        }
    }

    @FXML
    public void borrarInmueble(){
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return AlquilerDAO.removeAlquilerByIdInDB(seleccionado.get());
            }
        };

        task.setOnSucceeded(e->{
            if(task.getValue()){
                showReport("Alquiler eliminado correctamente.",2);
                updateTableViewAlquileres();
                tf_seleccionado.clear();
            }else{
                showReport("Fallo al eliminar alquiler.",2);
            }
        });

        if(seleccionado.get().isBlank()){
            showReport("Selecciona un alquiler.",2);
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás" +
                    " seguro del borrado del alquiler con id "+seleccionado.get()+ "?",
                    ButtonType.YES,ButtonType.NO);
            alert.setHeaderText(null);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES){
                exec.execute(task);
            }
        }
    }


}
