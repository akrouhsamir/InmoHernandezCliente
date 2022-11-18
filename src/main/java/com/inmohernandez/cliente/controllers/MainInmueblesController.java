package com.inmohernandez.cliente.controllers;


import com.google.gson.*;
import com.inmohernandez.cliente.models.Inmueble;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainInmueblesController {
    private static final String HOSTPORT = "localhost:8080";
    private static final String API = "/api/inmuebles";
    @FXML
    private TableView<Inmueble> tv_inmuebles;

    @FXML
    private TextField tf_search, tf_precio_desde, tf_precio_hasta,
            tf_seleccionado;

    @FXML
    private DatePicker date_publicacion_desde, date_publicacion_hasta;

    @FXML
    private ComboBox cb_zona;

    @FXML
    private Button btn_search, btn_crear, btn_filtrar, btn_limpiar,
            btn_editar, btn_borrar;

    private SimpleStringProperty search;


    @FXML
    private void initialize() {
        initTableViewInmuebles();
        updateTableViewInmuebles();
        //search.bind(tf_search.textProperty());
    }

    @FXML
    public void onSearchClick(){
        tv_inmuebles.getItems().addAll();
    }

    private void updateTableViewInmuebles(){
        tv_inmuebles.getItems().clear();
        tv_inmuebles.getItems().addAll(getInmueblesFromDB());
    }
    private void initTableViewInmuebles(){
        TableColumn colId, colTitulo, colPrecio,
                colMetrosConstruidos, colMetrosUtiles, colZona, colUbicacion,
                colHabitaciones, colBannos, colFechaPublicacion, colDescripcion;

        colId = new TableColumn<>("Id");
        colTitulo = new TableColumn<>("Titulo");
        colPrecio = new TableColumn<>("Precio");
        colMetrosConstruidos = new TableColumn<>("Metros Construidos");
        colMetrosUtiles = new TableColumn<>("Metros Utiles");
        colZona = new TableColumn<>("Zona");
        colUbicacion = new TableColumn<>("Ubicacion");
        colHabitaciones = new TableColumn<>("Habitaciones");
        colBannos = new TableColumn<>("Baños");
        colFechaPublicacion = new TableColumn("Publicacion");
        colDescripcion = new TableColumn<>("Descripcion");

        colId.setCellValueFactory(new PropertyValueFactory<Inmueble, Long>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<Inmueble, String>("titulo"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Inmueble, Float>("precio"));
        colMetrosConstruidos.setCellValueFactory(new PropertyValueFactory<Inmueble, Integer>("metrosConstruidos"));
        colMetrosUtiles.setCellValueFactory(new PropertyValueFactory<Inmueble, Integer>("metrosUtiles"));
        colZona.setCellValueFactory(new PropertyValueFactory<Inmueble, String>("zona"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<Inmueble, String>("ubicacion"));
        colHabitaciones.setCellValueFactory(new PropertyValueFactory<Inmueble, Integer>("habitaciones"));
        colBannos.setCellValueFactory(new PropertyValueFactory<Inmueble, Integer>("bannos"));
        colFechaPublicacion.setCellValueFactory(new PropertyValueFactory<Inmueble, String>("fechaPublicacion"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Inmueble, String>("descripcion"));

        tv_inmuebles.getColumns().addAll(colId, colTitulo, colPrecio,
                colMetrosConstruidos, colMetrosUtiles, colZona,
                colUbicacion, colHabitaciones, colBannos,colFechaPublicacion, colDescripcion);

    }

    private ObservableList<Inmueble> getInmueblesFromDB(){
        ObservableList<Inmueble> inmuebles = FXCollections.observableArrayList();
        URL url;
        HttpURLConnection connection;
        InputStreamReader isr;
        String rawData="",line;
        JsonArray jsonInmuebles;
        JsonObject jsonInmueble;
        Inmueble inmueble;


        try {
            url = new URL("http://"+HOSTPORT+API);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if(connection.getResponseCode() == 200){
                isr = new InputStreamReader(connection.getInputStream());
                jsonInmuebles = JsonParser.parseReader(isr).getAsJsonArray();
                isr.close();
                for(JsonElement element : jsonInmuebles){
                    inmueble = new Gson().fromJson(element, Inmueble.class);
                    inmuebles.add(inmueble);
                }

            }
            connection.disconnect();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return inmuebles;
    }


}