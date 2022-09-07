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
import modelo.cajas;

/**
 *
 * @author 2HDEV
 */
public class DAOcajas {

    public ArrayList<cajas> ListarCajas() {
        Connection con = conexion.getConnection();
        ArrayList<cajas> caja = new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("SELECT * FROM caja, sucursal WHERE caja.id_sucursal = sucursal.id_sucursal");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cajas box = new cajas();
                box.setId_caja(rs.getInt("id_caja"));
                box.setCodigo_caja(rs.getString("codigo_caja"));
                box.setNombre_sucursal(rs.getString("nombre_sucursal"));
                caja.add(box);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcajas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return caja;
    }

    public ArrayList<cajas> ListarCajasDisponibles(int id) {
        Connection con = conexion.getConnection();
        ArrayList<cajas> caja = new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("SELECT * FROM caja WHERE id_caja NOT IN (SELECT id_caja FROM nfactura WHERE id_caja != ?)");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cajas box = new cajas();
                box.setId_caja(rs.getInt("id_caja"));
                box.setCodigo_caja(rs.getString("codigo_caja"));
                caja.add(box);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcajas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return caja;
    }

    public cajas BuscarCaja(int id_caja) {
        Connection con = conexion.getConnection();
        cajas box = new cajas();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("SELECT * FROM caja, sucursal WHERE caja.id_sucursal = sucursal.id_sucursal AND  id_caja = ?");
            ps.setInt(1, id_caja);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                box.setId_caja(rs.getInt("id_caja"));
                box.setCodigo_caja(rs.getString("codigo_caja"));
                box.setNombre_sucursal(rs.getString("nombre_sucursal"));
                box.setId_sucursal(rs.getString("id_sucursal"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcajas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return box;
    }

    public int AgregarCaja(cajas c) {
        Connection con = conexion.getConnection();
        int r = 0;
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("INSERT INTO caja (codigo_caja, id_sucursal) VALUES (?, ?)");
            ps.setString(1, c.getCodigo_caja());
            ps.setInt(2, Integer.valueOf(c.getId_sucursal()));
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOcajas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarCaja(cajas c) {
        Connection con = conexion.getConnection();
        int r = 0;
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("UPDATE caja SET codigo_caja = ?, id_sucursal = ? WHERE id_caja = ?");
            ps.setString(1, c.getCodigo_caja());
            ps.setInt(2, Integer.valueOf(c.getId_sucursal()));
            ps.setInt(3, c.getId_caja());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOcajas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    public int EliminarCaja(int id_caja) {
        Connection con = conexion.getConnection();
        int r = 0;
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("DELETE FROM caja WHERE id_caja=?");
            ps.setInt(1, id_caja);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOcajas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
