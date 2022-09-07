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
import modelo.declaracion;
import modelo.ventas;

/**
 *
 * @author 2HDEV
 */
public class DAOdeclaraciones {
    
    funciones f = new funciones();
    
    public ArrayList<declaracion> Periodos(String tabla, String campo) {
        Connection con = conexion.getConnection();
        ArrayList<declaracion> declaracion = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT DISTINCT YEAR(fecha_" + campo + ") AS year FROM " + tabla + " WHERE close_" + campo + "=0  GROUP BY YEAR(fecha_" + campo + ")");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                declaracion d = new declaracion();
                d.setAnho(rs.getString("year"));
                declaracion.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOdeclaraciones.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return declaracion;
    }
    
    public ArrayList<declaracion> Meses(String tabla, String campo, String anho) {
        Connection con = conexion.getConnection();
        ArrayList<declaracion> declaracion = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SET lc_time_names = 'es_ES'");
            ps.execute();
            ps = con.prepareStatement("SELECT DISTINCT MONTHNAME(fecha_" + campo + ") AS monthname, MONTH(fecha_" + campo + ") AS month FROM " + tabla + " WHERE YEAR(fecha_" + campo + ")=? AND close_" + campo + "=0 GROUP BY MONTH(fecha_" + campo + ")");
            ps.setString(1, anho);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                declaracion d = new declaracion();
                d.setAnho(rs.getString("month"));
                d.setMes(rs.getString("monthname"));
                declaracion.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOdeclaraciones.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return declaracion;
    }
    
    public ArrayList<compras> ListarComprasHechauka(int mes, int year) {
        Connection con = conexion.getConnection();
        ArrayList<compras> com = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM comprashechauka WHERE MONTH(fecha_compra)=? AND YEAR(fecha_compra)=?");
            ps.setInt(1, mes);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                compras c = new compras();
                c.setFactura_compra(rs.getString("factura_compra"));
                c.setFecha_compra(f.ParseDI(rs.getString("fecha_compra")));
                c.setTimbrado_compra(rs.getInt("timbrado_compra"));
                c.setTipo_compra(rs.getString("tipo_compra"));
                c.setTotal_compra(rs.getString("total_compra"));
                c.setTotal_iva_compra(rs.getString("total_iva_compra"));
                c.setNombre_pro(rs.getString("nombre_pro"));
                c.setNdocumento_pro(rs.getString("ndocumento_pro"));
                c.setDv_pro(rs.getString("dv_pro"));
                c.setGrav10_compra(rs.getInt("gravada_10"));
                c.setGrav5_compra(rs.getInt("gravada_5"));
                c.setExenta_compra(rs.getInt("total_E"));
                c.setIva10_compra(rs.getInt("iva_10"));
                c.setIva5_compra(rs.getInt("iva_5"));
                com.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcompras.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return com;
    }
    
    public int TotalGravCompras(int mes, int year) {
        Connection con = conexion.getConnection();
        int compragrav = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT SUM(gravada_10)+SUM(gravada_5)+SUM(total_E) AS total_compra FROM `comprashechauka` WHERE YEAR(fecha_compra)=? AND MONTH(fecha_compra) = ?");
            ps.setInt(1, year);
            ps.setInt(2, mes);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                compragrav = rs.getInt("total_compra");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcompras.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return compragrav;
    }
    
    public ArrayList<ventas> ListarVentasHechauka(int mes, int year) {
        Connection con = conexion.getConnection();
        ArrayList<ventas> ven = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ventashechauka WHERE MONTH(fecha_venta)=? AND YEAR(fecha_venta)=?");
            ps.setInt(1, mes);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ventas v = new ventas();
                v.setNfactura_venta(f.FormatearNroFactura(rs.getString("nfactura_venta")));
                v.setFecha_venta(f.ParseDI(rs.getString("fecha_venta")));
                v.setTipo_venta(rs.getString("condicion_venta"));
                v.setTotal_venta(rs.getString("total_venta"));
                v.setTotal_iva_venta(rs.getString("total_iva_venta"));
                v.setGrav10_venta(rs.getInt("gravada_10"));
                v.setGrav5_venta(rs.getInt("gravada_5"));
                v.setExenta_venta(rs.getInt("total_E"));
                v.setNombre_cli(rs.getString("nombre_cli"));
                v.setNdocumento_cli(rs.getString("ndocumento_cli"));
                v.setDv_cli(rs.getString("dv_cli"));
                v.setTotaliva10(rs.getInt("totaliva10"));
                v.setTotaliva5(rs.getInt("totaliva5"));
                ven.add(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOventas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return ven;
    }
    
    public int TotalGravVentas(int mes, int year) {
        Connection con = conexion.getConnection();
        int ventagrav = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT SUM(total_venta) AS total,SUM(total_iva_venta) as iva,(SUM(total_venta)-SUM(total_iva_venta)) AS total_venta FROM ventas WHERE YEAR(fecha_venta)=? AND MONTH(fecha_venta) = ? AND close_venta=0");
            ps.setInt(1, year);
            ps.setInt(2, mes);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ventagrav = rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcompras.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return ventagrav;
    }
}
