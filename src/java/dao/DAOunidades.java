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
import modelo.unidades;

/**
 *
 * @author akio
 */
public class DAOunidades {

    public ArrayList<unidades> ListarUnidades() {
        Connection con = conexion.getConnection();
        ArrayList<unidades> uni = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM unidades");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                unidades u = new unidades();
                u.setId_unidad(rs.getString("id_unidad"));
                u.setNombre_u(rs.getString("nombre_u"));
                u.setEstado_unidad(rs.getInt("estado_unidad"));
                u.setAbreviatura_u(rs.getString("abreviatura_u"));
                uni.add(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOunidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return uni;
    }

    public ArrayList<unidades> ListarUnidadesHabilitadas() {
        Connection con = conexion.getConnection();
        ArrayList<unidades> uni = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM unidades WHERE estado_unidad=0");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                unidades u = new unidades();
                u.setId_unidad(rs.getString("id_unidad"));
                u.setNombre_u(rs.getString("nombre_u"));
                u.setAbreviatura_u(rs.getString("abreviatura_u"));
                uni.add(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOunidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return uni;
    }

    public unidades BuscarUnidades(String id) {
        Connection con = conexion.getConnection();
        unidades u = new unidades();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM unidades WHERE id_unidad = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                u.setId_unidad(rs.getString("id_unidad"));
                u.setNombre_u(rs.getString("nombre_u"));
                u.setAbreviatura_u(rs.getString("abreviatura_u"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOunidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return u;
    }

    public int AgregarUnidades(unidades u) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO unidades (nombre_u, abreviatura_u) VALUES (?, ?)");
            ps.setString(1, u.getNombre_u());
            ps.setString(2, u.getAbreviatura_u());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOunidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.getConnection();
        }
        return r;
    }

    public int ActualizarUnidades(unidades u) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE unidades SET nombre_u = ?, abreviatura_u = ? WHERE id_unidad = ?");
            ps.setString(1, u.getNombre_u());
            ps.setString(2, u.getAbreviatura_u());
            ps.setString(3, u.getId_unidad());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOunidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int InhabilitarUnidad(int id) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE unidades SET estado_unidad = !estado_unidad WHERE id_unidad=?");
            ps.setInt(1, id);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOmarcas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
