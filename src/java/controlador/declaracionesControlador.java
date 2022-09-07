/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOdeclaraciones;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author 2HDEV
 */
@WebServlet(name = "declaracionesControlador", urlPatterns = {"/declaracionesControlador"})
public class declaracionesControlador extends HttpServlet {

    DAOdeclaraciones dao = new DAOdeclaraciones();

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
        Gson gson = new Gson();
        String accion = request.getParameter("libro");
        if (accion.equalsIgnoreCase("ventas")) {
            pw.println(gson.toJson(dao.Periodos("ventas", "venta")));
        } else if (accion.equalsIgnoreCase("compras")) {
            pw.println(gson.toJson(dao.Periodos("compras", "compra")));
        } else if (accion.equalsIgnoreCase("mesesventas")) {
            pw.println(gson.toJson(dao.Meses("ventas", "venta", request.getParameter("periodo"))));
        } else if (accion.equalsIgnoreCase("mesescompras")) {
            pw.println(gson.toJson(dao.Meses("compras", "compra", request.getParameter("periodo"))));
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
