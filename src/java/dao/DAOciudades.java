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
import modelo.ciudades;

/**
 *
 * @author akio
 */
public class DAOciudades {

    public ArrayList<ciudades> ListarCiudades() {
        Connection con = conexion.getConnection();
        ArrayList<ciudades> ciu = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ciudades c INNER JOIN departamentos d ON d.id_departamento = c.departamentos_id_departamento");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ciudades c = new ciudades();
                c.setId_ciudad(rs.getString("id_ciudad"));
                c.setNombre_ciu(rs.getString("nombre_ciu"));
                c.setNombre_dep(rs.getString("nombre_dep"));
                ciu.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOciudades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return ciu;
    }

    public ciudades BuscarCiudad(String id) {
        ciudades c = new ciudades();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ciudades c INNER JOIN departamentos d ON d.id_departamento = c.departamentos_id_departamento WHERE id_ciudad = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c.setId_ciudad(rs.getString("id_ciudad"));
                c.setNombre_ciu(rs.getString("nombre_ciu"));
                c.setNombre_dep(rs.getString("nombre_dep"));
                c.setId_departamento(rs.getString("id_departamento"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOciudades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }

        return c;
    }

    public int AgregarCiudades(ciudades c) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO ciudades(nombre_ciu, departamentos_id_departamento) VALUES (?, ?)");
            ps.setString(1, c.getNombre_ciu());
            ps.setString(2, c.getId_departamento());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOciudades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarCiudades(ciudades c) {
        int r = 0;
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE ciudades SET nombre_ciu=?, departamentos_id_departamento = ? WHERE id_ciudad = ? ");
            ps.setString(1, c.getNombre_ciu());
            ps.setString(2, c.getId_departamento());
            ps.setString(3, c.getId_ciudad());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOciudades.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            conexion.Close(con);
        }
        return r;
    }
}
