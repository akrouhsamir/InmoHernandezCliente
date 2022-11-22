package com.inmohernandez.cliente.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.inmohernandez.cliente.dao.FloatPropertyAdapter;
import com.inmohernandez.cliente.dao.IntegerPropertyAdapter;
import com.inmohernandez.cliente.dao.LongPropertyAdapter;
import com.inmohernandez.cliente.dao.StringPropertyAdapter;
import javafx.beans.property.*;




public class Inmueble {
    @Expose
    @JsonAdapter(LongPropertyAdapter.class)
    public LongProperty idInmueble;
    @Expose
    @JsonAdapter(StringPropertyAdapter.class)
    public StringProperty titulo;
    @Expose
    @JsonAdapter(FloatPropertyAdapter.class)
    public FloatProperty precio;
    @Expose
    @JsonAdapter(StringPropertyAdapter.class)
    public StringProperty descripcion;
    @Expose
    @JsonAdapter(IntegerPropertyAdapter.class)
    public IntegerProperty metrosConstruidos;
    @Expose
    @JsonAdapter(IntegerPropertyAdapter.class)
    public IntegerProperty metrosUtiles;
    @Expose
    @JsonAdapter(StringPropertyAdapter.class)
    public StringProperty ubicacion;
    @Expose
    @JsonAdapter(StringPropertyAdapter.class)
    public StringProperty zona;
    @Expose
    @JsonAdapter(StringPropertyAdapter.class)
    public StringProperty fechaPublicacion;



    @Override
    public String toString() {
        return "Inmueble{" +
                "id=" + idInmueble +
                ", titulo=" + titulo +
                ", precio=" + precio +
                ", descripcion=" + descripcion +
                ", metrosConstruidos=" + metrosConstruidos +
                ", metrosUtiles=" + metrosUtiles +
                ", ubicacion=" + ubicacion +
                ", zona=" + zona +
                ", fechaPublicacion=" + fechaPublicacion +
                ", habitaciones=" + habitaciones +
                ", bannos=" + bannos +
                '}';
    }

    @Expose
    @JsonAdapter(IntegerPropertyAdapter.class)
    public IntegerProperty habitaciones;
    @Expose
    @JsonAdapter(IntegerPropertyAdapter.class)
    public IntegerProperty bannos;

    public Inmueble(long idInmueble, String titulo,
                    float precio, String descripcion,
                    int metrosConstruidos,
                    int metrosUtiles, String ubicacion,
                    String zona, String fechaPublicacion,
                    int habitaciones, int bannos) {
        this.idInmueble = new SimpleLongProperty(idInmueble);
        this.titulo = new SimpleStringProperty(titulo);
        this.precio = new SimpleFloatProperty(precio);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.metrosConstruidos = new SimpleIntegerProperty(metrosConstruidos);
        this.metrosUtiles = new SimpleIntegerProperty(metrosUtiles);
        this.ubicacion = new SimpleStringProperty(ubicacion);
        this.zona = new SimpleStringProperty(zona);
        this.fechaPublicacion = new SimpleStringProperty(fechaPublicacion);
        this.habitaciones = new SimpleIntegerProperty(habitaciones);
        this.bannos = new SimpleIntegerProperty(bannos);

    }


    public void formatDate(){
        String [] dts = getFechaPublicacion().split("-");
        this.fechaPublicacion.set(dts[2]+"/"+dts[1]+"/"+dts[0]);
    }

    public static String dateToSQLDate(String date){
        String [] dts = date.split("/");
        return dts[2]+"-"+dts[1]+"-"+  (dts[0].length() == 1? "0" + dts[0]: dts[0]);
    }

    public Inmueble(String titulo){
        this.titulo = new SimpleStringProperty(titulo);
    }

    public long getId() {
        return idInmueble.get();
    }

    public LongProperty idProperty() {
        return idInmueble;
    }

    public void setId(long idInmueble) {
        this.idInmueble.set(idInmueble);
    }

    public String getTitulo() {
        return titulo.get();
    }

    public StringProperty tituloProperty() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }

    public float getPrecio() {
        return precio.get();
    }

    public FloatProperty precioProperty() {
        return precio;
    }

    public void setPecio(float pecio) {
        this.precio.set(pecio);
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public int getMetrosConstruidos() {
        return metrosConstruidos.get();
    }

    public IntegerProperty metrosConstruidosProperty() {
        return metrosConstruidos;
    }

    public void setMetrosConstruidos(int metrosConstruidos) {
        this.metrosConstruidos.set(metrosConstruidos);
    }

    public int getMetrosUtiles() {
        return metrosUtiles.get();
    }

    public IntegerProperty metrosUtilesProperty() {
        return metrosUtiles;
    }

    public void setMetrosUtiles(int metrosUtiles) {
        this.metrosUtiles.set(metrosUtiles);
    }

    public String getUbicacion() {
        return ubicacion.get();
    }

    public StringProperty ubicacionProperty() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion.set(ubicacion);
    }

    public String getZona() {
        return zona.get();
    }

    public StringProperty zonaProperty() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona.set(zona);
    }

    public void setPrecio(float precio) {
        this.precio.set(precio);
    }

    public String getFechaPublicacion() {
        return fechaPublicacion.get();
    }

    public StringProperty fechaPublicacionProperty() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion.set(fechaPublicacion);
    }

    public int getHabitaciones() {
        return habitaciones.get();
    }

    public IntegerProperty habitacionesProperty() {
        return habitaciones;
    }

    public void setHabitaciones(int habitaciones) {
        this.habitaciones.set(habitaciones);
    }

    public int getBannos() {
        return bannos.get();
    }

    public IntegerProperty bannosProperty() {
        return bannos;
    }

    public void setBannos(int bannos) {
        this.bannos.set(bannos);
    }


}
