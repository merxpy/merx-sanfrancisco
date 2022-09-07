/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOcategorias;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.categorias;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author 2HDEV
 */
@WebServlet(name = "categoriasControlador", urlPatterns = {"/categoriasControlador"})
public class categoriasControlador extends HttpServlet {
    
    private final DAOcategorias dao = new DAOcategorias();

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
                obj.put("data", dao.ListarCategorias());
            } catch (JSONException ex) {
                Logger.getLogger(ciudadesControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("buscar")) {
            pw.println(gson.toJson(dao.BuscarCategorias(Integer.valueOf(request.getParameter("id")))));
        } else if (accion.equalsIgnoreCase("insertar")) {
            categorias categoria = gson.fromJson(request.getParameter("datos"), categorias.class);
            if (categoria.getId_categoria() == 0) {
                pw.println(dao.InsertarCategorias(categoria));
            } else {
                pw.println(dao.ActualizarCategorias(categoria));
            }
        } else if (accion.equalsIgnoreCase("estado")) {
            pw.println(dao.ActualizarEstadoCategoria(Integer.valueOf(request.getParameter("id_categoria"))));
        } else if (accion.equalsIgnoreCase("select")) {
            pw.println(gson.toJson(dao.ListarCategoriasxTipo(Integer.valueOf(request.getParameter("tipo")))));
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
