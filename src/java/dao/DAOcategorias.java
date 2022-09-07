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
import modelo.categorias;

/**
 *
 * @author 2HDEV
 */
public class DAOcategorias {

    funciones f = new funciones();

    public ArrayList<categorias> ListarCategorias() {
        Connection con = conexion.getConnection();
        ArrayList<categorias> categorias = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM categorias");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                categorias categoria = new categorias();
                categoria.setId_categoria(rs.getInt("id_categoria"));
                categoria.setCategoria(rs.getString("categoria"));
                categoria.setDescripcion_cat(rs.getString("descripcion"));
                categoria.setClose_cat(rs.getBoolean("close_cat"));
                categoria.setTipo_cat(rs.getInt("tipo_cat"));
                categorias.add(categoria);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcategorias.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return categorias;
    }

    public ArrayList<categorias> ListarCategoriasxTipo(int tipo) {
        Connection con = conexion.getConnection();
        ArrayList<categorias> categorias = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM categorias WHERE close_cat=0 AND tipo_cat=?");
            ps.setInt(1, tipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                categorias categoria = new categorias();
                categoria.setId_categoria(rs.getInt("id_categoria"));
                categoria.setCategoria(rs.getString("categoria"));
                categoria.setDescripcion_cat(rs.getString("descripcion"));
                categoria.setClose_cat(rs.getBoolean("close_cat"));
                categorias.add(categoria);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcategorias.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return categorias;
    }

    public categorias BuscarCategorias(int id_categoria) {
        Connection con = conexion.getConnection();
        categorias categoria = new categorias();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM categorias WHERE id_categoria=?");
            ps.setInt(1, id_categoria);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                categoria.setId_categoria(rs.getInt("id_categoria"));
                categoria.setCategoria(rs.getString("categoria"));
                categoria.setDescripcion_cat(rs.getString("descripcion"));
                categoria.setClose_cat(rs.getBoolean("close_cat"));
                categoria.setTipo_cat(rs.getInt("tipo_cat"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcategorias.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return categoria;
    }

    public int InsertarCategorias(categorias categoria) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO categorias (categoria, descripcion, tipo_cat) VALUES (?, ?, ?)");
            ps.setString(1, categoria.getCategoria());
            ps.setString(2, f.NoPosee(categoria.getDescripcion_cat()));
            ps.setInt(3, categoria.getTipo_cat());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOcategorias.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarCategorias(categorias categoria) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE categorias SET categoria=?, descripcion=?, tipo_cat=? WHERE id_categoria=?");
            ps.setString(1, categoria.getCategoria());
            ps.setString(2, f.NoPosee(categoria.getDescripcion_cat()));
            ps.setInt(3, categoria.getTipo_cat());
            ps.setInt(4, categoria.getId_categoria());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOcategorias.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarEstadoCategoria(int id) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE categorias SET close_cat=!close_cat WHERE id_categoria=?");
            ps.setInt(1, id);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOcategorias.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

}
