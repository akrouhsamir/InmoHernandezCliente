package com.inmohernandez.cliente;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

import java.io.IOException;
import java.time.LocalDate;

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
        Button btnCrear = robot.lookup("#btnCrear").queryAs(Button.class);
        robot.clickOn(btnCrear);
        robot.sleep(1000);
        FxAssert.verifyThat(robot.window("Crear inmueble"), WindowMatchers.isShowing());

        // Buscar el campo de texto "tfTitulo" en la interfaz gráfica
        TextField tfTitulo = robot.lookup("#tfTitulo").queryAs(TextField.class);
        // Hacer click en el campo de texto y escribir "Daddy Yanke"
        robot.clickOn(tfTitulo).write("Daddy Yanke");

        DatePicker datePublicacion = robot.lookup("#datePublicacion").queryAs(DatePicker.class);

        // Hacer click en el botón de flecha (arrow) del datepicker
        robot.interact(()-> {
            datePublicacion.show();
        });
        robot.sleep(1000);

        Button leftArrow = robot.lookup(".date-picker-year-button").queryAs(Button.class);
        robot.clickOn(leftArrow);




        // Buscar el campo de texto "tfPrecio" en la interfaz gráfica
        TextField tfPrecio = robot.lookup("#tfPrecio").queryAs(TextField.class);
        // Hacer click en el campo de texto y escribir "125650"
        robot.clickOn(tfPrecio).write("125650");

        // Buscar el combobox "cboxZona" en la interfaz gráfica
        ComboBox cboxZona = robot.lookup("#cboxZona").queryAs(ComboBox.class);
        // Hacer click en el combobox y seleccionar el item 1
        robot.clickOn(cboxZona).clickOn(String.valueOf(1));
    }
}