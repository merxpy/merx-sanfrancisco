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
import modelo.compras;

/**
 *
 * @author akio
 */
public class DAOcompras {

    funciones f = new funciones();
    DAOarticulos dao = new DAOarticulos();
    DAOcaja caja = new DAOcaja();
    DAOauditoria aud = new DAOauditoria();

    public ArrayList<compras> ListarCompras(String tipo_dato) {
        Connection con = conexion.getConnection();
        ArrayList<compras> com = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM compras c INNER JOIN proveedores p ON c.proveedores_id_proveedor = p.id_proveedor WHERE close_compra = 0 and tipo_dato=?");
            ps.setString(1, tipo_dato);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                compras c = new compras();
                c.setId_compra(rs.getInt("id_compra"));
                c.setFactura_compra(rs.getString("factura_compra"));
                c.setFecha_compra(f.ParseDI(rs.getString("fecha_compra")));
                c.setTipo_compra(rs.getString("tipo_compra"));
                c.setTotal_compra(rs.getString("total_compra"));
                c.setClose_compra(rs.getString("close_compra"));
                c.setNombre_pro(rs.getString("nombre_pro"));
                com.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcompras.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return com;
    }

    public compras BuscarCompras(int id) {
        Connection con = conexion.getConnection();
        compras c = new compras();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM compras c, proveedores p WHERE c.proveedores_id_proveedor = p.id_proveedor AND c.id_compra = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c.setId_compra(rs.getInt("id_compra"));
                c.setFactura_compra(rs.getString("factura_compra"));
                c.setTimbrado_compra(rs.getInt("timbrado_compra"));
                c.setFecha_compra(f.ParseDI(rs.getString("fecha_compra")));
                c.setTipo_compra(rs.getString("tipo_compra"));
                c.setTotal_compra(rs.getString("total_compra"));
                c.setTotal_descuento(rs.getInt("total_descuento"));
                c.setClose_compra(rs.getString("close_compra"));
                c.setNombre_pro(rs.getString("nombre_pro"));
                c.setNdocumento_pro(rs.getString("ndocumento_pro") + "-" + rs.getString("dv_pro"));
                c.setId_proveedor(rs.getString("id_proveedor"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcompras.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return c;
    }

    public ArrayList<compras> BuscarDetalles(String id) {
        Connection con = conexion.getConnection();
        ArrayList<compras> co = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM detalle_compra WHERE compras_id_compra = ?");
            ps.setInt(1, Integer.valueOf(id));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                compras c = new compras();
                c.setId_articulo(rs.getString("articulos_id_articulo"));
                c.setCodigo(rs.getString("codigo"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setCantidad(rs.getString("cantidad"));
                c.setPrecio(rs.getString("precio"));
                c.setDescuento(rs.getString("descuento"));
                c.setIva(rs.getString("iva"));
                c.setSubtotal(rs.getString("subtotal"));
                co.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcompras.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return co;
    }

    public int AgregarCompras(compras c, ArrayList<compras> detalle) {
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        int r = 0;
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement("SET @usuario = ?");
            ps.setString(1, c.getId_usuario());
            ps.execute();
            ps = con.prepareStatement("select * from compras where factura_compra=? AND timbrado_compra=?");
            ps.setString(1, c.getFactura_compra());
            ps.setInt(2, c.getTimbrado_compra());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                ps = con.prepareStatement("INSERT INTO compras (factura_compra, timbrado_compra, fecha_compra, tipo_compra, total_compra, total_descuento, total_iva_compra, tipo_dato, proveedores_id_proveedor, id_sucursal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, BuscarSucursalPorUsuario(?))", PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, c.getFactura_compra());
                ps.setInt(2, c.getTimbrado_compra());
                ps.setString(3, f.ParseDate(c.getFecha_compra()));
                ps.setString(4, c.getTipo_compra());
                ps.setString(5, c.getTotal_compra());
                ps.setInt(6, c.getTotal_descuento());
                ps.setString(7, c.getTotal_iva_compra());
                ps.setString(8, c.getTipo_dato());
                ps.setString(9, c.getId_proveedor());
                ps.setString(10, c.getId_usuario());
                ps.executeUpdate();
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    c.setId_compra((int) rs.getLong(1));
                    for (compras d : detalle) {
                        ps = con.prepareStatement("INSERT INTO detalle_compra(articulos_id_articulo, compras_id_compra, codigo, descripcion, cantidad, precio, descuento, iva, subtotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        ps.setInt(1, Integer.valueOf(d.getId_articulo()));
                        ps.setInt(2, c.getId_compra());
                        ps.setString(3, d.getCodigo());
                        ps.setString(4, d.getDescripcion());
                        ps.setInt(5, Integer.valueOf(d.getCantidad()));
                        ps.setString(6, d.getPrecio());
                        ps.setString(7, d.getDescuento());
                        ps.setString(8, d.getIva());
                        ps.setString(9, d.getSubtotal());
                        r = ps.executeUpdate();
                    }
                }

                con.commit();
            } else {
                r = 3;
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DAOcompras.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(DAOcompras.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            aud.afterInsertCompras(c, c.getId_usuario());
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarCompra(compras c, ArrayList<compras> detalle) {
        Connection con = conexion.getConnection();
        compras o = BuscarCompras(c.getId_compra());
        PreparedStatement ps;
        int r = 0;
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement("SET @usuario = ?");
            ps.setString(1, c.getId_usuario());
            ps.execute();
            ps = con.prepareStatement("UPDATE compras SET factura_compra=?, timbrado_compra=?, fecha_compra=?, tipo_compra=?, total_compra=?, total_descuento=?, total_iva_compra=?, tipo_dato = ?, proveedores_id_proveedor = ? WHERE id_compra=?");
            ps.setString(1, c.getFactura_compra());
            ps.setInt(2, c.getTimbrado_compra());
            ps.setString(3, f.ParseDate(c.getFecha_compra()));
            ps.setString(4, c.getTipo_compra());
            ps.setString(5, c.getTotal_compra());
            ps.setInt(6, c.getTotal_descuento());
            ps.setString(7, c.getTotal_iva_compra());
            ps.setString(8, c.getTipo_dato());
            ps.setString(9, c.getId_proveedor());
            ps.setInt(10, c.getId_compra());
            ps.executeUpdate();
            if (EliminarDetalleCompra(c) != 0) {
                for (compras d : detalle) {
                    ps = con.prepareStatement("INSERT INTO detalle_compra(articulos_id_articulo, compras_id_compra, codigo, descripcion, cantidad, precio, descuento, iva, subtotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, Integer.valueOf(d.getId_articulo()));
                    ps.setInt(2, c.getId_compra());
                    ps.setString(3, d.getCodigo());
                    ps.setString(4, d.getDescripcion());
                    ps.setInt(5, Integer.valueOf(d.getCantidad()));
                    ps.setString(6, d.getPrecio());
                    ps.setString(7, d.getDescuento());
                    ps.setString(8, d.getIva());
                    ps.setString(9, d.getSubtotal());
                    r = ps.executeUpdate();
                }
            }
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DAOcompras.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(DAOcompras.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            aud.afterUpdateCompras(c, o, c.getId_usuario());
            conexion.Close(con);
        }
        return r;
    }

    public int EliminarDetalleCompra(compras c) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            con.setAutoCommit(false);
            PreparedStatement pst = con.prepareStatement("SELECT * FROM detalle_compra WHERE compras_id_compra = ?");
            pst.setInt(1, c.getId_compra());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                PreparedStatement prs = con.prepareStatement("SELECT * FROM stock WHERE articulos_id_articulo = ?");
                prs.setString(1, rs.getString("articulos_id_articulo"));
                ResultSet res = prs.executeQuery();
                if (res.next()) {
                    int cant = 0;
                    cant = res.getInt("cantidad_stock") - rs.getInt("cantidad");
                    PreparedStatement ps = con.prepareStatement("UPDATE stock SET cantidad_stock = ? WHERE articulos_id_articulo = ?");
                    ps.setInt(1, cant);
                    ps.setString(2, rs.getString("articulos_id_articulo"));
                    ps.executeUpdate();
                }

            }

            PreparedStatement ps = con.prepareStatement("DELETE FROM detalle_compra WHERE compras_id_compra=?");
            ps.setInt(1, c.getId_compra());
            r = ps.executeUpdate();

            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DAOcompras.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(DAOcompras.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int CerrarCompra(int id, String a, String id_usu) {
        Connection con = conexion.getConnection();
        int r = 0;
        compras c = BuscarCompras(id);
        try {
            PreparedStatement ps = con.prepareStatement("SET @usuario = ?");
            ps.setString(1, id_usu);
            ps.execute();
            ps = con.prepareStatement("UPDATE compras SET close_compra = ? WHERE id_compra = ?");
            ps.setString(1, a);
            ps.setInt(2, id);
            r = ps.executeUpdate();

            if (a.equals("1")) {
                aud.afterAnularCompras(c, id_usu, "anulo");
            } else {
                aud.afterAnularCompras(c, id_usu, "restablecio");
            }
            PreparedStatement pst = con.prepareStatement("SELECT * FROM detalle_compra WHERE compras_id_compra = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int cant = 0;
                PreparedStatement prs = con.prepareStatement("SELECT cantidad_stock FROM stock WHERE articulos_id_articulo = ?");
                prs.setString(1, rs.getString("articulos_id_articulo"));
                ResultSet res = prs.executeQuery();
                if (res.next()) {
                    if (a.equals("1")) {
                        cant = res.getInt("cantidad_stock") - rs.getInt("cantidad");
                    } else if (a.equals("0")) {
                        cant = res.getInt("cantidad_stock") + rs.getInt("cantidad");
                    }
                    PreparedStatement p = con.prepareStatement("UPDATE stock SET cantidad_stock = ? WHERE articulos_id_articulo = ?");
                    p.setInt(1, cant);
                    p.setString(2, rs.getString("articulos_id_articulo"));
                    r = p.executeUpdate();
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcompras.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public ArrayList<compras> ListarComprasInhabilitadas(String tipo_dato) {
        Connection con = conexion.getConnection();
        ArrayList<compras> com = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM compras c INNER JOIN proveedores p ON c.proveedores_id_proveedor = p.id_proveedor WHERE close_compra = 1 AND tipo_dato=?");
            ps.setString(1, tipo_dato);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                compras c = new compras();
                c.setId_compra(rs.getInt("id_compra"));
                c.setFactura_compra(rs.getString("factura_compra"));
                c.setFecha_compra(rs.getString("fecha_compra"));
                c.setTipo_compra(rs.getString("tipo_compra"));
                c.setTotal_compra(rs.getString("total_compra"));
                c.setClose_compra(rs.getString("close_compra"));
                c.setNombre_pro(rs.getString("nombre_pro"));
                com.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcompras.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return com;
    }

    public ArrayList<compras> GraficoCompras() {
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        ArrayList<compras> compra = new ArrayList<>();
        try {
            for (int i = 6; i >= 0; i--) {
                ps = con.prepareStatement("SELECT CURDATE()-INTERVAL ? DAY AS fecha_compra");
                ps.setInt(1, i);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    compras c = new compras();
                    c.setFecha_compra(rs.getString("fecha_compra"));
                    ps = con.prepareStatement("SELECT SUM(total_compra) AS total_compra FROM comprasgraph WHERE DATE(fecha_compra) = ? GROUP BY fecha_compra");
                    ps.setString(1, c.getFecha_compra());
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        c.setTotal_compra(rs.getString("total_compra"));
                    } else {
                        c.setTotal_compra("0");
                    }
                    c.setFecha_compra(f.ParseDI(c.getFecha_compra()));
                    compra.add(c);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return compra;
    }

    public ArrayList<compras> GraficoGastos() {
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        ArrayList<compras> compra = new ArrayList<>();
        try {
            for (int i = 6; i >= 0; i--) {
                ps = con.prepareStatement("SELECT CURDATE()-INTERVAL ? DAY AS fecha_compra");
                ps.setInt(1, i);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    compras c = new compras();
                    c.setFecha_compra(rs.getString("fecha_compra"));
                    ps = con.prepareStatement("SELECT SUM(total_compra) AS total_compra FROM gastosgraph WHERE DATE(fecha_compra) = ? GROUP BY fecha_compra");
                    ps.setString(1, c.getFecha_compra());
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        c.setTotal_compra(rs.getString("total_compra"));
                    } else {
                        c.setTotal_compra("0");
                    }
                    c.setFecha_compra(f.ParseDI(c.getFecha_compra()));
                    compra.add(c);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return compra;
    }

    public int ObtenerValorUltimaCompra() {
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        int r = 0;
        try {
            ps = con.prepareStatement("SELECT total_compra FROM compras WHERE close_compra!=1 ORDER BY id_compra desc LIMIT 1 ");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                r = rs.getInt("total_compra");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
