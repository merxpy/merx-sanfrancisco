/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import conexion.conexion;
import dao.DAOclientes;
import dao.DAOpedidos;
import extras.funciones;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.clientes;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

/**
 *
 * @author 2HDEV
 */
@WebServlet(name = "ResumenControlador", urlPatterns = {"/ResumenControlador"})
public class ResumenControlador extends HttpServlet {

    DAOpedidos dao;
    DAOclientes cli;

    public ResumenControlador() {
        super();
        dao = new DAOpedidos();
        cli = new DAOclientes();
    }

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
        funciones f = new funciones();
        response.setContentType("application/pdf;charset=UTF-8");
        Connection con = conexion.getConnection();
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("ver")) {
            response.setContentType("application/pdf;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            File reporte = new File(getServletContext().getRealPath("Reportes/reporte_orden.jasper"));
            JasperPrint jasperPrint;
            try {
                JasperReport rep = (JasperReport) JRLoader.loadObject(reporte);
                Map parametros = new HashMap();
                parametros.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));
                parametros.put("fecha_inicio", f.ParseDate(request.getParameter("fecha_inicio")));
                parametros.put("fecha_fin", f.ParseDate(request.getParameter("fecha_fin")));
                parametros.put("periodo", request.getParameter("fecha_inicio") + " al " + request.getParameter("fecha_fin"));
                parametros.put("IMAGE_DIR", getServletContext().getRealPath("img/encabezado_sansa.png"));
                parametros.put("id_cliente", Integer.valueOf(request.getParameter("cliente")));
                jasperPrint = JasperFillManager.fillReport(rep, parametros, con);
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
                exporter.exportReport();
            } catch (NumberFormatException | JRException e) {
                e.printStackTrace();
            } finally {
                conexion.Close(con);
            }
        } else if (accion.equalsIgnoreCase("validar")) {
            PrintWriter pw = response.getWriter();
            pw.println(dao.ValidarReporteOrden(f.ParseDate(request.getParameter("fecha_inicio")), f.ParseDate(request.getParameter("fecha_fin")), Integer.valueOf(request.getParameter("id_cliente"))));
        } else if (accion.equalsIgnoreCase("historial")) {
            response.setContentType("application/pdf;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            File reporte = new File(getServletContext().getRealPath("Reportes/historial_cliente.jasper"));
            String ids = request.getParameter("ids");
            JasperPrint jasperPrint;
            try {
                JasperReport rep = (JasperReport) JRLoader.loadObject(reporte);
                Map parametros = new HashMap();
                parametros.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));
                parametros.put("SQL_ORDEN", "SELECT clientes.nombre_cli, clientes.ndocumento_cli, clientes.dv_cli, SUM(pedidos.descuento_ped) as descuento_ped FROM clientes, pedidos WHERE pedidos.id_cliente=clientes.id_cliente AND pedidos.close_ped=0 AND pedidos.id_cliente=" + Integer.valueOf(request.getParameter("cliente")) + " AND pedidos.id_pedido IN (" + ids + ") GROUP BY pedidos.id_cliente");
                parametros.put("SQL", "SELECT detalle_pedidos.codigo, detalle_pedidos.descripcion, detalle_pedidos.descripcion_ar, detalle_pedidos.precio, SUM(detalle_pedidos.cantidad) as cantidad, SUM(detalle_pedidos.subtotal) as subtotal, SUM(pedidos.descuento_ped) as descuento_ped FROM detalle_pedidos, pedidos WHERE pedidos.id_pedido=detalle_pedidos.pedidos_id_pedido AND pedidos.close_ped=0 AND pedidos.id_cliente=" + Integer.valueOf(request.getParameter("cliente")) + " AND pedidos.id_pedido IN (" + ids + ") GROUP BY detalle_pedidos.articulos_id_articulo ");
                parametros.put("IMAGE_DIR", getServletContext().getRealPath("img/encabezado_sansa.png"));
                parametros.put("id_cliente", Integer.valueOf(request.getParameter("cliente")));
                jasperPrint = JasperFillManager.fillReport(rep, parametros, con);
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
                exporter.exportReport();
            } catch (NumberFormatException | JRException e) {
                e.printStackTrace();
            } finally {
                conexion.Close(con);
            }
        } else if (accion.equalsIgnoreCase("historialxls")) {
            response.setContentType("application/octet-stream");
            response.setHeader("Pragma", "public");
            clientes cliente = cli.BuscarClientes(request.getParameter("cliente"));
            response.setHeader("Content-Disposition", "attachment; filename=" + cliente.getNombre_cli().replaceAll(" ", "_") + ".xls");
            ServletOutputStream out = response.getOutputStream();
            File reporte = new File(getServletContext().getRealPath("Reportes/historial_cliente.jasper"));
            String ids = request.getParameter("ids");
            JasperPrint jasperPrint;
            try {
                JasperReport rep = (JasperReport) JRLoader.loadObject(reporte);
                Map parametros = new HashMap();
                parametros.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));
                parametros.put("SQL_ORDEN", "SELECT clientes.nombre_cli, clientes.ndocumento_cli, clientes.dv_cli, SUM(pedidos.descuento_ped) as descuento_ped FROM clientes, pedidos WHERE pedidos.id_cliente=clientes.id_cliente AND pedidos.close_ped=0 AND pedidos.id_cliente=" + Integer.valueOf(request.getParameter("cliente")) + " AND pedidos.id_pedido IN (" + ids + ") GROUP BY pedidos.id_cliente");
                parametros.put("SQL", "SELECT detalle_pedidos.codigo, detalle_pedidos.descripcion, detalle_pedidos.descripcion_ar, detalle_pedidos.precio, SUM(detalle_pedidos.cantidad) as cantidad, SUM(detalle_pedidos.subtotal) as subtotal, SUM(pedidos.descuento_ped) as descuento_ped FROM detalle_pedidos, pedidos WHERE pedidos.id_pedido=detalle_pedidos.pedidos_id_pedido AND pedidos.close_ped=0 AND pedidos.id_cliente=" + Integer.valueOf(request.getParameter("cliente")) + " AND pedidos.id_pedido IN (" + ids + ") GROUP BY detalle_pedidos.articulos_id_articulo ");
                parametros.put("IMAGE_DIR", getServletContext().getRealPath("img/encabezado_sansa.png"));
                parametros.put("id_cliente", Integer.valueOf(request.getParameter("cliente")));
                jasperPrint = JasperFillManager.fillReport(rep, parametros, con);
                JRXlsExporter exporter = new JRXlsExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
                SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
                configuration.setOnePagePerSheet(true);
                exporter.setConfiguration(configuration);
                exporter.exportReport();
            } catch (NumberFormatException | JRException e) {
                e.printStackTrace();
            } finally {
                conexion.Close(con);
            }
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
