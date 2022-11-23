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
    @Expose
    @JsonAdapter(FloatPropertyAdapter.class)
    public FloatProperty mensualidad;
    @Expose
    @JsonAdapter(LongPropertyAdapter.class)
    public LongProperty idInmueble;

}
