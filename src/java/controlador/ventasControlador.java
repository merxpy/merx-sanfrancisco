/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOarticulos;
import dao.DAOventas;
import extras.funciones;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import modelo.articulos;
import modelo.ventas;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author akio
 */
@WebServlet(name = "ventasControlador", urlPatterns = {"/ventasControlador"})
public class ventasControlador extends HttpServlet {

    private DAOventas dao;
    funciones f = new funciones();

    public ventasControlador() {
        dao = new DAOventas();
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
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON);
        PrintWriter writer = response.getWriter();
        Gson gson = new Gson();
        HttpSession ok = request.getSession();
        String accion = request.getParameter("accion");
        JSONArray ids = new JSONArray();
        if (accion.equalsIgnoreCase("listar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarVentas());
            } catch (JSONException ex) {
                Logger.getLogger(ventasControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("0")) {
            try {
                int r = 0;
                int band;
                int max = 23;
                int total_venta = 0;
                double total_iva5 = 0;
                double total_iva10 = 0;
                double total_descuento = 0;
                int descuentos = Integer.valueOf(request.getParameter("total_descuento"));
                ventas v = new ventas();
                JSONArray array = new JSONArray(request.getParameter("detalle_venta"));
                DAOarticulos art = new DAOarticulos();
                Date fecha = new Date();
                DateFormat dfsql = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat tfsql = new SimpleDateFormat("hh:mm:ss");
                v.setFecha_venta(dfsql.format(fecha));
                v.setHora_venta(tfsql.format(fecha));
                v.setSucursal_id_sucursal(dao.obtenerSucursal(String.valueOf(ok.getAttribute("user"))));
                v.setNfactura_venta(request.getParameter("nfactura_venta"));
                v.setTipo_venta(request.getParameter("tipo_venta"));
                v.setTotal_venta(request.getParameter("total_venta"));
                v.setTotal_iva_venta(request.getParameter("total_iva_venta"));
                v.setTotaliva10(Integer.valueOf(request.getParameter("totaliva10")));
                v.setTotaliva5(Integer.valueOf(request.getParameter("totaliva5")));
                v.setCupon_venta(f.IsInt(request.getParameter("cupon_venta")));
                v.setId_usuario(String.valueOf(ok.getAttribute("user")));
                v.setId_pedido(request.getParameter("id_pedido"));
                if (request.getParameter("id_cliente") == null) {
                    v.setId_cliente("1");
                } else {
                    v.setId_cliente(request.getParameter("id_cliente"));
                }
                v.setId_venta(String.valueOf(dao.AgregarVentas(v)));
                if (!v.getId_venta().isEmpty()) {
                    band = 0;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        ventas det = new ventas();
                        articulos a = art.verificarCodigo(obj.getString("id_articulo"));
                        det.setId_venta(v.getId_venta());
                        det.setId_articulo(a.getId_articulo());
                        det.setCodigo(a.getCodigo_ar());
                        det.setDescripcion(a.getNombre_ar());
                        det.setCantidad(obj.getString("cantidad"));
                        det.setPrecio(obj.getString("precio"));
                        det.setIva(a.getIva_ar());
                        det.setSubtotal(obj.getString("subtotal"));
                        det.setDescuento(obj.getString("descuento"));
                        if (array.length() > max) {
                            band++;
                            if (band <= max && i < array.length() - 1) {
                                r = dao.AgregarDetalleVenta(det);
                                total_descuento += CalcularDescuentos(obj.getInt("subtotal"), obj.getInt("descuento"));
                                total_venta += obj.getInt("subtotal");
                                switch (a.getIva_ar()) {
                                    case "10":
                                        total_iva10 += CalcularIva(a.getIva_ar(), (obj.getInt("subtotal") - CalcularDescuentos(obj.getInt("subtotal"), obj.getInt("descuento"))));
                                        break;
                                    case "5":
                                        total_iva5 += CalcularIva(a.getIva_ar(), (obj.getInt("subtotal") - CalcularDescuentos(obj.getInt("subtotal"), obj.getInt("descuento"))));
                                        break;
                                }
                            } else {
                                if (i == array.length() - 1) {
                                    if (band == max) {
                                        ids.put(r);
                                        v.setId_venta(String.valueOf(dao.AgregarVentas(v)));
                                        det.setId_venta(v.getId_venta());
                                        total_venta = 0;
                                        total_iva10 = 0;
                                        total_iva5 = 0;
                                        total_descuento = 0;
                                    }
                                    total_descuento += CalcularDescuentos(obj.getInt("subtotal"), obj.getInt("descuento"));
                                    total_venta += obj.getInt("subtotal");
                                    switch (a.getIva_ar()) {
                                        case "10":
                                            total_iva10 += CalcularIva(a.getIva_ar(), (obj.getInt("subtotal") - CalcularDescuentos(obj.getInt("subtotal"), obj.getInt("descuento"))));
                                            break;
                                        case "5":
                                            total_iva5 += CalcularIva(a.getIva_ar(), (obj.getInt("subtotal") - CalcularDescuentos(obj.getInt("subtotal"), obj.getInt("descuento"))));
                                            break;
                                    }
                                    r = dao.AgregarDetalleVenta(det);
                                    ids.put(r);
                                    v.setTotal_venta(String.valueOf((Math.round(total_venta) - total_descuento)));
                                    v.setTotaliva10((int) Math.round(total_iva10));
                                    v.setTotaliva5((int) Math.round(total_iva5));
                                    v.setTotal_descuento((int) Math.round(total_descuento));
                                    v.setTotal_iva_venta(String.valueOf((Math.round(total_iva10) + Math.round(total_iva5))));
                                    if (descuentos != 0) {
                                        v.setTotal_venta(String.valueOf((Math.round(total_venta) - descuentos)));
                                        v.setTotal_descuento(descuentos);
                                    }
                                    dao.ActualizarMontosVentas(v);
                                } else {
                                    ids.put(r);
                                    v.setTotal_venta(String.valueOf((Math.round(total_venta) - total_descuento)));
                                    v.setTotaliva10((int) Math.round(total_iva10));
                                    v.setTotaliva5((int) Math.round(total_iva5));
                                    v.setTotal_descuento((int) Math.round(total_descuento));
                                    v.setTotal_iva_venta(String.valueOf((Math.round(total_iva10) + Math.round(total_iva5))));
                                    dao.ActualizarMontosVentas(v);
                                    band = 0;
                                    total_venta = 0;
                                    total_iva10 = 0;
                                    total_iva5 = 0;
                                    total_descuento = 0;
                                    v.setId_venta(String.valueOf(dao.AgregarVentas(v)));
                                    switch (a.getIva_ar()) {
                                        case "10":
                                            total_iva10 += CalcularIva(a.getIva_ar(), (obj.getInt("subtotal") - CalcularDescuentos(obj.getInt("subtotal"), obj.getInt("descuento"))));
                                            break;
                                        case "5":
                                            total_iva5 += CalcularIva(a.getIva_ar(), (obj.getInt("subtotal") - CalcularDescuentos(obj.getInt("subtotal"), obj.getInt("descuento"))));
                                            break;
                                    }
                                    total_venta += obj.getInt("subtotal");
                                    det.setId_venta(v.getId_venta());
                                    r = dao.AgregarDetalleVenta(det);
                                }
                            }
                        } else {
                            r = dao.AgregarDetalleVenta(det);
                        }
                    }
                    if (ids.length() == 0) {
                        writer.println(r);
                    } else {
                        writer.println(ids);
                    }
                }
            } catch (JSONException ex) {
                Logger.getLogger(ventasControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (accion.equalsIgnoreCase("anular")) {
            writer.println(dao.AnularVenta(request.getParameter("id_venta"), "1"));
        } else if (accion.equalsIgnoreCase("inhabilitados")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarVentasInhabilitadas());
            } catch (JSONException ex) {
                Logger.getLogger(ventasControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.println(obj);
        } else if (accion.equalsIgnoreCase("reestablecer")) {
            writer.println(dao.AnularVenta(request.getParameter("id_venta"), "0"));
        } else if (accion.equalsIgnoreCase("ultimo")) {
            writer.println(dao.UltimoNroFactura());
        } else if (accion.equalsIgnoreCase("inicio")) {
            writer.println(dao.ObtenerValorUltimaVenta());
        } else if (accion.equalsIgnoreCase("grafico")) {
            writer.println(gson.toJson(dao.GraficoVentas()));
        }
    }

    public double CalcularIva(String tipoiva, double monto) {
        double iva = 0.0;
        switch (tipoiva) {
            case "10":
                iva = monto / 11.0;
                break;
            case "5":
                iva = monto / 21.0;
                break;
        }
        return iva;
    }

    public double CalcularDescuentos(int monto, int porcentaje) {
        double descuentos = 0;

        if (porcentaje > 0 && porcentaje <= 100) {
            descuentos = ((monto * porcentaje) / 100.0);
        }

        return descuentos;
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
