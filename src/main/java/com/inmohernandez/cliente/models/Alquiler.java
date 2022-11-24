package com.inmohernandez.cliente.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.inmohernandez.cliente.dao.FloatPropertyAdapter;
import com.inmohernandez.cliente.dao.LongPropertyAdapter;
import com.inmohernandez.cliente.dao.StringPropertyAdapter;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;

public class Alquiler {
    @Expose
    @JsonAdapter(LongPropertyAdapter.class)
    public LongProperty id;
    @Expose
    @JsonAdapter(StringPropertyAdapter.class)
    public StringProperty cliente;
    @Expose
    @JsonAdapter(StringPropertyAdapter.class)
    public StringProperty fechaInicio;
    @Expose
    @JsonAdapter(StringPropertyAdapter.class)
    public StringProperty fechaFin;

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getCliente() {
        return cliente.get();
    }

    public StringProperty clienteProperty() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente.set(cliente);
    }

    public String getFechaInicio() {
        return fechaInicio.get();
    }

    public StringProperty fechaInicioProperty() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio.set(fechaInicio);
    }

    public String getFechaFin() {
        return fechaFin.get();
    }

    public StringProperty fechaFinProperty() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin.set(fechaFin);
    }

    public float getMensualidad() {
        return mensualidad.get();
    }

    public FloatProperty mensualidadProperty() {
        return mensualidad;
    }

    public void setMensualidad(float mensualidad) {
        this.mensualidad.set(mensualidad);
    }

    public long getIdInmueble() {
        return idInmueble.get();
    }

    public LongProperty idInmuebleProperty() {
        return idInmueble;
    }

    public void setIdInmueble(long idInmueble) {
        this.idInmueble.set(idInmueble);
    }

    @Override
    public String toString() {
        return "Alquiler{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", mensualidad=" + mensualidad +
                ", idInmueble=" + idInmueble +
                '}';
    }

    @Expose
    @JsonAdapter(FloatPropertyAdapter.class)
    public FloatProperty mensualidad;
    @Expose
    @JsonAdapter(LongPropertyAdapter.class)
    public LongProperty idInmueble;

}
