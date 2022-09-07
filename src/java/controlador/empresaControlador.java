/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOempresa;
import static extras.PrinterUtility.getPrinterServiceNameList;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.empresa;

/**
 *
 * @author akio
 */
@WebServlet(name = "empresaControlador", urlPatterns = {"/empresaControlador"})
public class empresaControlador extends HttpServlet {

    DAOempresa dao = new DAOempresa();

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
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter pw = response.getWriter();
        String accion = request.getParameter("accion");
        HttpSession ok = request.getSession();
        int id_usuario = Integer.valueOf(ok.getAttribute("user").toString());
        Gson gson = new Gson();
        if (accion.equalsIgnoreCase("mostrar")) {
            pw.println(gson.toJson(dao.MostrarEmpresa(id_usuario)));
        } else if (accion.equalsIgnoreCase("insertar")) {
            empresa e = gson.fromJson(request.getParameter("datos"), empresa.class);
            e.setId_usuario(ok.getAttribute("user").toString());
            if (e.getId_empresa() == 0) {
                pw.println(dao.AgregarEmpresa(e));
            } else {
                pw.println(dao.ActualizarEmpresa(e));
            }
        } else if (accion.equalsIgnoreCase("verificar")) {
            pw.println(dao.VerificarTimbrado(id_usuario));
        } else if (accion.equalsIgnoreCase("impresoras")) {
            pw.println(gson.toJson(getPrinterServiceNameList()));
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
