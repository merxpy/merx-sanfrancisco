/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import conexion.conexion;
import dao.DAOauditoria;
import dao.DAOempresa;
import dao.DAOusuarios;
import dao.DAOventas;
import extras.NumeroaLetras;
import extras.funciones;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
import modelo.empresa;
import modelo.usuarios;
import modelo.ventas;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author akio
 */
@WebServlet(name = "jasperControlador", urlPatterns = {"/merx"})
public class jasperControlador extends HttpServlet {

    DAOventas dao = new DAOventas();
    DAOusuarios usuario = new DAOusuarios();
    DAOempresa empresa = new DAOempresa();
    funciones f = new funciones();
    DAOauditoria aud = new DAOauditoria();

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
        Connection con = conexion.getConnection();
        File reporte = new File(getServletContext().getRealPath("Reportes/factura_sanfrancisco.jasper"));
        ventas v = dao.BuscarVentas(request.getParameter("parametro"));
        HttpSession ok = request.getSession();
        int id_usuario = Integer.valueOf(ok.getAttribute("user").toString());
        empresa emp = empresa.MostrarEmpresa(id_usuario);
        String accion = request.getParameter("accion") != null ? request.getParameter("accion") : "generar";
        JasperPrint jasperPrint;
        usuarios usu = usuario.BuscarUsuarios(ok.getAttribute("user").toString());
        v.setId_usuario(ok.getAttribute("user").toString());
        try {
            JasperReport rep = (JasperReport) JRLoader.loadObject(reporte);
            Map parametros = new HashMap();
            parametros.put("idventa", Integer.valueOf(request.getParameter("parametro")));
            parametros.put("fecha_venta", f.FechaFactura(v.getFecha_venta()));
            parametros.put("nfactura_venta", f.FormatearNroFactura(v.getNfactura_venta()));
            parametros.put("SUBREPORT_DIR", getServletContext().getRealPath("Reportes/subfactura_sanfrancisco.jasper"));
            parametros.put("total_letras", NumeroaLetras.cantidadConLetra(v.getTotal_venta()));
            parametros.put("vendedor", usu.getNombre_emp() + " " + usu.getApellido_emp());
            parametros.put("timbrado", String.valueOf(emp.getTimbrado_empresa()));
            parametros.put("validez", emp.getInicio_vigencia() + " al " + emp.getFin_vigencia());
            parametros.put("inicio", emp.getInicio_vigencia());
            parametros.put("fin", emp.getFin_vigencia());
            parametros.put("ruc", emp.getRuc_empresa() + "-" + emp.getDv_empresa());
            if (accion.equalsIgnoreCase("generar")) {
                response.setContentType("application/pdf");
                ServletOutputStream out = response.getOutputStream();
                if (v.getImpreso() == 0) {
                    dao.ActualizarEstadoImpresion(Integer.valueOf(request.getParameter("parametro")));
                    jasperPrint = JasperFillManager.fillReport(rep, parametros, con);
                } else {
                    jasperPrint = JasperFillManager.fillReport(rep, parametros, con);
                }
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
                exporter.exportReport();
            } else if (accion.equalsIgnoreCase("reimprimir")) {
                aud.beforeReimprimirVenta(v);
                PrintWriter pw = response.getWriter();
                response.setContentType("application/json;charset=UTF-8");
                JSONObject obj = new JSONObject();
                try {
                    obj.put("idventa", Integer.valueOf(request.getParameter("parametro")));
                    obj.put("fecha_venta", f.FechaFactura(v.getFecha_venta()));
                    obj.put("nfactura_venta", f.FormatearNroFactura(v.getNfactura_venta()));
                    obj.put("total_letras", NumeroaLetras.cantidadConLetra(v.getTotal_venta()));
                    obj.put("vendedor", usu.getNombre_emp() + " " + usu.getApellido_emp());
                    obj.put("autoimpresor_fa", "");
                    obj.put("autoimpresor_so", "");
                    obj.put("report", "factura_sanfrancisco.jasper");
                    obj.put("img", "san_isidro.png");
                    obj.put("subreport", "subfactura_sanfrancisco.jasper");
                    obj.put("no_valido", "");
                    obj.put("db", "lavanderia");
                    obj.put("printer", emp.getImpresora());
                } catch (JSONException ex) {
                    Logger.getLogger(jasperControlador.class.getName()).log(Level.SEVERE, null, ex);
                }
                pw.println(obj);
            } else {
                PrintWriter pw = response.getWriter();
                response.setContentType("application/json;charset=UTF-8");
                JSONObject obj = new JSONObject();
                aud.beforeReimprimirVenta(v);
                try {
                    obj.put("idventa", Integer.valueOf(request.getParameter("parametro")));
                    obj.put("fecha_venta", f.FechaFactura(v.getFecha_venta()));
                    obj.put("nfactura_venta", f.FormatearNroFactura(v.getNfactura_venta()));
                    obj.put("total_letras", NumeroaLetras.cantidadConLetra(v.getTotal_venta()));
                    obj.put("vendedor", usu.getNombre_emp() + " " + usu.getApellido_emp());
                    obj.put("autoimpresor_fa", "");
                    obj.put("autoimpresor_so", "");
                    obj.put("report", "factura_sanfrancisco.jasper");
                    obj.put("subreport", "subfactura_sanfrancisco.jasper");
                    obj.put("img", "san_isidro.png");
                    obj.put("no_valido", "");
                    obj.put("db", "lavanderia");
                    obj.put("printer", emp.getImpresora());
                } catch (JSONException ex) {
                    Logger.getLogger(jasperControlador.class.getName()).log(Level.SEVERE, null, ex);
                }
                pw.println(obj);
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
