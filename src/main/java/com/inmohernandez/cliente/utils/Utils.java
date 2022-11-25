package com.inmohernandez.cliente.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String strToSQLDate(String str){
        Date date;
        String result;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(str);
            result = new SimpleDateFormat("yyyy-MM-dd").format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String sqlDateToEUDate(String str){
        Date date;
        String result;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
            result = new SimpleDateFormat("dd/MM/yyyy").format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
