/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexion.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.mermas;

/**
 *
 * @author 2HDEV
 */
public class DAOmermas {
    
    public ArrayList<mermas> ListarMermas() {
        Connection con = conexion.getConnection();
        ArrayList<mermas> me = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM mermas");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                mermas m = new mermas();
                m.setId_merma(rs.getInt("id_merma"));
                m.setConcepto_merma(rs.getString("concepto_merma"));
                m.setTipo_merma(rs.getString("tipo_merma"));
                me.add(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOmermas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return me;
    }
    
    public mermas BuscarMerma(int id) {
        Connection con = conexion.getConnection();
        mermas m = new mermas();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM mermas WHERE id_merma = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                m.setId_merma(rs.getInt("id_merma"));
                m.setConcepto_merma(rs.getString("concepto_merma"));
                m.setTipo_merma(rs.getString("tipo_merma"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOmermas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return m;
    }
    
    public int AgregarMerma(mermas m) {
        int r = 0;
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO mermas (concepto_merma, tipo_merma) VALUES (?, ?)");
            ps.setString(1, m.getConcepto_merma());
            ps.setString(2, m.getTipo_merma());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOmermas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
    
    public int ActualizarMerma(mermas m) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE mermas SET concepto_merma = ?, tipo_merma = ? WHERE id_merma = ?");
            ps.setString(1, m.getConcepto_merma());
            ps.setString(2, m.getTipo_merma());
            ps.setInt(3, m.getId_merma());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOmermas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
    
    public int EliminarMerma(int id) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM mermas WHERE id_merma = ?");
            ps.setInt(1, id);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOmermas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
    
    public ArrayList<mermas> ListarHistorial() {
        ArrayList<mermas> me = new ArrayList<>();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM mermas_has_stock ms, mermas m, stock s WHERE ms.stock_id_stock = s.id_stock m.id_merma = ms.mermas_id_merma");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                mermas m = new mermas();
                m.setId_articulo(rs.getString("id_articulo"));
                m.setNombre_ar(rs.getString("nombre_ar"));
                m.setPrecio_venta_ar(rs.getString("precio_venta_ar"));
                m.setCantidad(rs.getInt("cantidad"));
                m.setTotal(rs.getInt("total"));
                m.setConcepto_merma(rs.getString("concepto_merma"));
                me.add(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOmermas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return me;
    }
}
