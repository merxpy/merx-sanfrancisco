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
import modelo.paises;

/**
 *
 * @author 2HDEV
 */
public class DAOpaises {

    public ArrayList<paises> ListarPaises() {
        Connection con = conexion.getConnection();
        ArrayList<paises> pa = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM paises WHERE close_pais = 0");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                paises p = new paises();
                p.setId_pais(rs.getInt("id_pais"));
                p.setNombre_pais(rs.getString("nombre_pais"));
                p.setMoneda_pais(rs.getString("moneda_pais"));
                p.setAbreviacion_pais(rs.getString("abreviacion_pais"));
                p.setIso_mon_pais(rs.getString("iso_mon_pais"));
                pa.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpaises.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pa;
    }

    public paises BuscarPaises(int id) {
        Connection con = conexion.getConnection();
        paises p = new paises();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM paises WHERE close_pais = 0 AND id_pais = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p.setId_pais(rs.getInt("id_pais"));
                p.setNombre_pais(rs.getString("nombre_pais"));
                p.setMoneda_pais(rs.getString("moneda_pais"));
                p.setAbreviacion_pais(rs.getString("abreviacion_pais"));
                p.setIso_mon_pais(rs.getString("iso_mon_pais"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpaises.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return p;
    }

    public int AgregarPaises(paises p) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO paises (nombre_pais, moneda_pais, abreviacion_pais, iso_mon_pais) VALUES (?, ?, ?, ?)");
            ps.setString(1, p.getNombre_pais());
            ps.setString(2, p.getMoneda_pais());
            ps.setString(3, p.getAbreviacion_pais());
            ps.setString(4, p.getIso_mon_pais());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOpaises.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.getConnection();
        }
        return r;
    }

    public int ActualizarPaises(paises p) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE paises SET nombre_pais = ?, moneda_pais = ?, abreviacion_pais = ?, iso_mon_pais = ? WHERE id_pais = ?");
            ps.setString(1, p.getNombre_pais());
            ps.setString(2, p.getMoneda_pais());
            ps.setString(3, p.getAbreviacion_pais());
            ps.setString(4, p.getIso_mon_pais());
            ps.setInt(5, p.getId_pais());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOpaises.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.getConnection();
        }
        return r;
    }

    public int EliminarPaises(paises p) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE paises SET close_pais = ? WHERE id_pais = ?");
            ps.setInt(1, p.getClose_pais());
            ps.setInt(2, p.getId_pais());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOpaises.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public ArrayList<paises> BuscarPaisesQuery(String q) {
        Connection con = conexion.getConnection();
        ArrayList<paises> paises = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM paises WHERE nombre_pais LIKE ? and close_pais=0");
            ps.setString(1, "%" + q + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                paises pais = new paises();
                pais.setNombre_pais(rs.getString("nombre_pais"));
                pais.setPhone_code(rs.getString("phone_code"));
                paises.add(pais);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpaises.class.getName()).log(Level.SEVERE, null, ex);
        }
        return paises;
    }

    public paises BuscarPhoneCodePais(String phone_code) {
        Connection con = conexion.getConnection();
        paises pais = new paises();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM paises WHERE phone_code=? and close_pais=0 LIMIT 1");
            ps.setString(1, phone_code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pais.setNombre_pais(rs.getString("nombre_pais"));
                pais.setPhone_code(rs.getString("phone_code"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpaises.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pais;
    }
}
