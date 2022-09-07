/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOmermas;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.mermas;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author 2HDEV
 */
@WebServlet(name = "mermasControlador", urlPatterns = {"/mermasControlador"})
public class mermasControlador extends HttpServlet {

    DAOmermas dao = new DAOmermas();

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
        PrintWriter pw = response.getWriter();
        Gson gson = new Gson();
        if (accion.equalsIgnoreCase("listar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarMermas());
            } catch (JSONException ex) {
                Logger.getLogger(mermasControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("buscar")) {
            int id = Integer.valueOf(request.getParameter("id"));
            pw.println(gson.toJson(dao.BuscarMerma(id)));
        } else if (accion.equalsIgnoreCase("insertar")) {
            mermas m = gson.fromJson(request.getParameter("datos"), mermas.class);
            if (m.getId_merma() == 0) {
                pw.println(dao.AgregarMerma(m));
            } else {
                pw.println(dao.ActualizarMerma(m));
            }
        } else if (accion.equalsIgnoreCase("eliminar")) {
            int id = Integer.valueOf(request.getParameter("id"));
            pw.println(dao.EliminarMerma(id));
        } else if (accion.equalsIgnoreCase("select")) {
            pw.println(gson.toJson(dao.ListarMermas()));
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
