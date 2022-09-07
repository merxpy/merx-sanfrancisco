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
import modelo.departamentos;

/**
 *
 * @author akio
 */
public class DAOdepartamentos {

    public ArrayList<departamentos> ListarDepartamentos() {
        Connection con = conexion.getConnection();
        ArrayList<departamentos> dep = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM departamentos");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                departamentos d = new departamentos();
                d.setId_departamento(rs.getString("id_departamento"));
                d.setNombre_dep(rs.getString("nombre_dep"));
                dep.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOdepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dep;
    }

    public departamentos BuscarDepartamento(String id) {
        departamentos d = new departamentos();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM departamentos WHERE id_departamento = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                d.setId_departamento(rs.getString("id_departamento"));
                d.setNombre_dep(rs.getString("nombre_dep"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOdepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return d;
    }

    public int AgregarDepartamentos(departamentos d) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO departamentos(nombre_dep) VALUES (?)");
            ps.setString(1, d.getNombre_dep());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOdepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarDepartamentos(departamentos d) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE departamentos SET nombre_dep = ? WHERE id_departamento = ?");
            ps.setString(1, d.getNombre_dep());
            ps.setString(2, d.getId_departamento());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOdepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
