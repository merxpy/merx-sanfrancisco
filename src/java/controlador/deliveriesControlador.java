/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOdeliveries;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.deliveries;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author 2HDEV
 */
@WebServlet(name = "deliveriesControlador", urlPatterns = {"/deliveriesControlador"})
public class deliveriesControlador extends HttpServlet {

    private DAOdeliveries dao;

    public deliveriesControlador() {
        super();
        dao = new DAOdeliveries();
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
        Gson gson = new Gson();
        PrintWriter pw = response.getWriter();
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("listar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarDeliveries(request.getParameter("estado")));
            } catch (JSONException ex) {
                Logger.getLogger(deliveriesControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("buscar")) {
            int id = Integer.valueOf(request.getParameter("id"));
            pw.println(gson.toJson(dao.BuscarDeliveries(id)));
        } else if (accion.equalsIgnoreCase("insertar")) {
            int id = Integer.valueOf(request.getParameter("id"));
            deliveries delivery = gson.fromJson(request.getParameter("data"), deliveries.class);
            if (id != 0) {
                delivery.setId_delivery(id);
                pw.println(dao.ActualizarDelivery(delivery));
            } else {
                pw.println(dao.AgregarDelivery(delivery));
            }
        } else if (accion.equalsIgnoreCase("eliminar")) {
            int id = Integer.valueOf(request.getParameter("id"));
            pw.println(dao.EliminarDelivery(id));
        } else if (accion.equalsIgnoreCase("entregar")) {
            deliveries delivery = gson.fromJson(request.getParameter("datos"), deliveries.class);
            pw.println(dao.UpdateEstado(delivery));
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
