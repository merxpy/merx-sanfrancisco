/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import conexion.conexion;
import dao.DAOcategorias;
import dao.DAOsucursal;
import dao.DAOusuarios;
import extras.funciones;
import java.io.File;
import java.io.IOException;
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
import javax.servlet.http.HttpSession;
import modelo.categorias;
import modelo.sucursales;
import modelo.usuarios;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
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
@WebServlet(name = "PrendasControlador", urlPatterns = {"/Prendas"})
public class PrendasControlador extends HttpServlet {

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
        funciones f = new funciones();
        HttpSession ok = request.getSession();
        DAOusuarios user = new DAOusuarios();
        DAOcategorias cat = new DAOcategorias();
        categorias categoria = new categorias();
        DAOsucursal sucursales = new DAOsucursal();
        usuarios usuario = user.BuscarUsuarios(ok.getAttribute("user").toString());
        sucursales sucursal = new sucursales();
        ServletOutputStream out = response.getOutputStream();
        Connection con = conexion.getConnection();
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("resumido")) {
            File reporte = new File(getServletContext().getRealPath("Reportes/articulos.jasper"));
            String sql = "SELECT codigo, descripcion, SUM(cantidad) as cantidad, categoria, estado FROM articulos_atendidos WHERE 1";
            if (!request.getParameter("start").equals("") && !request.getParameter("end").equals("")) {
                sql += String.format(" AND fecha_ped BETWEEN '%s' AND '%s'", f.ParseDate(request.getParameter("start")), f.ParseDate(request.getParameter("end")));
            }

            if (!request.getParameter("estado").equals("TODOS")) {
                sql += String.format(" AND estado = '%s'", request.getParameter("estado"));
            }

            if (!request.getParameter("categoria").equals("TODAS")) {
                sql += " AND id_categoria = " + request.getParameter("categoria");
                categoria = cat.BuscarCategorias(Integer.valueOf(request.getParameter("categoria")));
            }

            if (!request.getParameter("sucursal").equals("TODAS")) {
                sql += " AND id_sucursal = " + request.getParameter("sucursal");
                sucursal = sucursales.BuscarSucursales(request.getParameter("sucursal"));
            }
            sql += " GROUP BY id_articulo";
            JasperPrint jasperPrint;
            try {
                JasperReport rep = (JasperReport) JRLoader.loadObject(reporte);
                Map parametros = new HashMap();
                parametros.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));
                parametros.put("periodo", request.getParameter("start") + " al " + request.getParameter("end"));
                parametros.put("usuario", usuario.getNombre_emp() + " " + usuario.getApellido_emp());
                parametros.put("estado", request.getParameter("estado"));
                parametros.put("categoria", categoria.getCategoria() != null ? categoria.getCategoria() : "TODAS");
                parametros.put("sucursal", sucursal.getNombre_sucursal() != null ? sucursal.getNombre_sucursal() : "TODAS");
                parametros.put("SQL", sql);
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
        } else if (accion.equalsIgnoreCase("detallado")) {
            File reporte = new File(getServletContext().getRealPath("Reportes/articulos_detallados.jasper"));
            String sql = "";
            if (!request.getParameter("start").equals("") && !request.getParameter("end").equals("")) {
                sql += String.format(" AND fecha_ped BETWEEN '%s' AND '%s'", f.ParseDate(request.getParameter("start")), f.ParseDate(request.getParameter("end")));
            }

            if (!request.getParameter("estado").equals("TODOS")) {
                sql += String.format(" AND estado = '%s'", request.getParameter("estado"));
            }

            if (!request.getParameter("categoria").equals("TODAS")) {
                sql += " AND id_categoria = " + request.getParameter("categoria");
                categoria = cat.BuscarCategorias(Integer.valueOf(request.getParameter("categoria")));
            }

            if (!request.getParameter("sucursal").equals("TODAS")) {
                sql += " AND id_sucursal = " + request.getParameter("sucursal");
                sucursal = sucursales.BuscarSucursales(request.getParameter("sucursal"));
            }
            sql += " GROUP BY id_articulo ORDER BY pedidos_id_pedido";
            JasperPrint jasperPrint;
            try {
                JasperReport rep = (JasperReport) JRLoader.loadObject(reporte);
                Map parametros = new HashMap();
                parametros.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));
                parametros.put("SUBREPORT_DIR", getServletContext().getRealPath("Reportes/subarticulos_detallados.jasper"));
                parametros.put("periodo", request.getParameter("start") + " al " + request.getParameter("end"));
                parametros.put("usuario", usuario.getNombre_emp() + " " + usuario.getApellido_emp());
                parametros.put("estado", request.getParameter("estado"));
                parametros.put("categoria", categoria.getCategoria() != null ? categoria.getCategoria() : "TODAS");
                parametros.put("sucursal", sucursal.getNombre_sucursal() != null ? sucursal.getNombre_sucursal() : "TODAS");
                parametros.put("SQL", sql);
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
