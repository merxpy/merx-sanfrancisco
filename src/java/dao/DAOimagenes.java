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
import modelo.imagenes;

/**
 *
 * @author akio
 */
public class DAOimagenes {

    public ArrayList<imagenes> ListarImagenesxArticulo(String id_articulo) {
        Connection con = conexion.getConnection();
        ArrayList<imagenes> img = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM imagenes WHERE articulos_id_articulo = ?");
            ps.setString(1, id_articulo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                imagenes i = new imagenes();
                i.setId_imagen(rs.getString("id_imagen"));
                i.setImage_path(rs.getString("imagen_path"));
                i.setMime_type(rs.getString("mime_type"));
                i.setId_articulo(rs.getString("articulos_id_articulo"));
                img.add(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOimagenes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return img;
    }

    public int InsertarImagen(imagenes img) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO imagenes (imagen_path, mime_type, articulos_id_articulo) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, img.getImage_path());
            ps.setString(2, img.getMime_type());
            ps.setString(3, img.getId_articulo());
            r = ps.executeUpdate();
            if (r != 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    r = (int) rs.getLong(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOimagenes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int BorrarImagen(imagenes img) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM imagenes WHERE articulos_id_articulo = ? and id_imagen = ?");
            ps.setString(1, img.getId_articulo());
            ps.setString(2, img.getId_imagen());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOimagenes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public imagenes BuscarImagenxArticulo(int id_articulo) {
        imagenes imagen = new imagenes();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM imagenes WHERE articulos_id_articulo = ? LIMIT 1");
            ps.setInt(1, id_articulo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                imagen.setId_articulo(rs.getString("articulos_id_articulo"));
                imagen.setId_imagen(rs.getString("id_imagen"));
                imagen.setImage_path(rs.getString("imagen_path"));
                imagen.setMime_type(rs.getString("mime_type"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOimagenes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return imagen;
    }
}
