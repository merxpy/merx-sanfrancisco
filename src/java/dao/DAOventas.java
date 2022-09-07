/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexion.conexion;
import extras.funciones;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.ventas;

/**
 *
 * @author akio
 */
public class DAOventas {

    DAOcaja caja = new DAOcaja();
    funciones f = new funciones();

    public ArrayList<ventas> ListarVentas() {
        Connection con = conexion.getConnection();
        ArrayList<ventas> ven = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ventas v INNER JOIN clientes c ON v.clientes_id_cliente = c.id_cliente WHERE close_venta = 0");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ventas v = new ventas();
                v.setId_venta(rs.getString("id_venta"));
                v.setNfactura_venta(rs.getString("nfactura_venta"));
                v.setFecha_venta(f.ParseDI(rs.getString("fecha_venta")));
                v.setHora_venta(rs.getString("hora_venta"));
                v.setTipo_venta(rs.getString("tipo_venta"));
                v.setTotal_venta(rs.getString("total_venta"));
                v.setTotal_iva_venta(rs.getString("total_iva_venta"));
                v.setNombre_cli(rs.getString("nombre_cli"));
                v.setRazon_social(rs.getString("razon_social"));
                ven.add(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return ven;
    }

    public ventas BuscarVentas(String id) {
        Connection con = conexion.getConnection();
        ventas v = new ventas();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ventas v INNER JOIN clientes c ON v.clientes_id_cliente = c.id_cliente WHERE close_venta = 0 AND id_venta = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                v.setId_venta(rs.getString("id_venta"));
                v.setNfactura_venta(rs.getString("Nfactura_venta"));
                v.setFecha_venta(rs.getString("fecha_venta"));
                v.setHora_venta(rs.getString("hora_venta"));
                v.setTipo_venta(rs.getString("tipo_venta"));
                v.setTotal_venta(rs.getString("total_venta"));
                v.setTotal_iva_venta(rs.getString("total_iva_venta"));
                v.setNombre_cli(rs.getString("nombre_cli"));
                v.setNdocumento_cli(rs.getString("ndocumento_cli"));
                v.setDv_cli(rs.getString("dv_cli"));
                v.setImpreso(rs.getInt("impreso"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return v;
    }

    public int AgregarVentas(ventas v) {
        Connection con = conexion.getConnection();
        int r = 0;
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("SET @usuario = ?");
            ps.setInt(1, Integer.valueOf(v.getId_usuario()));
            ps.execute();
            CallableStatement cs = con.prepareCall("{call ventas(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            cs.setString(1, v.getNfactura_venta());
            cs.setString(2, v.getTipo_venta());
            cs.setString(3, v.getTotal_venta());
            cs.setInt(4, v.getTotal_descuento());
            cs.setString(5, v.getTotal_iva_venta());
            cs.setString(6, v.getId_cliente());
            cs.setString(7, v.getSucursal_id_sucursal());
            cs.setInt(8, 1);
            cs.setString(9, v.getCupon_venta());
            cs.setInt(10, v.getTotaliva10());
            cs.setInt(11, v.getTotaliva5());
            cs.setInt(12, Integer.valueOf(v.getId_usuario()));
            cs.setString(13, !v.getId_pedido().equals('0') ? v.getId_pedido() : null);
            cs.registerOutParameter(14, java.sql.Types.INTEGER);
            cs.execute();
            r = cs.getInt(14);
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int AgregarDetalleVenta(ventas v) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("INSERT INTO detalle_venta (articulos_id_articulo, ventas_id_venta, codigo, descripcion, cantidad, precio, descuento, iva, subtotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, v.getId_articulo());
            ps.setString(2, v.getId_venta());
            ps.setString(3, v.getCodigo());
            ps.setString(4, v.getDescripcion());
            ps.setString(5, v.getCantidad());
            ps.setString(6, v.getPrecio());
            ps.setString(7, v.getDescuento());
            ps.setString(8, v.getIva());
            ps.setString(9, v.getSubtotal());
            r = ps.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        if (r != 0) {
            r = Integer.valueOf(v.getId_venta());
        }
        return r;
    }

    public int ActualizarVentas(ventas v) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE ventas SET fecha_venta = ?, hora_venta = ?, tipo_venta = ?, total_venta = ?, total_iva_venta = ?, clientes_id_cliente = ?");
            ps.setString(1, v.getFecha_venta());
            ps.setString(2, v.getHora_venta());
            ps.setString(3, v.getTipo_venta());
            ps.setString(4, v.getTotal_venta());
            ps.setString(5, v.getTotal_iva_venta());
            ps.setString(6, v.getId_cliente());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarMontosVentas(ventas v) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE ventas SET total_venta = ?, total_iva_venta = ?, totaliva10=?, totaliva5=?, total_descuento=? WHERE id_venta=?");
            ps.setString(1, v.getTotal_venta());
            ps.setString(2, v.getTotal_iva_venta());
            ps.setInt(3, v.getTotaliva10());
            ps.setInt(4, v.getTotaliva5());
            ps.setInt(5, v.getTotal_descuento());
            ps.setString(6, v.getId_venta());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarDetalleVenta(ventas v) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE detalle_venta SET articulos_id_articulo = ?, cantidad = ?, precio = ?, iva = ?, subtotal = ?, subtota_iva = ? WHERE id_venta = ?");
            ps.setString(1, v.getId_articulo());
            ps.setString(2, v.getCantidad());
            ps.setString(3, v.getPrecio());
            ps.setString(4, v.getIva());
            ps.setString(5, v.getSubtotal());
            ps.setString(6, v.getId_venta());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int AnularVenta(String id, String a) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("SELECT id_pedido FROM ventas WHERE id_venta=?");
            ps.setInt(1, Integer.valueOf(id));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps = con.prepareStatement("UPDATE ventas SET close_venta = ? WHERE id_venta = ?");
                ps.setString(1, a);
                ps.setInt(2, Integer.valueOf(id));
                ps.executeUpdate();
                ps = con.prepareStatement("UPDATE pedidos SET facturado_ped = 0 WHERE id_pedido=?");
                ps.setInt(1, rs.getInt("id_pedido"));
                r = ps.executeUpdate();
                ps = con.prepareStatement("SELECT detalle_venta.* FROM ventas, detalle_venta WHERE ventas.id_venta=detalle_venta.ventas_id_venta AND ventas.id_pedido=?");
                ps.setInt(1, rs.getInt("id_pedido"));
            } else {
                ps = con.prepareStatement("UPDATE ventas SET close_venta = ? WHERE id_venta = ?");
                ps.setString(1, a);
                ps.setString(2, id);
                r = ps.executeUpdate();
                ps = con.prepareStatement("SELECT * FROM detalle_venta WHERE ventas_id_venta = ?");
                ps.setString(1, id);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                int cant = 0;
                ps = con.prepareStatement("SELECT cantidad_stock FROM stock WHERE articulos_id_articulo = ?");
                ps.setString(1, rs.getString("articulos_id_articulo"));
                ResultSet res = ps.executeQuery();
                if (res.next()) {
                    if (a.equals("1")) {
                        cant = res.getInt("cantidad_stock") + rs.getInt("cantidad");
                    } else if (a.equals("0")) {
                        cant = res.getInt("cantidad_stock") - rs.getInt("cantidad");
                    }
                    ps = con.prepareStatement("UPDATE stock SET cantidad_stock = ? WHERE articulos_id_articulo = ?");
                    ps.setInt(1, cant);
                    ps.setString(2, rs.getString("articulos_id_articulo"));
                    r = ps.executeUpdate();
                }

            }
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public ArrayList<ventas> ListarVentasInhabilitadas() {
        Connection con = conexion.getConnection();
        ArrayList<ventas> ven = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ventas v INNER JOIN clientes c ON v.clientes_id_cliente = c.id_cliente WHERE close_venta = 1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ventas v = new ventas();
                v.setId_venta(rs.getString("id_venta"));
                v.setNfactura_venta(rs.getString("nfactura_venta"));
                v.setFecha_venta(f.ParseDI(rs.getString("fecha_venta")));
                v.setHora_venta(rs.getString("hora_venta"));
                v.setTipo_venta(rs.getString("tipo_venta"));
                v.setTotal_venta(rs.getString("total_venta"));
                v.setTotal_iva_venta(rs.getString("total_iva_venta"));
                v.setNombre_cli(rs.getString("nombre_cli"));
                ven.add(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return ven;
    }

    public int ObtenerValorUltimaVenta() {
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        int r = 0;
        try {
            ps = con.prepareStatement("SELECT total_venta FROM ventas WHERE close_venta!=1 ORDER BY id_venta desc LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                r = rs.getInt("total_venta");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public String obtenerSucursal(String id_usu) {
        Connection con = conexion.getConnection();
        String suc = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT sucursal_id_sucursal FROM usuarios WHERE id_usuario = ?");
            ps.setString(1, id_usu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                suc = rs.getString("sucursal_id_sucursal");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return suc;
    }

    public int UltimoNroFactura() {
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        int nrofactura = 1;
        try {
            ps = con.prepareStatement("SELECT MAX(nfactura_venta) as nfactura_venta FROM ventas WHERE ventas.close_venta=0");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nrofactura = rs.getInt("nfactura_venta") + 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return nrofactura;
    }

    public ArrayList<ventas> GraficoVentas() {
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        ArrayList<ventas> venta = new ArrayList<>();
        try {
            for (int i = 6; i >= 0; i--) {
                ps = con.prepareStatement("SELECT CURDATE()-INTERVAL ? DAY AS fecha_venta");
                ps.setInt(1, i);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    ventas v = new ventas();
                    v.setFecha_venta(rs.getString("fecha_venta"));
                    ps = con.prepareStatement("SELECT SUM(total_venta) AS total_venta FROM ventasgraph WHERE fecha_venta = ? GROUP BY fecha_venta");
                    ps.setString(1, v.getFecha_venta());
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        v.setTotal_venta(rs.getString("total_venta"));
                    } else {
                        v.setTotal_venta("0");
                    }
                    v.setFecha_venta(f.ParseDI(v.getFecha_venta()));
                    venta.add(v);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return venta;
    }

    public void ActualizarEstadoImpresion(int id) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE ventas SET impreso = 1 WHERE id_venta = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }
}
