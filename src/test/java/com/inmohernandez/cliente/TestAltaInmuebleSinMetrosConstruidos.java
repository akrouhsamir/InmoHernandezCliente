package com.inmohernandez.cliente;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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
public class TestAltaInmuebleSinMetrosConstruidos {
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
    public void testSinMetrosConstruidos(FxRobot robot) throws InterruptedException {
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
        robot.sleep(1000);
        Label report = robot.lookup("#report").queryAs(Label.class);
        Assertions.assertEquals("Los metros cuadrados construidos están vacíos",report.getText());

    }
}
