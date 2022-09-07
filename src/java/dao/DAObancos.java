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
import modelo.bancos;

/**
 *
 * @author 2HDEV
 */
public class DAObancos {

    public ArrayList<bancos> ListarBancos() {
        Connection con = conexion.getConnection();
        ArrayList<bancos> bancos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bancos");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bancos banco = new bancos();
                banco.setId_banco(rs.getInt("id_banco"));
                banco.setNombre_banco(rs.getString("nombre_banco"));
                bancos.add(banco);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAObancos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return bancos;
    }

    public bancos BuscarBancos(int id) {
        Connection con = conexion.getConnection();
        bancos banco = new bancos();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bancos WHERE id_banco=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                banco.setId_banco(rs.getInt("id_banco"));
                banco.setNombre_banco(rs.getString("nombre_banco"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAObancos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return banco;
    }

    public int AgregarBanco(bancos banco) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO bancos (nombre_banco) VALUES (?)");
            ps.setString(1, banco.getNombre_banco());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAObancos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarBanco(bancos banco) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE bancos SET nombre_banco=? WHERE id_banco=?");
            ps.setString(1, banco.getNombre_banco());
            ps.setInt(2, banco.getId_banco());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAObancos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public ArrayList<bancos> BuscarBancosQuery(String q) {
        Connection con = conexion.getConnection();
        ArrayList<bancos> bancos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bancos WHERE nombre_banco LIKE ?");
            ps.setString(1, q);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bancos banco = new bancos();
                banco.setId_banco(rs.getInt("id_banco"));
                banco.setNombre_banco(rs.getString("nombre_banco"));
                bancos.add(banco);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAObancos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return bancos;
    }
}
