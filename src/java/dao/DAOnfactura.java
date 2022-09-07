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
import modelo.nfacturas;

/**
 *
 * @author akio
 */
public class DAOnfactura {

    public ArrayList<nfacturas> ListarNfacturas() {
        Connection con = conexion.getConnection();
        ArrayList<nfacturas> fac = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM nfactura nf, timbrado t,caja c WHERE nf.id_timbrado = t.id_timbrado AND nf.id_caja=c.id_caja");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nfacturas nf = new nfacturas();
                nf.setId_nfactura(rs.getInt("id_nfactura"));
                nf.setDel_nfac(rs.getString("del_nfac"));
                nf.setAl_nfac(rs.getString("al_nfac"));
                nf.setEstado_nfac(rs.getString("estado_nfac"));
                nf.setNumero_tim(rs.getString("numero_tim"));
                nf.setCodigo_caja(rs.getString("codigo_caja"));
                fac.add(nf);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOnfactura.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return fac;
    }

    public nfacturas BuscarNfacturas(String id) {
        Connection con = conexion.getConnection();
        nfacturas nf = new nfacturas();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM nfactura WHERE id_nfactura = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nf.setId_nfactura(rs.getInt("id_nfactura"));
                nf.setDel_nfac(rs.getString("del_nfac"));
                nf.setAl_nfac(rs.getString("al_nfac"));
                nf.setId_timbrado(rs.getInt("id_timbrado"));
                nf.setId_caja(rs.getInt("id_caja"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOnfactura.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return nf;
    }

    public int AgregarNfacturas(nfacturas nf) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO nfactura (del_nfac, al_nfac, id_timbrado, id_caja) VALUES (?, ?, ?, ?)");
            ps.setString(1, nf.getDel_nfac());
            ps.setString(2, nf.getAl_nfac());
            ps.setInt(3, nf.getId_timbrado());
            ps.setInt(4, nf.getId_caja());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOnfactura.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarNfacturas(nfacturas nf) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE nfactura SET del_nfac = ?, al_nfac = ?, id_timbrado = ?, id_caja = ? WHERE id_nfactura = ?");
            ps.setString(1, nf.getDel_nfac());
            ps.setString(2, nf.getAl_nfac());
            ps.setInt(3, nf.getId_timbrado());
            ps.setInt(4, nf.getId_caja());
            ps.setInt(5, nf.getId_nfactura());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOnfactura.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int UsarNumeros(String id, String u) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE nfactura SET estado_nfac = ? WHERE id_nfactura = ?");
            ps.setString(1, u);
            ps.setString(2, id);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOnfactura.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
