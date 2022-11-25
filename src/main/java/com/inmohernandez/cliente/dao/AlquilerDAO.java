package com.inmohernandez.cliente.dao;

import com.google.gson.*;
import com.inmohernandez.cliente.models.Alquiler;
import com.inmohernandez.cliente.models.Inmueble;
import com.inmohernandez.cliente.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
                alquiler.setFechaInicio(Utils.sqlDateToEUDate(alquiler.getFechaInicio()));
                alquiler.setFechaFin(Utils.sqlDateToEUDate(alquiler.getFechaFin()));
                alquileres.add(alquiler);
            }

        }
        connection.disconnect();
        return alquileres;
    }

    public static boolean postAlquilerByIdInDB(String id, String bodyJson){
        URL url;
        HttpURLConnection connection;
        boolean result = false;
        try {
            url = new URL(ROOTAPI + (id == null ? "": id));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.getOutputStream().write(bodyJson.getBytes("UTF-8"));
            connection.getOutputStream().close();
            connection.connect();
            if (connection.getResponseCode() == 200) {
                result = true;
            }
            connection.disconnect();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static Alquiler getAlquilerByIdFromDB(String id){
        HttpURLConnection connection;
        InputStreamReader isr;
        JsonObject jsonInmueble;
        Alquiler alquiler=null;
        try {
            connection = (HttpURLConnection) new URL(ROOTAPI + id).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == 200){
                isr = new InputStreamReader(connection.getInputStream());
                jsonInmueble = JsonParser.parseReader(isr).getAsJsonObject();
                isr.close();
                alquiler = new Gson().fromJson(jsonInmueble, Alquiler.class);
            }
            connection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return alquiler;
    }

    public static boolean removeAlquilerByIdInDB(String id){
        HttpURLConnection connection;
        boolean result=false;
        try {
            connection = (HttpURLConnection) new URL(ROOTAPI + id).openConnection();
            connection.setRequestMethod("DELETE");
            connection.connect();
            if (connection.getResponseCode() == 200){
                result = true;
            }
            connection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
