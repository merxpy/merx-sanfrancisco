/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.DAOordencompra;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.ordendecompra;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author akio
 */
@WebServlet(name = "ordencompraControlador", urlPatterns = {"/ordencompraControlador"})
public class ordencompraControlador extends HttpServlet {

    private DAOordencompra dao;

    public ordencompraControlador() {
        dao = new DAOordencompra();
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
        HttpSession ok = request.getSession();
        PrintWriter writer = response.getWriter();
        String accion = request.getParameter("accion");

        if (accion.equalsIgnoreCase("listar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarOrdenes());
            } catch (JSONException ex) {
                Logger.getLogger(ordencompraControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("listar")) {
            writer.println(gson.toJson(dao.BuscarOrdenCompra(request.getParameter("id_orden"))));
        } else if (accion.equalsIgnoreCase("insertar")) {
            Type list = new TypeToken<ArrayList<ordendecompra>>() {
            }.getType();
            ordendecompra c = gson.fromJson(request.getParameter("cabecera"), ordendecompra.class);
            ArrayList<ordendecompra> d = gson.fromJson(request.getParameter("detalle"), list);
            if (Integer.valueOf(c.getId_orden_compra()) != 0) {
                writer.println(dao.ActualizarOrden(c, d));
            } else {
                writer.println(dao.AgregarOrdenCompra(c, d));
            }
        } else if (accion.equalsIgnoreCase("aprobar")) {
            writer.println(dao.AprobarOrden(request.getParameter("id_orden_compra"), "1"));
        } else if (accion.equalsIgnoreCase("buscar")) {
            writer.println(gson.toJson(dao.BuscarOrdenCompra(request.getParameter("id_orden_compra"))));
        } else if (accion.equalsIgnoreCase("detalle")) {
            writer.println(gson.toJson(dao.BuscarDetalleOrden(request.getParameter("id_orden_compra"))));
        } else if (accion.equalsIgnoreCase("recibir")) {
            writer.println(dao.RecibirOrden(request.getParameter("id_orden_compra")));
        } else if (accion.equalsIgnoreCase("listarap")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarOrdenesAprobadas());
            } catch (JSONException ex) {
                Logger.getLogger(ordencompraControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("listarrec")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarOrdenesRicibidas());
            } catch (JSONException ex) {
                Logger.getLogger(ordencompraControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("desaprobar")) {
            writer.println(dao.AprobarOrden(request.getParameter("id_orden_compra"), "0"));
        } else if (accion.equalsIgnoreCase("anular")) {
            writer.println(dao.AnularOrden(request.getParameter("id_orden_compra"), "1"));
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
