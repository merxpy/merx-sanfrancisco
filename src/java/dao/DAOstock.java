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
import modelo.articulos;
import modelo.stock;

/**
 *
 * @author akio
 */
public class DAOstock {

    public ArrayList<stock> ListarStock() {
        Connection con = conexion.getConnection();
        ArrayList<stock> stock = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT *, (a.precio_compra_ar*s.cantidad_stock) AS total_stock FROM stock s, articulos a LEFT JOIN marcas m ON m.id_marca = a.marcas_id_marca LEFT JOIN etiquetas e ON e.id_etiqueta = a.etiquetas_id_etiqueta WHERE s.articulos_id_articulo = a.id_articulo and ((s.cantidad_stock>0 AND a.close_ar=1) OR a.close_ar=0) AND ((a.etiquetas_id_etiqueta != 1 AND e.nombre_e != 'GASTOS') OR a.etiquetas_id_etiqueta IS NULL)");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stock s = new stock();
                s.setId_stock(rs.getString("id_stock"));
                s.setId_articulo(rs.getString("id_articulo"));
                s.setCodigo_ar(rs.getString("codigo_ar"));
                s.setNombre_ar(rs.getString("nombre_ar"));
                s.setNombre_marca(rs.getString("nombre_marca"));
                s.setCantidad_stock(rs.getString("cantidad_stock"));
                s.setStock_minimo_ar(rs.getString("stock_minimo_ar"));
                s.setPrecio_venta_ar(rs.getString("precio_venta_ar"));
                s.setPrecio_compra_ar(rs.getString("precio_compra_ar"));
                s.setTotal_stock(rs.getInt("total_stock"));
                stock.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOstock.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.getConnection();
        }
        return stock;
    }

    public ArrayList<stock> ListarStockHabilitados() {
        Connection con = conexion.getConnection();
        ArrayList<stock> stock = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM stock s, articulos a LEFT JOIN marcas m ON m.id_marca = a.marcas_id_marca LEFT JOIN etiquetas e ON e.id_etiqueta=a.etiquetas_id_etiqueta LEFT JOIN categorias ON a.id_categoria=categorias.id_categoria WHERE s.articulos_id_articulo = a.id_articulo AND a.close_ar=0 AND (cantidad_stock > 0 OR e.id_etiqueta=1) AND (e.nombre_e!='GASTOS' OR e.id_etiqueta IS null)");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stock s = new stock();
                s.setId_stock(rs.getString("id_stock"));
                s.setId_articulo(rs.getString("id_articulo"));
                s.setCodigo_ar(rs.getString("codigo_ar"));
                s.setNombre_ar(rs.getString("nombre_ar"));
                s.setDescripcion_ar(rs.getString("descripcion_ar"));
                s.setNombre_marca(rs.getString("nombre_marca"));
                s.setCantidad_stock(rs.getString("cantidad_stock"));
                s.setStock_minimo_ar(rs.getString("stock_minimo_ar"));
                s.setPrecio_venta_ar(rs.getString("precio_venta_ar"));
                s.setPrecio_compra_ar(rs.getString("precio_compra_ar"));
                s.setId_categoria(rs.getInt("id_categoria"));
                s.setCategoria(rs.getString("categoria"));
                s.setIva_ar(rs.getString("iva_ar"));
                stock.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOstock.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.getConnection();
        }
        return stock;
    }

    public ArrayList<stock> ListarStockxCategoria(int id_categoria) {
        Connection con = conexion.getConnection();
        ArrayList<stock> stock = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM stock s, articulos a LEFT JOIN marcas m ON m.id_marca = a.marcas_id_marca LEFT JOIN etiquetas e ON e.id_etiqueta=a.etiquetas_id_etiqueta WHERE s.articulos_id_articulo = a.id_articulo AND a.close_ar=0 AND (cantidad_stock > 0 OR e.id_etiqueta=1) AND (e.nombre_e!='GASTOS' OR e.id_etiqueta IS null) AND a.id_categoria=?");
            ps.setInt(1, id_categoria);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stock s = new stock();
                s.setId_stock(rs.getString("id_stock"));
                s.setId_articulo(rs.getString("id_articulo"));
                s.setCodigo_ar(rs.getString("codigo_ar"));
                s.setNombre_ar(rs.getString("nombre_ar"));
                s.setNombre_marca(rs.getString("nombre_marca"));
                s.setCantidad_stock(rs.getString("cantidad_stock"));
                s.setStock_minimo_ar(rs.getString("stock_minimo_ar"));
                s.setPrecio_venta_ar(rs.getString("precio_venta_ar"));
                s.setPrecio_compra_ar(rs.getString("precio_compra_ar"));
                s.setIva_ar(rs.getString("iva_ar"));
                stock.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOstock.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.getConnection();
        }
        return stock;
    }

    public ArrayList<stock> ListarStockGastos() {
        Connection con = conexion.getConnection();
        ArrayList<stock> stock = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM stock s, articulos a LEFT JOIN marcas m ON m.id_marca = a.marcas_id_marca LEFT JOIN etiquetas e ON e.id_etiqueta=a.etiquetas_id_etiqueta WHERE s.articulos_id_articulo = a.id_articulo AND a.close_ar=0 AND e.nombre_e='GASTOS'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stock s = new stock();
                s.setId_stock(rs.getString("id_stock"));
                s.setId_articulo(rs.getString("id_articulo"));
                s.setCodigo_ar(rs.getString("codigo_ar"));
                s.setNombre_ar(rs.getString("nombre_ar"));
                s.setNombre_marca(rs.getString("nombre_marca"));
                s.setCantidad_stock(rs.getString("cantidad_stock"));
                s.setStock_minimo_ar(rs.getString("stock_minimo_ar"));
                s.setPrecio_venta_ar(rs.getString("precio_venta_ar"));
                s.setPrecio_compra_ar(rs.getString("precio_compra_ar"));
                stock.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOstock.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.getConnection();
        }
        return stock;
    }

    public stock BuscarStock(String id) {
        Connection con = conexion.getConnection();
        stock s = new stock();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM stock s INNER JOIN articulos a ON s.articulos_id_articulo = a.id_articulo WHERE id_articulo = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                s.setId_stock(rs.getString("id_stock"));
                s.setId_articulo(rs.getString("id_articulo"));
                s.setNombre_ar(rs.getString("nombre_ar"));
                s.setCantidad_stock(rs.getString("cantidad_stock"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOstock.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return s;
    }

    public void StockInicial(String id, String cant) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE stock SET cantidad_stock = ? WHERE articulos_id_articulo = ?");
            ps.setString(1, cant);
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOstock.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void AgregarArticuloStock(articulos a) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO stock (articulos_id_articulo) VALUES (?)");
            ps.setString(1, a.getId_articulo());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOstock.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }

    }

    public int ObtenerTotalStock() {
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        int r = 0;
        try {
            ps = con.prepareStatement("SELECT SUM(cantidad_stock) AS cantidad_stock FROM stock");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                r = rs.getInt("cantidad_stock");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOstock.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int VerificarStockxMarca(String estado, int id_marca) {
        Connection con = conexion.getConnection();
        int r = 0;
        String sql = "SELECT *, (a.precio_venta_ar*s.cantidad_stock) AS total_stock, IFNULL((SELECT ROUND(AVG(precio),0) FROM `detalle_compra` WHERE articulos_id_articulo=a.id_articulo) ,0) AS PMP FROM stock s, articulos a LEFT JOIN marcas m ON m.id_marca = a.marcas_id_marca LEFT JOIN etiquetas e ON e.id_etiqueta = a.etiquetas_id_etiqueta WHERE s.articulos_id_articulo = a.id_articulo and ((s.cantidad_stock>0 AND a.close_ar=1) OR a.close_ar=0) AND ((a.etiquetas_id_etiqueta != 1 AND e.nombre_e != 'GASTOS') OR a.etiquetas_id_etiqueta IS NULL)";
        if (id_marca != 0) {
            sql += " AND id_marca = " + id_marca;
        }
        if (estado.equalsIgnoreCase("D")) {
            sql += " AND (s.cantidad_stock>=a.stock_minimo_ar AND s.cantidad_stock>0)";
        } else if (estado.equalsIgnoreCase("A")) {
            sql += " AND s.cantidad_stock<=0";
        } else if (estado.equalsIgnoreCase("M")) {
            sql += " AND s.cantidad_stock=a.stock_minimo_ar";
        }
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                r = 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOstock.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
