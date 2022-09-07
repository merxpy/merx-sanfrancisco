/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOproveedores;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import modelo.proveedores;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author akio
 */
@WebServlet(name = "proveedorControlador", urlPatterns = {"/proveedorControlador"})
public class proveedorControlador extends HttpServlet {

    private DAOproveedores dao;

    public proveedorControlador() {
        dao = new DAOproveedores();
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
        proveedores p = new proveedores();
        HttpSession ok = request.getSession();
        ArrayList<proveedores> pro = new ArrayList<>();
        String accion = request.getParameter("accion");

        if (accion.equalsIgnoreCase("0")) {
            p.setNombre_pro(request.getParameter("nombre"));
            p.setRepresentante_legal_pro(request.getParameter("representante"));
            p.setNdocumento_pro(request.getParameter("ndocumento"));
            p.setDv_pro(request.getParameter("dv"));
            p.setDireccion_pro(request.getParameter("direccion"));
            p.setTelefono_pro(request.getParameter("telefono"));
            p.setCelular_pro(request.getParameter("celular"));
            p.setCorreo_pro(request.getParameter("correo"));
            p.setObservacion_pro(request.getParameter("obs"));
            p.setId_ciudad(request.getParameter("ciudad"));
            String id = dao.AgregarProveedores(p, String.valueOf(ok.getAttribute("user")));
            int r = Integer.valueOf(id);
            try {
                JSONArray a = new JSONArray(request.getParameter("marcas"));
                if (a.length() != 0) {
                    for (int x = 0; x < a.length(); x++) {
                        proveedores pr = new proveedores();
                        pr.setId_proveedor(p.getId_proveedor());
                        pr.setId_marca(a.getString(x));
                        pro.add(pr);
                    }
                    r = dao.CargarMarcasxProveedor(pro);
                }
            } catch (JSONException ex) {
                Logger.getLogger(proveedorControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(r);
        } else if (accion.equalsIgnoreCase("listar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarProveedores());
            } catch (JSONException ex) {
                Logger.getLogger(proveedorControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("select")) {
            writer.println(gson.toJson(dao.ListarProveedores()));
        } else if (accion.equalsIgnoreCase("buscar")) {
            writer.println(gson.toJson(dao.BuscarProveedores(request.getParameter("id_proveedor"))));
        } else if (accion.equalsIgnoreCase("selectMarcas")) {
            writer.println(gson.toJson(dao.BuscarMarcasxProveedor(request.getParameter("id_proveedor"))));
        } else if (accion.equalsIgnoreCase("1")) {
            p.setId_proveedor(request.getParameter("id_proveedor"));
            p.setNombre_pro(request.getParameter("nombre"));
            p.setRepresentante_legal_pro(request.getParameter("representante"));
            p.setNdocumento_pro(request.getParameter("ndocumento"));
            p.setDv_pro(request.getParameter("dv"));
            p.setDireccion_pro(request.getParameter("direccion"));
            p.setTelefono_pro(request.getParameter("telefono"));
            p.setCelular_pro(request.getParameter("celular"));
            p.setCorreo_pro(request.getParameter("correo"));
            p.setObservacion_pro(request.getParameter("obs"));
            p.setId_ciudad(request.getParameter("ciudad"));
            int r = dao.ActualizarProveedores(p, String.valueOf(ok.getAttribute("user")));
            try {
                JSONArray a = new JSONArray(request.getParameter("marcas"));
                if (a.length() != 0) {
                    for (int x = 0; x < a.length(); x++) {
                        proveedores pr = new proveedores();
                        pr.setId_proveedor(p.getId_proveedor());
                        pr.setId_marca(a.getString(x));
                        pro.add(pr);
                    }
                    r = dao.CargarMarcasxProveedor(pro);
                }
            } catch (JSONException ex) {
                Logger.getLogger(proveedorControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(r);
        } else if (accion.equalsIgnoreCase("cerrar")) {
            writer.println(dao.CerrarProveedores(request.getParameter("id_proveedor"), "1", String.valueOf(ok.getAttribute("user"))));
        } else if (accion.equalsIgnoreCase("inhabilitados")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarProveedoresInhabilitados());
            } catch (JSONException ex) {
                Logger.getLogger(proveedorControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("habilitar")) {
            writer.println(dao.CerrarProveedores(request.getParameter("id_proveedor"), "0", String.valueOf(ok.getAttribute("user"))));
        } else if (accion.equalsIgnoreCase("query")) {
            writer.println(gson.toJson(dao.ProveedoresQuery("%" + request.getParameter("q") + "%")));
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
