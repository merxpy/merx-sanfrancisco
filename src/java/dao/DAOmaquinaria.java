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
import modelo.maquinaria;

/**
 *
 * @author 2HDEV
 */
public class DAOmaquinaria {

    public ArrayList<maquinaria> ListarMaquinarias() {
        ArrayList<maquinaria> maquinaria = new ArrayList<>();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM maquinaria, marcas WHERE marcas.id_marca=maquinaria.id_marca AND maquinaria.anulado != 1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                maquinaria maq = new maquinaria();
                maq.setId_maquinaria(rs.getInt("id_maquinaria"));
                maq.setTipo_maquinaria(rs.getString("tipo_maquinaria"));
                maq.setSerie(rs.getString("serie"));
                maq.setModelo(rs.getString("modelo"));
                maq.setNombre_marca(rs.getString("nombre_marca"));
                maquinaria.add(maq);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOmaquinaria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return maquinaria;
    }

    public ArrayList<maquinaria> ListarMaquinariasInhabilitadas() {
        ArrayList<maquinaria> maquinaria = new ArrayList<>();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM maquinaria, marcas WHERE marcas.id_marca=maquinaria.id_marca AND maquinaria.anulado = 1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                maquinaria maq = new maquinaria();
                maq.setId_maquinaria(rs.getInt("id_maquinaria"));
                maq.setTipo_maquinaria(rs.getString("tipo_maquinaria"));
                maq.setSerie(rs.getString("serie"));
                maq.setModelo(rs.getString("modelo"));
                maq.setNombre_marca(rs.getString("nombre_marca"));
                maquinaria.add(maq);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOmaquinaria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return maquinaria;
    }

    public maquinaria BuscarMaquinarias(int id_maquinaria) {
        maquinaria maq = new maquinaria();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM maquinaria WHERE id_maquinaria = ? AND anulado=0");
            ps.setInt(1, id_maquinaria);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                maq.setId_maquinaria(rs.getInt("id_maquinaria"));
                maq.setTipo_maquinaria(rs.getString("tipo_maquinaria"));
                maq.setSerie(rs.getString("serie"));
                maq.setModelo(rs.getString("modelo"));
                maq.setId_marca(rs.getString("id_marca"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOmaquinaria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return maq;
    }

    public int InsertarMaquinaria(maquinaria maq) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO maquinaria (tipo_maquinaria, modelo, serie, id_marca) VALUES (?, ?, ?, ?)");
            ps.setString(1, maq.getTipo_maquinaria());
            ps.setString(2, maq.getModelo());
            ps.setString(3, maq.getSerie());
            ps.setString(4, maq.getId_marca());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOmaquinaria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarMaquinaria(maquinaria maq) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE maquinaria SET tipo_maquinaria=?, modelo=?, serie=?, id_marca=? WHERE id_maquinaria=?");
            ps.setString(1, maq.getTipo_maquinaria());
            ps.setString(2, maq.getModelo());
            ps.setString(3, maq.getSerie());
            ps.setString(4, maq.getId_marca());
            ps.setInt(5, maq.getId_maquinaria());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOmaquinaria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int AnularMaquinaria(int id_maquinaria) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE maquinaria SET anulado=!anulado WHERE id_maquinaria=?");
            ps.setInt(1, id_maquinaria);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOmaquinaria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public ArrayList<maquinaria> MaquinariasQuery(String query) {
        Connection con = conexion.getConnection();
        ArrayList<maquinaria> maquinarias = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM maquinaria WHERE tipo_maquinaria LIKE ? OR modelo LIKE ? OR serie LIKE ?");
            ps.setString(1, query);
            ps.setString(2, query);
            ps.setString(3, query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                maquinaria maq = new maquinaria();
                maq.setId_maquinaria(rs.getInt("id_maquinaria"));
                maq.setTipo_maquinaria(rs.getString("tipo_maquinaria"));
                maq.setSerie(rs.getString("serie"));
                maq.setModelo(rs.getString("modelo"));
                maquinarias.add(maq);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOmaquinaria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return maquinarias;
    }
}
