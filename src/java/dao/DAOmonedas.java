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
import modelo.monedas;

/**
 *
 * @author 2HDEV
 */
public class DAOmonedas {

    public ArrayList<monedas> ListarMonedas() {
        Connection con = conexion.getConnection();
        ArrayList<monedas> mon = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM monedas m, paises p WHERE close_moneda = 0 AND p.id_pais = m.id_pais");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                monedas m = new monedas();
                m.setId_moneda(rs.getInt("id_moneda"));
                m.setValor_moneda(rs.getInt("valor_moneda"));
                m.setNombre_pais(rs.getString("nombre_pais"));
                m.setMoneda_pais(rs.getString("moneda_pais"));
                m.setIso_mon_pais(rs.getString("iso_mon_pais"));
                mon.add(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOmonedas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return mon;
    }

    public monedas BuscarMonedas(int id) {
        Connection con = conexion.getConnection();
        monedas m = new monedas();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM monedas m, paises p WHERE close_moneda = 0 AND p.id_pais = m.id_pais AND m.id_moneda = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                m.setId_moneda(rs.getInt("id_moneda"));
                m.setValor_moneda(rs.getInt("valor_moneda"));
                m.setNombre_pais(rs.getString("nombre_pais"));
                m.setMoneda_pais(rs.getString("moneda_pais"));
                m.setIso_mon_pais(rs.getString("iso_mon_pais"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOmonedas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return m;
    }

    public int AgregarMonedas(monedas m) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO monedas (valor_moneda, id_pais) VALUES (?, ?)");
            ps.setInt(1, m.getValor_moneda());
            ps.setInt(2, m.getId_pais());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOmonedas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarMonedas(monedas m) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE monedas SET valor_moneda=? , id_pais=? WHERE id_moneda = ?");
            ps.setInt(1, m.getValor_moneda());
            ps.setInt(2, m.getId_pais());
            ps.setInt(3, m.getId_moneda());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOmonedas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int EliminarMoneda(monedas m) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE monedas SET close_moneda=? WHERE id_moneda = ?");
            ps.setInt(1, m.getClose_moneda());
            ps.setInt(2, m.getId_moneda());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOmonedas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public ArrayList<monedas> BuscarMonedasxPais(int id) {
        Connection con = conexion.getConnection();
        ArrayList<monedas> mon = new ArrayList<>();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM monedas WHERE id_pais = ? AND close_moneda = 0");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                monedas m = new monedas();
                m.setId_moneda(rs.getInt("id_moneda"));
                m.setValor_moneda(rs.getInt("valor_moneda"));
                m.setId_pais(rs.getInt(rs.getInt("id_pais")));
                mon.add(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOmonedas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return mon;
    }
}
