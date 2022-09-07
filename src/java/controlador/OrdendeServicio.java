/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import conexion.conexion;
import dao.DAOempresa;
import dao.DAOpedidos;
import extras.funciones;
import static extras.funciones.PrintReportToPrinter;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
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
import modelo.empresa;
import modelo.pedidos;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author 2HDEV
 */
@WebServlet(name = "OrdendeServicio", urlPatterns = {"/orden_de_servicio"})
public class OrdendeServicio extends HttpServlet {

    DAOpedidos dao = new DAOpedidos();
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
        response.setContentType("application/pdf;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        Connection con = conexion.getConnection();
        File reporte = new File(getServletContext().getRealPath("Reportes/orden_trabajo.jasper"));
        String accion = request.getParameter("accion");
        HttpSession ok = request.getSession();
        int id_usuario = Integer.valueOf(ok.getAttribute("user").toString());
        pedidos ped = dao.BuscarNroClienteOrden(Integer.valueOf(request.getParameter("parametro")));
        JasperPrint jasperPrint;
        try {
            JasperReport rep = (JasperReport) JRLoader.loadObject(reporte);
            Map parametros = new HashMap();
            parametros.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));
            parametros.put("id_pedido", Integer.valueOf(request.getParameter("parametro")));
            parametros.put("SUBREPORT_DIR", getServletContext().getRealPath("Reportes/suborden_trabajo.jasper"));
            parametros.put("IMAGE_DIR", getServletContext().getRealPath("img/san_francisco.png"));
            jasperPrint = JasperFillManager.fillReport(rep, parametros, con);
            if (accion.equalsIgnoreCase("orden")) {
                response.setHeader("Content-Disposition", "inline;filename=" + f.FormatearNroFactura(request.getParameter("parametro")) + "-" + ped.getNombre_cli());
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            } else if (accion.equalsIgnoreCase("imprimir")) {
                PrintReportToPrinter(jasperPrint, id_usuario);
            } else if (accion.equalsIgnoreCase("etiqueta")) {
                response.setHeader("Content-Disposition", "inline;filename=Orden_N_" + f.FormatearNroFactura(request.getParameter("parametro")) + "-" + ped.getNombre_cli().replace(" ", "_"));
                reporte = new File(getServletContext().getRealPath("Reportes/etiqueta_orden.jasper"));
                rep = (JasperReport) JRLoader.loadObject(reporte);
                parametros = new HashMap();
                parametros.put("id_pedido", Integer.valueOf(request.getParameter("parametro")));
                parametros.put("IMAGE_DIR", getServletContext().getRealPath("img/san_francisco.png"));
                jasperPrint = JasperFillManager.fillReport(rep, parametros, con);
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            } else if (accion.equalsIgnoreCase("imprimir_etiqueta")) {
                System.out.println("HOLA ");
                reporte = new File(getServletContext().getRealPath("Reportes/etiqueta_orden.jasper"));
                rep = (JasperReport) JRLoader.loadObject(reporte);
                parametros = new HashMap();
                parametros.put("id_pedido", Integer.valueOf(request.getParameter("parametro")));
                parametros.put("IMAGE_DIR", getServletContext().getRealPath("img/san_francisco.png"));
                System.out.println("Imagen "+getServletContext().getRealPath("img/san_francisco.png"));
                jasperPrint = JasperFillManager.fillReport(rep, parametros, con);
                PrintReportToPrinter(jasperPrint, id_usuario);
            }
        } catch (NumberFormatException | JRException e) {
            e.printStackTrace();
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
