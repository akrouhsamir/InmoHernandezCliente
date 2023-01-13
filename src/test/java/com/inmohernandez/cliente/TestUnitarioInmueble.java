package com.inmohernandez.cliente;

import com.inmohernandez.cliente.dao.InmuebleDAO;
import com.inmohernandez.cliente.models.Inmueble;
import com.inmohernandez.cliente.utils.Utils;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestUnitarioInmueble {
    long id;
    @Test
    @BeforeAll
    public void testInsertarInmueble(){
        String titulo = "Terraza de campotest";
        float precio = 130000f;
        String descripcion = "Piso de prueba";
        int metros = 120;
        int metrosUtiles= 110;
        String ubicacion = "Calle test";
        String zona = "Almerimar";
        String fechaPublicacion = "06/06/2020";
        int habitaciones = 3;
        int bannos = 2;

        StringBuilder bodyJson= new StringBuilder();
        bodyJson.append("{");
        bodyJson.append("\"titulo\" : \"" + titulo + "\", ");
        bodyJson.append("\"precio\" : " + precio + ", ");
        bodyJson.append("\"descripcion\" : \"" + descripcion + "\", ");
        bodyJson.append("\"metrosConstruidos\" : " + metros + ", ");
        bodyJson.append("\"metrosUtiles\" : " + metrosUtiles + ", ");
        bodyJson.append("\"ubicacion\" : \"" + ubicacion + "\", ");
        bodyJson.append("\"zona\" : \"" + zona + "\", ");
        bodyJson.append("\"fechaPublicacion\" : \"" + Utils.strToSQLDate(fechaPublicacion) + "\", ");
        bodyJson.append("\"habitaciones\" : " + habitaciones + ", ");
        bodyJson.append("\"bannos\" : " + bannos);
        bodyJson.append("}");
        Inmueble  inmueble = InmuebleDAO.postInmuebleByIdInDB(null,bodyJson.toString());

        id = inmueble.getId();
        Assertions.assertEquals(titulo,inmueble.getTitulo());
        Assertions.assertEquals(fechaPublicacion,Utils.sqlDateToEUDate(inmueble.getFechaPublicacion()));
        Assertions.assertEquals(Float.valueOf(precio),inmueble.getPrecio());
        Assertions.assertEquals(zona,inmueble.getZona());
        Assertions.assertEquals(ubicacion,inmueble.getUbicacion());
        Assertions.assertEquals(habitaciones,inmueble.getHabitaciones());
        Assertions.assertEquals(bannos,inmueble.getBannos());
        Assertions.assertEquals(metros,inmueble.getMetrosConstruidos());
        Assertions.assertEquals(metrosUtiles,inmueble.getMetrosUtiles());
        Assertions.assertEquals(descripcion,inmueble.getDescripcion());
    }

    @Test
    public void testBorrarInmueble(){
        int affectedRows = InmuebleDAO.removeInmuebleByIdInDB(String.valueOf(id));
        Assertions.assertEquals(1,affectedRows);
    }
}
