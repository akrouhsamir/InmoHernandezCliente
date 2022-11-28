package com.inmohernandez.cliente.controllers;

import com.inmohernandez.cliente.MainApp;
import com.inmohernandez.cliente.dao.InmuebleDAO;
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


public class MainInmueblesController {
    private List<Inmueble> lastInmueblesRefresh = FXCollections.observableArrayList();
    @FXML
    private TableView<Inmueble> tv_inmuebles;
    @FXML
    private TextField tf_search, tf_precio_desde, tf_precio_hasta,tf_seleccionado;
    @FXML
    private DatePicker date_publicacion_desde, date_publicacion_hasta;
    @FXML
    private ComboBox cb_zona;
    @FXML
    Label report;
    private SimpleStringProperty search,zona,precioDesde, precioHasta, publicacionDesde, publicacionHasta,seleccionado;
    private String msgFromMoldInmueble;

    public void setMsgFromMoldInmueble(String msgFromMoldInmueble) {
        this.msgFromMoldInmueble = msgFromMoldInmueble;
    }

    private Executor exec;


    @FXML
    private void initialize() {
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });
        search = new SimpleStringProperty();
        zona = new SimpleStringProperty();
        precioDesde = new SimpleStringProperty();
        precioHasta = new SimpleStringProperty();
        publicacionDesde = new SimpleStringProperty();
        publicacionHasta = new SimpleStringProperty();
        seleccionado = new SimpleStringProperty();

        search.bind(tf_search.textProperty());
        zona.bind(cb_zona.valueProperty());
        precioDesde.bind(tf_precio_desde.textProperty());
        precioHasta.bind(tf_precio_hasta.textProperty());
        publicacionDesde.bind(date_publicacion_desde.getEditor().textProperty());
        publicacionHasta.bind(date_publicacion_hasta.getEditor().textProperty());
        seleccionado.bind(tf_seleccionado.textProperty());

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
                    showReport("Solo digitos",2);
                }
            }
        });

        tf_precio_hasta.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf_precio_hasta.setText(newValue.replaceAll("\\D", ""));
                    showReport("Solo digitos",2);
                }
            }
        });
    }

    public void initFiltroPublicacion(){
        final String fechaPattern = "([1-9]|[0-2][0-9]|3[0-1])/(0[1-9]|1[0-2]|[1-9])/(\\d{4})";

        date_publicacion_hasta.getEditor().textProperty().addListener(new ChangeListener<String>() {
            Date bef,aft;
            String desde;
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches(fechaPattern)) {
                    if(!newValue.isBlank()){
                        showReport("Requerido (dd/MM/YYYY)", 2);
                    }
                    Platform.runLater(() -> {
                        date_publicacion_hasta.getEditor().clear();
                    });
                }else{
                    desde = date_publicacion_desde.getEditor().getText();
                    if(!desde.isBlank()){
                        try {
                            bef = new SimpleDateFormat("dd/MM/yyyy").parse(desde);
                            aft = new SimpleDateFormat("dd/MM/yyyy").parse(newValue);
                            if(aft.before(bef)){
                                Platform.runLater(()->{
                                    date_publicacion_hasta.getEditor().setText(desde);
                                });
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        date_publicacion_desde.getEditor().textProperty().addListener(new ChangeListener<String>() {
            Date bef,aft;
            String hasta;
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches(fechaPattern)) {
                    if(!newValue.isBlank()){
                        showReport("Requerido (dd/MM/YYYY)", 2);
                    }
                    Platform.runLater(() -> {
                        date_publicacion_desde.getEditor().clear();
                    });
                }else{
                    hasta = date_publicacion_hasta.getEditor().getText();
                    if(!hasta.isBlank()){
                        try {
                            aft = new SimpleDateFormat("dd/MM/yyyy").parse(hasta);
                            bef = new SimpleDateFormat("dd/MM/yyyy").parse(newValue);
                            if(aft.before(bef)){
                                Platform.runLater(()->{
                                    date_publicacion_desde.getEditor().setText(hasta);
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

        colId.setMaxWidth(60);
        colTitulo.setMinWidth(160);
        colPrecio.setMinWidth(110);
        colFechaPublicacion.setMinWidth(100);
        colFechaPublicacion.setMaxWidth(100);
        colZona.setMinWidth(130);
        colZona.setMaxWidth(130);
        colUbicacion.setMinWidth(200);
        colDescripcion.setMinWidth(400);


        colId.setCellValueFactory(new PropertyValueFactory<Inmueble, Long>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<Inmueble, String>("titulo"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Inmueble, Float>("precio"));
        colPrecio.setCellFactory(tc -> new TableCell<Inmueble, Float>() {
            @Override
            protected void updateItem(Float precio, boolean empty) {
                super.updateItem(precio, empty);
                if (empty) {
                    setText(null);
                } else {
                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                    setText(formatter.format(precio)+" €");
                }
            }
        });
        colMetrosConstruidos.setCellValueFactory(new PropertyValueFactory<Inmueble, Integer>("metrosConstruidos"));
        colMetrosUtiles.setCellValueFactory(new PropertyValueFactory<Inmueble, Integer>("metrosUtiles"));
        colZona.setCellValueFactory(new PropertyValueFactory<Inmueble, String>("zona"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<Inmueble, String>("ubicacion"));
        colHabitaciones.setCellValueFactory(new PropertyValueFactory<Inmueble, Integer>("habitaciones"));
        colBannos.setCellValueFactory(new PropertyValueFactory<Inmueble, Integer>("bannos"));
        colFechaPublicacion.setCellValueFactory(new PropertyValueFactory<Inmueble, String>("fechaPublicacion"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Inmueble, String>("descripcion"));

        tv_inmuebles.getColumns().addAll(colId, colTitulo, colPrecio,colFechaPublicacion,
                colMetrosConstruidos, colMetrosUtiles, colZona,
                colUbicacion, colHabitaciones, colBannos, colDescripcion);

    }

    private void updateTableViewInmuebles(){
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append("\"busqueda\" : \""+search.get()+"\", ");
        sb.append("\"zona\" : \""+(zona.get().equals("Todas las zonas") ? "": zona.get())+"\", ");
        sb.append("\"precio_desde\" : "+(precioDesde.get().equals("") ? 0: precioDesde.get())+", ");
        sb.append("\"precio_hasta\" : "+(precioHasta.get().equals("") ? Float.MAX_VALUE: precioHasta.get())+", ");
        sb.append("\"publicacion_desde\" : \""+(publicacionDesde.get().isBlank() ? "1970-01-01": Utils.strToSQLDate(publicacionDesde.get()))+"\", ");
        sb.append("\"publicacion_hasta\" : \""+(publicacionHasta.get().isBlank() ? "2299-01-01": Utils.strToSQLDate(publicacionHasta.get()))+"\"");
        sb.append("}");

        Task<ObservableList<Inmueble>> task = new Task<ObservableList<Inmueble>>() {
            @Override
            protected ObservableList<Inmueble> call() throws Exception {
                return InmuebleDAO.getInmueblesFromDB(sb.toString());
            }
        };

        task.setOnSucceeded(e->{
            tv_inmuebles.getItems().addAll((ObservableList<Inmueble>) task.getValue());
            lastInmueblesRefresh = (ObservableList<Inmueble>) task.getValue();
        });

        task.setOnFailed(e->{
            showReport("Error al conectar con el servidor",5);
        });

        tv_inmuebles.getItems().clear();
        exec.execute(task);
    }




    @FXML
    public void crearInmuble(){
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("mold-inmueble-view.fxml"));
        MoldInmuebleController moldController;
        stage.setTitle("Crear inmueble");
        stage.setResizable(false);
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.getIcons().add(new Image(MainApp.class.getResource("imgs/rhinoapp.png").toExternalForm()));
        stage.initModality(Modality.APPLICATION_MODAL);
        moldController = fxmlLoader.getController();
        moldController.initController("Crear",null,stage,this);
        stage.showAndWait();
        showReport(msgFromMoldInmueble,2);
        updateTableViewInmuebles();
    }

    @FXML
    public void editarInmueble(){
        Task<Inmueble> task = new Task<Inmueble>() {
            @Override
            protected Inmueble call() throws Exception {
                return InmuebleDAO.getInmuebleByIdFromDB(seleccionado.get());
            }
        };
        task.setOnSucceeded(ee->{
            Stage stage;
            FXMLLoader fxmlLoader;
            MoldInmuebleController moldController;
            Inmueble inmueble;
            inmueble = task.getValue();
            if(inmueble == null){
                showReport("Inmueble no encontrado.",2);
            }else{
                stage = new Stage();
                fxmlLoader = new FXMLLoader(MainApp.class.getResource("mold-inmueble-view.fxml"));
                stage.setTitle("Editar inmueble");
                stage.getIcons().add(new Image(MainApp.class.getResource("imgs/rhinoapp.png").toExternalForm()));
                stage.setResizable(false);
                try {
                    stage.setScene(new Scene(fxmlLoader.load()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.initModality(Modality.APPLICATION_MODAL);
                moldController = fxmlLoader.getController();
                moldController.initController("Editar",inmueble,stage,this);
                stage.showAndWait();
                showReport(msgFromMoldInmueble,2);
                updateTableViewInmuebles();
            }
        });
        if(seleccionado.get().isBlank()){
            showReport("Selecciona un inmueble.",2);
        }else{
            exec.execute(task);
        }
    }

    @FXML
    public void borrarInmueble(){
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return InmuebleDAO.removeInmuebleByIdInDB(seleccionado.get());
            }
        };
        task.setOnSucceeded(e->{
            if(task.getValue()){
                showReport("Inmueble eliminado correctamente.",2);
                updateTableViewInmuebles();
                tf_seleccionado.clear();
            }else{
                showReport("Fallo al eliminar inmueble.",2);
            }
        });
        task.setOnFailed(e-> task.getException().printStackTrace());

        if(seleccionado.get().isBlank()){
            showReport("Selecciona un inmueble.",2);
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás" +
                    " seguro del borrado del inmueble con id "+seleccionado.get()+ "?",
                    ButtonType.YES,ButtonType.NO);
            alert.setHeaderText(null);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES){
                exec.execute(task);
            }
        }
    }

    @FXML
    public void verAlquileresInmueble(){
        Task<Inmueble> task = new Task<Inmueble>() {
            @Override
            protected Inmueble call() throws Exception {
                return InmuebleDAO.getInmuebleByIdFromDB(seleccionado.get());
            }
        };

        task.setOnSucceeded(ee->{
            Stage stage;
            FXMLLoader fxmlLoader;
            MainAquileresController alquileresController;
            Inmueble inmueble;
            inmueble = task.getValue();
            if(inmueble == null){
                showReport("Inmueble no encontrado.",2);
            }else{
                stage = new Stage();
                fxmlLoader = new FXMLLoader(MainApp.class.getResource("main-alquileres-view.fxml"));
                stage.setTitle("Alquileres: "+inmueble.getTitulo());
                stage.setMinHeight(600);
                stage.setMinWidth(900);
                try {
                    stage.setScene(new Scene(fxmlLoader.load()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.getIcons().add(new Image(MainApp.class.getResource("imgs/rhinoapp.png").toExternalForm()));
                stage.initModality(Modality.APPLICATION_MODAL);
                alquileresController = fxmlLoader.getController();
                alquileresController.initController(stage,this,Integer.parseInt(seleccionado.get()));
                stage.showAndWait();
                updateTableViewInmuebles();
            }
        });

        if(seleccionado.get().isBlank()){
            showReport("Selecciona un inmueble.",2);
        }else{
            exec.execute(task);
        }
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
}