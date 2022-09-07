/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.DAOcaja;
import dao.DAOcompras;
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
import modelo.compras;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author akio
 */
@WebServlet(name = "comprasControlador", urlPatterns = {"/comprasControlador"})
public class comprasControlador extends HttpServlet {

    private DAOcompras dao;
    private DAOcaja caja;

    public comprasControlador() {
        super();
        dao = new DAOcompras();
        caja = new DAOcaja();
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
        HttpSession ok = request.getSession();
        Gson gson = new Gson();
        String accion = request.getParameter("accion");

        if (accion.equals("listar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarCompras(request.getParameter("tipo_dato")));
            } catch (JSONException ex) {
                Logger.getLogger(comprasControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equals("buscar")) {
            writer.println(gson.toJson(dao.BuscarCompras(Integer.valueOf(request.getParameter("id_compra")))));
        } else if (accion.equals("detalle")) {
            writer.println(gson.toJson(dao.BuscarDetalles(request.getParameter("id_compra"))));
        } else if (accion.equals("insertar")) {
            Type list = new TypeToken<ArrayList<compras>>() {
            }.getType();
            compras c = gson.fromJson(request.getParameter("cabecera"), compras.class);
            c.setId_usuario(String.valueOf(ok.getAttribute("user")));
            ArrayList<compras> d = gson.fromJson(request.getParameter("detalle"), list);
            if (c.getId_compra() != 0) {
                writer.println(dao.ActualizarCompra(c, d));
            } else {
                writer.println(dao.AgregarCompras(c, d));
            }
        } else if (accion.equalsIgnoreCase("anular")) {
            writer.println(dao.CerrarCompra(Integer.valueOf(request.getParameter("id_compra")), "1", String.valueOf(ok.getAttribute("user"))));
        } else if (accion.equalsIgnoreCase("inhabilitados")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarComprasInhabilitadas(request.getParameter("tipo_dato")));
            } catch (JSONException ex) {
                Logger.getLogger(comprasControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("reestablecer")) {
            writer.println(dao.CerrarCompra(Integer.valueOf(request.getParameter("id_compra")), "0", String.valueOf(ok.getAttribute("user"))));
        } else if (accion.equalsIgnoreCase("inicio")) {
            writer.println(dao.ObtenerValorUltimaCompra());
        } else if (accion.equalsIgnoreCase("grafico")) {
            writer.println(gson.toJson(dao.GraficoCompras()));
        } else if (accion.equalsIgnoreCase("graficogastos")) {
            writer.println(gson.toJson(dao.GraficoGastos()));
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
