/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOmonedas;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.monedas;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author 2HDEV
 */
@WebServlet(name = "monedasControlador", urlPatterns = {"/monedasControlador"})
public class monedasControlador extends HttpServlet {

    DAOmonedas dao = new DAOmonedas();

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
        int id_usu = (int) ok.getAttribute("user");
        Gson gson = new Gson();
        if (accion.equalsIgnoreCase("listar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarMonedas());
            } catch (JSONException ex) {
                Logger.getLogger(monedasControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("buscar")) {
            int id = Integer.valueOf(request.getParameter("id"));
            pw.println(gson.toJson(dao.BuscarMonedas(id)));
        } else if (accion.equalsIgnoreCase("insertar")) {
            monedas m = gson.fromJson(request.getParameter("datos"), monedas.class);
            if (m.getId_moneda() == 0) {
                pw.println(dao.AgregarMonedas(m));
            } else {
                pw.println(dao.ActualizarMonedas(m));
            }
        } else if (accion.equalsIgnoreCase("eliminar")) {
            monedas m = new monedas();
            m.setId_moneda(Integer.valueOf(request.getParameter("id")));
            m.setClose_moneda(1);
            pw.println(dao.EliminarMoneda(m));
        } else if (accion.equalsIgnoreCase("monedasxpais")) {
            pw.println(gson.toJson(dao.BuscarMonedasxPais(1)));
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
