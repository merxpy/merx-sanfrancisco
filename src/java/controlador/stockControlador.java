/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOstock;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author akio
 */
@WebServlet(name = "stockControlador", urlPatterns = {"/stockControlador"})
public class stockControlador extends HttpServlet {

    private DAOstock dao;

    public stockControlador() {
        dao = new DAOstock();
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
        PrintWriter writer = response.getWriter();
        Gson gson = new Gson();
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("listar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarStock());
            } catch (JSONException ex) {
                Logger.getLogger(stockControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("listarhabilitados")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarStockHabilitados());
            } catch (JSONException ex) {
                Logger.getLogger(stockControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("inicio")) {
            writer.println(dao.ObtenerTotalStock());
        } else if (accion.equalsIgnoreCase("listargastos")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarStockGastos());
            } catch (JSONException ex) {
                Logger.getLogger(stockControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("sxm")) {
            if (request.getParameter("id_marca").equalsIgnoreCase("all")) {
                writer.println(dao.VerificarStockxMarca(request.getParameter("estado"), 0));
            } else {
                writer.println(dao.VerificarStockxMarca(request.getParameter("estado"), Integer.valueOf(request.getParameter("id_marca"))));
            }
        } else if (accion.equalsIgnoreCase("orden")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarStockxCategoria(Integer.valueOf(request.getParameter("id_categoria"))));
            } catch (JSONException ex) {
                Logger.getLogger(stockControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
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
