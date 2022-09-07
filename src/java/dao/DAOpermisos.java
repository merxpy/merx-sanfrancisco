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
import modelo.permisos;

/**
 *
 * @author 2HDEV
 */
public class DAOpermisos {

    public ArrayList<permisos> ListarPermisos() {
        ArrayList<permisos> permiso = new ArrayList<>();
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("SELECT * FROM permisos");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                permisos p = new permisos();
                p.setId_permiso(String.valueOf(rs.getInt("id_permiso")));
                p.setModulo_per(rs.getString("modulo_per"));
                permiso.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOpermisos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return permiso;
    }
}
