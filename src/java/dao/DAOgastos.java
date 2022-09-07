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
import modelo.gastos;

/**
 *
 * @author 2HDEV
 */
public class DAOgastos {

    funciones funcion = new funciones();

    public ArrayList<gastos> ListarGastos(int id_usuario) {
        
        Connection con = conexion.getConnection();
        ArrayList<gastos> gastos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT gastos.*, proveedores.nombre_pro FROM gastos, proveedores WHERE gastos.id_proveedor=proveedores.id_proveedor AND anulado!=1 AND gastos.id_sucursal=BuscarSucursalPorUsuario(?)");
            ps.setInt(1, id_usuario);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                gastos gasto = new gastos();
                gasto.setId_gasto(rs.getInt("id_gasto"));
                gasto.setFecha(rs.getDate("fecha"));
                gasto.setTimbrado(rs.getInt("timbrado"));
                gasto.setNro_factura(rs.getString("nro_factura"));
                gasto.setTipo_gasto(rs.getString("tipo_gasto"));
                gasto.setTotal_10(rs.getInt("total_10"));
                gasto.setTotal_5(rs.getInt("total_5"));
                gasto.setTotal_exenta(rs.getInt("total_exenta"));
                gasto.setTotal_gasto(rs.getInt("total_gasto"));
                gasto.setId_proveedor(rs.getString("id_proveedor"));
                gasto.setNombre_pro(rs.getString("nombre_pro"));
                gastos.add(gasto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOgastos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return gastos;
    }

    public gastos BuscarGastos(int id) {
        Connection con = conexion.getConnection();
        gastos gasto = new gastos();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM gastos, proveedores WHERE id_gasto = ? AND proveedores.id_proveedor=gastos.id_proveedor AND anulado!=1");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                gasto.setId_gasto(rs.getInt("id_gasto"));
                gasto.setFecha(rs.getDate("fecha"));
                gasto.setTimbrado(rs.getInt("timbrado"));
                gasto.setNro_factura(rs.getString("nro_factura"));
                gasto.setTipo_gasto(rs.getString("tipo_gasto"));
                gasto.setTotal_10(rs.getInt("total_10"));
                gasto.setTotal_5(rs.getInt("total_5"));
                gasto.setTotal_exenta(rs.getInt("total_exenta"));
                gasto.setTotal_gasto(rs.getInt("total_gasto"));
                gasto.setId_proveedor(rs.getString("id_proveedor"));
                gasto.setNombre_pro(rs.getString("nombre_pro"));
                gasto.setNdocumento_pro(rs.getString("ndocumento_pro") + "-" + rs.getString("dv_pro"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOgastos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return gasto;
    }

    public int AgregarGastos(gastos gasto) {
        Connection con = conexion.getConnection();
        Double iva_10 = new Double(gasto.getTotal_10());
        iva_10 = iva_10 / 11;
        Double iva_5 = new Double(gasto.getTotal_5());
        iva_5 = iva_5 / 21;
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SET @usuario = ?");
            ps.setString(1, gasto.getId_usuario());
            ps.execute();
            ps = con.prepareStatement("SELECT * FROM gastos WHERE nro_factura=? AND timbrado=?");
            ps.setString(1, gasto.getNro_factura());
            ps.setInt(2, gasto.getTimbrado());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                ps = con.prepareStatement("INSERT INTO gastos(nro_factura, timbrado, fecha, tipo_gasto, total_10, total_5, total_exenta, iva_10, iva_5, total_gasto, id_proveedor, id_sucursal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, gasto.getNro_factura());
                ps.setInt(2, gasto.getTimbrado());
                ps.setDate(3, gasto.getFecha());
                ps.setString(4, gasto.getTipo_gasto());
                ps.setInt(5, gasto.getTotal_10());
                ps.setInt(6, gasto.getTotal_5());
                ps.setInt(7, gasto.getTotal_exenta());
                ps.setInt(8, (int) (Math.round(iva_10)));
                ps.setInt(9, (int) (Math.round(iva_5)));
                ps.setInt(10, gasto.getTotal_gasto());
                ps.setString(11, gasto.getId_proveedor());
                ps.setString(12, gasto.getId_usuario());
                r = ps.executeUpdate();
            } else {
                r = 3;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOgastos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarGastos(gastos gasto) {
        Connection con = conexion.getConnection();
        int r = 0;
        Double iva_10 = new Double(gasto.getTotal_10());
        iva_10 = iva_10 / 11;
        Double iva_5 = new Double(gasto.getTotal_5());
        iva_5 = iva_5 / 21;
        try {
            PreparedStatement ps = con.prepareStatement("SET @usuario = ?");
            ps.setString(1, gasto.getId_usuario());
            ps.execute();
            ps = con.prepareStatement("UPDATE gastos SET nro_factura=?, timbrado=?, fecha=?, tipo_gasto=?, "
                    + "total_10=?, total_5=?, total_exenta=?, iva_10=?, iva_5=?, total_gasto=?, id_proveedor=? WHERE id_gasto=?");
            ps.setString(1, gasto.getNro_factura());
            ps.setInt(2, gasto.getTimbrado());
            ps.setDate(3, gasto.getFecha());
            ps.setString(4, gasto.getTipo_gasto());
            ps.setInt(5, gasto.getTotal_10());
            ps.setInt(6, gasto.getTotal_5());
            ps.setInt(7, gasto.getTotal_exenta());
            ps.setInt(8, (int) (Math.round(iva_10)));
            ps.setInt(9, (int) (Math.round(iva_5)));
            ps.setInt(10, gasto.getTotal_gasto());
            ps.setString(11, gasto.getId_proveedor());
            ps.setInt(12, gasto.getId_gasto());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOgastos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int AnularGasto(int id, int id_usuario) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE gastos SET anulado =!anulado WHERE id_gasto=?");
            ps.setInt(1, id);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOgastos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
