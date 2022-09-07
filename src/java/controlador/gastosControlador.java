/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.DAOgastos;
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
import modelo.gastos;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author 2HDEV
 */
@WebServlet(name = "gastosControlador", urlPatterns = {"/gastosControlador"})
public class gastosControlador extends HttpServlet {

    DAOgastos dao = new DAOgastos();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        PrintWriter pw = response.getWriter();
        HttpSession ok = request.getSession();
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("listar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarGastos(Integer.valueOf(ok.getAttribute("user").toString())));
            } catch (JSONException ex) {
                Logger.getLogger(gastosControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("buscar")) {
            int id = Integer.valueOf(request.getParameter("id"));
            pw.println(gson.toJson(dao.BuscarGastos(id)));
        } else if (accion.equalsIgnoreCase("insertar")) {
            gastos gasto = gson.fromJson(request.getParameter("datos"), gastos.class);
            if (gasto.getId_gasto() != 0) {
                gasto.setId_usuario(String.valueOf(ok.getAttribute("user")));
                pw.println(gson.toJson(dao.ActualizarGastos(gasto)));
            } else {
                gasto.setId_usuario(String.valueOf(ok.getAttribute("user")));
                pw.println(gson.toJson(dao.AgregarGastos(gasto)));
            }
        } else if (accion.equalsIgnoreCase("anular")) {
            int id = Integer.valueOf(request.getParameter("id"));
            int id_usuario = Integer.valueOf(String.valueOf(ok.getAttribute("user")));
            pw.println(gson.toJson(dao.AnularGasto(id, id_usuario)));
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
