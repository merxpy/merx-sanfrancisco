/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import conexion.conexion;
import extras.funciones;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@WebServlet(name = "InformeCaja", urlPatterns = {"/InformeCaja"})
public class InformeCaja extends HttpServlet {

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
        ServletOutputStream out = response.getOutputStream();
        Connection con = conexion.getConnection();
        File reporte = new File(getServletContext().getRealPath("Reportes/informe_caja.jasper"));
        JasperPrint jasperPrint;
        try {
            JasperReport rep = (JasperReport) JRLoader.loadObject(reporte);
            Map parametros = new HashMap();
            String query = !request.getParameter("sucursal").equalsIgnoreCase("TODAS") ? " AND sucursal.id_sucursal=" + request.getParameter("sucursal") : "";
            query += !request.getParameter("tipo_pago").equalsIgnoreCase("TODAS") ? " AND detalle_caja.tipo_pago=" + request.getParameter("tipo_pago") : "";
            parametros.put("inicio", f.ParseDate(request.getParameter("inicio")));
            parametros.put("fin", f.ParseDate(request.getParameter("fin")));
            parametros.put("QUERY", !request.getParameter("tipo_pago").equalsIgnoreCase("TODAS") ? " AND detalle_caja.tipo_pago=" + request.getParameter("tipo_pago") : "");

            parametros.put("SQL", String.format("SELECT SUM(IFNULL(pagos.monto_pago, IFNULL(detalle_caja.debe_caja, 0))) AS ingresos, "
                    + "SUM(IFNULL(compras.total_compra, IFNULL(gastos.total_gasto, IFNULL(detalle_caja.haber_caja, 0)))) AS egresos "
                    + "FROM detalle_caja LEFT JOIN pedidos ON pedidos.id_pedido=detalle_caja.id_pedido "
                    + "LEFT JOIN compras ON compras.id_compra=detalle_caja.id_compra LEFT JOIN gastos ON gastos.id_gasto=detalle_caja.id_gasto "
                    + "LEFT JOIN pagos ON pagos.id_pedido=pedidos.id_pedido LEFT JOIN caja ON caja.id_caja=detalle_caja.id_caja "
                    + "LEFT JOIN sucursal ON sucursal.id_sucursal=caja.id_sucursal WHERE detalle_caja.fecha_detalle BETWEEN '%s' AND '%s' %s",
                    f.ParseDate(request.getParameter("inicio")), f.ParseDate(request.getParameter("fin")), query));

            parametros.put("PSQL", String.format("SELECT detalle_caja.id_caja, detalle_caja.fecha_detalle, sucursal.nombre_sucursal FROM detalle_caja, caja, sucursal "
                    + "WHERE caja.id_caja=detalle_caja.id_caja AND detalle_caja.fecha_detalle BETWEEN '%s' AND '%s' "
                    + "AND sucursal.id_sucursal=caja.id_sucursal %s GROUP BY detalle_caja.fecha_detalle, "
                    + "detalle_caja.id_caja ORDER BY `detalle_caja`.`fecha_detalle` DESC ", f.ParseDate(request.getParameter("inicio")),
                    f.ParseDate(request.getParameter("fin")), query));

            parametros.put("SUBREPORT_DIR", getServletContext().getRealPath("Reportes/subreporte_caja.jasper"));
            parametros.put("SUBSUBREPORT_DIR", getServletContext().getRealPath("Reportes/subsubreporte_caja.jasper"));
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
