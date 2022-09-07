/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.DAOanotacion;
import dao.DAOusuarios;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.usuarios;

/**
 *
 * @author akio
 */
@WebServlet(name = "loginControlador", urlPatterns = {"/loginControlador"})
public class loginControlador extends HttpServlet {

    private DAOusuarios dao;
    private DAOanotacion nota;

    public loginControlador() {
        dao = new DAOusuarios();
        nota = new DAOanotacion();
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
        response.setContentType("application/json;charset=UTF-8");
        String accion = request.getParameter("accion");
        usuarios u = new usuarios();
        PrintWriter writer = response.getWriter();
        HttpSession ok = request.getSession();
        if (accion == null) {
            u.setAlias_usu(request.getParameter("usuario"));
            u.setPassword_usu(request.getParameter("pass"));
            int r = dao.VerificarUsuario(u);
            if (r != 0 && r != -1) {
                ok.setMaxInactiveInterval(-1);
                ok.setAttribute("user", r);
                nota.EliminarNotasVencidas();
                writer.println(1);
            } else {
                writer.println(r);
            }
        } else if (accion.equalsIgnoreCase("session")) {
            if (ok.getAttribute("user") != null) {
                writer.println(1);
            } else {
                writer.println(0);
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
