/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOunidades;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import modelo.unidades;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author akio
 */
@WebServlet(name = "unidadesControlador", urlPatterns = {"/unidadesControlador"})
public class unidadesControlador extends HttpServlet {

    private DAOunidades dao;

    public unidadesControlador() {
        dao = new DAOunidades();
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
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON);
        PrintWriter writer = response.getWriter();
        Gson gson = new Gson();
        String accion = request.getParameter("accion");

        if (accion.equalsIgnoreCase("listar")) {
            JSONObject o = new JSONObject();
            try {
                o.put("data", dao.ListarUnidades());
            } catch (JSONException ex) {
                Logger.getLogger(unidadesControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(o);
        } else if (accion.equalsIgnoreCase("select")) {
            writer.println(gson.toJson(dao.ListarUnidadesHabilitadas()));
        } else if (accion.equalsIgnoreCase("0")) {
            unidades u = new unidades();
            u.setNombre_u(request.getParameter("nombre_u"));
            u.setAbreviatura_u(request.getParameter("abreviatura_u"));
            writer.println(dao.AgregarUnidades(u));
        } else if (accion.equalsIgnoreCase("buscar")) {
            writer.println(gson.toJson(dao.BuscarUnidades(request.getParameter("id_unidad"))));
        } else if (accion.equalsIgnoreCase("1")) {
            unidades u = new unidades();
            u.setId_unidad(request.getParameter("id_unidad"));
            u.setNombre_u(request.getParameter("nombre_u"));
            u.setAbreviatura_u(request.getParameter("abreviatura_u"));
            writer.println(dao.ActualizarUnidades(u));
        } else if (accion.equalsIgnoreCase("inhabilitar")) {
            writer.println(dao.InhabilitarUnidad(Integer.valueOf(request.getParameter("id_unidad"))));
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
