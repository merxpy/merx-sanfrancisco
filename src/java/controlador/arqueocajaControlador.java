/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.DAOarqueocaja;
import dao.DAOempresa;
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
import modelo.arqueocaja;
import modelo.empresa;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author 2HDEV
 */
@WebServlet(name = "arquecajaControlador", urlPatterns = {"/arqueocajaControlador"})
public class arqueocajaControlador extends HttpServlet {

    private final DAOarqueocaja dao;

    public arqueocajaControlador() {
        dao = new DAOarqueocaja();
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
        String accion = request.getParameter("accion");
        PrintWriter pw = response.getWriter();
        HttpSession ok = request.getSession();
        Gson gson = new Gson();
        int id_usuario = Integer.valueOf(ok.getAttribute("user").toString());
        if (accion.equalsIgnoreCase("listar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarArqueosdeCaja(Integer.valueOf(ok.getAttribute("user").toString())));
            } catch (JSONException ex) {
                Logger.getLogger(arqueocajaControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("insertar")) {
            Type list = new TypeToken<ArrayList<arqueocaja>>() {
            }.getType();
            arqueocaja cabecera = gson.fromJson(request.getParameter("datos"), arqueocaja.class);
            ArrayList<arqueocaja> arqueo = gson.fromJson(request.getParameter("detalle"), list);
            cabecera.setId_usuario(ok.getAttribute("user").toString());
            dao.UpdateEstadoArqueo(Integer.valueOf(ok.getAttribute("user").toString()));
            int r = dao.AgregarArqueo(cabecera, arqueo);
            if (r == 1) {
                JSONObject obj = new JSONObject();
                DAOempresa business = new DAOempresa();
                empresa emp = business.MostrarEmpresa(id_usuario);
                try {
                    obj.put("id_usuario", id_usuario);
                    obj.put("printer", emp.getThermal_printer());
                    obj.put("report", "cierre_caja.jasper");
                    obj.put("subreport", "subcierre_caja.jasper");
                } catch (JSONException ex) {
                    Logger.getLogger(arqueocajaControlador.class.getName()).log(Level.SEVERE, null, ex);
                }
                pw.println(obj);
            } else {
                pw.println(r);
            }
        } else if (accion.equalsIgnoreCase("arqueo")) {
            pw.println(gson.toJson(dao.ObtenerMovientosCaja(Integer.valueOf(ok.getAttribute("user").toString()))));
        } else if (accion.equalsIgnoreCase("validar")) {
            pw.println(dao.ValidarParametros(request.getParameter("fecha_inicio"), request.getParameter("fecha_fin"), Integer.valueOf(request.getParameter("id_usuario"))));
        } else if (accion.equalsIgnoreCase("abrir")) {
            Type list = new TypeToken<ArrayList<arqueocaja>>() {
            }.getType();
            ArrayList<arqueocaja> arqueo = gson.fromJson(request.getParameter("detalle"), list);
            pw.println(dao.AbrirCaja(arqueo, Integer.valueOf(ok.getAttribute("user").toString()), Integer.valueOf(request.getParameter("cambio"))));
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
