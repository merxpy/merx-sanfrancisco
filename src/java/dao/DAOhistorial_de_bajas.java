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
import modelo.historial_de_bajas;

/**
 *
 * @author 2HDEV
 */
public class DAOhistorial_de_bajas {

    public ArrayList<historial_de_bajas> ListarHistorial() {
        ArrayList<historial_de_bajas> hd = new ArrayList<>();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT h.id_historial_de_baja, a.nombre_ar, m.concepto_merma, h.cantidad_baja, h.total_baja FROM historial_de_bajas h,"
                    + " articulos a, mermas m WHERE a.id_articulo = h.id_articulo AND m.id_merma=h.id_merma ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                historial_de_bajas h = new historial_de_bajas();
                h.setId_historial_de_baja(rs.getInt("id_historial_de_baja"));
                h.setNombre_ar(rs.getString("nombre_ar"));
                h.setConcepto_merma(rs.getString("concepto_merma"));
                h.setCantidad_baja(rs.getInt("cantidad_baja"));
                h.setTotal_baja(rs.getInt("total_baja"));
                hd.add(h);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOhistorial_de_bajas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return hd;
    }

    public historial_de_bajas BuscarHistorialdeBajas(int id) {
        historial_de_bajas h = new historial_de_bajas();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT h.id_merma, h.id_stock, h.id_articulo, h.cantidad_baja, h.total_baja, h.hora_fecha_baja, s.cantidad_stock FROM historial_de_bajas h, articulos a, mermas m, stock s WHERE a.id_articulo = h.id_articulo AND m.id_merma=h.id_merma AND s.id_stock=h.id_stock AND id_historial_de_baja = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                h.setId_historial_de_baja(id);
                h.setId_merma(rs.getInt("id_merma"));
                h.setId_stock(rs.getString("id_stock"));
                h.setId_articulo(rs.getString("id_articulo"));
                h.setCantidad_baja(rs.getInt("cantidad_baja"));
                h.setTotal_baja(rs.getInt("total_baja"));
                h.setFecha_hora_baja(rs.getString("hora_fecha_baja"));
                h.setCantidad(rs.getInt("cantidad_stock"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOhistorial_de_bajas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return h;
    }

    public int AgregarBajas(ArrayList<historial_de_bajas> hb, int id) {
        int r = 0;
        Connection con = conexion.getConnection();
        try {
            con.setAutoCommit(false);
            for (historial_de_bajas h : hb) {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM stock WHERE articulos_id_articulo = ?");
                ps.setString(1, h.getId_articulo());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    h.setId_stock(rs.getString("id_stock"));
                    h.setCantidad(rs.getInt("cantidad_stock"));
                }
                ps = con.prepareStatement("INSERT INTO historial_de_bajas (cantidad_baja, total_baja, hora_fecha_baja, usuario, id_stock, id_articulo, id_merma) VALUES (?, ?, NOW(), ?, ?, ?, ?)");
                ps.setInt(1, h.getCantidad_baja());
                ps.setInt(2, h.getTotal_baja());
                ps.setInt(3, id);
                ps.setString(4, h.getId_stock());
                ps.setString(5, h.getId_articulo());
                ps.setInt(6, h.getId_merma());
                ps.executeUpdate();

                ps = con.prepareStatement("SELECT * FROM mermas WHERE id_merma = ?");
                ps.setInt(1, h.getId_merma());
                rs = ps.executeQuery();
                if (rs.next()) {
                    ps = con.prepareStatement("UPDATE stock SET cantidad_stock = ? WHERE id_stock = ?");
                    if (rs.getString("tipo_merma").equals("ALTA")) {
                        ps.setInt(1, h.getCantidad() + h.getCantidad_baja());
                    } else {
                        ps.setInt(1, h.getCantidad() - h.getCantidad_baja());
                    }
                    ps.setInt(2, Integer.valueOf(h.getId_stock()));
                    r = ps.executeUpdate();
                }
            }
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DAOhistorial_de_bajas.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(DAOhistorial_de_bajas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    public int EliminarBaja(int id) {
        int r = 0;
        Connection con = conexion.getConnection();
        historial_de_bajas hb = BuscarHistorialdeBajas(id);
        try {
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement("SELECT * FROM mermas WHERE id_merma = ?");
            ps.setInt(1, hb.getId_merma());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps = con.prepareStatement("UPDATE stock SET cantidad_stock = ? WHERE id_stock = ?");
                if (rs.getString("tipo_merma").equals("ALTA")) {
                    ps.setInt(1, hb.getCantidad() - hb.getCantidad_baja());
                } else {
                    ps.setInt(1, hb.getCantidad() + hb.getCantidad_baja());
                }
                ps.setInt(2, Integer.valueOf(hb.getId_stock()));
                r = ps.executeUpdate();
            }

            ps = con.prepareStatement("DELETE FROM historial_de_bajas WHERE id_historial_de_baja = ?");
            ps.setInt(1, hb.getId_historial_de_baja());
            r = ps.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DAOhistorial_de_bajas.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(DAOhistorial_de_bajas.class.getName()).log(Level.SEVERE, null, ex);
        }

        return r;
    }
}
