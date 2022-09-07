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
import modelo.ordendecompra;

/**
 *
 * @author akio
 */
public class DAOordencompra {

    funciones f = new funciones();
    DAOauditoria dao = new DAOauditoria();

    public ArrayList<ordendecompra> ListarOrdenes() {
        Connection con = conexion.getConnection();
        ArrayList<ordendecompra> oc = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ordendecompra oc INNER JOIN proveedores p ON p.id_proveedor = oc.proveedores_id_proveedor WHERE oc.recibido_ord = 0 AND oc.close_ord = 0 and aprobado_ord = 0");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ordendecompra o = new ordendecompra();
                o.setId_orden_compra(rs.getString("id_orden_compra"));
                o.setFecha_ord(f.ParseDI(rs.getString("fecha_ord")));
                o.setAprobado_ord(rs.getString("aprobado_ord"));
                o.setRecibido_ord(rs.getString("recibido_ord"));
                o.setClose_ord(rs.getString("close_ord"));
                o.setTotal_ord(rs.getString("total_ord"));
                o.setTotal_iva_ord(rs.getString("total_iva_ord"));
                o.setId_proveedor(rs.getString("id_proveedor"));
                o.setNombre_pro(rs.getString("nombre_pro"));
                oc.add(o);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOordencompra.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return oc;
    }

    public ArrayList<ordendecompra> ListarOrdenesAprobadas() {
        Connection con = conexion.getConnection();
        ArrayList<ordendecompra> oc = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ordendecompra oc INNER JOIN proveedores p ON p.id_proveedor = oc.proveedores_id_proveedor WHERE oc.recibido_ord = 0 AND oc.close_ord = 0 and aprobado_ord = 1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ordendecompra o = new ordendecompra();
                o.setId_orden_compra(rs.getString("id_orden_compra"));
                o.setFecha_ord(f.ParseDI(rs.getString("fecha_ord")));
                o.setAprobado_ord(rs.getString("aprobado_ord"));
                o.setRecibido_ord(rs.getString("recibido_ord"));
                o.setClose_ord(rs.getString("close_ord"));
                o.setTotal_ord(rs.getString("total_ord"));
                o.setTotal_iva_ord(rs.getString("total_iva_ord"));
                o.setId_proveedor(rs.getString("id_proveedor"));
                o.setNombre_pro(rs.getString("nombre_pro"));
                oc.add(o);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOordencompra.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return oc;
    }

    public ArrayList<ordendecompra> ListarOrdenesRicibidas() {
        Connection con = conexion.getConnection();
        ArrayList<ordendecompra> oc = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ordendecompra oc INNER JOIN proveedores p ON p.id_proveedor = oc.proveedores_id_proveedor WHERE oc.recibido_ord = 1 AND oc.close_ord = 0 and aprobado_ord = 1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ordendecompra o = new ordendecompra();
                o.setId_orden_compra(rs.getString("id_orden_compra"));
                o.setFecha_ord(f.ParseDI(rs.getString("fecha_ord")));
                o.setAprobado_ord(rs.getString("aprobado_ord"));
                o.setRecibido_ord(rs.getString("recibido_ord"));
                o.setClose_ord(rs.getString("close_ord"));
                o.setTotal_ord(rs.getString("total_ord"));
                o.setTotal_iva_ord(rs.getString("total_iva_ord"));
                o.setId_proveedor(rs.getString("id_proveedor"));
                o.setNombre_pro(rs.getString("nombre_pro"));
                oc.add(o);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOordencompra.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return oc;
    }

    public ordendecompra BuscarOrdenCompra(String id_orden_compra) {
        Connection con = conexion.getConnection();
        ordendecompra o = new ordendecompra();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ordendecompra oc INNER JOIN proveedores p ON p.id_proveedor = oc.proveedores_id_proveedor WHERE id_orden_compra = ? AND close_ord=0");
            ps.setInt(1, Integer.valueOf(id_orden_compra));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                o.setId_orden_compra(rs.getString("id_orden_compra"));
                o.setFecha_ord(f.ParseDI(rs.getString("fecha_ord")));
                o.setAprobado_ord(rs.getString("aprobado_ord"));
                o.setRecibido_ord(rs.getString("recibido_ord"));
                o.setClose_ord(rs.getString("close_ord"));
                o.setTotal_ord(rs.getString("total_ord"));
                o.setTotal_iva_ord(rs.getString("total_iva_ord"));
                o.setId_proveedor(rs.getString("id_proveedor"));
                o.setNombre_pro(rs.getString("nombre_pro"));
                o.setNdocumento_pro(rs.getString("ndocumento_pro"));
                o.setDv_pro(rs.getString("dv_pro"));
                o.setComentario_ord(rs.getString("comentario_ord"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOordencompra.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return o;
    }

    public ArrayList<ordendecompra> BuscarDetalleOrden(String id_orden_compra) {
        Connection con = conexion.getConnection();
        ArrayList<ordendecompra> o = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM detalle_orden WHERE id_orden_compra = ?");
            ps.setString(1, id_orden_compra);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ordendecompra or = new ordendecompra();
                or.setId_orden_compra(rs.getString("id_orden_compra"));
                or.setId_articulo(rs.getString("id_articulo"));
                or.setCodigo_ord(rs.getString("codigo_ord"));
                or.setDescripcion_ord(rs.getString("descripcion_ord"));
                or.setCantidad_ord(rs.getString("cantidad_ord"));
                or.setPrecio_ord(rs.getString("precio_ord"));
                or.setIva_ord(rs.getString("iva_ord"));
                or.setSubtotal_ord(rs.getString("subtotal_ord"));
                o.add(or);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOordencompra.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return o;
    }

    public int AgregarOrdenCompra(ordendecompra o, ArrayList<ordendecompra> detalle) {
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        int r = 0;
        int id = 0;
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement("INSERT INTO ordendecompra (fecha_ord, total_ord, total_iva_ord, comentario_ord, proveedores_id_proveedor) VALUES (?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, f.ParseDate(o.getFecha_ord()));
            ps.setInt(2, Integer.valueOf(o.getTotal_ord()));
            ps.setInt(3, Integer.valueOf(o.getTotal_iva_ord()));
            ps.setString(4, o.getComentario_ord());
            ps.setInt(5, Integer.valueOf(o.getId_proveedor()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = (int) rs.getLong(1);
                for (ordendecompra d : detalle) {
                    ps = con.prepareStatement("INSERT INTO detalle_orden (id_orden_compra, id_articulo, codigo_ord, descripcion_ord, cantidad_ord, precio_ord, iva_ord, subtotal_ord) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, id);
                    ps.setInt(2, Integer.valueOf(d.getId_articulo()));
                    ps.setString(3, d.getCodigo_ord());
                    ps.setString(4, d.getDescripcion_ord());
                    ps.setInt(5, Integer.valueOf(d.getCantidad_ord()));
                    ps.setInt(6, Integer.valueOf(d.getPrecio_ord()));
                    ps.setInt(7, Integer.valueOf(d.getIva_ord()));
                    ps.setInt(8, Integer.valueOf(d.getSubtotal_ord()));
                    r = ps.executeUpdate();
                }
            }
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DAOordencompra.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(DAOordencompra.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int AgregarDetalleCompra(ordendecompra d) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO detalle_orden (id_orden_compra, id_articulo, codigo_ord, descripcion_ord, cantidad_ord, precio_ord, iva_ord, subtotal_ord) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, d.getId_orden_compra());
            ps.setString(2, d.getId_articulo());
            ps.setString(3, d.getCodigo_ord());
            ps.setString(4, d.getDescripcion_ord());
            ps.setString(5, d.getCantidad_ord());
            ps.setString(6, d.getPrecio_ord());
            ps.setString(7, d.getIva_ord());
            ps.setString(8, d.getSubtotal_ord());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOordencompra.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int AprobarOrden(String id, String x) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE ordendecompra SET aprobado_ord = ? WHERE id_orden_compra = ?");
            ps.setString(1, x);
            ps.setString(2, id);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOordencompra.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int RecibirOrden(String id) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE ordendecompra SET recibido_ord = ? WHERE id_orden_compra = ?");
            ps.setString(1, "1");
            ps.setString(2, id);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOordencompra.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public void DeleteDetalleOrden(String id_orden_compra) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM detalle_orden WHERE id_orden_compra = ?");
            ps.setString(1, id_orden_compra);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOordencompra.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }

    }

    public int ActualizarOrden(ordendecompra o, ArrayList<ordendecompra> detalle) {
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        int r = 0;
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement("UPDATE ordendecompra SET fecha_ord = ?, total_ord = ?, total_iva_ord = ?, comentario_ord = ?, proveedores_id_proveedor = ? WHERE id_orden_compra = ?");
            ps.setString(1, f.ParseDate(o.getFecha_ord()));
            ps.setInt(2, Integer.valueOf(o.getTotal_ord()));
            ps.setInt(3, Integer.valueOf(o.getTotal_iva_ord()));
            ps.setString(4, o.getComentario_ord());
            ps.setInt(5, Integer.valueOf(o.getId_proveedor()));
            ps.setInt(6, Integer.valueOf(o.getId_orden_compra()));
            r = ps.executeUpdate();
            if (r != 0) {
                ps = con.prepareStatement("DELETE FROM detalle_orden WHERE id_orden_compra = ?");
                ps.setInt(1, Integer.valueOf(o.getId_orden_compra()));
                ps.execute();
                for (ordendecompra d : detalle) {
                    ps = con.prepareStatement("INSERT INTO detalle_orden (id_orden_compra, id_articulo, codigo_ord, descripcion_ord, cantidad_ord, precio_ord, iva_ord, subtotal_ord) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, Integer.valueOf(o.getId_orden_compra()));
                    ps.setInt(2, Integer.valueOf(d.getId_articulo()));
                    ps.setString(3, d.getCodigo_ord());
                    ps.setString(4, d.getDescripcion_ord());
                    ps.setInt(5, Integer.valueOf(d.getCantidad_ord()));
                    ps.setInt(6, Integer.valueOf(d.getPrecio_ord()));
                    ps.setInt(7, Integer.valueOf(d.getIva_ord()));
                    ps.setInt(8, Integer.valueOf(d.getSubtotal_ord()));
                    r = ps.executeUpdate();
                }
            }
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DAOordencompra.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(DAOordencompra.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.getConnection();
        }
        return r;
    }

    public int AnularOrden(String id, String a) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE ordendecompra SET close_ord = ? WHERE id_orden_compra = ?");
            ps.setString(1, a);
            ps.setString(2, id);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOordencompra.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
