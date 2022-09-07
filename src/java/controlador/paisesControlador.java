/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOpaises;
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
import modelo.paises;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author 2HDEV
 */
@WebServlet(name = "paisesControlador", urlPatterns = {"/paisesControlador"})
public class paisesControlador extends HttpServlet {

    DAOpaises dao = new DAOpaises();

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
                obj.put("data", dao.ListarPaises());
            } catch (JSONException ex) {
                Logger.getLogger(paisesControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("buscar")) {
            int id = Integer.valueOf(request.getParameter("id_pais"));
            pw.println(gson.toJson(dao.BuscarPaises(id)));
        } else if (accion.equalsIgnoreCase("insertar")) {
            paises p = gson.fromJson(request.getParameter("datos"), paises.class);
            if (p.getId_pais() == 0) {
                pw.println(dao.AgregarPaises(p));
            } else {
                pw.println(dao.ActualizarPaises(p));
            }
        } else if (accion.equalsIgnoreCase("eliminar")) {
            paises p = new paises();
            p.setId_pais(Integer.valueOf(request.getParameter("id")));
            p.setClose_pais(1);
            pw.println(dao.EliminarPaises(p));
        } else if (accion.equalsIgnoreCase("select")) {
            pw.println(gson.toJson(dao.ListarPaises()));
        } else if (accion.equalsIgnoreCase("query")) {
            pw.println(gson.toJson(dao.BuscarPaisesQuery(request.getParameter("q"))));
        } else if (accion.equalsIgnoreCase("phonecode")) {
            pw.println(gson.toJson(dao.BuscarPhoneCodePais(request.getParameter("phone_code"))));
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
