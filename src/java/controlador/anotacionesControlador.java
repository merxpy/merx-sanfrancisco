/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOanotacion;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.anotaciones;

/**
 *
 * @author 2HDEV
 */
@WebServlet(name = "anotacionesControlador", urlPatterns = {"/anotacionesControlador"})
public class anotacionesControlador extends HttpServlet {

    DAOanotacion dao = new DAOanotacion();

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
        Gson gson = new Gson();
        HttpSession ok = request.getSession();
        String accion = request.getParameter("accion");
        int id_usuario = Integer.valueOf(ok.getAttribute("user").toString());
        PrintWriter pw = response.getWriter();
        if (accion.equalsIgnoreCase("listar")) {
            pw.println(gson.toJson(dao.ListarAnotaciones(id_usuario)));
        } else if (accion.equalsIgnoreCase("buscar")) {
            int id = Integer.valueOf(request.getParameter("id"));
            pw.println(gson.toJson(dao.BuscarAnotacion(id, id_usuario)));
        } else if (accion.equalsIgnoreCase("insertar")) {
            anotaciones nota = gson.fromJson(request.getParameter("datos"), anotaciones.class);
            nota.setId_usuario(ok.getAttribute("user").toString());
            if (nota.getId_anotacion() != 0) {
                pw.println(dao.ActualizarNota(nota));
            } else {
                pw.println(dao.AgregarNota(nota));
            }
        } else if (accion.equalsIgnoreCase("eliminar")) {
            int id = Integer.valueOf(request.getParameter("id"));
            anotaciones nota = new anotaciones();
            nota.setId_anotacion(id);
            nota.setId_usuario(ok.getAttribute("user").toString());
            pw.println(dao.EliminarNota(nota));
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
