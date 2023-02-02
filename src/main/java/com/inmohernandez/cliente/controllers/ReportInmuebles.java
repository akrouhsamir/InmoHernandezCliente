package com.inmohernandez.cliente.controllers;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.swing.JRViewer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JFrame;

public class ReportInmuebles extends JFrame {
    public void showReportConSubreport(int idInmueble) throws JRException, ClassNotFoundException, SQLException {

        String servidor = "jdbc:mariadb://localhost:5555/inmohernandezdb?useSSL=false";
        String usuario = "root";
        String passwd = "adminer";

        Connection conexionBBDD;

        conexionBBDD = DriverManager.getConnection(servidor, usuario, passwd);


        String reportSrcFile = "src/main/java/com/inmohernandez/cliente/controllers/InmuebleMain.jrxml";
        String subReportSrcFile = "src/main/java/com/inmohernandez/cliente/controllers/InmuebleDetalle.jrxml";

        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        JasperReport jasperSubReport = JasperCompileManager.compileReport(subReportSrcFile);


        HashMap<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("Subreport", jasperSubReport);
        parameters.put("CodInmuebleMain", idInmueble);

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conexionBBDD);
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(1000, 800);
        this.setVisible(true);
    }

    public void showReport() throws JRException, ClassNotFoundException, SQLException {

        String servidor = "jdbc:mariadb://localhost:5555/inmohernandezdb?useSSL=false";
        String usuario = "root";
        String passwd = "adminer";

        Connection conexionBBDD;

        conexionBBDD = DriverManager.getConnection(servidor, usuario, passwd);


        String reportSrcFile = "src/main/java/com/inmohernandez/cliente/controllers/InmuebleGeneral.jrxml";


        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);

        HashMap<String, Object> parameters = new HashMap<String, Object>();

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conexionBBDD);
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(1000, 800);
        this.setVisible(true);
    }
}
