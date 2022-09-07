/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.DAOempresa;
import dao.DAOpedidos;
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
import modelo.empresa;
import modelo.pedidos;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author 2HDEV
 */
@WebServlet(name = "pedidosControlador", urlPatterns = {"/pedidosControlador"})
public class pedidosControlador extends HttpServlet {

    DAOpedidos dao = new DAOpedidos();
    DAOusuarios usu = new DAOusuarios();

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
        Gson gson = new Gson();
        PrintWriter pw = response.getWriter();
        HttpSession ok = request.getSession();
        int id_usuario = Integer.valueOf(ok.getAttribute("user").toString());
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("listarRetirados")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarPedidosRetiradosPorUsuarioYSucursal(id_usuario));
                if (usu.VerificarAdmin(id_usuario).equals("SI")) {
                    obj.put("data", dao.ListarPedidosAdmin());
                }
            } catch (JSONException ex) {
                Logger.getLogger(pedidosControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("listarPendientes")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarPedidosPendientesPorUsuarioYSucursal(id_usuario));
                if (usu.VerificarAdmin(id_usuario).equals("SI")) {
                    obj.put("data", dao.ListarPedidosPendientes());
                }
            } catch (JSONException ex) {
                Logger.getLogger(pedidosControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("listarARetirar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarPedidosARetirarPorUsuarioYSucursal(id_usuario));
                if (usu.VerificarAdmin(id_usuario).equals("SI")) {
                    obj.put("data", dao.ListarPedidosARetirar());
                }
            } catch (JSONException ex) {
                Logger.getLogger(pedidosControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("verTodos")) {
            JSONObject obj = new JSONObject();
            try {
                if (usu.VerificarAdmin(id_usuario).equals("SI")) {
                    obj.put("data", dao.ListarTodos());
                }
            } catch (JSONException ex) {
                Logger.getLogger(pedidosControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);

        } else if (accion.equalsIgnoreCase("facturados")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarPedidosFacturados());
            } catch (JSONException ex) {
                Logger.getLogger(pedidosControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("anulados")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarPedidosAnulados());
            } catch (JSONException ex) {
                Logger.getLogger(pedidosControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("porretirar")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarPedidosporRetirar());
            } catch (JSONException ex) {
                Logger.getLogger(pedidosControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("verfacturados")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarFacturados());
            } catch (JSONException ex) {
                Logger.getLogger(pedidosControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("verdeliveries")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarDelivery());
            } catch (JSONException ex) {
                Logger.getLogger(pedidosControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("buscar")) {
            int aprobado = request.getParameter("aprobado") != null ? Integer.valueOf(request.getParameter("aprobado")) : 0;
            pw.println(gson.toJson(dao.BuscarPedido(Integer.valueOf(request.getParameter("id_pedido")), aprobado)));
        } else if (accion.equalsIgnoreCase("detalle")) {
            pw.println(gson.toJson(dao.BuscarDetallePedidos(Integer.valueOf(request.getParameter("id_pedido")))));
        } else if (accion.equalsIgnoreCase("insertar")) {
            Type list = new TypeToken<ArrayList<pedidos>>() {
            }.getType();
            pedidos pedido = gson.fromJson(request.getParameter("datos"), pedidos.class);
            pedido.setFechahora_entrega(request.getParameter("fechahora_entrega"));
            pedido.setCreado_por(id_usuario);
            pedido.setId_usuario(String.valueOf(id_usuario));
            ArrayList<pedidos> det = gson.fromJson(request.getParameter("detalle"), list);
            if (pedido.getId_pedido() != 0) {
                pw.println(dao.ActualizarPedido(pedido, det));
            } else {
                pw.println(dao.InsertarPedido(pedido, det));
            }
        } else if (accion.equalsIgnoreCase("anular")) {
            pedidos p = new pedidos();
            p.setMotivo_anulado(request.getParameter("motivo_anulado"));
            p.setId_pedido(Integer.valueOf(request.getParameter("id")));
            p.setId_usuario(ok.getAttribute("user").toString());
            p.setClose_ped(1);
            pw.println(dao.AnularPedido(p));
        } else if (accion.equalsIgnoreCase("aprobar")) {
            pw.println(dao.AprobarPedido(Integer.valueOf(request.getParameter("id_pedido")), id_usuario));
        } else if (accion.equalsIgnoreCase("nueva_factura")) {
            pw.println(gson.toJson(dao.GenerarFactura(Integer.valueOf(request.getParameter("id_pedido")))));
        } else if (accion.equalsIgnoreCase("facturar_pedido")) {
            pw.println(dao.FacturarPedido(Integer.valueOf(request.getParameter("id_pedido"))));
        } else if (accion.equalsIgnoreCase("retirar")) {
            pw.println(dao.RetirarPedido(Integer.valueOf(request.getParameter("id_pedido")), "A RETIRAR", id_usuario));
        } else if (accion.equalsIgnoreCase("retirado")) {
            pw.println(dao.RetirarPedido(Integer.valueOf(request.getParameter("id_pedido")), "RETIRADO", id_usuario));
        } else if (accion.equalsIgnoreCase("compartir")) {
            pw.println(gson.toJson(dao.CompartirPedido(Integer.valueOf(request.getParameter("id_pedido")))));
        } else if (accion.equalsIgnoreCase("actualizardetalle")) {
            Type list = new TypeToken<ArrayList<pedidos>>() {
            }.getType();
            ArrayList<pedidos> pedido = gson.fromJson(request.getParameter("detalle"), list);
            pw.println(gson.toJson(dao.ActualizarEstadoOrden(pedido)));
        } else if (accion.equalsIgnoreCase("habilitar")) {
            pedidos p = new pedidos();
            p.setMotivo_anulado(request.getParameter("motivo_anulado"));
            p.setId_pedido(Integer.valueOf(request.getParameter("id")));
            p.setId_usuario(ok.getAttribute("user").toString());
            p.setClose_ped(0);
            pw.println(dao.HabilitarOrden(p));
        } else if (accion.equalsIgnoreCase("pago_total")) {
            pedidos p = new pedidos();
            p.setId_pedido(Integer.valueOf(request.getParameter("id")));
            p.setId_usuario(ok.getAttribute("user").toString());
            p.setRetirado_ped(request.getParameter("retirado"));
            p.setId_tipo_pago(Integer.valueOf(request.getParameter("tipo_pago")));
            if (request.getParameter("numero_operacion") != null) {
                p.setNro_movimiento(request.getParameter("numero_operacion"));
            }
            if (request.getParameter("banco") != null) {
                p.setId_banco(Integer.valueOf(request.getParameter("banco")));
            }
            if (!p.getRetirado_ped().equals("0")) {
                dao.RetirarPedido(p.getId_pedido(), p.getRetirado_ped(), id_usuario);
            }
            pw.println(dao.AgregarPagoTotal(p));
        } else if (accion.equalsIgnoreCase("clientes_historial")) {
            pedidos pedido = new pedidos();
            pedido.setId_cliente(request.getParameter("id"));
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarClienteHistorial(pedido));
            } catch (JSONException ex) {
                Logger.getLogger(pedidosControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("OrdenesFacturadas")) {
            int r = 0;
            Type list = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            ArrayList<Integer> p = gson.fromJson(request.getParameter("ides"), list);
            for (Integer a : p) {
                r = dao.FacturarPedido(a);
            }
            pw.println(r);
        } else if (accion.equalsIgnoreCase("buscar_orden")) {
            pw.println(gson.toJson(dao.BuscarOrden(Integer.valueOf(request.getParameter("id_pedido")))));
        } else if (accion.equals("print")) {
            DAOempresa business = new DAOempresa();
            empresa emp = business.MostrarEmpresa(id_usuario);
            JSONObject obj = new JSONObject();
            try {
                obj.put("report", "orden_trabajo.jasper");
                obj.put("subreport", "suborden_trabajo.jasper");
                obj.put("id_pedido", Integer.valueOf(request.getParameter("parametro")));
                obj.put("img", "san_francisco.png");
                obj.put("db", "lavanderia");
                obj.put("printer", emp.getImpresora());
            } catch (JSONException ex) {
                Logger.getLogger(OrdendeServicio.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("cuentas")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", dao.ListarCuentasPendientes());
            } catch (JSONException ex) {
                Logger.getLogger(pedidosControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println(obj);
        } else if (accion.equalsIgnoreCase("cobrar")) {
            int id = Integer.valueOf(request.getParameter("id"));
            pw.println(gson.toJson(dao.ListarPedidosporClientes(id)));
        } else if (accion.equalsIgnoreCase("cobrarcuenta")) {
            pedidos pedido = gson.fromJson(request.getParameter("datos"), pedidos.class);
            pedido.setId_usuario(String.valueOf(id_usuario));
            pw.println(dao.CobrarCuentas(pedido));
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
