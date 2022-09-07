/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.DAOusuarios;
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
import modelo.usuarios;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author akio
 */
@WebServlet(name = "usuarioControlador", urlPatterns = {"/usuarioControlador"})
public class usuarioControlador extends HttpServlet {

    private DAOusuarios dao;

    public usuarioControlador() {
        dao = new DAOusuarios();
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
        HttpSession ok = request.getSession();
        PrintWriter writer = response.getWriter();
        Gson gson = new Gson();
        String accion = request.getParameter("accion");
        JSONArray per;
        if (accion.equalsIgnoreCase("cajero")) {
            writer.println(gson.toJson(dao.BuscarUsuarios(String.valueOf(ok.getAttribute("user")))));
        } else if (accion.equalsIgnoreCase("listar")) {
            JSONObject o = new JSONObject();
            try {
                o.put("data", dao.ListarUsuarios());
            } catch (JSONException ex) {
                Logger.getLogger(usuarioControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(o);
        } else if (accion.equalsIgnoreCase("insertar")) {
            usuarios u = gson.fromJson(request.getParameter("datos"), usuarios.class);
            Type listType = new TypeToken<ArrayList<usuarios>>() {
            }.getType();
            ArrayList<usuarios> permiso = gson.fromJson(request.getParameter("permisos"), listType);
            if (Integer.valueOf(u.getId_usuario()) != 0) {
                writer.println(gson.toJson(dao.ActualizarUsuarios(permiso, u, String.valueOf(ok.getAttribute("user")))));
            } else {
                writer.println(gson.toJson(dao.AgregarUsuarios(permiso, u, String.valueOf(ok.getAttribute("user")))));
            }
        } else if (accion.equalsIgnoreCase("actual")) {
            writer.println(gson.toJson(dao.BuscarUsuarios(String.valueOf(ok.getAttribute("user")))));
        } else if (accion.equalsIgnoreCase("permisos")) {
            writer.println(gson.toJson(dao.BuscarPermisos(Integer.valueOf(String.valueOf(ok.getAttribute("user"))))));
        } else if (accion.equalsIgnoreCase("buscar")) {
            if (Integer.valueOf(request.getParameter("id_usuario")) != 1) {
                writer.println(gson.toJson(dao.BuscarUsuarios(request.getParameter("id_usuario"))));
            } else {
                writer.println(gson.toJson(dao.BuscarUsuarios("0")));
            }
        } else if (accion.equalsIgnoreCase("uxp")) {
            if (Integer.valueOf(request.getParameter("id_usuario")) != 1) {
                writer.println(gson.toJson(dao.BuscarPermisoxUsuario(Integer.valueOf(request.getParameter("id_usuario")), Integer.valueOf(request.getParameter("id_permiso")))));
            } else {
                writer.println(gson.toJson(dao.BuscarPermisoxUsuario(0, Integer.valueOf(request.getParameter("id_permiso")))));
            }
        } else if (accion.equalsIgnoreCase("inhabilitar")) {
            writer.println(dao.CerrarUsuario(Integer.valueOf(request.getParameter("id"))));
        } else if (accion.equalsIgnoreCase("actualizar")) {
            usuarios u = new usuarios();
            u.setId_usuario(ok.getAttribute("user").toString());
            u.setOldpass(request.getParameter("oldpass"));
            u.setPassword_usu(request.getParameter("newpass"));
            writer.println(dao.VerificarPassword(u));
        } else if (accion.equalsIgnoreCase("usuario")) {
            writer.println(gson.toJson(dao.BuscarPermisoxUsuario(Integer.valueOf(String.valueOf(ok.getAttribute("user"))), Integer.valueOf(request.getParameter("id_permiso")))));
        } else if (accion.equalsIgnoreCase("cambiarpwd")) {
            usuarios u = gson.fromJson(request.getParameter("datos"), usuarios.class);
            if (Integer.valueOf(u.getId_usuario()) != 1) {
                writer.println(dao.ActualizarPassword(u));
            } else {
                writer.println(2);
            }
        } else if (accion.equalsIgnoreCase("admin")) {
            writer.println(gson.toJson(dao.VerificarAdmin(Integer.valueOf(String.valueOf(ok.getAttribute("user"))))));
        } else if (accion.equalsIgnoreCase("select")) {
            writer.println(gson.toJson(dao.ListarUsuariosReporte()));
        } else if (accion.equalsIgnoreCase("asignar")) {
            writer.println(gson.toJson(dao.ListarUsuarios()));
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
