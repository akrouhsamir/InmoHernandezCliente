package com.inmohernandez.cliente.controllers;

import com.google.gson.*;
import com.inmohernandez.cliente.models.Inmueble;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MoldInmuebleController {
    private String mode;
    private String idInmueble;
    private Stage myStage;

    private MainInmueblesController mainController;


    private SimpleStringProperty titulo, precio, publicacion, zona,
    ubicacion, habitaciones, bannos, descripcion, m2, m2utiles;


    public void initController(String mode, String idInmueble, Stage myStage, MainInmueblesController mainController){
        this.idInmueble = idInmueble;
        this.mode = mode;
        this.myStage = myStage;
        this.mainController = mainController;

        if(mode.equals("Crear")){
            btn_mod.setText("Crear");
        }else{
            btn_mod.setText("Editar");
        }

        titulo = new SimpleStringProperty();
        precio = new SimpleStringProperty();
        publicacion = new SimpleStringProperty();
        zona = new SimpleStringProperty();
        ubicacion = new SimpleStringProperty();
        habitaciones = new SimpleStringProperty();
        bannos = new SimpleStringProperty();
        descripcion = new SimpleStringProperty();
        m2 = new SimpleStringProperty();
        m2utiles = new SimpleStringProperty();

        titulo.bind(tf_titulo.textProperty());
        precio.bind(tf_precio.textProperty());
        publicacion.bind(date_publicacion.valueProperty().asString());
        zona.bind(cbox_zona.valueProperty());
        ubicacion.bind(tf_ubicacion.textProperty());
        habitaciones.bind(spinner_habitaciones.valueProperty().asString());
        bannos.bind(spinner_bannos.valueProperty().asString());
        descripcion.bind(ta_descripcion.textProperty());
        m2.bind(tf_m2.textProperty());
        m2utiles.bind(tf_m2utiles.textProperty());

        initZonaComboBox();
        initHabitacionesSpinner();
        initBannosSpinner();
        initPrecio();
        initMetros();
        initPublicacion();
    }

    public void initPrecio(){
        tf_precio.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf_precio.setText(oldValue);
                }
            }
        });
    }

    public void initMetros(){
        tf_m2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf_m2.setText(oldValue);
                }
            }
        });

        tf_m2utiles.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf_m2utiles.setText(oldValue);
                }
            }
        });
    }


    public void initPublicacion(){
        final String fechaPattern = "([1-9]|[0-2][0-9]|3[0-1])/(0[1-9]|1[0-2])/(\\d{4})";

        date_publicacion.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches(fechaPattern)) {
                    Platform.runLater(() -> {
                        date_publicacion.getEditor().setText(oldValue);
                    });
                }
            }
        });
    }


    public void initZonaComboBox(){
        List<String> zonas = FXCollections.observableArrayList();
        zonas.add("Ejido Centro");
        zonas.add("Ejido Norte");
        zonas.add("Ejido Sur");
        zonas.add("Almerimar");
        zonas.add("Balerma");
        zonas.add("Guardias Viejas");
        zonas.add("Las Norias");
        zonas.add("Pampanico");
        zonas.add("Santa María del Aguila");
        zonas.add("Matagorda");
        zonas.add("San Agustín");
        zonas.sort(null);
        zonas.add(0,"Todas las zonas");
        cbox_zona.getItems().addAll(zonas);
        cbox_zona.getSelectionModel().selectFirst();
    }

    public void initHabitacionesSpinner(){
        spinner_habitaciones.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100,
                0));
        spinner_habitaciones.setEditable(true);
        spinner_habitaciones.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("[1-9]\\d*")){
                    spinner_habitaciones.getEditor().setText(s);
                }
            }
        });
    }

    public void initBannosSpinner(){
        spinner_bannos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100,
                0));
        spinner_bannos.setEditable(true);
        spinner_bannos.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("[1-9]\\d*")){
                    spinner_bannos.getEditor().setText(s);
                }
            }
        });
    }
    @FXML
    private TextField tf_titulo, tf_precio, tf_ubicacion, tf_m2, tf_m2utiles;

    @FXML
    private TextArea ta_descripcion;

    @FXML
    private DatePicker date_publicacion;

    @FXML
    private Spinner spinner_habitaciones, spinner_bannos;

    @FXML
    private Button btn_mod;

    @FXML
    private ComboBox cbox_zona;

    @FXML
    private Label report;

    @FXML
    public void modInmueble(){
        postInmuebleToDB();
    }

    @FXML
    public void cancelar(){
        myStage.close();
    }

    public boolean comprobarFormulario(){
        boolean correcto = true;
        if(titulo.get().isBlank()){
            report.setText("El titulo está vacío");
            correcto = false;
        }
        if(precio.get().isBlank()){
            report.setText("El precio está vacío");
            correcto = false;
        }
        if(m2.get().isBlank()){
            report.setText("Los metros cuadrados construidos están vacíos");
            correcto = false;
        }
        if(m2utiles.get().isBlank()){
            tf_m2utiles.setText(m2.get());
        }
        if(publicacion.get().equals("null")){
            report.setText("La fecha de publicación está vacía");
            correcto = false;
        }

        if (zona.get().equals("null")) {
            report.setText("Seleccione una zona");
            correcto = false;
        }


        return correcto;
    }

    private void postInmuebleToDB() {
        URL url;
        HttpURLConnection connection;
        Inmueble inmueble;

        StringBuilder sb = new StringBuilder();

        if (comprobarFormulario()) {
            sb.append("{");
            sb.append("\"titulo\" : \"" + titulo.get() + "\", ");
            sb.append("\"precio\" : " + precio.get() + ", ");
            sb.append("\"descripcion\" : \"" + descripcion.get() + "\", ");
            sb.append("\"metrosConstruidos\" : " + m2.get() + ", ");
            sb.append("\"metrosUtiles\" : " + m2utiles.get() + ", ");
            sb.append("\"ubicacion\" : \"" + ubicacion.get() + "\", ");
            sb.append("\"zona\" : \"" + (zona.get().equals("Todas las zonas") ? "" : zona.get()) + "\", ");
            sb.append("\"fechaPublicacion\" : \"" + publicacion.get() + "\", ");
            sb.append("\"habitaciones\" : " + habitaciones.get() + ", ");
            sb.append("\"bannos\" : " + bannos.get());
            sb.append("}");
            System.out.println(sb.toString());

            try {
                url = new URL("http://localhost:8080/api/inmuebles/");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.getOutputStream().write(sb.toString().getBytes("UTF-8"));
                connection.getOutputStream().close();
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    mainController.setLastMoldInmuebleResult(titulo.get());
                    myStage.close();
                }
                connection.disconnect();

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
