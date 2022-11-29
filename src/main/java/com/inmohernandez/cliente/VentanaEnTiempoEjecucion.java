package com.inmohernandez.cliente;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class VentanaEnTiempoEjecucion extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 580, 200);
        Button btnMod = new Button("Crear");
        Button btnCancelar = new Button("Cancelar");
        TextField tfCliente = new TextField();
        TextField tfMensualidad = new TextField();
        DatePicker date_inicio = new DatePicker();
        DatePicker date_fin = new DatePicker();
        Label lbCliente = new Label("Cliente");
        Label lbInicio = new Label("Inicio alquiler");
        Label lbFin = new Label("Fin alquiler");
        Label lbMensualidad = new Label("Mensualidad");
        GridPane botones = new GridPane();

        GridPane.setConstraints(lbCliente, 0,1);
        GridPane.setConstraints(lbInicio, 0,2);
        GridPane.setConstraints(lbMensualidad, 0,3);
        GridPane.setConstraints(lbFin, 2,2);
        GridPane.setConstraints(tfCliente,1,1,3,1);
        GridPane.setConstraints(date_inicio,1,2,1,1);
        GridPane.setConstraints(date_fin,3,2,1,1);
        GridPane.setConstraints(tfMensualidad,1,3,1,1);
        GridPane.setConstraints(botones,0,5,4,4);
        GridPane.setConstraints(btnMod,0,0,2,1);
        GridPane.setConstraints(btnCancelar,2,0,2,1);

        root.setPadding(new Insets(10,30,10,30));
        botones.setPadding(new Insets(30,0,0,0));

        root.setHgap(10);
        root.setVgap(10);

        botones.setHgap(10);
        botones.setVgap(10);

        btnMod.setPrefSize(Double.MAX_VALUE,Double.MAX_VALUE);
        btnCancelar.setPrefSize(Double.MAX_VALUE,Double.MAX_VALUE);

        botones.getChildren().addAll(btnMod,btnCancelar);
        root.getChildren().addAll(tfCliente, tfMensualidad
        , date_inicio, date_fin, lbFin, lbInicio, lbCliente, lbMensualidad, botones);

        stage.setResizable(false);
        stage.setTitle("Editar");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}