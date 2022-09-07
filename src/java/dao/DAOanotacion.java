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
import modelo.anotaciones;

/**
 *
 * @author 2HDEV
 */
public class DAOanotacion {

    funciones f = new funciones();

    public ArrayList<anotaciones> ListarAnotaciones(int id) {
        Connection con = conexion.getConnection();
        ArrayList<anotaciones> notas = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM anotaciones WHERE id_usuario = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                anotaciones nota = new anotaciones();
                nota.setId_anotacion(rs.getInt("id_anotacion"));
                nota.setTitulo_anotacion(rs.getString("titulo_anotacion"));
                nota.setContenido_anotacion(rs.getString("contenido_anotacion"));
                nota.setColor_anotacion(rs.getString("color_anotacion"));
                nota.setFechahora_anotacion(f.ParseDHI(rs.getString("fechahora_anotacion")));
                nota.setVencimiento_anotacion(rs.getString("vencimiento_anotacion"));
                notas.add(nota);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOanotacion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return notas;
    }

    public anotaciones BuscarAnotacion(int id, int id_usuario) {
        Connection con = conexion.getConnection();
        anotaciones nota = new anotaciones();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM anotaciones WHERE id_anotacion = ? and id_usuario=?");
            ps.setInt(1, id);
            ps.setInt(2, id_usuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nota.setId_anotacion(rs.getInt("id_anotacion"));
                nota.setTitulo_anotacion(rs.getString("titulo_anotacion"));
                nota.setContenido_anotacion(rs.getString("contenido_anotacion"));
                nota.setColor_anotacion(rs.getString("color_anotacion"));
                nota.setFechahora_anotacion(f.ParseDHI(rs.getString("fechahora_anotacion")));
                nota.setVencimiento_anotacion(f.ParseDI(rs.getString("vencimiento_anotacion")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOanotacion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return nota;
    }

    public int AgregarNota(anotaciones nota) {
        Connection con = conexion.getConnection();
        int r = 0;
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("SET @usuario = ?");
            ps.setString(1, nota.getId_usuario());
            ps.execute();
            ps = con.prepareStatement("INSERT INTO anotaciones(titulo_anotacion, contenido_anotacion, color_anotacion, vencimiento_anotacion, id_usuario) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, nota.getTitulo_anotacion());
            ps.setString(2, nota.getContenido_anotacion());
            ps.setString(3, nota.getColor_anotacion());
            ps.setString(4, f.ParseDate(nota.getVencimiento_anotacion()));
            ps.setInt(5, Integer.valueOf(nota.getId_usuario()));
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOanotacion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarNota(anotaciones nota) {
        Connection con = conexion.getConnection();
        int r = 0;
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("SET @usuario = ?");
            ps.setString(1, nota.getId_usuario());
            ps.execute();
            ps = con.prepareStatement("UPDATE anotaciones SET titulo_anotacion=?, contenido_anotacion=?, color_anotacion=?, vencimiento_anotacion=? WHERE id_anotacion=? AND id_usuario = ?");
            ps.setString(1, nota.getTitulo_anotacion());
            ps.setString(2, nota.getContenido_anotacion());
            ps.setString(3, nota.getColor_anotacion());
            ps.setString(4, f.ParseDate(nota.getVencimiento_anotacion()));
            ps.setInt(5, nota.getId_anotacion());
            ps.setInt(6, Integer.valueOf(nota.getId_usuario()));
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOanotacion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int EliminarNota(anotaciones nota) {
        Connection con = conexion.getConnection();
        int r = 0;
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("SET @usuario = ?");
            ps.setString(1, String.valueOf(nota.getId_usuario()));
            ps.execute();
            ps = con.prepareStatement("DELETE FROM anotaciones WHERE id_anotacion = ? AND id_usuario = ?");
            ps.setInt(1, nota.getId_anotacion());
            ps.setInt(2, Integer.valueOf(nota.getId_usuario()));
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOanotacion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public void EliminarNotasVencidas() {
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("DELETE FROM anotaciones WHERE DATE(vencimiento_anotacion) < DATE(NOW())");
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DAOanotacion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }
}
