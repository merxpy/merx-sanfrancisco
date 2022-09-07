/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.DAOdetallecaja;
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
import modelo.detalle_caja;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author akio
 */
@WebServlet(name = "cajaControlador", urlPatterns = {"/cajaControlador"})
public class cajaControlador extends HttpServlet {

    private final DAOdetallecaja dao;

    public cajaControlador() {
        dao = new DAOdetallecaja();
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
        int id_usuario = Integer.valueOf(ok.getAttribute("user").toString());
        Gson gson = new Gson();
        PrintWriter writer = response.getWriter();
        String accion = request.getParameter("accion");

        if (accion.equalsIgnoreCase("actual")) {
            writer.println(gson.toJson(dao.BuscarSaldoActual()));
        } else if (accion.equalsIgnoreCase("apertura")) {
            ArrayList<detalle_caja> det = new ArrayList<>();
            Type listType = new TypeToken<ArrayList<detalle_caja>>() {
            }.getType();
            if (request.getParameter("datos") != null && !request.getParameter("datos").isEmpty()) {
                det = gson.fromJson(request.getParameter("datos"), listType);
            }
            detalle_caja c = new detalle_caja();
            if (Integer.valueOf(request.getParameter("tipo_detalle")) == 1) {
                c.setEstado_caja("E");
                c.setConcepto_detalle(request.getParameter("concepto_detalle"));
                c.setSaldo_actual(Integer.valueOf(request.getParameter("saldo_caja")));
                c.setDebe_caja(Integer.valueOf(request.getParameter("saldo_caja")));
                c.setHaber_caja(0);
            } else {
                c.setEstado_caja("S");
                c.setConcepto_detalle(request.getParameter("concepto_detalle"));
                c.setSaldo_actual(-1 * Integer.valueOf(request.getParameter("saldo_caja")));
                c.setHaber_caja(Integer.valueOf(request.getParameter("saldo_caja")));
                c.setDebe_caja(0);
            }
            if (c.getEstado_caja().equals("M")) {
                c.setConcepto_detalle(request.getParameter("concepto_detalle"));
            }
            c.setId_usuario(String.valueOf(ok.getAttribute("user")));
            writer.println(dao.AgregarMovimentos(c, det));
        } else if (accion.equalsIgnoreCase("verificar")) {
            writer.println(dao.VerificarAperturaCaja(Integer.valueOf(ok.getAttribute("user").toString())));
        } else if (accion.equalsIgnoreCase("listar")) {
            JSONObject obj = new JSONObject();
            detalle_caja c = new detalle_caja();
            c.setId_usuario(String.valueOf(id_usuario));
            c.setId_detalle(Integer.valueOf(request.getParameter("id")));
            //System.out.println("Esto tiene id detalle "+request.getParameter("id"));
            try {
                obj.put("data", dao.ListarCaja(c));
            } catch (JSONException ex) {
                Logger.getLogger(cajaControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("buscar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.BuscarCaja(id_usuario, request.getParameter("fecha")));
            } catch (JSONException ex) {
                Logger.getLogger(cajaControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("todos")) {
            writer.println(gson.toJson(dao.ListarCajas(Integer.valueOf(String.valueOf(ok.getAttribute("user"))))));
        } else if (accion.equalsIgnoreCase("saldo")) {
            writer.println(gson.toJson(dao.BuscarCambioInicial(Integer.valueOf(ok.getAttribute("user").toString()))));
        } else if (accion.equalsIgnoreCase("ultimo")) {
            writer.println(gson.toJson(dao.BuscarUltimoSaldoActivo(Integer.valueOf(ok.getAttribute("user").toString()))));
        } else if (accion.equalsIgnoreCase("listarcajas")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarCajasPorApertura(id_usuario));
            } catch (JSONException ex) {
                Logger.getLogger(cajaControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
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
