module com.inmohernandez.cliente {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.sql;


    opens com.inmohernandez.cliente.models to javafx.base, com.google.gson;
    exports com.inmohernandez.cliente.models;


    opens com.inmohernandez.cliente to javafx.fxml;
    exports com.inmohernandez.cliente;


    opens com.inmohernandez.cliente.controllers to javafx.fxml;
    exports com.inmohernandez.cliente.controllers;

    opens com.inmohernandez.cliente.dao to com.google.gson;
    exports com.inmohernandez.cliente.dao;

    opens com.inmohernandez.cliente.utils to javafx.fxml;
    exports com.inmohernandez.cliente.utils;
}