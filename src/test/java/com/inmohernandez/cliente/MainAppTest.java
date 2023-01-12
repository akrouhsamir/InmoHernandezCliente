package com.inmohernandez.cliente;

import com.inmohernandez.cliente.models.Inmueble;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.WindowMatchers;
import java.io.IOException;

@ExtendWith(ApplicationExtension.class)
class MainAppTest {

    Pane mainroot;
    Stage mainstage;
    @Start
    public void start(Stage stage) throws IOException {
        mainroot = (Pane) FXMLLoader.load(getClass().getResource("main-inmuebles-view.fxml"));
        mainstage = stage;
        stage.setScene(new Scene(mainroot));
        stage.show();
        stage.toFront();
    }

    @Test
    @Before
    public void testAltaInmueble(FxRobot robot) throws InterruptedException {
        final String titulo = "Apartamento en Balerma Nuevo";
        final String fecha = "19/08/2019";
        final String precio = "125650";
        final String zona = "Balerma";
        final String ubicacion = "Calle Carlomagno";
        final int habitacionesExtra = 3;
        final int bannosExtra = 2;
        final int metros = 150;
        final int metrosUtiles = 130;
        final String descripcion = "Buen apartamento de venta chulisimo";

        robot.clickOn("#btnCrear");
        robot.sleep(1000);
        FxAssert.verifyThat(robot.window("Crear inmueble"), WindowMatchers.isShowing());

        robot.clickOn("#tfTitulo").write(titulo);

        DatePicker datePublicacion = robot.lookup("#datePublicacion").queryAs(DatePicker.class);
        datePublicacion.getEditor().setText(fecha);

        robot.clickOn("#tfPrecio").write(precio);

        robot.clickOn("#cboxZona");
        robot.sleep(300);
        robot.clickOn(zona);


        robot.clickOn("#tfUbicacion").write(ubicacion);

        robot.clickOn("#spinnerHabitaciones").type(KeyCode.UP).type(KeyCode.UP).type(KeyCode.UP);
        robot.clickOn("#spinnerBannos").type(KeyCode.UP).type(KeyCode.UP);

        robot.clickOn("#tfMetros").write(String.valueOf(metros));
        robot.clickOn("#tfMetrosUtiles").write(String.valueOf(metrosUtiles));

        robot.clickOn("#taDescripcion").write(descripcion);

        robot.clickOn("#btnCrearInmueble");

        TableView tableView = robot.lookup("#tvInmuebles").queryAs(TableView.class);
        int lastIndex = tableView.getItems().size() - 1;
        robot.interact(() -> tableView.scrollTo(lastIndex));
        robot.interact(() -> tableView.getSelectionModel().select(lastIndex));
        Inmueble inmueble = (Inmueble)tableView.getSelectionModel().getSelectedItem();

        Assertions.assertEquals(titulo,inmueble.getTitulo());
        Assertions.assertEquals(fecha,inmueble.getFechaPublicacion());
        Assertions.assertEquals(Float.valueOf(precio),inmueble.getPrecio());
        Assertions.assertEquals(zona,inmueble.getZona());
        Assertions.assertEquals(ubicacion,inmueble.getUbicacion());
        Assertions.assertEquals(habitacionesExtra+1,inmueble.getHabitaciones());
        Assertions.assertEquals(bannosExtra+1,inmueble.getBannos());
        Assertions.assertEquals(metros,inmueble.getMetrosConstruidos());
        Assertions.assertEquals(metrosUtiles,inmueble.getMetrosUtiles());
        Assertions.assertEquals(descripcion,inmueble.getDescripcion());

    }

    @Test
    @Before
    public void testSinTitulo(FxRobot robot)throws InterruptedException{
        final String fecha = "19/08/2019";
        final String precio = "125650";
        final String zona = "Balerma";
        final String ubicacion = "Calle Carlomagno";
        final int habitacionesExtra = 3;
        final int bannosExtra = 2;
        final int metros = 150;
        final int metrosUtiles = 130;
        final String descripcion = "Buen apartamento de venta chulisimo";

        robot.clickOn("#btnCrear");
        robot.sleep(1000);
        FxAssert.verifyThat(robot.window("Crear inmueble"), WindowMatchers.isShowing());


        DatePicker datePublicacion = robot.lookup("#datePublicacion").queryAs(DatePicker.class);
        datePublicacion.getEditor().setText(fecha);

        robot.clickOn("#tfPrecio").write(precio);

        robot.clickOn("#cboxZona");
        robot.sleep(300);
        robot.clickOn(zona);


        robot.clickOn("#tfUbicacion").write(ubicacion);

        robot.clickOn("#spinnerHabitaciones").type(KeyCode.UP,habitacionesExtra);
        robot.clickOn("#spinnerBannos").type(KeyCode.UP,bannosExtra);

        robot.clickOn("#tfMetros").write(String.valueOf(metros));
        robot.clickOn("#tfMetrosUtiles").write(String.valueOf(metrosUtiles));

        robot.clickOn("#taDescripcion").write(descripcion);

        robot.clickOn("#btnCrearInmueble");
        robot.sleep(500);
        Label report = robot.lookup("#report").queryAs(Label.class);
        Assertions.assertEquals("El titulo está vacío",report.getText());

    }

    @Test
    @Before
    public void testSinFecha(FxRobot robot)throws InterruptedException{
        final String titulo = "Apartamento en Balerma Nuevo";
        final String precio = "125650";
        final String zona = "Balerma";
        final String ubicacion = "Calle Carlomagno";
        final int habitacionesExtra = 3;
        final int bannosExtra = 2;
        final int metros = 150;
        final int metrosUtiles = 130;
        final String descripcion = "Buen apartamento de venta chulisimo";

        robot.clickOn("#btnCrear");
        robot.sleep(1000);
        FxAssert.verifyThat(robot.window("Crear inmueble"), WindowMatchers.isShowing());

        robot.clickOn("#tfTitulo").write(titulo);
        robot.clickOn("#tfPrecio").write(precio);

        robot.clickOn("#cboxZona").clickOn(zona);


        robot.clickOn("#tfUbicacion").write(ubicacion);

        robot.clickOn("#spinnerHabitaciones").type(KeyCode.UP,habitacionesExtra);
        robot.clickOn("#spinnerBannos").type(KeyCode.UP,bannosExtra);

        robot.clickOn("#tfMetros").write(String.valueOf(metros));
        robot.clickOn("#tfMetrosUtiles").write(String.valueOf(metrosUtiles));

        robot.clickOn("#taDescripcion").write(descripcion);

        robot.clickOn("#btnCrearInmueble");
        robot.sleep(500);
        Label report = robot.lookup("#report").queryAs(Label.class);
        Assertions.assertEquals("La fecha de publicación está vacía",report.getText());

    }


    @Test
    @Before
    public void tesSinPrecio(FxRobot robot) throws InterruptedException {
        final String titulo = "Apartamento en Balerma Nuevo";
        final String fecha = "19/08/2019";
        final String zona = "Balerma";
        final String ubicacion = "Calle Carlomagno";
        final int habitacionesExtra = 3;
        final int bannosExtra = 2;
        final int metros = 150;
        final int metrosUtiles = 130;
        final String descripcion = "Buen apartamento de venta chulisimo";

        robot.clickOn("#btnCrear");
        robot.sleep(1000);
        FxAssert.verifyThat(robot.window("Crear inmueble"), WindowMatchers.isShowing());

        robot.clickOn("#tfTitulo").write(titulo);

        DatePicker datePublicacion = robot.lookup("#datePublicacion").queryAs(DatePicker.class);
        datePublicacion.getEditor().setText(fecha);


        robot.clickOn("#cboxZona").clickOn(zona);


        robot.clickOn("#tfUbicacion").write(ubicacion);

        robot.clickOn("#spinnerHabitaciones").type(KeyCode.UP).type(KeyCode.UP).type(KeyCode.UP);
        robot.clickOn("#spinnerBannos").type(KeyCode.UP).type(KeyCode.UP);

        robot.clickOn("#tfMetros").write(String.valueOf(metros));
        robot.clickOn("#tfMetrosUtiles").write(String.valueOf(metrosUtiles));

        robot.clickOn("#taDescripcion").write(descripcion);

        robot.clickOn("#btnCrearInmueble");

        robot.sleep(500);
        Label report = robot.lookup("#report").queryAs(Label.class);
        Assertions.assertEquals("El precio está vacío",report.getText());

    }

    @Test
    @Before
    public void tesSinMetrosConstruidos(FxRobot robot) throws InterruptedException {
        final String titulo = "Apartamento en Balerma Nuevo";
        final String fecha = "19/08/2019";
        final String zona = "Balerma";
        final String ubicacion = "Calle Carlomagno";
        final int habitacionesExtra = 3;
        final int bannosExtra = 2;
        final int metrosUtiles = 130;
        final String descripcion = "Buen apartamento de venta chulisimo";
        final String precio = "125650";

        robot.clickOn("#btnCrear");
        robot.sleep(1000);
        FxAssert.verifyThat(robot.window("Crear inmueble"), WindowMatchers.isShowing());

        robot.clickOn("#tfTitulo").write(titulo);

        DatePicker datePublicacion = robot.lookup("#datePublicacion").queryAs(DatePicker.class);
        datePublicacion.getEditor().setText(fecha);

        robot.clickOn("#tfPrecio").write(precio);
        robot.clickOn("#cboxZona").clickOn(zona);


        robot.clickOn("#tfUbicacion").write(ubicacion);

        robot.clickOn("#spinnerHabitaciones").type(KeyCode.UP).type(KeyCode.UP).type(KeyCode.UP);
        robot.clickOn("#spinnerBannos").type(KeyCode.UP).type(KeyCode.UP);


        robot.clickOn("#taDescripcion").write(descripcion);

        robot.clickOn("#btnCrearInmueble");
        robot.sleep(500);
        Label report = robot.lookup("#report").queryAs(Label.class);
        Assertions.assertEquals("Los metros cuadrados construidos están vacíos",report.getText());

    }

}