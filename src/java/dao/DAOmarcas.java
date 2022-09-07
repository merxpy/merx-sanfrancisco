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
import modelo.marcas;

/**
 *
 * @author akio
 */
public class DAOmarcas {

    funciones f = new funciones();

    public ArrayList<marcas> ListarMarcas() {
        Connection con = conexion.getConnection();
        ArrayList<marcas> mar = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM marcas");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                marcas m = new marcas();
                m.setId_marca(rs.getString("id_marca"));
                m.setNombre_marca(rs.getString("nombre_marca"));
                m.setEstado_marca(rs.getInt("estado_marca"));
                m.setDescripcion_marca(rs.getString("descripcion_marca"));
                mar.add(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOmarcas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return mar;
    }

    public ArrayList<marcas> ListarMarcasDisponibles() {
        Connection con = conexion.getConnection();
        ArrayList<marcas> mar = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM marcas WHERE estado_marca = 0");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                marcas m = new marcas();
                m.setId_marca(rs.getString("id_marca"));
                m.setNombre_marca(rs.getString("nombre_marca"));
                m.setEstado_marca(rs.getInt("estado_marca"));
                m.setDescripcion_marca(rs.getString("descripcion_marca"));
                mar.add(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOmarcas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return mar;
    }

    public marcas BuscarMarcas(String id_marca) {
        Connection con = conexion.getConnection();
        marcas m = new marcas();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM marcas WHERE id_marca = ?");
            ps.setString(1, id_marca);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                m.setNombre_marca(rs.getString("nombre_marca"));
                m.setDescripcion_marca(rs.getString("descripcion_marca"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOmarcas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return m;
    }

    public int AgregarMarcas(marcas m) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO marcas (nombre_marca, descripcion_marca) VALUES (?, ?)");
            ps.setString(1, m.getNombre_marca());
            ps.setString(2, f.NoPosee(m.getDescripcion_marca()));
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOmarcas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarMarcas(marcas m) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE marcas SET nombre_marca = ?, descripcion_marca = ? WHERE  id_marca = ?");
            ps.setString(1, m.getNombre_marca());
            ps.setString(2, f.NoPosee(m.getDescripcion_marca()));
            ps.setString(3, m.getId_marca());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOmarcas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int DeleteMarcas(int id) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE marcas SET estado_marca = !estado_marca WHERE id_marca=?");
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
