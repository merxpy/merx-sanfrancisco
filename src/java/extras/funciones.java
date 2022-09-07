/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extras;

import dao.DAOempresa;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterName;
import modelo.empresa;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

/**
 *
 * @author akio
 */
public class funciones {

    public String NoPosee(String s) {
        if ("".equals(s) || s == null) {
            s = "-";
        }
        return s;
    }

    public String IsInt(String s) {
        if ("".equals(s) || s == null) {
            s = "0";
        }
        return s;
    }

    public String VeriId(String s) {
        if ("".equals(s) || s == null) {
            s = null;
        }
        return s;
    }

    public String ParseDI(String d) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dfsql = new SimpleDateFormat("yyyy-MM-dd");
        String r = "";
        try {
            Date date = dfsql.parse(d);
            r = df.format(date);
        } catch (ParseException ex) {
            Logger.getLogger(funciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    public String ParseDate(String d) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dfsql = new SimpleDateFormat("yyyy-MM-dd");
        String r = "";
        try {
            Date date = df.parse(d);
            r = dfsql.format(date);
        } catch (ParseException ex) {
            Logger.getLogger(funciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    public boolean esNumero(String a) {
        try {
            double d = Double.parseDouble(a);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public String Entero(String numero) {
        String x = numero != null ? numero.replace(".", "") : "0";
        return x;
    }

    public String FechaFactura(String d) {
        String fecha = null;
        try {
            DateFormat dfsql = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
            Date date;
            date = dfsql.parse(d);
            fecha = formateador.format(date);
        } catch (ParseException ex) {
            Logger.getLogger(funciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fecha.toUpperCase();
    }

    public String FormatearNroFactura(String n) {
        String nformated = "000000";
        String res = nformated.substring(0, 7 - n.length()) + n;
        return res;
    }

    public String ParseDHI(String d) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        DateFormat dfsql = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String r = "";
        try {
            Date date = dfsql.parse(d);
            r = df.format(date);
        } catch (ParseException ex) {
            Logger.getLogger(funciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    public Date ParseDT(String d) {
        DateFormat dfsql = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = null;
        try {
            date = dfsql.parse(d);
        } catch (ParseException ex) {
            Logger.getLogger(funciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public static final void PrintReportToPrinter(JasperPrint jp, int id_usuario) throws JRException {
        // TODO Auto-generated method stub
        DAOempresa dao = new DAOempresa();
        empresa emp = dao.MostrarEmpresa(id_usuario);
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        printRequestAttributeSet.add(OrientationRequested.PORTRAIT);
        //printRequestAttributeSet.add(new PrinterResolution(300, 300, PrinterResolution.DPI));
        //printRequestAttributeSet.add(MediaSizeName.ISO_A4); //setting page size
        printRequestAttributeSet.add(new Copies(1));
        PrinterName printerName = new PrinterName(emp.getImpresora(), null); //gets printer 

        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
        printServiceAttributeSet.add(printerName);

        JRPrintServiceExporter exporter = new JRPrintServiceExporter();

        exporter.setExporterInput(new SimpleExporterInput(jp));
        SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
        configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
        configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
        configuration.setDisplayPageDialog(false);
        configuration.setDisplayPrintDialog(false);
        exporter.setConfiguration(configuration);
        exporter.exportReport();
    }
}
