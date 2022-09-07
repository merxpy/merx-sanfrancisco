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
import modelo.articulos;

/**
 *
 * @author akio
 */
public class DAOarticulos {

    funciones f = new funciones();
    DAOauditoria dao = new DAOauditoria();

    public int verificarAccion(articulos ar, String id_usu) {
        if (ar.getId_articulo() == null || ar.getId_articulo().equalsIgnoreCase("0")) {
            return AgregarArticulos(ar, id_usu);
        } else {
            return ActualizarArticulos(ar, id_usu);
        }
    }

    public ArrayList<articulos> ListarArticulos() {
        Connection con = conexion.getConnection();
        ArrayList<articulos> art = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM articulos a INNER JOIN unidades u ON a.unidades_id_unidad = u.id_unidad LEFT JOIN marcas m ON m.id_marca = a.marcas_id_marca LEFT JOIN etiquetas e ON e.id_etiqueta = a.etiquetas_id_etiqueta LEFT JOIN categorias ON categorias.id_categoria=a.id_categoria WHERE close_ar = 0");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                articulos a = new articulos();
                a.setId_articulo(rs.getString("id_articulo"));
                a.setNombre_ar(rs.getString("nombre_ar"));
                a.setBarcode_ar(rs.getString("barcode_ar"));
                a.setDescripcion_ar(rs.getString("descripcion_ar"));
                a.setCodigo_ar(rs.getString("codigo_ar"));
                a.setPrecio_venta_ar(rs.getString("precio_venta_ar"));
                a.setPrecio_compra_ar(rs.getString("precio_compra_ar"));
                a.setUtilidad(rs.getString("utilidad"));
                a.setIva_ar(rs.getString("iva_ar"));
                a.setClose_ar(rs.getString("close_ar"));
                a.setStock_minimo_ar(rs.getString("stock_minimo_ar"));
                a.setNombre_u(rs.getString("nombre_u"));
                a.setNombre_marca(rs.getString("nombre_marca"));
                a.setNombre_e(rs.getString("nombre_e"));
                a.setCategoria(rs.getString("categoria"));
                a.setId_categoria(rs.getInt("id_categoria"));
                art.add(a);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOarticulos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return art;
    }

    public ArrayList<articulos> ListarArticulosInhabilitados() {
        Connection con = conexion.getConnection();
        ArrayList<articulos> art = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM articulos a INNER JOIN unidades u ON a.unidades_id_unidad = u.id_unidad LEFT JOIN marcas m ON m.id_marca = a.marcas_id_marca LEFT JOIN etiquetas e ON e.id_etiqueta = a.etiquetas_id_etiqueta LEFT JOIN categorias ON categorias.id_categoria=a.id_categoria WHERE close_ar = 1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                articulos a = new articulos();
                a.setId_articulo(rs.getString("id_articulo"));
                a.setNombre_ar(rs.getString("nombre_ar"));
                a.setBarcode_ar(rs.getString("barcode_ar"));
                a.setDescripcion_ar(rs.getString("descripcion_ar"));
                a.setCodigo_ar(rs.getString("codigo_ar"));
                a.setPrecio_venta_ar(rs.getString("precio_venta_ar"));
                a.setPrecio_compra_ar(rs.getString("precio_compra_ar"));
                a.setUtilidad(rs.getString("utilidad"));
                a.setMonto_utilidad_ar(rs.getString("monto_utilidad_ar"));
                a.setPrecio_neto_ar(rs.getString("precio_neto_ar"));
                a.setPrecio_utilidad_ar(rs.getString("precio_utilidad_ar"));
                a.setPrecio_venta_ar(rs.getString("precio_venta_ar"));
                a.setIva_venta_ar(rs.getString("iva_venta_ar"));
                a.setIva_ar(rs.getString("iva_ar"));
                a.setClose_ar(rs.getString("close_ar"));
                a.setStock_minimo_ar(rs.getString("stock_minimo_ar"));
                a.setAbreviatura_u(rs.getString("abreviatura_u"));
                a.setNombre_u(rs.getString("nombre_u"));
                a.setNombre_marca(rs.getString("nombre_marca"));
                a.setNombre_e(rs.getString("nombre_e"));
                a.setCategoria(rs.getString("categoria"));
                a.setId_categoria(rs.getInt("id_categoria"));
                art.add(a);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOarticulos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return art;
    }

    public articulos BuscarArticulos(String id) {
        Connection con = conexion.getConnection();
        articulos a = new articulos();
        try {
            PreparedStatement ps = con.prepareStatement(String.format("SELECT * FROM articulos a INNER JOIN unidades u ON a.unidades_id_unidad = u.id_unidad LEFT JOIN marcas m ON m.id_marca = a.marcas_id_marca LEFT JOIN etiquetas e ON e.id_etiqueta = a.etiquetas_id_etiqueta LEFT JOIN categorias ON categorias.id_categoria=a.id_categoria WHERE id_articulo = %s", id));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                a.setId_articulo(rs.getString("id_articulo"));
                a.setNombre_ar(rs.getString("nombre_ar"));
                a.setBarcode_ar(rs.getString("barcode_ar"));
                a.setDescripcion_ar(rs.getString("descripcion_ar"));
                a.setCodigo_ar(rs.getString("codigo_ar"));
                a.setPrecio_venta_ar(rs.getString("precio_venta_ar"));
                a.setPrecio_compra_ar(rs.getString("precio_compra_ar"));
                a.setUtilidad(rs.getString("utilidad"));
                a.setMonto_utilidad_ar(rs.getString("monto_utilidad_ar"));
                a.setPrecio_neto_ar(rs.getString("precio_neto_ar"));
                a.setPrecio_utilidad_ar(rs.getString("precio_utilidad_ar"));
                a.setPrecio_venta_ar(rs.getString("precio_venta_ar"));
                a.setIva_venta_ar(rs.getString("iva_venta_ar"));
                a.setIva_ar(rs.getString("iva_ar"));
                a.setClose_ar(rs.getString("close_ar"));
                a.setStock_minimo_ar(rs.getString("stock_minimo_ar"));
                a.setAbreviatura_u(rs.getString("abreviatura_u"));
                a.setNombre_u(rs.getString("nombre_u"));
                a.setNombre_marca(rs.getString("nombre_marca"));
                a.setNombre_e(rs.getString("nombre_e"));
                a.setCategoria(rs.getString("categoria"));
                a.setId_categoria(rs.getInt("id_categoria"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOarticulos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }

        return a;
    }

    public int AgregarArticulos(articulos a, String id_usu) {
        int r = 0;
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO articulos(codigo_ar, nombre_ar, barcode_ar, descripcion_ar, iva_ar, precio_venta_ar, precio_compra_ar, utilidad, monto_utilidad_ar, precio_neto_ar, precio_utilidad_ar, iva_venta_ar, stock_minimo_ar, unidades_id_unidad, marcas_id_marca, etiquetas_id_etiqueta, id_categoria) VALUES (?, UPPER(?), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, a.getCodigo_ar());
            ps.setString(2, a.getNombre_ar());
            ps.setString(3, f.IsInt(a.getBarcode_ar()));
            ps.setString(4, f.NoPosee(a.getDescripcion_ar()));
            ps.setString(5, f.IsInt(a.getIva_ar()));
            ps.setString(6, f.IsInt(a.getPrecio_venta_ar()));
            ps.setString(7, f.IsInt(a.getPrecio_compra_ar()));
            ps.setString(8, f.IsInt(a.getUtilidad()));
            ps.setString(9, f.IsInt(a.getMonto_utilidad_ar()));
            ps.setString(10, f.IsInt(a.getPrecio_neto_ar()));
            ps.setString(11, f.IsInt(a.getPrecio_utilidad_ar()));
            ps.setString(12, f.IsInt(a.getIva_venta_ar()));
            ps.setString(13, f.IsInt(a.getStock_minimo_ar()));
            ps.setString(14, a.getId_unidad() != null ? a.getId_unidad() : "12");
            ps.setString(15, f.VeriId(a.getId_marca()));
            ps.setString(16, f.VeriId(a.getId_etiqueta()));
            ps.setString(17, a.getId_categoria() != 0 ? String.valueOf(a.getId_categoria()) : null);
            r = ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                a.setId_articulo(String.valueOf(rs.getLong(1)));
            }

            dao.afterInsertArticulos(a, id_usu);
        } catch (SQLException ex) {
            Logger.getLogger(DAOarticulos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        if (r > 0) {
            return Integer.valueOf(a.getId_articulo());
        } else {
            return r;
        }
    }

    public int ActualizarArticulos(articulos a, String id_usu) {
        int r = 0;
        Connection con = conexion.getConnection();
        articulos o = BuscarArticulos(a.getId_articulo());
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE articulos SET codigo_ar = ?, nombre_ar = UPPER(?), barcode_ar = ?, descripcion_ar = ?, iva_ar = ?, precio_venta_ar = ?, precio_compra_ar = ?, utilidad = ?, monto_utilidad_ar = ?, precio_neto_ar = ?, precio_utilidad_ar = ?, iva_venta_ar = ?, stock_minimo_ar = ?, unidades_id_unidad = ?, marcas_id_marca = ?, etiquetas_id_etiqueta = ?,"
                    + " id_categoria=? WHERE id_articulo = ?");
            ps.setString(1, a.getCodigo_ar());
            ps.setString(2, a.getNombre_ar());
            ps.setString(3, f.IsInt(a.getBarcode_ar()));
            ps.setString(4, f.NoPosee(a.getDescripcion_ar()));
            ps.setString(5, a.getIva_ar());
            ps.setString(6, a.getPrecio_venta_ar());
            ps.setString(7, f.IsInt(a.getPrecio_compra_ar()));
            ps.setString(8, f.IsInt(a.getUtilidad()));
            ps.setString(9, a.getMonto_utilidad_ar());
            ps.setString(10, a.getPrecio_neto_ar());
            ps.setString(11, a.getPrecio_utilidad_ar());
            ps.setString(12, a.getIva_venta_ar());
            ps.setString(13, f.IsInt(a.getStock_minimo_ar()));
            ps.setString(14, a.getId_unidad());
            ps.setString(15, f.VeriId(a.getId_marca()));
            ps.setString(16, f.VeriId(a.getId_etiqueta()));
            ps.setString(17, a.getId_categoria() != 0 ? String.valueOf(a.getId_categoria()) : null);
            ps.setString(18, a.getId_articulo());
            r = ps.executeUpdate();
            if (r > 0) {
                r = Integer.valueOf(a.getId_articulo());
            }
            dao.afterUpdateArticulos(a, o, id_usu);
        } catch (SQLException ex) {
            Logger.getLogger(DAOarticulos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int CerrarArticulo(String id, String a, String id_usu) {
        int r = 0;
        Connection con = conexion.getConnection();
        articulos ar = BuscarArticulos(id);
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE articulos SET close_ar = ? WHERE id_articulo = ?");
            ps.setString(1, a);
            ps.setString(2, id);
            r = ps.executeUpdate();
            if (a.equals("1")) {
                dao.afterAnularArticulos(ar, id_usu, "inhabilito");
            } else {
                dao.afterAnularArticulos(ar, id_usu, "habilito");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOarticulos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int GetLastId() {
        int l = 110001;
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id_articulo FROM articulos ORDER BY id_articulo desc LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                l += rs.getInt("id_articulo");
                verificarCodigoA(l);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOarticulos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.getConnection();
        }
        return l;
    }

    public int verificarCodigoA(int c) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT codigo_ar FROM articulos WHERE codigo_ar = ? and close_ar = 0");
            ps.setInt(1, c);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c += 1;
                verificarCodigoA(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOarticulos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return c;
    }

    public articulos verificarCodigo(String cod) {
        articulos a = new articulos();
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        try {
            if (f.esNumero(cod)) {
                ps = con.prepareStatement("SELECT articulos.*, stock.cantidad_stock FROM articulos, stock WHERE (codigo_ar = " + cod + " OR barcode_ar = " + cod + " OR id_articulo = " + cod + ") AND close_ar = 0 AND stock.articulos_id_articulo=articulos.id_articulo ");
            } else {
                ps = con.prepareStatement("SELECT articulos.*, stock.cantidad_stock FROM articulos, stock WHERE codigo_ar = '" + cod + "' AND close_ar = 0 AND stock.articulos_id_articulo=articulos.id_articulo");
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                a.setId_articulo(rs.getString("id_articulo"));
                a.setNombre_ar(rs.getString("nombre_ar"));
                a.setBarcode_ar(rs.getString("barcode_ar"));
                a.setDescripcion_ar(rs.getString("descripcion_ar"));
                a.setCodigo_ar(rs.getString("codigo_ar"));
                a.setPrecio_venta_ar(rs.getString("precio_venta_ar"));
                a.setPrecio_compra_ar(rs.getString("precio_compra_ar"));
                a.setUtilidad(rs.getString("utilidad"));
                a.setMonto_utilidad_ar(rs.getString("monto_utilidad_ar"));
                a.setPrecio_neto_ar(rs.getString("precio_neto_ar"));
                a.setPrecio_utilidad_ar(rs.getString("precio_utilidad_ar"));
                a.setPrecio_venta_ar(rs.getString("precio_venta_ar"));
                a.setIva_venta_ar(rs.getString("iva_venta_ar"));
                a.setIva_ar(rs.getString("iva_ar"));
                a.setClose_ar(rs.getString("close_ar"));
                a.setStock_minimo_ar(rs.getString("stock_minimo_ar"));
                a.setId_unidad(rs.getString("unidades_id_unidad"));
                a.setId_marca(rs.getString("marcas_id_marca"));
                a.setId_etiqueta(rs.getString("etiquetas_id_etiqueta"));
                a.setStock_inicial(rs.getString("cantidad_stock"));
                a.setId_categoria(rs.getInt("id_categoria"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOarticulos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return a;
    }

    public int EliminarArticulos(int id) {
        int r = 0;
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM articulos WHERE id_articulo=?");
            ps.setInt(1, id);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOarticulos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
