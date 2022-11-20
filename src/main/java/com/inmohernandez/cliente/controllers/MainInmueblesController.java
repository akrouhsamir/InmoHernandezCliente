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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainInmueblesController {
    private static final String HOSTPORT = "localhost:8080";
    private static final String API = "/api/inmuebles";

    private List<Inmueble> lastInmueblesRefresh = FXCollections.observableArrayList();
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

    @FXML
    Label report_precio, report_publicacion;

    private SimpleStringProperty search,zona,precioDesde, precioHasta, publicacionDesde, publicacionHasta;


    @FXML
    private void initialize() {
        search = new SimpleStringProperty();
        zona = new SimpleStringProperty();
        precioDesde = new SimpleStringProperty();
        precioHasta = new SimpleStringProperty();
        publicacionDesde = new SimpleStringProperty();
        publicacionHasta = new SimpleStringProperty();


        search.bind(tf_search.textProperty());
        zona.bind(cb_zona.valueProperty());
        precioDesde.bind(tf_precio_desde.textProperty());
        precioHasta.bind(tf_precio_hasta.textProperty());
        publicacionDesde.bind(date_publicacion_desde.valueProperty().asString());
        publicacionHasta.bind(date_publicacion_hasta.valueProperty().asString());


        initZonaComboBox();
        initFiltroPrecio();
        initFiltroPublicacion();
        initTableViewInmuebles();
        updateTableViewInmuebles();
        eventoDobleClickTV();
    }

    public void initFiltroPrecio(){
        tf_precio_desde.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf_precio_desde.setText(newValue.replaceAll("\\D", ""));
                    report_precio.setText("Solo digitos");
                }else{
                    report_precio.setText("");
                }
            }
        });

        tf_precio_hasta.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf_precio_hasta.setText(newValue.replaceAll("\\D", ""));
                    report_precio.setText("Solo digitos");
                }else{
                    report_precio.setText("");
                }
            }
        });
    }

    private void eventoDobleClickTV(){
        tv_inmuebles.setRowFactory(tv -> {
            TableRow<Inmueble> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount()==2 && (!row.isEmpty())){
                   tf_seleccionado.setText(row.getItem().getId()+"");
                }
            });
            return row;
        });
    }

    public void initFiltroPublicacion(){
        final String fechaPattern = "([1-9]|[0-2][0-9]|3[0-1])/(0[1-9]|1[0-2])/(\\d{4})";

        date_publicacion_hasta.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue.matches(fechaPattern)) {
                    report_publicacion.setText("");
                }else{
                    Platform.runLater(() -> {
                        date_publicacion_hasta.getEditor().clear();
                    });
                    report_publicacion.setText("Requerido (dd/MM/YYYY)");
                }
            }
        });

        date_publicacion_desde.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue.matches(fechaPattern)) {
                    report_publicacion.setText("");
                }else{
                    Platform.runLater(() -> {
                        date_publicacion_desde.getEditor().clear();
                    });
                    report_publicacion.setText("Requerido (dd/MM/YYYY)");
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
        cb_zona.getItems().addAll(zonas);
        cb_zona.getSelectionModel().selectFirst();
    }



    @FXML
    public void clickLimpiarFiltros(){
        tf_precio_desde.clear();
        tf_precio_hasta.clear();
        cb_zona.getSelectionModel().selectFirst();
        date_publicacion_desde.setValue(null);
        date_publicacion_hasta.setValue(null);
        updateTableViewInmuebles();
    }

    @FXML
    public void clickAplicarFiltros(){
        updateTableViewInmuebles();
    }

    @FXML
    public void onSearchClick(){
        updateTableViewInmuebles();
    }

    private void updateTableViewInmuebles(){
        ObservableList<Inmueble> inmuebles = getInmueblesFromDB();
        tv_inmuebles.getItems().clear();
        tv_inmuebles.getItems().addAll(inmuebles);
        lastInmueblesRefresh = inmuebles;
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

        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append("\"busqueda\" : \""+search.get()+"\", ");
        sb.append("\"zona\" : \""+(zona.get().equals("Todas las zonas") ? "": zona.get())+"\", ");
        sb.append("\"precio_desde\" : "+(precioDesde.get().equals("") ? 0: precioDesde.get())+", ");
        sb.append("\"precio_hasta\" : "+(precioHasta.get().equals("") ? Float.MAX_VALUE: precioHasta.get())+", ");
        sb.append("\"publicacion_desde\" : \""+(publicacionDesde.get().equals("null") ? "1970-01-01": publicacionDesde.get())+"\", ");
        sb.append("\"publicacion_hasta\" : \""+(publicacionHasta.get().equals("null") ? "2299-01-01": publicacionHasta.get())+"\"");
        sb.append("}");
        System.out.println(sb.toString());

        try {
            url = new URL("http://localhost:8080/api/inmuebles/filtros");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.getOutputStream().write(sb.toString().getBytes("UTF-8"));
            connection.getOutputStream().close();
            connection.connect();
            if(connection.getResponseCode() == 200){
                isr = new InputStreamReader(connection.getInputStream());
                jsonInmuebles = JsonParser.parseReader(isr).getAsJsonArray();
                isr.close();
                for(JsonElement element : jsonInmuebles){
                    inmueble = new Gson().fromJson(element, Inmueble.class);
                    inmueble.formatDate();
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

    @FXML
    public void keyTypedSearch(){
        String b = search.get().trim().toLowerCase();
        String titulo,id;

        tv_inmuebles.getItems().clear();
        for(Inmueble i : lastInmueblesRefresh){
            id = String.valueOf(i.getId()).trim().toLowerCase();
            titulo = i.getTitulo().trim().toLowerCase();
            if(id.startsWith(b) || titulo.startsWith(b)){
                tv_inmuebles.getItems().add(i);
            }
        }
    }


}