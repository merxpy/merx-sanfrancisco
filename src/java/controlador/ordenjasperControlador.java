/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import conexion.conexion;
import dao.DAOordencompra;
import extras.funciones;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.ordendecompra;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

/**
 *
 * @author 2HDEV
 */
@WebServlet(name = "ordenjasperControlador", urlPatterns = {"/orden"})
public class ordenjasperControlador extends HttpServlet {

    DAOordencompra dao = new DAOordencompra();
    funciones f = new funciones();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/pdf");
        HttpSession ok = request.getSession();
        ServletOutputStream out = response.getOutputStream();
        Connection con = conexion.getConnection();
        File reporte = new File(getServletContext().getRealPath("Reportes/orden_compra.jasper"));
        ordendecompra ord = dao.BuscarOrdenCompra(request.getParameter("parametro"));
        try {
            JasperReport rep = (JasperReport) JRLoader.loadObject(reporte);
            Map parametros = new HashMap();
            parametros.put("id_orden", Integer.valueOf(request.getParameter("parametro")));
            parametros.put("fecha_ord", ord.getFecha_ord());
            parametros.put("IMGCAB_DIR", getServletContext().getRealPath("img/ordencab.png"));
            if (ord.getId_orden_compra() != null) {
                parametros.put("nro_orden", f.FormatearNroFactura(ord.getId_orden_compra()));
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(rep, parametros, con);
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            exporter.exportReport();

        } catch (NumberFormatException | JRException e) {
            Logger.getLogger(ordencompraControlador.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            conexion.Close(con);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
