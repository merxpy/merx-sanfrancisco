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
import modelo.tipo_pago;

/**
 *
 * @author 2HDEV
 */
public class DAOtipopago {

    public ArrayList<tipo_pago> ListarTipoPago() {
        Connection con = conexion.getConnection();
        ArrayList<tipo_pago> tp = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tipo_pago");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tipo_pago t = new tipo_pago();
                t.setId_tipo_pago(rs.getInt("id_tipo_pago"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setAbr(rs.getString("abr"));
                tp.add(t);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOtipopago.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return tp;
    }

    public tipo_pago BuscarTipoPago(int id) {
        Connection con = conexion.getConnection();
        tipo_pago t = new tipo_pago();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tipo_pago WHERE id_tipo_pago = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                t.setId_tipo_pago(rs.getInt("id_tipo_pago"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setAbr(rs.getString("abr"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOtipopago.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return t;
    }

    public int AgregarTipoPago(tipo_pago t) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO tipo_pago(descripcion, abr) VALUES (?, ?)");
            ps.setString(1, t.getDescripcion());
            ps.setString(2, t.getAbr());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOtipopago.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarTipoPago(tipo_pago t) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE tipo_pago SET descripcion=?, abr=? WHERE id_tipo_pago=?");
            ps.setString(1, t.getDescripcion());
            ps.setString(2, t.getAbr());
            ps.setInt(3, t.getId_tipo_pago());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOtipopago.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    public int EliminarTipoPago(int id) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM tipo_pago WHERE id_tipo_pago = ?");
            ps.setInt(1, id);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOtipopago.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }
}
