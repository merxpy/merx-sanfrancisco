/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOempleados;
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
 * @author 2HDEV
 */
@WebServlet(name = "empleadosControlador", urlPatterns = {"/empleadosControlador"})
public class empleadosControlador extends HttpServlet {
    
    DAOempleados dao = new DAOempleados();
    DAOusuarios u = new DAOusuarios();

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
        Gson gson = new Gson();
        HttpSession ok = request.getSession();
        PrintWriter pw = response.getWriter();
        
        if (accion.equalsIgnoreCase("buscar")) {
            usuarios us = u.BuscarUsuarios(ok.getAttribute("user").toString());
            pw.println(gson.toJson(dao.BuscarEmpleado(us.getId_empleado())));
        } else if (accion.equalsIgnoreCase("insertar")) {
            usuarios us = u.BuscarUsuarios(ok.getAttribute("user").toString());
            usuarios emp = gson.fromJson(request.getParameter("datos"), usuarios.class);
            emp.setId_usuario(ok.getAttribute("user").toString());
            emp.setId_empleado(us.getId_empleado());
            pw.println(dao.ActualizarEmpleados(emp));
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
