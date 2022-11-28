package com.inmohernandez.cliente.controllers;

import com.inmohernandez.cliente.dao.AlquilerDAO;
import com.inmohernandez.cliente.models.Alquiler;
import com.inmohernandez.cliente.utils.Utils;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MoldAlquilerController {
    @FXML
    private TextField tf_cliente, tf_mensualidad;

    @FXML
    private DatePicker date_inicio, date_fin;

    @FXML
    private Button btn_mod;

    @FXML
    private Label report;

    private Alquiler alquiler;

    private Stage myStage;

    private MainAquileresController mainController;

    private SimpleStringProperty cliente, inicio, fin, mensualidad;

    private String idInmueble;

    private Executor exec;

    public void initController(String mode, Alquiler alquiler, Stage myStage, MainAquileresController mainController,String idInmueble){
        this.alquiler = alquiler;
        this.myStage = myStage;
        this.mainController = mainController;
        this.idInmueble = idInmueble;

        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });

        if(mode.equals("Crear")){
            btn_mod.setText("Crear");
        }else{
            btn_mod.setText("Editar");
        }

        cliente = new SimpleStringProperty();
        inicio = new SimpleStringProperty();
        fin = new SimpleStringProperty();
        mensualidad = new SimpleStringProperty();

        cliente.bind(tf_cliente.textProperty());
        inicio.bind(date_inicio.getEditor().textProperty());
        fin.bind(date_fin.getEditor().textProperty());
        mensualidad.bind(tf_mensualidad.textProperty());

        initMensualidad();
        initFechaAlquiler();

        if(alquiler != null){
            tf_cliente.setText(alquiler.getCliente());
            tf_mensualidad.setText(String.valueOf(alquiler.getMensualidad()));
            date_inicio.getEditor().setText(Utils.sqlDateToEUDate(alquiler.getFechaInicio()));
            date_fin.getEditor().setText(Utils.sqlDateToEUDate(alquiler.getFechaFin()));
        }
    }

    public void initMensualidad(){
        tf_mensualidad.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*\\.?\\d*")) {
                    tf_mensualidad.setText(oldValue);
                }
            }
        });
    }

    public void initFechaAlquiler(){
        final String fechaPattern = "([1-9]|[0-2][0-9]|3[0-1])/(0[1-9]|1[0-2]|[1-9])/(\\d{4})";

        date_inicio.getEditor().textProperty().addListener(new ChangeListener<String>() {
            Date bef, aft;
            String hasta;

            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches(fechaPattern)) {
                    if(!newValue.isBlank()){
                        showReport("Requerido (dd/MM/YYYY)", 2);
                    }
                    Platform.runLater(() -> {
                        date_inicio.getEditor().clear();
                    });
                } else {
                    hasta = date_fin.getEditor().getText();
                    if (!hasta.isBlank()) {
                        try {
                            aft = new SimpleDateFormat("dd/MM/yyyy").parse(hasta);
                            bef = new SimpleDateFormat("dd/MM/yyyy").parse(newValue);
                            if (aft.before(bef)) {
                                Platform.runLater(() -> {
                                    date_inicio.getEditor().setText(hasta);
                                });
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        date_fin.getEditor().textProperty().addListener(new ChangeListener<String>() {
            Date bef, aft;
            String desde;

            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches(fechaPattern)) {
                    if(!newValue.isBlank()){
                        showReport("Requerido (dd/MM/YYYY)", 2);
                    }
                    Platform.runLater(() -> {
                        date_fin.getEditor().clear();
                    });
                } else {
                    desde = date_inicio.getEditor().getText();
                    if (!desde.isBlank()) {
                        try {
                            bef = new SimpleDateFormat("dd/MM/yyyy").parse(desde);
                            aft = new SimpleDateFormat("dd/MM/yyyy").parse(newValue);
                            if (aft.before(bef)) {
                                Platform.runLater(() -> {
                                    date_fin.getEditor().setText(desde);
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

    public void showReport(String msg,int dur){
        report.setText(msg);
        PauseTransition pause = new PauseTransition(Duration.seconds(dur));
        pause.setOnFinished(event ->
                report.setText(""));
        pause.play();
    }

    public boolean comprobarFormulario(){
        boolean correcto = true;
        String report = "";
        if(cliente.get().isBlank()){
            correcto = false;
            report = "Completa el cliente.";
        }

        if(inicio.get().isBlank()){
            correcto = false;
            report = "Indica el comienzo del alquiler";
        }

        if(fin.get().isBlank()){
            correcto = false;
            report = "Indica el fin del alquiler";
        }

        if(mensualidad.get().isBlank()){
            correcto = false;
            report = "Indica la mensualidad";
        }

        showReport(report,3);
        return correcto;
    }

    @FXML
    public void cancelar(){
        myStage.close();
    }

    @FXML
    public void modAlquiler(){
        StringBuilder sb = new StringBuilder();
        if (comprobarFormulario()) {
            sb.append("{");
            sb.append("\"cliente\" : \"" + cliente.get() + "\", ");
            sb.append("\"fechaInicio\" : \"" + Utils.strToSQLDate(inicio.get()) + "\", ");
            sb.append("\"fechaFin\" : \"" + Utils.strToSQLDate(fin.get()) + "\", ");
            sb.append("\"mensualidad\" : " + mensualidad.get() + ", ");
            sb.append("\"idInmueble\" : " + idInmueble);
            sb.append("}");

            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return AlquilerDAO.postAlquilerByIdInDB(alquiler == null ? null:String.valueOf(alquiler.getId()),sb.toString());
                }
            };

            task.setOnSucceeded(ee->{
                if(task.getValue()){
                    myStage.close();
                    mainController.setMsgFromMoldAlquiler("Alquiler " + ((alquiler == null) ? "creado": "editado") + " correctamente");
                }else{
                    showReport("Fallo al postear inmueble.",3);
                }
            });
            exec.execute(task);
        }
    }
}
