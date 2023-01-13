package com.inmohernandez.cliente;

import com.inmohernandez.cliente.dao.InmuebleDAO;
import com.inmohernandez.cliente.utils.Utils;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
class TestUnitarioInmueble {

    @Test
    @Before
    public void testInsertarInmueble(){
        StringBuilder bodyJson= new StringBuilder();
        bodyJson.append("{");
        bodyJson.append("\"titulo\" : \"" + titulo.get() + "\", ");
        bodyJson.append("\"precio\" : " + precio.get() + ", ");
        bodyJson.append("\"descripcion\" : \"" + descripcion.get() + "\", ");
        bodyJson.append("\"metrosConstruidos\" : " + m2.get() + ", ");
        bodyJson.append("\"metrosUtiles\" : " + m2utiles.get() + ", ");
        bodyJson.append("\"ubicacion\" : \"" + ubicacion.get() + "\", ");
        bodyJson.append("\"zona\" : \"" + (zona.get().equals("Todas las zonas") ? "" : zona.get()) + "\", ");
        bodyJson.append("\"fechaPublicacion\" : \"" + Utils.strToSQLDate(publicacion.get()) + "\", ");
        bodyJson.append("\"habitaciones\" : " + habitaciones.get() + ", ");
        bodyJson.append("\"bannos\" : " + bannos.get());
        bodyJson.append("}");
        InmuebleDAO.postInmuebleByIdInDB(null,bodyJson);
    }
}
