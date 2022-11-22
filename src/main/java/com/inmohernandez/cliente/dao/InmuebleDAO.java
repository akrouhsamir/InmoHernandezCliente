package com.inmohernandez.cliente.dao;

import com.google.gson.*;

import com.inmohernandez.cliente.models.Inmueble;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InmuebleDAO {
    private static final String ROOTAPI = "http://localhost:8080/api/inmuebles/";

    public static ObservableList<Inmueble> getInmueblesFromDB(String bodyJson) throws IOException {
        ObservableList<Inmueble> inmuebles = null;
        URL url;
        HttpURLConnection connection;
        InputStreamReader isr;
        JsonArray jsonInmuebles;
        Inmueble inmueble;

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
            inmuebles = FXCollections.observableArrayList();
            isr = new InputStreamReader(connection.getInputStream());
            jsonInmuebles = JsonParser.parseReader(isr).getAsJsonArray();
            isr.close();
            for (JsonElement element : jsonInmuebles) {
                inmueble = new Gson().fromJson(element, Inmueble.class);
                inmueble.formatDate();
                inmuebles.add(inmueble);
            }

        }
        connection.disconnect();
        return inmuebles;
    }

    public static Inmueble getInmuebleByIdFromDB(String id){
        HttpURLConnection connection;
        InputStreamReader isr;
        JsonObject jsonInmueble;
        Inmueble inmueble=null;
        try {
            connection = (HttpURLConnection) new URL(ROOTAPI + id).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == 200){
                isr = new InputStreamReader(connection.getInputStream());
                jsonInmueble = JsonParser.parseReader(isr).getAsJsonObject();
                isr.close();
                inmueble = new Gson().fromJson(jsonInmueble, Inmueble.class);
            }
            connection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return inmueble;
    }

    public static boolean postInmuebleByIdInDB(String id, String bodyJson){
        URL url;
        HttpURLConnection connection;
        boolean result = false;
        try {
            url = new URL("http://localhost:8080/api/inmuebles/" + (id == null ? "": id));
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

    public static boolean removeInmuebleByIdInDB(String id){
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
