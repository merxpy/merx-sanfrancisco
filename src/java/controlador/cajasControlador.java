/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOcajas;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.cajas;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author 2HDEV
 */
@WebServlet(name = "cajasControlador", urlPatterns = {"/cajasControlador"})
public class cajasControlador extends HttpServlet {

    DAOcajas dao = new DAOcajas();

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
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("listar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarCajas());
            } catch (JSONException ex) {
                Logger.getLogger(cajasControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("buscar")) {
            pw.println(gson.toJson(dao.BuscarCaja(Integer.valueOf(request.getParameter("id_caja")))));
        } else if (accion.equalsIgnoreCase("insertar")) {
            cajas c = gson.fromJson(request.getParameter("datos"), cajas.class);
            if (c.getId_caja() != 0) {
                pw.println(dao.ActualizarCaja(c));
            } else {
                pw.println(dao.AgregarCaja(c));

            }
        } else if (accion.equalsIgnoreCase("eliminar")) {
            int id_caja = Integer.valueOf(request.getParameter("id_caja"));
            pw.println(dao.EliminarCaja(id_caja));
        } else if (accion.equalsIgnoreCase("select")) {
            int id = Integer.valueOf(request.getParameter("id_caja"));
            pw.println(gson.toJson(dao.ListarCajasDisponibles(id)));
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
