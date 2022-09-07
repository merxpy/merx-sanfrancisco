/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOclientes;
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
import javax.ws.rs.core.MediaType;
import modelo.clientes;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author akio
 */
@WebServlet(name = "clientesControlador", urlPatterns = {"/clientesControlador"})
public class clientesControlador extends HttpServlet {

    private DAOclientes dao;

    public clientesControlador() {
        dao = new DAOclientes();
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
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession ok = request.getSession();
        response.setContentType(MediaType.APPLICATION_JSON);
        PrintWriter writer = response.getWriter();
        Gson gson = new Gson();
        String accion = request.getParameter("accion");

        if (accion.equalsIgnoreCase("listar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarClientes());
            } catch (JSONException ex) {
                Logger.getLogger(clientesControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("0")) {
            clientes cli = new clientes();
            cli.setNombre_cli(request.getParameter("nombre_cli"));
            cli.setNdocumento_cli(request.getParameter("ndocumento_cli"));
            cli.setDv_cli(request.getParameter("dv_cli"));
            cli.setTelefono_cli(request.getParameter("telefono_cli"));
            cli.setCelular_cli(request.getParameter("celular_cli"));
            cli.setDireccion_cli(request.getParameter("direccion_cli"));
            cli.setObservacion_cli(request.getParameter("observacion_cli"));
            cli.setId_ciudad(request.getParameter("id_ciudad"));
            cli.setEmail_cli(request.getParameter("email_cli"));
            cli.setGooglemaps_cli(request.getParameter("googlemaps_cli"));
            cli.setDescuento_cli(Integer.valueOf(request.getParameter("descuento_cli")));
            cli.setFecha_nac(request.getParameter("fecha_nac"));
            cli.setReferencia_cli(request.getParameter("referencia_cli"));
            cli.setRazon_social(request.getParameter("razon_social"));
            cli.setRuc(request.getParameter("ruc"));
            cli.setRuc_dv(request.getParameter("ruc_dv"));
            writer.println(dao.AgregarClientes(cli, String.valueOf(ok.getAttribute("user"))));
        } else if (accion.equalsIgnoreCase("completar")) {
            writer.println(gson.toJson(dao.ListarClientes()));
        } else if (accion.equalsIgnoreCase("buscar")) {
            writer.println(gson.toJson(dao.BuscarClientes(request.getParameter("id_cliente"))));
        } else if (accion.equalsIgnoreCase("1")) {
            clientes cli = new clientes();
            cli.setNombre_cli(request.getParameter("nombre_cli"));
            cli.setNdocumento_cli(request.getParameter("ndocumento_cli"));
            cli.setDv_cli(request.getParameter("dv_cli"));
            cli.setTelefono_cli(request.getParameter("telefono_cli"));
            cli.setCelular_cli(request.getParameter("celular_cli"));
            cli.setDireccion_cli(request.getParameter("direccion_cli"));
            cli.setObservacion_cli(request.getParameter("observacion_cli"));
            cli.setId_ciudad(request.getParameter("id_ciudad"));
            cli.setGooglemaps_cli(request.getParameter("googlemaps_cli"));
            cli.setDescuento_cli(Integer.valueOf(request.getParameter("descuento_cli")));
            cli.setFecha_nac(request.getParameter("fecha_nac"));
            cli.setId_cliente(request.getParameter("id_cliente"));
            cli.setReferencia_cli(request.getParameter("referencia_cli"));
            cli.setRazon_social(request.getParameter("razon_social"));
            cli.setRuc(request.getParameter("ruc"));
            cli.setRuc_dv(request.getParameter("ruc_dv"));
            writer.println(dao.Actualizarclientes(cli, String.valueOf(ok.getAttribute("user"))));
        } else if (accion.equalsIgnoreCase("cerrar")) {
            writer.println(dao.CerrarClientes(request.getParameter("id_cliente"), "1", String.valueOf(ok.getAttribute("user"))));
        } else if (accion.equalsIgnoreCase("inhabilitados")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarClientesInhabilitados());
            } catch (JSONException ex) {
                Logger.getLogger(clientesControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("habilitar")) {
            writer.println(dao.CerrarClientes(request.getParameter("id_cliente"), "0", String.valueOf(ok.getAttribute("user"))));
        } else if (accion.equalsIgnoreCase("query")) {
            writer.println(gson.toJson(dao.ClientesQuery("%" + request.getParameter("q") + "%")));
        } else if (accion.equalsIgnoreCase("validar")) {
            writer.println(gson.toJson(dao.ValidarClientes(request.getParameter("ndoc"))));
        } else if (accion.equalsIgnoreCase("actualizar")) {
            clientes cliente = gson.fromJson(request.getParameter("datos"), clientes.class);
            writer.println(dao.InsertUpdateClientes(cliente, String.valueOf(ok.getAttribute("user"))));
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
