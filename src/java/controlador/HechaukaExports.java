/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.DAOdeclaraciones;
import dao.DAOempresa;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.compras;
import modelo.empresa;
import modelo.ventas;

/**
 *
 * @author 2HDEV
 */
@WebServlet(name = "HechaukaExports", urlPatterns = {"/HechaukaExports"})
public class HechaukaExports extends HttpServlet {

    DAOempresa em = new DAOempresa();
    DAOdeclaraciones decla = new DAOdeclaraciones();

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
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Pragma", "public");
        String accion = request.getParameter("tipo_info");
        String mes = Integer.valueOf(request.getParameter("mes")) < 10 ? "0" + request.getParameter("mes") : request.getParameter("mes");
        HttpSession ok = request.getSession();
        int id_usuario = Integer.valueOf(ok.getAttribute("user").toString());
        empresa empresa = em.MostrarEmpresa(id_usuario);
        response.setHeader("Content-Disposition", "attachment; filename=" + empresa.getRepresentante_legal().replace(" ", "_") + "_" + accion + "_" + request.getParameter("periodo") + "_" + mes + ".txt");
        OutputStream out;
        int l;
        byte[] b = new byte[16 * 1024 * 1024];
        File temp = new File("xvideos.txt");
        if (accion.equalsIgnoreCase("compras")) {
            ArrayList<compras> comp = decla.ListarComprasHechauka(Integer.valueOf(request.getParameter("mes")), Integer.valueOf(request.getParameter("periodo")));
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {
                bw.write("1\t" + request.getParameter("periodo") + mes + "\t1\t911\t211\t" + empresa.getRuc_empresa() + "\t" + empresa.getDv_empresa() + "\t" + empresa.getRepresentante_legal() + "\t\t0\t\t" + comp.size() + "\t" + decla.TotalGravCompras(Integer.valueOf(request.getParameter("mes")), Integer.valueOf(request.getParameter("periodo"))) + "\tNO\t2");
                for (compras c : comp) {
                    bw.newLine();
                    int tipo = 1;
                    int cuotas = 0;
                    if (c.getTipo_compra().equals("crédito")) {
                        tipo = 2;
                        cuotas = 1;
                    }
                    bw.write("2\t" + c.getNdocumento_pro() + "\t" + c.getDv_pro() + "\t" + c.getNombre_pro() + "\t" + c.getTimbrado_compra() + "\t1\t" + c.getFactura_compra() + "\t" + c.getFecha_compra() + "\t" + c.getGrav10_compra() + "\t" + c.getIva10_compra() + "\t" + c.getGrav5_compra() + "\t" + c.getIva5_compra() + "\t" + c.getExenta_compra() + "\t0\t" + tipo + "\t" + cuotas);
                }
            }
            try (InputStream in = new FileInputStream(temp)) {
                out = response.getOutputStream();
                while ((l = in.read(b)) > 0) {
                    out.write(b, 0, l);
                }
            }
            out.close();
        } else if (accion.equalsIgnoreCase("ventas")) {
            ArrayList<ventas> venta = decla.ListarVentasHechauka(Integer.valueOf(request.getParameter("mes")), Integer.valueOf(request.getParameter("periodo")));
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {
                bw.write("1\t" + request.getParameter("periodo") + mes + "\t1\t921\t221\t" + empresa.getRuc_empresa() + "\t" + empresa.getDv_empresa() + "\t" + empresa.getRepresentante_legal() + "\t\t0\t\t" + venta.size() + "\t" + decla.TotalGravVentas(Integer.valueOf(request.getParameter("mes")), Integer.valueOf(request.getParameter("periodo"))) + "\t2");
                for (ventas v : venta) {
                    bw.newLine();
                    int tipo = 1;
                    int cuotas = 0;
                    if (v.getTipo_venta().equals("2")) {
                        tipo = 2;
                        cuotas = 1;
                    }
                    bw.write("2\t" + v.getNdocumento_cli() + "\t" + v.getDv_cli() + "\t" + v.getNombre_cli() + "\t1\t001-002-" + v.getNfactura_venta() + "\t" + v.getFecha_venta() + "\t" + v.getGrav10_venta() + "\t" + v.getTotaliva10() + "\t" + v.getGrav5_venta() + "\t" + v.getTotaliva5() + "\t" + v.getExenta_venta() + "\t" + v.getTotal_venta() + "\t" + tipo + "\t" + cuotas + "\t" + empresa.getTimbrado_empresa());
                }
            }
            try (InputStream in = new FileInputStream(temp)) {
                out = response.getOutputStream();
                while ((l = in.read(b)) > 0) {
                    out.write(b, 0, l);
                }
            }
            out.close();
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
