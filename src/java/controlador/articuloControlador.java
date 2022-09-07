/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOarticulos;
import dao.DAOstock;
import extras.funciones;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.articulos;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author akio
 */
@WebServlet(name = "articuloControlador", urlPatterns = {"/articuloControlador"})
@MultipartConfig(maxFileSize = 16177215)
public class articuloControlador extends HttpServlet {

    private static final String master_dir = "Optisoft";
    private static final String save_dir = "Productos";

    private DAOarticulos dao;
    funciones f = new funciones();

    public articuloControlador() {
        dao = new DAOarticulos();
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
        if (accion.equalsIgnoreCase("listar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarArticulos());
            } catch (JSONException ex) {
                Logger.getLogger(articuloControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("0")) {
            articulos a = gson.fromJson(request.getParameter("datos"), articulos.class);
            a.setPrecio_venta_ar(f.Entero(a.getPrecio_venta_ar()));
            a.setPrecio_compra_ar(f.Entero(a.getPrecio_compra_ar()));
            a.setPrecio_neto_ar(f.Entero(a.getPrecio_neto_ar()));
            a.setUtilidad(a.getUtilidad() != null ? a.getUtilidad().replace(",", ".") : "0");
            a.setMonto_utilidad_ar(f.Entero(a.getMonto_utilidad_ar()));
            a.setPrecio_utilidad_ar(f.Entero(a.getPrecio_utilidad_ar()));
            a.setIva_venta_ar(f.Entero(a.getIva_venta_ar()));
            int r = dao.verificarAccion(a, String.valueOf(ok.getAttribute("user")));
            if (r > 0) {
                if (a.getStock_inicial() != null) {
                    DAOstock stock = new DAOstock();
                    stock.AgregarArticuloStock(a);
                    stock.StockInicial(a.getId_articulo(), a.getStock_inicial());
                }
            }
            writer.println(r);
        } else if ("code".equals(accion)) {
            writer.println(dao.GetLastId());
        } else if (accion.equalsIgnoreCase("completar")) {
            writer.println(gson.toJson(dao.ListarArticulos()));
        } else if (accion.equalsIgnoreCase("verificar")) {
            String cod = request.getParameter("cod");
            writer.println(gson.toJson(dao.verificarCodigo(cod)));
        } else if (accion.equalsIgnoreCase("1")) {
            articulos a = new articulos();
            a.setCodigo_ar(request.getParameter("codigo_ar"));
            a.setBarcode_ar(request.getParameter("barcode_ar"));
            a.setNombre_ar(request.getParameter("nombre_ar"));
            a.setDescripcion_ar(request.getParameter("descripcion_ar"));
            a.setPrecio_venta_ar(request.getParameter("precio_venta_ar"));
            a.setPrecio_compra_ar(request.getParameter("precio_compra_ar"));
            a.setPrecio_neto_ar(request.getParameter("precio_neto_ar"));
            a.setUtilidad(request.getParameter("utilidad"));
            a.setMonto_utilidad_ar(request.getParameter("monto_utilidad_ar"));
            a.setPrecio_utilidad_ar(request.getParameter("precio_utilidad_ar"));
            a.setIva_venta_ar(request.getParameter("iva_venta_ar"));
            a.setIva_ar(request.getParameter("iva_ar"));
            a.setStock_minimo_ar(request.getParameter("stock_minimo_ar"));
            a.setId_unidad(request.getParameter("id_unidad"));
            a.setId_marca(request.getParameter("id_marca"));
            a.setId_etiqueta(request.getParameter("id_etiqueta"));
            a.setId_articulo(request.getParameter("id_articulo"));
            writer.println(dao.ActualizarArticulos(a, String.valueOf(ok.getAttribute("user"))));
        } else if (accion.equalsIgnoreCase("cerrar")) {
            writer.println(dao.CerrarArticulo(request.getParameter("id_articulo"), "1", String.valueOf(ok.getAttribute("user"))));
        } else if (accion.equalsIgnoreCase("inhabilitados")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarArticulosInhabilitados());
            } catch (JSONException ex) {
                Logger.getLogger(articuloControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("habilitar")) {
            writer.println(dao.CerrarArticulo(request.getParameter("id_articulo"), "0", String.valueOf(ok.getAttribute("user"))));
        } else if (accion.equalsIgnoreCase("buscar")) {
            writer.println(gson.toJson(dao.BuscarArticulos(request.getParameter("cod"))));
        } else if (accion.equalsIgnoreCase("eliminar")) {
            int id = Integer.valueOf(request.getParameter("id"));
            writer.println(dao.EliminarArticulos(id));
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
