/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexion.conexion;
import extras.funciones;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.articulos;
import modelo.pedidos;

/**
 *
 * @author 2HDEV
 */
public class DAOpedidos {

    funciones f = new funciones();
    DAOarticulos dao = new DAOarticulos();

    public ArrayList<pedidos> ListarPedidosRetiradosPorUsuarioYSucursal(int id_usuario) {
        Connection con = conexion.getConnection();
        ArrayList<pedidos> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped, pedidos.fechahora_entrega, "
                    + "pedidos.close_ped, (pedidos.total_ped-pedidos.descuento_ped) as total_ped, pedidos.comentario_ped, clientes.nombre_cli, clientes.ndocumento_cli, clientes.dv_cli, "
                    + "pedidos.aprobado_ped, pedidos.facturado_ped, pedidos.retirado_ped, pedidos.id_sucursal, sucursal.nombre_sucursal, "
                    + "ObtenerMontoPagado(pedidos.id_pedido) as monto_pago FROM pedidos, clientes, sucursal WHERE pedidos.id_cliente=clientes.id_cliente "
                    + "AND close_ped=0 AND pedidos.id_sucursal = BuscarSucursalPorUsuario(?) AND sucursal.id_sucursal = pedidos.id_sucursal "
                    + "AND retirado_ped = 'RETIRADO' AND aprobado_ped = 1");
            ps.setInt(1, id_usuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos p = new pedidos();
                p.setId_pedido(rs.getInt("id_pedido"));
                p.setNro_orden(rs.getInt("nro_orden"));
                p.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                p.setFechahora_entrega(f.ParseDI(rs.getString("fechahora_entrega")));
                p.setClose_ped(rs.getInt("close_ped"));
                p.setTotal_ped(rs.getInt("total_ped"));
                p.setComentario_ped(rs.getString("comentario_ped"));
                p.setNombre_cli(rs.getString("nombre_cli"));
                p.setNdocumento_cli(rs.getString("ndocumento_cli") + "-" + rs.getString("dv_cli"));
                p.setAprobado_ped(rs.getInt("aprobado_ped"));
                p.setFacturado_ped(rs.getInt("facturado_ped"));
                p.setSenha_ped(rs.getInt("monto_pago"));
                p.setRetirado_ped(rs.getString("retirado_ped"));
                p.setId_sucursal(rs.getString("id_sucursal"));
                p.setNombre_sucursal(rs.getString("nombre_sucursal"));
                pedidos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedidos;
    }

    public ArrayList<pedidos> ListarPedidosPendientesPorUsuarioYSucursal(int id_usuario) {
        Connection con = conexion.getConnection();
        ArrayList<pedidos> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped, pedidos.fechahora_entrega, "
                    + "pedidos.close_ped, (pedidos.total_ped-pedidos.descuento_ped) as total_ped, pedidos.comentario_ped, clientes.nombre_cli, clientes.ndocumento_cli, clientes.dv_cli, "
                    + "pedidos.aprobado_ped, pedidos.facturado_ped, pedidos.retirado_ped, pedidos.id_sucursal, sucursal.nombre_sucursal, "
                    + "ObtenerMontoPagado(pedidos.id_pedido) as monto_pago FROM pedidos, clientes, sucursal WHERE pedidos.id_cliente=clientes.id_cliente "
                    + "AND close_ped=0 AND pedidos.id_sucursal = BuscarSucursalPorUsuario(?) AND sucursal.id_sucursal = pedidos.id_sucursal "
                    + "AND retirado_ped = 'PENDIENTE'");
            ps.setInt(1, id_usuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos p = new pedidos();
                p.setId_pedido(rs.getInt("id_pedido"));
                p.setNro_orden(rs.getInt("nro_orden"));
                p.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                p.setFechahora_entrega(f.ParseDI(rs.getString("fechahora_entrega")));
                p.setClose_ped(rs.getInt("close_ped"));
                p.setTotal_ped(rs.getInt("total_ped"));
                p.setComentario_ped(rs.getString("comentario_ped"));
                p.setNombre_cli(rs.getString("nombre_cli"));
                p.setNdocumento_cli(rs.getString("ndocumento_cli") + "-" + rs.getString("dv_cli"));
                p.setAprobado_ped(rs.getInt("aprobado_ped"));
                p.setFacturado_ped(rs.getInt("facturado_ped"));
                p.setSenha_ped(rs.getInt("monto_pago"));
                p.setRetirado_ped(rs.getString("retirado_ped"));
                p.setId_sucursal(rs.getString("id_sucursal"));
                p.setNombre_sucursal(rs.getString("nombre_sucursal"));

                pedidos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedidos;
    }

    public ArrayList<pedidos> ListarPedidosARetirarPorUsuarioYSucursal(int id_usuario) {
        Connection con = conexion.getConnection();
        ArrayList<pedidos> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped, pedidos.fechahora_entrega, "
                    + "pedidos.close_ped, (pedidos.total_ped-pedidos.descuento_ped) as total_ped, pedidos.comentario_ped, clientes.nombre_cli, clientes.ndocumento_cli, clientes.dv_cli, "
                    + "pedidos.aprobado_ped, pedidos.facturado_ped, pedidos.retirado_ped, pedidos.id_sucursal, sucursal.nombre_sucursal, "
                    + "ObtenerMontoPagado(pedidos.id_pedido) as monto_pago FROM pedidos, clientes, sucursal WHERE pedidos.id_cliente=clientes.id_cliente "
                    + "AND close_ped=0 AND pedidos.id_sucursal = BuscarSucursalPorUsuario(?) AND sucursal.id_sucursal = pedidos.id_sucursal "
                    + "AND retirado_ped='A RETIRAR'");
            ps.setInt(1, id_usuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos p = new pedidos();
                p.setId_pedido(rs.getInt("id_pedido"));
                p.setNro_orden(rs.getInt("nro_orden"));
                p.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                p.setFechahora_entrega(f.ParseDI(rs.getString("fechahora_entrega")));
                p.setClose_ped(rs.getInt("close_ped"));
                p.setTotal_ped(rs.getInt("total_ped"));
                p.setComentario_ped(rs.getString("comentario_ped"));
                p.setNombre_cli(rs.getString("nombre_cli"));
                p.setNdocumento_cli(rs.getString("ndocumento_cli") + "-" + rs.getString("dv_cli"));
                p.setAprobado_ped(rs.getInt("aprobado_ped"));
                p.setFacturado_ped(rs.getInt("facturado_ped"));
                p.setSenha_ped(rs.getInt("monto_pago"));
                p.setRetirado_ped(rs.getString("retirado_ped"));
                p.setId_sucursal(rs.getString("id_sucursal"));
                p.setNombre_sucursal(rs.getString("nombre_sucursal"));

                pedidos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedidos;
    }

    public ArrayList<pedidos> ListarTodos() {
        Connection con = conexion.getConnection();
        ArrayList<pedidos> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped, pedidos.fechahora_entrega, "
                    + "pedidos.close_ped, (pedidos.total_ped-pedidos.descuento_ped) as total_ped, pedidos.comentario_ped, clientes.nombre_cli, clientes.ndocumento_cli, clientes.dv_cli, "
                    + "pedidos.aprobado_ped, pedidos.facturado_ped, pedidos.retirado_ped, pedidos.id_sucursal, sucursal.nombre_sucursal, "
                    + "ObtenerMontoPagado(pedidos.id_pedido) as monto_pago FROM pedidos, clientes, sucursal WHERE pedidos.id_cliente=clientes.id_cliente "
                    + "AND close_ped=0 AND sucursal.id_sucursal = pedidos.id_sucursal");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos p = new pedidos();
                p.setId_pedido(rs.getInt("id_pedido"));
                p.setNro_orden(rs.getInt("nro_orden"));
                p.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                p.setFechahora_entrega(f.ParseDI(rs.getString("fechahora_entrega")));
                p.setClose_ped(rs.getInt("close_ped"));
                p.setTotal_ped(rs.getInt("total_ped"));
                p.setComentario_ped(rs.getString("comentario_ped"));
                p.setNombre_cli(rs.getString("nombre_cli"));
                p.setNdocumento_cli(rs.getString("ndocumento_cli") + "-" + rs.getString("dv_cli"));
                p.setAprobado_ped(rs.getInt("aprobado_ped"));
                p.setFacturado_ped(rs.getInt("facturado_ped"));
                p.setSenha_ped(rs.getInt("monto_pago"));
                p.setRetirado_ped(rs.getString("retirado_ped"));
                p.setId_sucursal(rs.getString("id_sucursal"));
                p.setNombre_sucursal(rs.getString("nombre_sucursal"));

                pedidos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedidos;
    }

    public ArrayList<pedidos> ListarFacturados() {
        Connection con = conexion.getConnection();
        ArrayList<pedidos> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped, pedidos.fechahora_entrega, "
                    + "pedidos.close_ped, (pedidos.total_ped-pedidos.descuento_ped) as total_ped, pedidos.comentario_ped, clientes.nombre_cli, clientes.ndocumento_cli, clientes.dv_cli, "
                    + "pedidos.aprobado_ped, pedidos.facturado_ped, pedidos.retirado_ped, pedidos.id_sucursal, sucursal.nombre_sucursal, "
                    + "ObtenerMontoPagado(pedidos.id_pedido) as monto_pago FROM pedidos, clientes, sucursal WHERE pedidos.id_cliente=clientes.id_cliente "
                    + "AND close_ped=0 AND sucursal.id_sucursal = pedidos.id_sucursal AND pedidos.facturado_ped=1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos p = new pedidos();
                p.setId_pedido(rs.getInt("id_pedido"));
                p.setNro_orden(rs.getInt("nro_orden"));
                p.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                p.setFechahora_entrega(f.ParseDI(rs.getString("fechahora_entrega")));
                p.setClose_ped(rs.getInt("close_ped"));
                p.setTotal_ped(rs.getInt("total_ped"));
                p.setComentario_ped(rs.getString("comentario_ped"));
                p.setNombre_cli(rs.getString("nombre_cli"));
                p.setNdocumento_cli(rs.getString("ndocumento_cli") + "-" + rs.getString("dv_cli"));
                p.setAprobado_ped(rs.getInt("aprobado_ped"));
                p.setFacturado_ped(rs.getInt("facturado_ped"));
                p.setSenha_ped(rs.getInt("monto_pago"));
                p.setRetirado_ped(rs.getString("retirado_ped"));
                p.setId_sucursal(rs.getString("id_sucursal"));
                p.setNombre_sucursal(rs.getString("nombre_sucursal"));

                pedidos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedidos;
    }

    public ArrayList<pedidos> ListarDelivery() {
        Connection con = conexion.getConnection();
        ArrayList<pedidos> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped, pedidos.fechahora_entrega, "
                    + "pedidos.close_ped, (pedidos.total_ped-pedidos.descuento_ped) as total_ped, pedidos.comentario_ped, clientes.nombre_cli, clientes.ndocumento_cli, clientes.dv_cli, "
                    + "pedidos.aprobado_ped, pedidos.facturado_ped, pedidos.retirado_ped, pedidos.id_sucursal, sucursal.nombre_sucursal, "
                    + "ObtenerMontoPagado(pedidos.id_pedido) as monto_pago FROM pedidos, clientes, sucursal WHERE pedidos.id_cliente=clientes.id_cliente "
                    + "AND close_ped=0 AND sucursal.id_sucursal = pedidos.id_sucursal AND pedidos.delivery_ped=1 AND (pedidos.retirado_ped='PENDIENTE' OR pedidos.retirado_ped='A RETIRAR')");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos p = new pedidos();
                p.setId_pedido(rs.getInt("id_pedido"));
                p.setNro_orden(rs.getInt("nro_orden"));
                p.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                p.setFechahora_entrega(f.ParseDI(rs.getString("fechahora_entrega")));
                p.setClose_ped(rs.getInt("close_ped"));
                p.setTotal_ped(rs.getInt("total_ped"));
                p.setComentario_ped(rs.getString("comentario_ped"));
                p.setNombre_cli(rs.getString("nombre_cli"));
                p.setNdocumento_cli(rs.getString("ndocumento_cli") + "-" + rs.getString("dv_cli"));
                p.setAprobado_ped(rs.getInt("aprobado_ped"));
                p.setFacturado_ped(rs.getInt("facturado_ped"));
                p.setSenha_ped(rs.getInt("monto_pago"));
                p.setRetirado_ped(rs.getString("retirado_ped"));
                p.setId_sucursal(rs.getString("id_sucursal"));
                p.setNombre_sucursal(rs.getString("nombre_sucursal"));

                pedidos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedidos;
    }

    public ArrayList<pedidos> ListarPedidosAdmin() {
        Connection con = conexion.getConnection();
        ArrayList<pedidos> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped, pedidos.fechahora_entrega, "
                    + "pedidos.close_ped, (pedidos.total_ped-pedidos.descuento_ped) as total_ped, pedidos.comentario_ped, clientes.nombre_cli, clientes.ndocumento_cli, clientes.dv_cli, "
                    + "pedidos.aprobado_ped, pedidos.facturado_ped, pedidos.retirado_ped, pedidos.id_sucursal, sucursal.nombre_sucursal, "
                    + "ObtenerMontoPagado(pedidos.id_pedido) as monto_pago FROM pedidos, clientes, sucursal WHERE pedidos.id_cliente=clientes.id_cliente "
                    + "AND close_ped=0 AND sucursal.id_sucursal = pedidos.id_sucursal AND retirado_ped = 'RETIRADO'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos p = new pedidos();
                p.setId_pedido(rs.getInt("id_pedido"));
                p.setNro_orden(rs.getInt("nro_orden"));
                p.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                p.setFechahora_entrega(f.ParseDI(rs.getString("fechahora_entrega")));
                p.setClose_ped(rs.getInt("close_ped"));
                p.setTotal_ped(rs.getInt("total_ped"));
                p.setComentario_ped(rs.getString("comentario_ped"));
                p.setNombre_cli(rs.getString("nombre_cli"));
                p.setNdocumento_cli(rs.getString("ndocumento_cli") + "-" + rs.getString("dv_cli"));
                p.setAprobado_ped(rs.getInt("aprobado_ped"));
                p.setFacturado_ped(rs.getInt("facturado_ped"));
                p.setSenha_ped(rs.getInt("monto_pago"));
                p.setRetirado_ped(rs.getString("retirado_ped"));
                p.setId_sucursal(rs.getString("id_sucursal"));
                p.setNombre_sucursal(rs.getString("nombre_sucursal"));

                pedidos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedidos;
    }

    public ArrayList<pedidos> ListarPedidosPendientes() {
        Connection con = conexion.getConnection();
        ArrayList<pedidos> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped, pedidos.fechahora_entrega, "
                    + "pedidos.close_ped, (pedidos.total_ped-pedidos.descuento_ped) as total_ped, pedidos.comentario_ped, clientes.nombre_cli, clientes.ndocumento_cli, clientes.dv_cli, "
                    + "pedidos.aprobado_ped, pedidos.facturado_ped, pedidos.retirado_ped, pedidos.id_sucursal, sucursal.nombre_sucursal, "
                    + "ObtenerMontoPagado(pedidos.id_pedido) as monto_pago FROM pedidos, clientes, sucursal WHERE pedidos.id_cliente=clientes.id_cliente "
                    + "AND close_ped=0 AND sucursal.id_sucursal = pedidos.id_sucursal AND pedidos.retirado_ped='PENDIENTE'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos p = new pedidos();
                p.setId_pedido(rs.getInt("id_pedido"));
                p.setNro_orden(rs.getInt("nro_orden"));
                p.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                p.setFechahora_entrega(f.ParseDI(rs.getString("fechahora_entrega")));
                p.setClose_ped(rs.getInt("close_ped"));
                p.setTotal_ped(rs.getInt("total_ped"));
                p.setComentario_ped(rs.getString("comentario_ped"));
                p.setNombre_cli(rs.getString("nombre_cli"));
                p.setNdocumento_cli(rs.getString("ndocumento_cli") + "-" + rs.getString("dv_cli"));
                p.setAprobado_ped(rs.getInt("aprobado_ped"));
                p.setFacturado_ped(rs.getInt("facturado_ped"));
                p.setSenha_ped(rs.getInt("monto_pago"));
                p.setRetirado_ped(rs.getString("retirado_ped"));
                p.setId_sucursal(rs.getString("id_sucursal"));
                p.setNombre_sucursal(rs.getString("nombre_sucursal"));
                pedidos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedidos;
    }

    public ArrayList<pedidos> ListarPedidosARetirar() {
        Connection con = conexion.getConnection();
        ArrayList<pedidos> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped, pedidos.fechahora_entrega, "
                    + "pedidos.close_ped, (pedidos.total_ped-pedidos.descuento_ped) as total_ped, pedidos.comentario_ped, clientes.nombre_cli, clientes.ndocumento_cli, clientes.dv_cli, "
                    + "pedidos.aprobado_ped, pedidos.facturado_ped, pedidos.retirado_ped, pedidos.id_sucursal, sucursal.nombre_sucursal, "
                    + "ObtenerMontoPagado(pedidos.id_pedido) as monto_pago FROM pedidos, clientes, sucursal WHERE pedidos.id_cliente=clientes.id_cliente "
                    + "AND close_ped=0 AND sucursal.id_sucursal = pedidos.id_sucursal AND pedidos.retirado_ped='A RETIRAR'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos p = new pedidos();
                p.setId_pedido(rs.getInt("id_pedido"));
                p.setNro_orden(rs.getInt("nro_orden"));
                p.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                p.setFechahora_entrega(f.ParseDI(rs.getString("fechahora_entrega")));
                p.setClose_ped(rs.getInt("close_ped"));
                p.setTotal_ped(rs.getInt("total_ped"));
                p.setComentario_ped(rs.getString("comentario_ped"));
                p.setNombre_cli(rs.getString("nombre_cli"));
                p.setNdocumento_cli(rs.getString("ndocumento_cli") + "-" + rs.getString("dv_cli"));
                p.setAprobado_ped(rs.getInt("aprobado_ped"));
                p.setFacturado_ped(rs.getInt("facturado_ped"));
                p.setSenha_ped(rs.getInt("monto_pago"));
                p.setRetirado_ped(rs.getString("retirado_ped"));
                p.setId_sucursal(rs.getString("id_sucursal"));
                p.setNombre_sucursal(rs.getString("nombre_sucursal"));
                pedidos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedidos;
    }

    public ArrayList<pedidos> ListarPedidosporRetirar() {
        Connection con = conexion.getConnection();
        ArrayList<pedidos> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped, pedidos.fechahora_entrega, "
                    + "pedidos.close_ped, (pedidos.total_ped-pedidos.descuento_ped) as total_ped, pedidos.comentario_ped, clientes.nombre_cli, clientes.ndocumento_cli, clientes.dv_cli, "
                    + "pedidos.aprobado_ped, pedidos.facturado_ped, pedidos.retirado_ped, pedidos.id_sucursal, sucursal.nombre_sucursal, "
                    + "ObtenerMontoPagado(pedidos.id_pedido) as monto_pago FROM pedidos, clientes, sucursal WHERE pedidos.id_cliente=clientes.id_cliente "
                    + "AND pedidos.aprobado_ped=1 AND pedidos.retirado_ped='RETIRADO' "
                    + "AND pedidos.close_ped=0 AND pedidos.id_sucursal=sucursal.id_sucursal");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos p = new pedidos();
                p.setId_pedido(rs.getInt("id_pedido"));
                p.setNro_orden(rs.getInt("nro_orden"));
                p.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                p.setFechahora_entrega(f.ParseDI(rs.getString("fechahora_entrega")));
                p.setClose_ped(rs.getInt("close_ped"));
                p.setTotal_ped(rs.getInt("total_ped"));
                p.setComentario_ped(rs.getString("comentario_ped"));
                p.setNombre_cli(rs.getString("nombre_cli"));
                p.setAprobado_ped(rs.getInt("aprobado_ped"));
                p.setFacturado_ped(rs.getInt("facturado_ped"));
                p.setSenha_ped(rs.getInt("monto_pago"));
                pedidos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedidos;
    }

    public ArrayList<pedidos> ListarPedidosFacturados() {
        Connection con = conexion.getConnection();
        ArrayList<pedidos> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped, pedidos.fechahora_entrega, "
                    + "pedidos.close_ped, (pedidos.total_ped-pedidos.descuento_ped) as total_ped, pedidos.comentario_ped, clientes.nombre_cli, clientes.ndocumento_cli, "
                    + "clientes.dv_cli, pedidos.aprobado_ped,pedidos.facturado_ped, pedidos.facturado_ped, pedidos.retirado_ped, pedidos.id_sucursal, "
                    + "sucursal.nombre_sucursal, ObtenerMontoPagado(pedidos.id_pedido) as monto_pago FROM pedidos, clientes, sucursal "
                    + "WHERE pedidos.id_cliente=clientes.id_cliente AND pedidos.aprobado_ped=1 AND pedidos.facturado_ped=1 AND pedidos.close_ped=0 "
                    + "AND pedidos.id_sucursal=sucursal.id_sucursal");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos p = new pedidos();
                p.setId_pedido(rs.getInt("id_pedido"));
                p.setNro_orden(rs.getInt("nro_orden"));
                p.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                p.setFechahora_entrega(f.ParseDI(rs.getString("fechahora_entrega")));
                p.setClose_ped(rs.getInt("close_ped"));
                p.setTotal_ped(rs.getInt("total_ped"));
                p.setComentario_ped(rs.getString("comentario_ped"));
                p.setNombre_cli(rs.getString("nombre_cli"));
                p.setAprobado_ped(rs.getInt("aprobado_ped"));
                p.setFacturado_ped(rs.getInt("facturado_ped"));
                p.setSenha_ped(rs.getInt("monto_pago"));
                pedidos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedidos;
    }

    public ArrayList<pedidos> ListarPedidosAnulados() {
        Connection con = conexion.getConnection();
        ArrayList<pedidos> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped, pedidos.fechahora_entrega, "
                    + "pedidos.close_ped, (pedidos.total_ped-pedidos.descuento_ped) as total_ped, pedidos.comentario_ped, clientes.nombre_cli, clientes.ndocumento_cli, clientes.dv_cli, "
                    + "pedidos.aprobado_ped, pedidos.facturado_ped, pedidos.retirado_ped, pedidos.id_sucursal, sucursal.nombre_sucursal, "
                    + "pedidos.motivo_anulado, ObtenerMontoPagado(pedidos.id_pedido) as monto_pago FROM pedidos, clientes, sucursal "
                    + "WHERE pedidos.id_cliente=clientes.id_cliente AND pedidos.close_ped=1 AND sucursal.id_sucursal = pedidos.id_sucursal");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos p = new pedidos();
                p.setId_pedido(rs.getInt("id_pedido"));
                p.setNro_orden(rs.getInt("nro_orden"));
                p.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                p.setFechahora_entrega(f.ParseDI(rs.getString("fechahora_entrega")));
                p.setClose_ped(rs.getInt("close_ped"));
                p.setTotal_ped(rs.getInt("total_ped"));
                p.setComentario_ped(rs.getString("comentario_ped"));
                p.setNombre_cli(rs.getString("nombre_cli"));
                p.setNdocumento_cli(rs.getString("ndocumento_cli") + "-" + rs.getString("dv_cli"));
                p.setAprobado_ped(rs.getInt("aprobado_ped"));
                p.setFacturado_ped(rs.getInt("facturado_ped"));
                p.setSenha_ped(rs.getInt("monto_pago"));
                p.setRetirado_ped(rs.getString("retirado_ped"));
                p.setId_sucursal(rs.getString("id_sucursal"));
                p.setNombre_sucursal(rs.getString("nombre_sucursal"));
                p.setMotivo_anulado(rs.getString("motivo_anulado"));
                pedidos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedidos;
    }

    public pedidos BuscarPedido(int id, int aprobado) {
        Connection con = conexion.getConnection();
        pedidos pedido = new pedidos();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped,pedidos.close_ped, "
                    + "pedidos.total_ped, pedidos.descuento_ped, pedidos.comentario_ped, clientes.id_cliente, clientes.nombre_cli, clientes.ndocumento_cli, "
                    + "clientes.dv_cli, clientes.telefono_cli, clientes.celular_cli, pedidos.fechahora_entrega, pedidos.lugar_entrega, "
                    + "pedidos.delivery_ped, pedidos.nota_especial,pedidos.asignado_a, empleados.nombre_emp, empleados.apellido_emp, "
                    + "ObtenerMontoPagado(pedidos.id_pedido) as monto_pago FROM pedidos, clientes, usuarios, empleados WHERE "
                    + "pedidos.id_cliente=clientes.id_cliente AND pedidos.id_pedido=? AND pedidos.close_ped=0 AND pedidos.aprobado_ped = ? AND "
                    + "usuarios.empleados_id_empleado=empleados.id_empleado AND usuarios.id_usuario=pedidos.asignado_a");
            ps.setInt(1, id);
            ps.setInt(2, aprobado);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pedido.setId_pedido(rs.getInt("id_pedido"));
                pedido.setNro_orden(rs.getInt("nro_orden"));
                pedido.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                pedido.setClose_ped(rs.getInt("close_ped"));
                pedido.setTotal_ped(rs.getInt("total_ped"));
                pedido.setDescuento_ped(rs.getInt("descuento_ped"));
                pedido.setComentario_ped(rs.getString("comentario_ped"));
                pedido.setId_cliente(rs.getString("id_cliente"));
                pedido.setNombre_cli(rs.getString("nombre_cli"));
                pedido.setNdocumento_cli(rs.getString("ndocumento_cli") + "-" + rs.getString("dv_cli"));
                pedido.setCelular_cli(rs.getString("celular_cli"));
                pedido.setTelefono_cli(rs.getString("telefono_cli"));
                pedido.setFechahora_entrega(rs.getString("fechahora_entrega"));
                pedido.setLugar_entrega(rs.getString("lugar_entrega"));
                pedido.setDelivery_ped(rs.getInt("delivery_ped"));
                pedido.setNota_especial(rs.getString("nota_especial"));
                pedido.setAsignado_a(rs.getInt("asignado_a"));
                pedido.setSenha_ped(rs.getInt("monto_pago"));
                pedido.setNombre_emp(rs.getString("nombre_emp") + " " + rs.getString("apellido_emp"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedido;
    }

    public pedidos BuscarNroClienteOrden(int id) {
        Connection con = conexion.getConnection();
        pedidos pedido = new pedidos();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id_pedido, nombre_cli FROM pedidos, clientes, usuarios, empleados "
                    + "WHERE pedidos.id_cliente=clientes.id_cliente AND id_pedido=? AND close_ped=0 "
                    + "AND usuarios.empleados_id_empleado=empleados.id_empleado AND usuarios.id_usuario=pedidos.asignado_a");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pedido.setId_pedido(rs.getInt("id_pedido"));
                pedido.setNombre_cli(rs.getString("nombre_cli"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedido;
    }

    public pedidos GenerarFactura(int id) {
        Connection con = conexion.getConnection();
        pedidos pedido = new pedidos();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped,pedidos.close_ped, "
                    + "pedidos.total_ped, pedidos.descuento_ped, pedidos.comentario_ped, clientes.id_cliente, clientes.nombre_cli, clientes.ndocumento_cli, "
                    + "clientes.dv_cli,clientes.razon_social, clientes.ruc, clientes.ruc_dv, clientes.telefono_cli, clientes.celular_cli, pedidos.fechahora_entrega, pedidos.lugar_entrega, "
                    + "pedidos.delivery_ped, pedidos.nota_especial,pedidos.asignado_a, empleados.nombre_emp, empleados.apellido_emp, "
                    + "ObtenerMontoPagado(pedidos.id_pedido) as monto_pago FROM pedidos, clientes, usuarios, empleados WHERE "
                    + "pedidos.id_cliente=clientes.id_cliente AND pedidos.id_pedido=? AND pedidos.close_ped=0 AND "
                    + "usuarios.empleados_id_empleado=empleados.id_empleado AND usuarios.id_usuario=pedidos.asignado_a");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pedido.setId_pedido(rs.getInt("id_pedido"));
                pedido.setNro_orden(rs.getInt("nro_orden"));
                pedido.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                pedido.setClose_ped(rs.getInt("close_ped"));
                pedido.setTotal_ped(rs.getInt("total_ped"));
                pedido.setDescuento_ped(rs.getInt("descuento_ped"));
                pedido.setComentario_ped(rs.getString("comentario_ped"));
                pedido.setId_cliente(rs.getString("id_cliente"));
                pedido.setNombre_cli(rs.getString("nombre_cli"));
                pedido.setNdocumento_cli(rs.getString("ndocumento_cli") + "-" + rs.getString("dv_cli"));
                pedido.setRazon_social(rs.getString("razon_social"));
                pedido.setRuc(rs.getString("ruc") + "-" + rs.getString("ruc_dv"));
                pedido.setCelular_cli(rs.getString("celular_cli"));
                pedido.setTelefono_cli(rs.getString("telefono_cli"));
                pedido.setFechahora_entrega(rs.getString("fechahora_entrega"));
                pedido.setLugar_entrega(rs.getString("lugar_entrega"));
                pedido.setDelivery_ped(rs.getInt("delivery_ped"));
                pedido.setNota_especial(rs.getString("nota_especial"));
                pedido.setAsignado_a(rs.getInt("asignado_a"));
                pedido.setSenha_ped(rs.getInt("monto_pago"));
                pedido.setNombre_emp(rs.getString("nombre_emp") + " " + rs.getString("apellido_emp"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedido;
    }

    public pedidos CompartirPedido(int id) {
        Connection con = conexion.getConnection();
        pedidos pedido = new pedidos();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM pedidos, clientes WHERE pedidos.id_cliente=clientes.id_cliente AND id_pedido=? AND close_ped=0");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pedido.setId_pedido(rs.getInt("id_pedido"));
                pedido.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                pedido.setClose_ped(rs.getInt("close_ped"));
                pedido.setTotal_ped(rs.getInt("total_ped"));
                pedido.setComentario_ped(rs.getString("comentario_ped"));
                pedido.setId_cliente(rs.getString("id_cliente"));
                pedido.setNombre_cli(rs.getString("nombre_cli"));
                pedido.setNdocumento_cli(rs.getString("ndocumento_cli") + "-" + rs.getString("dv_cli"));
                pedido.setCelular_cli(rs.getString("celular_cli"));
                pedido.setTelefono_cli(rs.getString("telefono_cli"));
                pedido.setFechahora_entrega(rs.getString("fechahora_entrega"));
                pedido.setLugar_entrega(rs.getString("lugar_entrega"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedido;
    }

    public ArrayList<pedidos> BuscarDetallePedidos(int id_pedido) {
        Connection con = conexion.getConnection();
        ArrayList<pedidos> detalle = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM detalle_pedidos WHERE pedidos_id_pedido=?");
            ps.setInt(1, id_pedido);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos d = new pedidos();
                d.setId_pedido(rs.getInt("pedidos_id_pedido"));
                d.setId_articulo(rs.getString("articulos_id_articulo"));
                d.setCodigo(rs.getString("codigo"));
                d.setDescripcion(rs.getString("descripcion"));
                d.setDescripcion_ar(rs.getString("descripcion_ar"));
                d.setCantidad(rs.getString("cantidad"));
                d.setPrecio(rs.getString("precio"));
                d.setIva(rs.getString("iva"));
                d.setSubtotal(rs.getString("subtotal"));
                d.setId_categoria(rs.getInt("id_categoria"));
                detalle.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return detalle;
    }

    public int InsertarPedido(pedidos pedido, ArrayList<pedidos> detalle) {
        Connection con = conexion.getConnection();
        int r = 0;
        PreparedStatement ps;
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement("SET @usuario = ?");
            ps.setInt(1, Integer.valueOf(pedido.getId_usuario()));
            ps.execute();
            ps = con.prepareStatement("SELECT MAX(nro_orden)+1 AS nro_orden FROM pedidos WHERE id_sucursal=BuscarSucursalPorUsuario(?)");
            ps.setInt(1, Integer.valueOf(pedido.getId_usuario()));
            System.out.println("Este es el dichoso usuario " + pedido.getId_usuario());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pedido.setNro_orden(rs.getInt("nro_orden"));
            }
            ps = con.prepareStatement("INSERT INTO pedidos (nro_orden, fecha_ped, total_ped, total_iva_ped, descuento_ped,comentario_ped, id_delivery, id_cliente, fechahora_entrega, lugar_entrega, delivery_ped, nota_especial, creado_por, asignado_a, id_sucursal, motivo_anulado) VALUES (?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, BuscarSucursalPorUsuario(?), ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, pedido.getNro_orden());
            ps.setInt(2, pedido.getTotal_ped());
            ps.setInt(3, pedido.getTotal_iva_ped());
            ps.setInt(4, pedido.getDescuento_ped());
            ps.setString(5, pedido.getComentario_ped());
            ps.setString(6, pedido.getId_delivery() == 0 ? null : String.valueOf(pedido.getId_delivery()));
            ps.setString(7, pedido.getId_cliente());
            ps.setString(8, pedido.getFechahora_entrega());
            ps.setString(9, pedido.getLugar_entrega());
            ps.setInt(10, pedido.getDelivery_ped());
            ps.setString(11, pedido.getNota_especial());
            ps.setInt(12, pedido.getCreado_por());
            ps.setInt(13, pedido.getAsignado_a());
            ps.setString(14, pedido.getId_usuario());
            ps.setString(15, "-");
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                pedido.setId_pedido((int) rs.getLong(1));
                for (pedidos d : detalle) {
                    articulos articulo = new articulos();
                    articulo = dao.BuscarArticulos(d.getId_articulo());
                    ps = con.prepareStatement("INSERT INTO detalle_pedidos (pedidos_id_pedido, articulos_id_articulo, codigo, descripcion, descripcion_ar, cantidad, precio, iva, subtotal, id_categoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, pedido.getId_pedido());
                    ps.setInt(2, Integer.valueOf(d.getId_articulo()));
                    ps.setString(3, d.getCodigo());
                    ps.setString(4, d.getDescripcion());
                    ps.setString(5, d.getDescripcion_ar());
                    ps.setInt(6, Integer.valueOf(d.getCantidad()));
                    ps.setInt(7, Integer.valueOf(d.getPrecio()));
                    ps.setString(8, articulo.getIva_ar());
                    ps.setInt(9, Integer.valueOf(d.getPrecio()) * Integer.valueOf(d.getCantidad()));
                    ps.setInt(10, d.getId_categoria() == 0 ? 6 : d.getId_categoria());
                    r = ps.executeUpdate();
                }

                if (pedido.getSenha_ped() != 0) {
                    ps = con.prepareStatement("INSERT INTO pagos (fecha_pago, monto_pago, nro_comprobante, descripcion_pago, id_tipo_pago, id_pedido, id_banco) VALUES(NOW(), ?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, pedido.getSenha_ped());
                    ps.setString(2, pedido.getNro_movimiento());
                    if (pedido.getSenha_ped() == (pedido.getTotal_ped() - pedido.getDescuento_ped())) {
                        ps.setString(3, "PAGO_TOTAL");
                    } else {
                        ps.setString(3, "SENHA");
                    }
                    ps.setInt(4, pedido.getId_tipo_pago());
                    ps.setInt(5, pedido.getId_pedido());
                    ps.setString(6, pedido.getId_banco() != 0 ? String.valueOf(pedido.getId_banco()) : null);
                    ps.executeUpdate();

                    if (pedido.getSenha_ped() == (pedido.getTotal_ped() - pedido.getDescuento_ped())) {
                        ps = con.prepareStatement("UPDATE pedidos SET pagado_ped=1 WHERE id_pedido=?");
                        ps.setInt(1, pedido.getId_pedido());
                        ps.executeUpdate();

                        ps = con.prepareStatement("INSERT INTO detalle_caja(fecha_detalle, hora_detalle, concepto_detalle, tipo_pago, saldo_anterior, "
                                + "saldo_actual, debe_caja, haber_caja, tipo_detalle, id_arqueo, id_caja, id_pedido, id_usuario) "
                                + "VALUES (NOW(), NOW(), CONCAT('PAGO TOTAL POR LA ORDEN DE TRABAJO N° ', ?, ' PERTENECIENTE AL CLIENTE ',"
                                + "(SELECT nombre_cli FROM clientes WHERE id_cliente = ?)), ?, "
                                + "IFNULL(ObtenerSaldoActual(?),0), IFNULL((ObtenerSaldoActual(?)+ObtenerMontoPagado(?)),0), ObtenerMontoPagado(?), "
                                + "0, 'M', 0, BuscarCajaXSucursal(?), ?, ?)");
                        ps.setInt(1, pedido.getId_pedido());
                        ps.setString(2, pedido.getId_cliente());
                        ps.setInt(3, pedido.getId_tipo_pago());
                        ps.setString(4, pedido.getId_usuario());
                        ps.setString(5, pedido.getId_usuario());
                        ps.setInt(6, pedido.getId_pedido());
                        ps.setInt(7, pedido.getId_pedido());
                        ps.setInt(8, Integer.valueOf(pedido.getId_usuario()));
                        ps.setInt(9, pedido.getId_pedido());
                        ps.setString(10, pedido.getId_usuario());
                        ps.executeUpdate();
                    } else {
                        ps = con.prepareStatement("INSERT INTO detalle_caja(fecha_detalle, hora_detalle, concepto_detalle, tipo_pago, saldo_anterior, saldo_actual, debe_caja, "
                                + "haber_caja, tipo_detalle, id_caja, id_pedido, id_usuario) VALUES (NOW(), NOW(), "
                                + "CONCAT('SEÑA POR LA ORDEN DE TRABAJO N° ', ?, ' PERTENECIENTE AL CLIENTE ',(SELECT nombre_cli FROM clientes "
                                + "WHERE id_cliente = ?)), ?, IFNULL(ObtenerSaldoActual(?),0), IFNULL((ObtenerSaldoActual(?)+ObtenerMontoPagado(?)),0), "
                                + "ObtenerMontoPagado(?), 0, 'M', BuscarCajaXSucursal(?), ?, ?)");
                        ps.setInt(1, pedido.getId_pedido());
                        ps.setString(2, pedido.getId_cliente());
                        ps.setInt(3, pedido.getId_tipo_pago());
                        ps.setString(4, pedido.getId_usuario());
                        ps.setString(5, pedido.getId_usuario());
                        ps.setInt(6, pedido.getId_pedido());
                        ps.setInt(7, pedido.getId_pedido());
                        ps.setInt(8, Integer.valueOf(pedido.getId_usuario()));
                        ps.setInt(9, pedido.getId_pedido());
                        ps.setString(10, pedido.getId_usuario());
                        ps.executeUpdate();
                    }
                }

                if (pedido.getId_delivery() != 0 && pedido.getDelivery_ped() != 0) {
                    ps = con.prepareStatement("UPDATE deliveries SET estado=? WHERE id_delivery=?");
                    ps.setString(1, "ENVIO");
                    ps.setInt(2, pedido.getId_delivery());
                    ps.executeUpdate();
                } else if (pedido.getId_delivery() != 0) {
                    ps = con.prepareStatement("UPDATE deliveries SET estado=? WHERE id_delivery=?");
                    ps.setString(1, "GENERADO");
                    ps.setInt(2, pedido.getId_delivery());
                    ps.executeUpdate();
                }

                r = pedido.getId_pedido();
            }
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarPedido(pedidos pedido, ArrayList<pedidos> detalle) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("SET @usuario = ?");
            ps.setInt(1, Integer.valueOf(pedido.getId_usuario()));
            ps.execute();
            ps = con.prepareStatement("UPDATE pedidos SET total_ped=?, total_iva_ped=?, descuento_ped=?, comentario_ped=?, id_cliente=?, fechahora_entrega=?, "
                    + "lugar_entrega=?, nota_especial=?, asignado_a=?, delivery_ped=? WHERE id_pedido=?");
            ps.setInt(1, pedido.getTotal_ped());
            ps.setInt(2, pedido.getTotal_iva_ped());
            ps.setInt(3, pedido.getDescuento_ped());
            ps.setString(4, pedido.getComentario_ped());
            ps.setString(5, pedido.getId_cliente());
            ps.setString(6, pedido.getFechahora_entrega());
            ps.setString(7, pedido.getLugar_entrega());
            ps.setString(8, pedido.getNota_especial());
            ps.setInt(9, pedido.getAsignado_a());
            ps.setInt(10, pedido.getDelivery_ped());
            ps.setInt(11, pedido.getId_pedido());
            ps.executeUpdate();
            ps = con.prepareStatement("SELECT * FROM detalle_pedidos WHERE pedidos_id_pedido=?");
            ps.setInt(1, pedido.getId_pedido());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int cant = 0;
                PreparedStatement prs = con.prepareStatement("SELECT cantidad_stock FROM stock WHERE articulos_id_articulo = ?");
                prs.setString(1, rs.getString("articulos_id_articulo"));
                ResultSet res = prs.executeQuery();
                if (res.next()) {
                    cant = res.getInt("cantidad_stock") + rs.getInt("cantidad");
                    PreparedStatement p = con.prepareStatement("UPDATE stock SET cantidad_stock = ? WHERE articulos_id_articulo = ?");
                    p.setInt(1, cant);
                    p.setString(2, rs.getString("articulos_id_articulo"));
                    r = p.executeUpdate();
                }
            }
            ps = con.prepareStatement("DELETE FROM detalle_pedidos WHERE pedidos_id_pedido=?");
            ps.setInt(1, pedido.getId_pedido());
            ps.executeUpdate();
            for (pedidos d : detalle) {
                articulos articulo = new articulos();
                articulo = dao.BuscarArticulos(d.getId_articulo());
                ps = con.prepareStatement("INSERT INTO detalle_pedidos (pedidos_id_pedido, articulos_id_articulo, codigo, descripcion, descripcion_ar, cantidad, precio, iva, subtotal, id_categoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, pedido.getId_pedido());
                ps.setInt(2, Integer.valueOf(d.getId_articulo()));
                ps.setString(3, d.getCodigo());
                ps.setString(4, d.getDescripcion());
                ps.setString(5, d.getDescripcion_ar());
                ps.setInt(6, Integer.valueOf(d.getCantidad()));
                ps.setInt(7, Integer.valueOf(d.getPrecio()));
                ps.setString(8, articulo.getIva_ar());
                ps.setInt(9, (Integer.valueOf(d.getPrecio()) * Integer.valueOf(d.getCantidad())));
                ps.setInt(10, d.getId_categoria() == 0 ? 6 : d.getId_categoria());
                r = ps.executeUpdate();
            }

            ps = con.prepareStatement("SELECT * FROM pagos WHERE id_pedido=?");
            ps.setInt(1, pedido.getId_pedido());
            rs = ps.executeQuery();

            if (rs.next()) {
                ps = con.prepareStatement("SELECT pedidos.id_cliente, pagos.id_tipo_pago, ObtenerMontoPagado(?) AS senha_ped FROM pedidos LEFT JOIN pagos ON pedidos.id_pedido=pagos.id_pedido WHERE pedidos.id_pedido = ?");
                ps.setInt(1, pedido.getId_pedido());
                ps.setInt(2, pedido.getId_pedido());
                rs = ps.executeQuery();
                if (rs.next()) {
                    if (pedido.getSenha_ped() != rs.getInt("senha_ped")) {
                        if (pedido.getSenha_ped() > rs.getInt("senha_ped")) {
                            ps = con.prepareStatement("INSERT INTO detalle_caja(fecha_detalle, hora_detalle, concepto_detalle, tipo_pago, saldo_anterior, saldo_actual, debe_caja, "
                                    + "haber_caja, tipo_detalle, id_caja, id_pedido, id_usuario) VALUES (NOW(), NOW(), "
                                    + "CONCAT('AGREGADO A LA SEÑA DE LA ORDEN DE TRABAJO N° ', ?, ' PERTENECIENTE AL CLIENTE ',(SELECT nombre_cli "
                                    + "FROM clientes WHERE id_cliente = ?)), ?, IFNULL(ObtenerSaldoActual(?),0), IFNULL((ObtenerSaldoActual(?)+?),0), ?, "
                                    + "0, 'M', BuscarCajaXSucursal(?), ?, ?)");
                            ps.setInt(1, pedido.getId_pedido());
                            ps.setString(2, pedido.getId_cliente());
                            ps.setInt(3, pedido.getId_tipo_pago());
                            ps.setString(4, pedido.getId_usuario());
                            ps.setString(5, pedido.getId_usuario());
                            ps.setInt(6, pedido.getSenha_ped() - rs.getInt("senha_ped"));
                            ps.setInt(7, pedido.getSenha_ped() - rs.getInt("senha_ped"));
                            ps.setInt(8, Integer.valueOf(pedido.getId_usuario()));
                            ps.setInt(9, pedido.getId_pedido());
                            ps.setString(10, pedido.getId_usuario());
                            ps.executeUpdate();
                        } else {
                            if (pedido.getSenha_ped() == 0) {
                                pedido.setId_cliente(rs.getString("id_cliente"));
                                pedido.setId_tipo_pago(rs.getInt("id_tipo_pago"));
                            }
                            ps = con.prepareStatement("INSERT INTO detalle_caja(fecha_detalle, hora_detalle, concepto_detalle, tipo_pago, saldo_anterior, saldo_actual, debe_caja, "
                                    + "haber_caja, tipo_detalle, id_venta, id_pedido, id_usuario) VALUES (NOW(), NOW(), "
                                    + "CONCAT('DESCONTADO DE LA SEÑA DE LA ORDEN DE TRABAJO N° ', ?, ' PERTENECIENTE AL CLIENTE ',(SELECT nombre_cli "
                                    + "FROM clientes WHERE id_cliente = ?)), ?,IFNULL(ObtenerSaldoActual(?),0), IFNULL((ObtenerSaldoActual(?)-?),0), 0, "
                                    + "?, 'M', BuscarCajaXSucursal(?), ?, ?)");
                            ps.setInt(1, pedido.getId_pedido());
                            ps.setString(2, pedido.getId_cliente());
                            ps.setInt(3, pedido.getId_tipo_pago());
                            ps.setString(4, pedido.getId_usuario());
                            ps.setString(5, pedido.getId_usuario());
                            ps.setInt(6, rs.getInt("senha_ped") - pedido.getSenha_ped());
                            ps.setInt(7, rs.getInt("senha_ped") - pedido.getSenha_ped());
                            ps.setInt(8, Integer.valueOf(pedido.getId_usuario()));
                            ps.setInt(9, pedido.getId_pedido());
                            ps.setString(10, pedido.getId_usuario());
                            ps.executeUpdate();
                        }
                    }
                }
                ps = con.prepareStatement("UPDATE pagos SET fecha_pago=NOW(), monto_pago=?, nro_comprobante=?, descripcion_pago=?, id_tipo_pago=?, id_pedido=?, id_banco=? WHERE id_pedido=?");
                ps.setInt(1, pedido.getSenha_ped());
                ps.setString(2, pedido.getNro_movimiento());
                if (pedido.getSenha_ped() == (pedido.getTotal_ped() - pedido.getDescuento_ped())) {
                    ps.setString(3, "PAGO_TOTAL");
                } else {
                    ps.setString(3, "SENHA");
                }
                ps.setInt(4, pedido.getId_tipo_pago());
                ps.setInt(5, pedido.getId_pedido());
                ps.setString(6, pedido.getId_banco() != 0 ? String.valueOf(pedido.getId_banco()) : null);
                ps.setInt(7, pedido.getId_pedido());
                ps.executeUpdate();
                if (pedido.getSenha_ped() == (pedido.getTotal_ped() - pedido.getDescuento_ped())) {
                    ps = con.prepareStatement("UPDATE pedidos SET pagado_ped=1 WHERE id_pedido=?");
                    ps.setInt(1, pedido.getId_pedido());
                    ps.executeUpdate();
                }
            } else {
                if (pedido.getSenha_ped() != 0) {
                    ps = con.prepareStatement("INSERT INTO pagos (fecha_pago, monto_pago, nro_comprobante, descripcion_pago, id_tipo_pago, id_pedido, id_banco) VALUES(NOW(), ?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, pedido.getSenha_ped());
                    ps.setString(2, pedido.getNro_movimiento());
                    if (pedido.getSenha_ped() == (pedido.getTotal_ped() - pedido.getDescuento_ped())) {
                        ps.setString(3, "PAGO_TOTAL");
                    } else {
                        ps.setString(3, "SENHA");
                    }
                    ps.setInt(4, pedido.getId_tipo_pago());
                    ps.setInt(5, pedido.getId_pedido());
                    ps.setString(6, pedido.getId_banco() != 0 ? String.valueOf(pedido.getId_banco()) : null);
                    ps.executeUpdate();

                    if (pedido.getSenha_ped() == (pedido.getTotal_ped() - pedido.getDescuento_ped())) {
                        ps = con.prepareStatement("UPDATE pedidos SET pagado_ped=1 WHERE id_pedido=?");
                        ps.setInt(1, pedido.getId_pedido());
                        ps.executeUpdate();

                        ps = con.prepareStatement("INSERT INTO detalle_caja(fecha_detalle, hora_detalle, concepto_detalle, tipo_pago, saldo_anterior, saldo_actual, debe_caja, "
                                + "haber_caja, tipo_detalle, id_caja, id_pedido, id_usuario) VALUES (NOW(), NOW(), "
                                + "CONCAT('PAGO TOTAL POR LA ORDEN DE TRABAJO N° ', ?, ' PERTENECIENTE AL CLIENTE ',(SELECT nombre_cli FROM clientes "
                                + "WHERE id_cliente = ?)), ?,IFNULL(ObtenerSaldoActual(?),0), IFNULL((ObtenerSaldoActual(?)+ObtenerMontoPagado(?)),0), "
                                + "ObtenerMontoPagado(?), 0, 'M', BuscarCajaXSucursal(?), ?, ?)");
                        ps.setInt(1, pedido.getId_pedido());
                        ps.setString(2, pedido.getId_cliente());
                        ps.setInt(3, pedido.getId_tipo_pago());
                        ps.setString(4, pedido.getId_usuario());
                        ps.setString(5, pedido.getId_usuario());
                        ps.setInt(6, pedido.getId_pedido());
                        ps.setInt(7, pedido.getId_pedido());
                        ps.setInt(8, Integer.valueOf(pedido.getId_usuario()));
                        ps.setInt(9, pedido.getId_pedido());
                        ps.setString(10, pedido.getId_usuario());
                        ps.executeUpdate();
                    } else {
                        ps = con.prepareStatement("INSERT INTO detalle_caja(fecha_detalle, hora_detalle, concepto_detalle, tipo_pago, saldo_anterior, saldo_actual, debe_caja, "
                                + "haber_caja, tipo_detalle, id_caja, id_pedido, id_usuario) VALUES (NOW(), NOW(), "
                                + "CONCAT('SEÑA POR LA ORDEN DE TRABAJO N° ', ?, ' PERTENECIENTE AL CLIENTE ',(SELECT nombre_cli FROM clientes "
                                + "WHERE id_cliente = ?)), ?,IFNULL(ObtenerSaldoActual(?),0), IFNULL((ObtenerSaldoActual(?)+ObtenerMontoPagado(?)),0), "
                                + "ObtenerMontoPagado(?), 0, 'M', BuscarCajaXSucursal(?), ?, ?)");
                        ps.setInt(1, pedido.getId_pedido());
                        ps.setString(2, pedido.getId_cliente());
                        ps.setInt(3, pedido.getId_tipo_pago());
                        ps.setString(4, pedido.getId_usuario());
                        ps.setString(5, pedido.getId_usuario());
                        ps.setInt(6, pedido.getId_pedido());
                        ps.setInt(7, pedido.getId_pedido());
                        ps.setInt(8, Integer.valueOf(pedido.getId_usuario()));
                        ps.setInt(9, pedido.getId_pedido());
                        ps.setString(10, pedido.getId_usuario());
                        ps.executeUpdate();
                    }
                }
            }

            if (r != 0) {
                r = pedido.getId_pedido();
            }
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int AprobarPedido(int id_pedido, int id_usuario) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SET @usuario = ?");
            ps.setInt(1, id_usuario);
            ps.execute();
            ps = con.prepareStatement("UPDATE pedidos SET aprobado_ped = !aprobado_ped WHERE id_pedido=?");
            ps.setInt(1, id_pedido);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int AnularPedido(pedidos p) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SET @usuario = ?");
            ps.setInt(1, Integer.valueOf(p.getId_usuario()));
            ps.execute();
            ps = con.prepareStatement("SELECT pedidos.id_cliente, pagos.id_tipo_pago FROM pedidos LEFT JOIN pagos ON pedidos.id_pedido=pagos.id_pedido WHERE pedidos.id_pedido = ?");
            ps.setInt(1, p.getId_pedido());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p.setId_cliente(rs.getString("id_cliente"));
                p.setId_tipo_pago(rs.getInt("id_tipo_pago"));
            }
            ps = con.prepareStatement("UPDATE pedidos SET close_ped=?, motivo_anulado=? WHERE id_pedido=?");
            ps.setInt(1, p.getClose_ped());
            ps.setString(2, p.getMotivo_anulado());
            ps.setInt(3, p.getId_pedido());
            ps.executeUpdate();
            ps = con.prepareStatement("SELECT * FROM detalle_pedidos WHERE pedidos_id_pedido=?");
            ps.setInt(1, p.getId_pedido());
            rs = ps.executeQuery();
            while (rs.next()) {
                int cant = 0;
                PreparedStatement prs = con.prepareStatement("SELECT cantidad_stock FROM stock WHERE articulos_id_articulo = ?");
                prs.setString(1, rs.getString("articulos_id_articulo"));
                ResultSet res = prs.executeQuery();
                if (res.next()) {
                    if (p.getClose_ped() == 1) {
                        cant = res.getInt("cantidad_stock") + rs.getInt("cantidad");
                    } else if (p.getClose_ped() == 0) {
                        cant = res.getInt("cantidad_stock") - rs.getInt("cantidad");
                    }
                    PreparedStatement pd = con.prepareStatement("UPDATE stock SET cantidad_stock = ? WHERE articulos_id_articulo = ?");
                    pd.setInt(1, cant);
                    pd.setString(2, rs.getString("articulos_id_articulo"));
                    r = pd.executeUpdate();
                }
            }
            if (p.getId_tipo_pago() != 0) {
                ps = con.prepareStatement("INSERT INTO detalle_caja(fecha_detalle, hora_detalle, concepto_detalle, tipo_pago, saldo_anterior, saldo_actual, debe_caja, "
                        + "haber_caja, tipo_detalle, id_caja, id_pedido, id_usuario) VALUES (NOW(), NOW(), "
                        + "CONCAT('SE ANULO LA ORDEN DE TRABAJO N° ', ?, ' PERTENECIENTE AL CLIENTE ',(SELECT nombre_cli FROM clientes "
                        + "WHERE id_cliente = ?)), ?,IFNULL(ObtenerSaldoActual(?),0), IFNULL((ObtenerSaldoActual(?)-ObtenerMontoPagado(?)),0), 0, "
                        + "ObtenerMontoPagado(?), 'M', BuscarCajaXSucursal(?), ?, ?)");
                ps.setInt(1, p.getId_pedido());
                ps.setString(2, p.getId_cliente());
                ps.setInt(3, p.getId_tipo_pago());
                ps.setString(4, p.getId_usuario());
                ps.setString(5, p.getId_usuario());
                ps.setInt(6, p.getId_pedido());
                ps.setInt(7, p.getId_pedido());
                ps.setInt(8, Integer.valueOf(p.getId_usuario()));
                ps.setInt(9, p.getId_pedido());
                ps.setString(10, p.getId_usuario());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int RetirarPedido(int id_pedido, String estado, int id_usuario) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SET @usuario = ?");
            ps.setInt(1, id_usuario);
            ps.execute();
            ps = con.prepareStatement("UPDATE pedidos SET retirado_ped=? WHERE id_pedido=?");
            ps.setString(1, estado);
            ps.setInt(2, id_pedido);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int FacturarPedido(int id_pedido) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("UPDATE pedidos SET facturado_ped = 1 WHERE id_pedido=?");
            ps.setInt(1, id_pedido);
            System.out.println(ps);
            r = ps.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarEstadoOrden(ArrayList<pedidos> pedido) {
        int r = 0;
        Connection con = conexion.getConnection();
        try {
            for (pedidos d : pedido) {
                PreparedStatement ps = con.prepareStatement("UPDATE detalle_pedidos SET id_categoria=? WHERE pedidos_id_pedido=? AND articulos_id_articulo=?");
                ps.setInt(1, d.getId_categoria());
                ps.setInt(2, d.getId_pedido());
                ps.setInt(3, Integer.valueOf(d.getId_articulo()));
                r = ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int HabilitarOrden(pedidos p) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_cliente, pagos.id_tipo_pago FROM pedidos LEFT JOIN pagos ON pedidos.id_pedido=pagos.id_pedido WHERE pedidos.id_pedido = ?");
            ps.setInt(1, p.getId_pedido());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p.setId_cliente(rs.getString("id_cliente"));
                p.setId_tipo_pago(rs.getInt("id_tipo_pago"));
            }
            ps = con.prepareStatement("UPDATE pedidos SET close_ped=?, motivo_anulado=? WHERE id_pedido=?");
            ps.setInt(1, p.getClose_ped());
            ps.setString(2, p.getMotivo_anulado());
            ps.setInt(3, p.getId_pedido());
            ps.executeUpdate();
            ps = con.prepareStatement("SELECT * FROM detalle_pedidos WHERE pedidos_id_pedido=?");
            ps.setInt(1, p.getId_pedido());
            rs = ps.executeQuery();
            while (rs.next()) {
                int cant = 0;
                PreparedStatement prs = con.prepareStatement("SELECT cantidad_stock FROM stock WHERE articulos_id_articulo = ?");
                prs.setString(1, rs.getString("articulos_id_articulo"));
                ResultSet res = prs.executeQuery();
                if (res.next()) {
                    if (p.getClose_ped() == 1) {
                        cant = res.getInt("cantidad_stock") + rs.getInt("cantidad");
                    } else if (p.getClose_ped() == 0) {
                        cant = res.getInt("cantidad_stock") - rs.getInt("cantidad");
                    }
                    PreparedStatement pd = con.prepareStatement("UPDATE stock SET cantidad_stock = ? WHERE articulos_id_articulo = ?");
                    pd.setInt(1, cant);
                    pd.setString(2, rs.getString("articulos_id_articulo"));
                    r = pd.executeUpdate();
                }
            }
            if (p.getId_tipo_pago() != 0) {
                ps = con.prepareStatement("INSERT INTO detalle_caja(fecha_detalle, hora_detalle, concepto_detalle, tipo_pago, saldo_anterior, saldo_actual, debe_caja, "
                        + "haber_caja, tipo_detalle, id_caja, id_pedido, id_usuario) VALUES (NOW(), NOW(), "
                        + "CONCAT('SE HABILITÓ LA ORDEN DE TRABAJO N° ', ?, ' PERTENECIENTE AL CLIENTE ',(SELECT nombre_cli FROM clientes "
                        + "WHERE id_cliente = ?)), ?,IFNULL(ObtenerSaldoActual(?),0), IFNULL((ObtenerSaldoActual(?)+ObtenerMontoPagado(?)),0), "
                        + "ObtenerMontoPagado(?), 0, 'M', BuscarCajaXSucursal(?), ?, ?)");
                ps.setInt(1, p.getId_pedido());
                ps.setString(2, p.getId_cliente());
                ps.setInt(3, p.getId_tipo_pago());
                ps.setString(4, p.getId_usuario());
                ps.setString(5, p.getId_usuario());
                ps.setInt(6, p.getId_pedido());
                ps.setInt(7, p.getId_pedido());
                ps.setInt(8, Integer.valueOf(p.getId_usuario()));
                ps.setInt(9, p.getId_pedido());
                ps.setString(10, p.getId_usuario());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int AgregarPagoTotal(pedidos p) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("UPDATE pedidos SET pagado_ped=1 WHERE id_pedido=?");
            ps.setInt(1, p.getId_pedido());
            ps.executeUpdate();

            ps = con.prepareStatement("SELECT (total_ped-descuento_ped) as total_ped, ObtenerMontoPagado(?) AS monto_pago, id_cliente FROM pedidos WHERE id_pedido=? AND close_ped=0");
            ps.setInt(1, p.getId_pedido());
            ps.setInt(2, p.getId_pedido());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p.setId_cliente(rs.getString("id_cliente"));
                ps = con.prepareStatement("INSERT INTO pagos (fecha_pago, monto_pago, nro_comprobante, descripcion_pago, id_tipo_pago, id_pedido, id_banco) VALUES(NOW(), ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, rs.getInt("total_ped") - rs.getInt("monto_pago"));
                ps.setString(2, p.getNro_movimiento());
                ps.setString(3, "PAGO_TOTAL");
                ps.setInt(4, p.getId_tipo_pago());
                ps.setInt(5, p.getId_pedido());
                ps.setString(6, p.getId_banco() != 0 ? String.valueOf(p.getId_banco()) : null);
                r = ps.executeUpdate();
            }

            ps = con.prepareStatement("INSERT INTO detalle_caja(fecha_detalle, hora_detalle, concepto_detalle, tipo_pago, saldo_anterior, saldo_actual, debe_caja, "
                    + "haber_caja, tipo_detalle, id_caja, id_pedido, id_usuario) VALUES (NOW(), NOW(), "
                    + "CONCAT('PAGO TOTAL POR LA ORDEN DE TRABAJO N° ', ?, ' PERTENECIENTE AL CLIENTE ',(SELECT nombre_cli FROM clientes "
                    + "WHERE id_cliente = ?)), ?,IFNULL(ObtenerSaldoActual(?),0), IFNULL((ObtenerSaldoActual(?)+?),0), ?, "
                    + "0, 'M', BuscarCajaXSucursal(?), ?, ?)");
            ps.setInt(1, p.getId_pedido());
            ps.setString(2, p.getId_cliente());
            ps.setInt(3, p.getId_tipo_pago());
            ps.setString(4, p.getId_usuario());
            ps.setString(5, p.getId_usuario());
            ps.setInt(6, rs.getInt("total_ped") - rs.getInt("monto_pago"));
            ps.setInt(7, rs.getInt("total_ped") - rs.getInt("monto_pago"));
            ps.setInt(8, Integer.valueOf(p.getId_usuario()));
            ps.setInt(9, p.getId_pedido());
            ps.setString(10, p.getId_usuario());
            ps.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            conexion.Close(con);
        }

        return r;
    }

    public int ValidarReporteOrden(String fecha_inicio, String fecha_fin, int id_cliente) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT clientes.nombre_cli, clientes.ndocumento_cli, clientes.dv_cli, SUM(pagos.monto_pago) "
                    + "as monto_pago, SUM(pedidos.total_ped) as total_ped FROM clientes, pedidos LEFT JOIN pagos ON pedidos.id_pedido=pagos.id_pago "
                    + "WHERE pedidos.fecha_ped BETWEEN ? AND ? AND pedidos.id_cliente=clientes.id_cliente AND pedidos.close_ped=0 AND pedidos.id_cliente=? "
                    + "GROUP BY pedidos.id_cliente");
            ps.setString(1, fecha_inicio);
            ps.setString(2, fecha_fin);
            ps.setInt(3, id_cliente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                r = 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public ArrayList<pedidos> ListarClienteHistorial(pedidos c) {
        Connection con = conexion.getConnection();
        ArrayList<pedidos> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped, pedidos.fechahora_entrega, "
                    + "pedidos.close_ped, pedidos.total_ped, pedidos.comentario_ped, clientes.nombre_cli, clientes.ndocumento_cli, clientes.dv_cli, "
                    + "pedidos.aprobado_ped, pedidos.facturado_ped, pedidos.retirado_ped, pedidos.id_sucursal, sucursal.nombre_sucursal, "
                    + "ObtenerMontoPagado(pedidos.id_pedido) as monto_pago, clientes.id_cliente FROM pedidos, clientes, sucursal WHERE "
                    + "pedidos.id_cliente=clientes.id_cliente AND close_ped=0 AND sucursal.id_sucursal = pedidos.id_sucursal AND clientes.id_cliente=?");
            ps.setString(1, c.getId_cliente());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos p = new pedidos();
                p.setId_pedido(rs.getInt("id_pedido"));
                p.setNro_orden(rs.getInt("nro_orden"));
                p.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                p.setFechahora_entrega(f.ParseDI(rs.getString("fechahora_entrega")));
                p.setClose_ped(rs.getInt("close_ped"));
                p.setTotal_ped(rs.getInt("total_ped"));
                p.setComentario_ped(rs.getString("comentario_ped"));
                p.setNombre_cli(rs.getString("nombre_cli"));
                p.setNdocumento_cli(rs.getString("ndocumento_cli") + "-" + rs.getString("dv_cli"));
                p.setAprobado_ped(rs.getInt("aprobado_ped"));
                p.setFacturado_ped(rs.getInt("facturado_ped"));
                p.setRetirado_ped(rs.getString("retirado_ped"));
                p.setSenha_ped(rs.getInt("monto_pago"));
                p.setId_sucursal(rs.getString("id_sucursal"));
                p.setNombre_sucursal(rs.getString("nombre_sucursal"));
                p.setId_cliente(rs.getString("id_cliente"));
                pedidos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedidos;
    }

    public pedidos BuscarOrden(int id) {
        Connection con = conexion.getConnection();
        pedidos pedido = new pedidos();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.nro_orden, pedidos.fecha_ped,pedidos.close_ped, "
                    + "pedidos.total_ped, pedidos.descuento_ped, pedidos.comentario_ped, clientes.id_cliente, clientes.nombre_cli, clientes.ndocumento_cli, "
                    + "clientes.dv_cli, clientes.telefono_cli, clientes.celular_cli, pedidos.fechahora_entrega, pedidos.lugar_entrega, "
                    + "pedidos.delivery_ped, pedidos.nota_especial,pedidos.asignado_a, empleados.nombre_emp, empleados.apellido_emp, "
                    + "ObtenerMontoPagado(pedidos.id_pedido) as monto_pago FROM pedidos, clientes, usuarios, empleados WHERE "
                    + "pedidos.id_cliente=clientes.id_cliente AND pedidos.id_pedido=? AND pedidos.close_ped=0 AND "
                    + "usuarios.empleados_id_empleado=empleados.id_empleado AND usuarios.id_usuario=pedidos.asignado_a");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pedido.setId_pedido(rs.getInt("id_pedido"));
                pedido.setNro_orden(rs.getInt("nro_orden"));
                pedido.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                pedido.setClose_ped(rs.getInt("close_ped"));
                pedido.setDescuento_ped(rs.getInt("descuento_ped"));
                pedido.setTotal_ped(rs.getInt("total_ped"));
                pedido.setComentario_ped(rs.getString("comentario_ped"));
                pedido.setId_cliente(rs.getString("id_cliente"));
                pedido.setNombre_cli(rs.getString("nombre_cli"));
                pedido.setNdocumento_cli(rs.getString("ndocumento_cli") + "-" + rs.getString("dv_cli"));
                pedido.setCelular_cli(rs.getString("celular_cli"));
                pedido.setTelefono_cli(rs.getString("telefono_cli"));
                pedido.setFechahora_entrega(rs.getString("fechahora_entrega"));
                pedido.setLugar_entrega(rs.getString("lugar_entrega"));
                pedido.setDelivery_ped(rs.getInt("delivery_ped"));
                pedido.setNota_especial(rs.getString("nota_especial"));
                pedido.setAsignado_a(rs.getInt("asignado_a"));
                pedido.setSenha_ped(rs.getInt("monto_pago"));
                pedido.setNombre_emp(rs.getString("nombre_emp") + " " + rs.getString("apellido_emp"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedido;
    }

    public ArrayList<pedidos> ListarCuentasPendientes() {
        Connection con = conexion.getConnection();
        ArrayList<pedidos> cuentas = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM cuentas");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos cuenta = new pedidos();
                cuenta.setId_pedido(1);
                cuenta.setId_cliente(rs.getString("id_cliente"));
                cuenta.setNombre_cli(rs.getString("nombre_cli"));
                cuenta.setNombre_sucursal(rs.getString("nombre_sucursal"));
                cuenta.setNdocumento_cli(rs.getString("ndocumento_cli"));
                cuenta.setTotal_ped(rs.getInt("total"));
                cuenta.setSenha_ped(rs.getInt("senha"));
                cuenta.setSaldo(rs.getInt("saldo"));
                cuentas.add(cuenta);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return cuentas;
    }

    public ArrayList<pedidos> ListarPedidosporClientes(int id_cliente) {
        ArrayList<pedidos> pedidos = new ArrayList<>();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pedidos.id_pedido, pedidos.fecha_ped, clientes.id_cliente, clientes.nombre_cli, "
                    + "CONCAT(clientes.ndocumento_cli,\" \", clientes.dv_cli) as ruc_cli, "
                    + "IFNULL(SUM(pagos.monto_pago),0) as senha, (pedidos.total_ped-IFNULL(SUM(pagos.monto_pago),0)) as total_ped "
                    + "FROM pedidos LEFT JOIN pagos ON pagos.id_pedido=pedidos.id_pedido, clientes WHERE pagado_ped=0 AND close_ped=0 "
                    + "AND pedidos.id_cliente=? AND clientes.id_cliente=pedidos.id_cliente GROUP BY pedidos.id_pedido");
            ps.setInt(1, id_cliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos p = new pedidos();
                p.setId_pedido(rs.getInt("id_pedido"));
                p.setId_cliente(rs.getString("id_cliente"));
                p.setFecha_ped(f.ParseDI(rs.getString("fecha_ped")));
                p.setNombre_cli(rs.getString("nombre_cli"));
                p.setNdocumento_cli(rs.getString("ruc_cli"));
                p.setSenha_ped(rs.getInt("senha"));
                p.setTotal_ped(rs.getInt("total_ped"));
                pedidos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pedidos;
    }

    public int CobrarCuentas(pedidos pedido) {
        int r = 0;
        Connection con = conexion.getConnection();
        ArrayList<pedidos> pedidos = ListarPedidosporClientes(Integer.valueOf(pedido.getId_cliente()));
        PreparedStatement ps;
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement("SET @usuario = ?");
            ps.setInt(1, Integer.valueOf(pedido.getId_usuario()));
            ps.execute();
            for (pedidos p : pedidos) {
                int descuento = 0;
                if (pedido.getDescuento_ped() > 0) {
                    ps = con.prepareStatement("SELECT descuento_ped FROM pedidos WHERE id_pedido=? AND close_ped=0");
                    ps.setInt(1, p.getId_pedido());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        descuento = rs.getInt("descuento_ped") + pedido.getDescuento_ped();
                        ps = con.prepareStatement("UPDATE pedidos SET descuento_ped=? WHERE id_pedido=?");
                        if (descuento > p.getTotal_ped()) {
                            ps.setInt(1, p.getTotal_ped());
                            pedido.setDescuento_ped(descuento - p.getTotal_ped());
                        } else {
                            ps.setInt(1, descuento);
                            pedido.setDescuento_ped(0);
                        }
                        ps.setInt(2, p.getId_pedido());
                        ps.executeUpdate();
                    }
                }

                if (pedido.getTotal_ped() != 0) {
                    if (pedido.getTotal_ped() >= p.getTotal_ped()) {
                        int pago = pedido.getTotal_ped() - p.getTotal_ped();
                        ps = con.prepareStatement("UPDATE pedidos SET pagado_ped=1 WHERE id_pedido=?");
                        ps.setInt(1, p.getId_pedido());
                        ps.executeUpdate();

                        ps = con.prepareStatement("SET @monto_pago=(SELECT IFNULL(SUM(monto_pago),0) FROM pagos WHERE id_pedido=?)");
                        ps.setInt(1, p.getId_pedido());
                        ps.executeQuery();

                        ps = con.prepareStatement("SELECT (total_ped-descuento_ped) as total_ped, @monto_pago AS monto_pago, id_cliente FROM pedidos WHERE id_pedido=? AND close_ped=0");
                        ps.setInt(1, p.getId_pedido());
                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                            p.setId_cliente(rs.getString("id_cliente"));
                            ps = con.prepareStatement("INSERT INTO pagos (fecha_pago, monto_pago, nro_comprobante, descripcion_pago, id_tipo_pago, id_pedido, id_banco) VALUES(NOW(), ?, ?, ?, ?, ?, ?)");
                            ps.setInt(1, rs.getInt("total_ped") - rs.getInt("monto_pago"));
                            ps.setString(2, pedido.getNro_movimiento());
                            ps.setString(3, "PAGO_TOTAL");
                            ps.setInt(4, pedido.getId_tipo_pago());
                            ps.setInt(5, p.getId_pedido());
                            ps.setString(6, pedido.getId_banco() != 0 ? String.valueOf(p.getId_banco()) : null);
                            r = ps.executeUpdate();
                        }

                        ps = con.prepareStatement("SET @id_detalle=(SELECT max(detalle_caja.id_detalle) FROM detalle_caja, usuarios WHERE detalle_caja.id_caja=usuarios.id_caja AND usuarios.id_usuario=?)");
                        ps.setInt(1, Integer.valueOf(pedido.getId_usuario()));
                        ps.execute();

                        ps = con.prepareStatement("SET @id_caja=(SELECT id_caja FROM usuarios WHERE id_usuario=?)");
                        ps.setInt(1, Integer.valueOf(pedido.getId_usuario()));
                        ps.execute();

                        ps = con.prepareStatement("SET @saldo_actual=(SELECT saldo_actual FROM detalle_caja WHERE id_detalle=@id_detalle)");
                        ps.execute();

                        ps = con.prepareStatement("INSERT INTO detalle_caja(fecha_detalle, hora_detalle, concepto_detalle, tipo_pago, saldo_anterior, saldo_actual, "
                                + "debe_caja, haber_caja, tipo_detalle, id_arqueo, id_caja, id_pedido, id_usuario) "
                                + "VALUES (NOW(), NOW(), CONCAT('SEÑA POR LA ORDEN DE TRABAJO N° ', ?, ' PERTENECIENTE AL CLIENTE ', "
                                + "(SELECT nombre_cli FROM clientes WHERE id_cliente = ?)), ?, IFNULL( @saldo_actual,0), (IFNULL(@saldo_actual,0)+?), ?, 0, 'M', 0, "
                                + "@id_caja, ?, ?)");
                        ps.setInt(1, p.getId_pedido());
                        ps.setString(2, p.getId_cliente());
                        ps.setInt(3, pedido.getId_tipo_pago());
                        ps.setInt(4, rs.getInt("total_ped") - rs.getInt("monto_pago"));
                        ps.setInt(5, rs.getInt("total_ped") - rs.getInt("monto_pago"));
                        ps.setInt(6, p.getId_pedido());
                        ps.setString(7, pedido.getId_usuario());
                        r = ps.executeUpdate();
                        pedido.setTotal_ped(pago);
                    } else {
                        ps = con.prepareStatement("INSERT INTO pagos (fecha_pago, monto_pago, nro_comprobante, descripcion_pago, id_tipo_pago, id_pedido, id_banco) VALUES(NOW(), ?, ?, ?, ?, ?, ?)");
                        ps.setInt(1, pedido.getTotal_ped());
                        ps.setString(2, p.getNro_movimiento());
                        ps.setString(3, "SENHA");
                        ps.setInt(4, pedido.getId_tipo_pago());
                        ps.setInt(5, p.getId_pedido());
                        ps.setString(6, pedido.getId_banco() != 0 ? String.valueOf(pedido.getId_banco()) : null);
                        ps.executeUpdate();

                        ps = con.prepareStatement("SET @id_detalle=(SELECT max(detalle_caja.id_detalle) FROM detalle_caja, usuarios WHERE detalle_caja.id_caja=usuarios.id_caja AND usuarios.id_usuario=?)");
                        ps.setInt(1, Integer.valueOf(pedido.getId_usuario()));
                        ps.execute();

                        ps = con.prepareStatement("SET @id_caja=(SELECT id_caja FROM usuarios WHERE id_usuario=?)");
                        ps.setInt(1, Integer.valueOf(pedido.getId_usuario()));
                        ps.execute();

                        ps = con.prepareStatement("SET @saldo_actual=(SELECT saldo_actual FROM detalle_caja WHERE id_detalle=@id_detalle)");
                        ps.execute();

                        ps = con.prepareStatement("INSERT INTO detalle_caja(fecha_detalle, hora_detalle, concepto_detalle, tipo_pago, saldo_anterior, saldo_actual, "
                                + "debe_caja, haber_caja, tipo_detalle, id_arqueo, id_caja, id_pedido, id_usuario) "
                                + "VALUES (NOW(), NOW(), CONCAT('SEÑA POR LA ORDEN DE TRABAJO N° ', ?, ' PERTENECIENTE AL CLIENTE ', "
                                + "(SELECT nombre_cli FROM clientes WHERE id_cliente = ?)), ?, IFNULL( @saldo_actual,0), (IFNULL(@saldo_actual,0)+?), ?, 0, 'M', 0, "
                                + "@id_caja, ?, ?)");
                        ps.setInt(1, p.getId_pedido());
                        ps.setString(2, p.getId_cliente());
                        ps.setInt(3, pedido.getId_tipo_pago());
                        ps.setInt(4, pedido.getTotal_ped());
                        ps.setInt(5, pedido.getTotal_ped());
                        ps.setInt(6, p.getId_pedido());
                        ps.setString(7, pedido.getId_usuario());
                        r = ps.executeUpdate();
                        pedido.setTotal_ped(0);
                    }
                }
            }
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DAOpedidos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
