package com.inmohernandez.cliente.dao;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.inmohernandez.cliente.models.Alquiler;
import com.inmohernandez.cliente.models.Inmueble;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AlquilerDAO {
    private static final String ROOTAPI = "http://localhost:8080/api/alquileres/";

    public static ObservableList<Alquiler> getAlquileresFromDB(String bodyJson) throws IOException {
        ObservableList<Alquiler> alquileres = null;
        URL url;
        HttpURLConnection connection;
        InputStreamReader isr;
        JsonArray jsonAlquileres;
        Alquiler alquiler;

        url = new URL(ROOTAPI + "filtros");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.getOutputStream().write(bodyJson.getBytes("UTF-8"));
        connection.getOutputStream().close();
        connection.connect();
        if (connection.getResponseCode() == 200) {
            alquileres = FXCollections.observableArrayList();
            isr = new InputStreamReader(connection.getInputStream());
            jsonAlquileres = JsonParser.parseReader(isr).getAsJsonArray();
            isr.close();
            for (JsonElement element : jsonAlquileres) {
                alquiler = new Gson().fromJson(element, Alquiler.class);
                alquileres.add(alquiler);
            }

        }
        connection.disconnect();
        return alquileres;
    }
}
