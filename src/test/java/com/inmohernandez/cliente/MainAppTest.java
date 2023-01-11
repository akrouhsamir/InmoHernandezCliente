package com.inmohernandez.cliente;

import com.inmohernandez.cliente.models.Inmueble;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
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
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.service.query.PointQuery;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
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
    public void testAltaInmueble(FxRobot robot) throws InterruptedException {
        final String titulo = "Apartamento en Balerma Nuevo";


        robot.clickOn("#btnCrear");
        robot.sleep(1000);
        FxAssert.verifyThat(robot.window("Crear inmueble"), WindowMatchers.isShowing());

        robot.clickOn("#tfTitulo").write(titulo);

        DatePicker datePublicacion = robot.lookup("#datePublicacion").queryAs(DatePicker.class);
        datePublicacion.getEditor().setText("19/08/2019");

        robot.clickOn("#tfPrecio").write("125650");

        robot.clickOn("#cboxZona").clickOn("Balerma");


        robot.clickOn("#tfUbicacion").write("Calle Carlomagno");

        robot.clickOn("#spinnerHabitaciones").type(KeyCode.UP).type(KeyCode.UP).type(KeyCode.UP);
        robot.clickOn("#spinnerBannos").type(KeyCode.UP).type(KeyCode.UP);

        robot.clickOn("#tfMetros").write("150");
        robot.clickOn("#tfMetrosUtiles").write("150");

        robot.clickOn("#taDescripcion").write("Buen apartamento de venta chulisimo");

        robot.clickOn("#btnCrearInmueble");

        TableView tableView = robot.lookup("#tvInmuebles").queryAs(TableView.class);
        int lastIndex = tableView.getItems().size() - 1;
        robot.interact(() -> tableView.scrollTo(lastIndex));
        robot.interact(() -> tableView.getSelectionModel().select(lastIndex));
        Inmueble inmueble = (Inmueble)tableView.getSelectionModel().getSelectedItem();

        Assertions.assertEquals(titulo,inmueble.getTitulo());
        Assertions.assertEquals();
    }




}