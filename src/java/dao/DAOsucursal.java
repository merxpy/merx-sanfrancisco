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
import modelo.sucursales;

/**
 *
 * @author akio
 */
public class DAOsucursal {

    public ArrayList<sucursales> ListarSucursales() {
        Connection con = conexion.getConnection();
        ArrayList<sucursales> s = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM sucursal");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sucursales suc = new sucursales();
                suc.setId_sucursal(rs.getString("id_sucursal"));
                suc.setCodigo_sucursal(rs.getString("codigo_sucursal"));
                suc.setNombre_sucursal(rs.getString("nombre_sucursal"));
                s.add(suc);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOsucursal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return s;
    }

    public sucursales BuscarSucursales(String id) {
        Connection con = conexion.getConnection();
        sucursales s = new sucursales();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM sucursal WHERE id_sucursal = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                s.setCodigo_sucursal(rs.getString("codigo_sucursal"));
                s.setNombre_sucursal(rs.getString("nombre_sucursal"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOsucursal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return s;
    }

    public int AgregarSucursales(sucursales s) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO sucursal(codigo_sucursal, nombre_sucursal) values(?, ?)");
            ps.setString(1, s.getCodigo_sucursal());
            ps.setString(2, s.getNombre_sucursal());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOsucursal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarSucursal(sucursales s) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE sucursal SET nombre_sucursal = ?, codigo_sucursal = ? WHERE id_sucursal = ?");
            ps.setString(1, s.getNombre_sucursal());
            ps.setString(2, s.getCodigo_sucursal());
            ps.setInt(3, Integer.valueOf(s.getId_sucursal()));
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOsucursal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int EliminarSucursal(int id) {
        Connection con = conexion.getConnection();
        int r = 0;
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("DELETE FROM sucursal WHERE id_sucursal = ?");
            ps.setInt(1, id);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOsucursal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
