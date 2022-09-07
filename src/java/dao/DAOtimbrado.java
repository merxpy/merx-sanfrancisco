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
import modelo.timbrado;

/**
 *
 * @author akio
 */
public class DAOtimbrado {

    funciones f = new funciones();

    public ArrayList<timbrado> ListarTimbrados() {
        Connection con = conexion.getConnection();
        ArrayList<timbrado> tim = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM timbrado WHERE estado_tim='DISPONIBLE'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                timbrado t = new timbrado();
                t.setId_timbrado(rs.getInt("id_timbrado"));
                t.setNumero_tim(rs.getString("numero_tim"));
                t.setVencimiento_tim(f.ParseDI(rs.getString("vencimiento_tim")));
                tim.add(t);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOtimbrado.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return tim;
    }

    public ArrayList<timbrado> ListarTimbradosVencidos() {
        Connection con = conexion.getConnection();
        ArrayList<timbrado> tim = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM timbrado WHERE estado_tim='VENCIDO'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                timbrado t = new timbrado();
                t.setId_timbrado(rs.getInt("id_timbrado"));
                t.setNumero_tim(rs.getString("numero_tim"));
                t.setVencimiento_tim(f.ParseDI(rs.getString("vencimiento_tim")));
                tim.add(t);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOtimbrado.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return tim;
    }

    public ArrayList<timbrado> ListarTimbradosDisponibles(int id) {
        Connection con = conexion.getConnection();
        ArrayList<timbrado> tim = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM timbrado WHERE id_timbrado NOT IN (SELECT id_timbrado FROM nfactura WHERE id_timbrado != ?)");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                timbrado t = new timbrado();
                t.setId_timbrado(rs.getInt("id_timbrado"));
                t.setNumero_tim(rs.getString("numero_tim"));
                t.setVencimiento_tim(f.ParseDI(rs.getString("vencimiento_tim")));
                tim.add(t);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOtimbrado.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return tim;
    }

    public timbrado BuscarTimbrado(String id) {
        Connection con = conexion.getConnection();
        timbrado t = new timbrado();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM timbrado WHERE id_timbrado = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                t.setId_timbrado(rs.getInt("id_timbrado"));
                t.setNumero_tim(rs.getString("numero_tim"));
                t.setVencimiento_tim(f.ParseDI(rs.getString("vencimiento_tim")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOtimbrado.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return t;
    }

    public int AgregarTimbrados(timbrado t) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO timbrado(numero_tim, vencimiento_tim) VALUES (?, ?)");
            ps.setString(1, t.getNumero_tim());
            ps.setString(2, f.ParseDate(t.getVencimiento_tim()));
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOtimbrado.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarTimbrado(timbrado t) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE timbrado SET numero_tim = ?, vencimiento_tim = ? WHERE id_timbrado = ?");
            ps.setString(1, t.getNumero_tim());
            ps.setString(2, f.ParseDate(t.getVencimiento_tim()));
            ps.setInt(3, t.getId_timbrado());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOtimbrado.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
