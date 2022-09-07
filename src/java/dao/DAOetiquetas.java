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
import modelo.etiquetas;

/**
 *
 * @author akio
 */
public class DAOetiquetas {

    funciones f = new funciones();

    public ArrayList<etiquetas> ListarEtiquetas() {
        Connection con = conexion.getConnection();
        ArrayList<etiquetas> eti = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM etiquetas");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                etiquetas e = new etiquetas();
                e.setId_etiqueta(rs.getString("id_etiqueta"));
                e.setNombre_e(rs.getString("nombre_e"));
                e.setEstado_etiqueta(rs.getInt("estado_etiqueta"));
                e.setAbreviacion_e(rs.getString("abreviacion_e"));
                eti.add(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOetiquetas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return eti;
    }

    public ArrayList<etiquetas> ListarEtiquetasHabilitadas() {
        Connection con = conexion.getConnection();
        ArrayList<etiquetas> eti = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM etiquetas WHERE estado_etiqueta=0");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                etiquetas e = new etiquetas();
                e.setId_etiqueta(rs.getString("id_etiqueta"));
                e.setNombre_e(rs.getString("nombre_e"));
                e.setAbreviacion_e(rs.getString("abreviacion_e"));
                eti.add(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOetiquetas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return eti;
    }

    public etiquetas BuscarEtiquetas(String id) {
        Connection con = conexion.getConnection();
        etiquetas e = new etiquetas();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM etiquetas WHERE id_etiqueta = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                e.setNombre_e(rs.getString("nombre_e"));
                e.setAbreviacion_e(rs.getString("abreviacion_e"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOetiquetas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return e;
    }

    public int AgregarEtiquetas(etiquetas e) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO etiquetas(nombre_e, abreviacion_e) VALUES (?, ?)");
            ps.setString(1, e.getNombre_e());
            ps.setString(2, f.NoPosee(e.getAbreviacion_e()));
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOetiquetas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarEtiquetas(etiquetas e) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE etiquetas SET nombre_e = ?, abreviacion_e = ? WHERE id_etiqueta = ?");
            ps.setString(1, e.getNombre_e());
            ps.setString(2, f.NoPosee(e.getAbreviacion_e()));
            ps.setString(3, e.getId_etiqueta());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOetiquetas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int EliminarEtiquetas(int id) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE etiquetas SET estado_etiqueta = !estado_etiqueta WHERE id_etiqueta=?");
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
