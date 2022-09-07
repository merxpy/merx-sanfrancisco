/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOetiquetas;
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
import modelo.etiquetas;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author akio
 */
@WebServlet(name = "etiquetasControlador", urlPatterns = {"/etiquetasControlador"})
public class etiquetasControlador extends HttpServlet {

    private DAOetiquetas dao;

    public etiquetasControlador() {
        dao = new DAOetiquetas();
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
        if (accion.equalsIgnoreCase("0")) {
            etiquetas e = new etiquetas();
            e.setNombre_e(request.getParameter("nombre_e"));
            e.setAbreviacion_e(request.getParameter("abreviacion_e"));
            writer.println(dao.AgregarEtiquetas(e));
        } else if (accion.equalsIgnoreCase("listar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarEtiquetas());
            } catch (JSONException ex) {
                Logger.getLogger(etiquetasControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("select")) {
            writer.println(gson.toJson(dao.ListarEtiquetasHabilitadas()));
        } else if (accion.equalsIgnoreCase("buscar")) {
            writer.println(gson.toJson(dao.BuscarEtiquetas(request.getParameter("id_etiqueta"))));
        } else if (accion.equalsIgnoreCase("1")) {
            etiquetas e = new etiquetas();
            e.setNombre_e(request.getParameter("nombre_e"));
            e.setAbreviacion_e(request.getParameter("abreviacion_e"));
            e.setId_etiqueta(request.getParameter("id_etiqueta"));
            writer.println(dao.ActualizarEtiquetas(e));
        } else if (accion.equalsIgnoreCase("inhabilitar")) {
            writer.println(dao.EliminarEtiquetas(Integer.valueOf(request.getParameter("id_etiqueta"))));
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
