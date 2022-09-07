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
import modelo.auditoria;
import modelo.clientes;
import modelo.compras;
import modelo.ordendecompra;
import modelo.proveedores;
import modelo.usuarios;
import modelo.ventas;

/**
 *
 * @author akio
 */
public class DAOauditoria {

    funciones f = new funciones();

    public ArrayList<auditoria> ListarAuditoria(String tabla_au) {
        ArrayList<auditoria> aud = new ArrayList<>();
        Connection con = conexion.getConnection();
        
        try {
            PreparedStatement ps = con.prepareStatement(String.format("SELECT * FROM auditoria a INNER JOIN usuarios u ON a.usuario_au = u.id_usuario INNER JOIN empleados e ON e.id_empleado = u.empleados_id_empleado WHERE a.tabla_au = '%s'", tabla_au));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                auditoria a = new auditoria();
                a.setId_auditoria(rs.getString("id_auditoria"));
                a.setId_auditado(rs.getString("id_auditado"));
                a.setFecha_hora_au(f.ParseDHI(rs.getString("fecha_hora_au")));
                a.setDetalle_au(rs.getString("detalle_au"));
                a.setTabla_au(rs.getString("tabla_au"));
                a.setAlias_usu(rs.getString("alias_usu"));
                a.setNombre_emp(rs.getString("nombre_emp") + " " + rs.getString("apellido_emp"));
                aud.add(a);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return aud;
    }

    public void afterInsertArticulos(articulos a, String id_usu) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setString(1, a.getId_articulo());
            ps.setString(2, "articulos");
            ps.setString(3, "Se agregó el artículo " + a.getNombre_ar() + " con código N° " + a.getCodigo_ar());
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void afterUpdateArticulos(articulos n, articulos o, String id_usu) {
        Connection con = conexion.getConnection();
        String detalle = "Se actualizó el artículo " + n.getNombre_ar() + " con el código N° " + n.getCodigo_ar() + ": ";
        if (!n.getCodigo_ar().equals(o.getCodigo_ar())) {
            detalle += "el código N° " + o.getCodigo_ar() + " por " + n.getCodigo_ar() + " ";
        }

        if (!n.getBarcode_ar().equals(o.getBarcode_ar())) {
            detalle += "el código de barras " + o.getBarcode_ar() + " por " + f.IsInt(n.getBarcode_ar()) + " ";
        }

        if (!n.getNombre_ar().equals(o.getNombre_ar())) {
            detalle += "el nombre " + o.getNombre_ar() + " por " + n.getNombre_ar() + " ";
        }

        if (!n.getDescripcion_ar().equals(o.getDescripcion_ar())) {
            detalle += "la descripción " + o.getDescripcion_ar() + " por " + f.NoPosee(n.getDescripcion_ar()) + " ";
        }

        if (!n.getStock_minimo_ar().equals(o.getStock_minimo_ar())) {
            detalle += "el stock mínimo " + o.getStock_minimo_ar() + " por " + f.IsInt(n.getStock_minimo_ar()) + " ";
        }

        if (!n.getPrecio_venta_ar().equals(o.getPrecio_venta_ar())) {
            detalle += "el precio de venta " + o.getPrecio_venta_ar() + " por " + n.getPrecio_venta_ar() + " ";
        }

        if (!n.getPrecio_compra_ar().equals(n.getPrecio_compra_ar())) {
            detalle += "el precio de compra " + o.getPrecio_compra_ar() + " por " + f.IsInt(n.getPrecio_compra_ar()) + " ";
        }

        if (!n.getIva_ar().equals(o.getIva_ar())) {
            detalle += "el iva " + o.getIva_ar() + " por " + n.getIva_ar() + " ";
        }

        if (!n.getId_unidad().equals(o.getId_unidad())) {
            detalle += "la unidad " + o.getId_unidad() + " por " + n.getId_unidad() + " ";
        }

        if (!n.getId_marca().equals(o.getId_marca())) {
            detalle += "la marca " + o.getId_marca() + " por " + f.VeriId(n.getId_marca()) + " ";
        }

        if (!n.getId_etiqueta().equals(o.getId_etiqueta())) {
            detalle += "la etiqueta " + o.getId_etiqueta() + " por " + f.VeriId(n.getId_etiqueta()) + " ";
        }

        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setString(1, n.getId_articulo());
            ps.setString(2, "articulos");
            ps.setString(3, detalle);
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void afterInsertClientes(clientes c, String id_usu) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setString(1, c.getId_cliente());
            ps.setString(2, "clientes");
            ps.setString(3, "Se agregó el cliente " + c.getNombre_cli() + " con RUC N° " + c.getNdocumento_cli() + "-" + c.getDv_cli());
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void afterUpdateClientes(clientes n, clientes o, String id_usu) {
        Connection con = conexion.getConnection();
        String detalle = "Se actualizó el cliente " + n.getNombre_cli() + " con RUC N° " + n.getNdocumento_cli() + "-" + n.getDv_cli() + ": ";
        if (!n.getNombre_cli().equals(o.getNombre_cli())) {
            detalle += "el nombre " + o.getNombre_cli() + " por " + n.getNombre_cli() + " ";
        }

        if (!n.getNdocumento_cli().equals(o.getNdocumento_cli())) {
            detalle += "el RUC " + o.getNdocumento_cli() + "-" + o.getDv_cli() + " por " + n.getNdocumento_cli() + "-" + n.getDv_cli() + " ";
        }

        if (!n.getTelefono_cli().equals(o.getTelefono_cli())) {
            detalle += "el número de teléfono " + o.getTelefono_cli() + " por " + f.NoPosee(n.getTelefono_cli()) + " ";
        }

        if (!n.getCelular_cli().equals(o.getCelular_cli())) {
            detalle += "el número de celular " + o.getCelular_cli() + " por " + f.NoPosee(n.getCelular_cli()) + " ";
        }

        if (!n.getDireccion_cli().equals(o.getDireccion_cli())) {
            detalle += "la dirección " + o.getDireccion_cli() + " por " + f.NoPosee(n.getDireccion_cli()) + " ";
        }

        if (!n.getObservacion_cli().equals(o.getObservacion_cli())) {
            detalle += "la observación " + o.getObservacion_cli() + " por " + f.NoPosee(n.getObservacion_cli()) + " ";
        }

        if (!n.getId_ciudad().equals(o.getId_ciudad())) {
            detalle += "la ciudad " + o.getId_ciudad() + " por " + n.getId_ciudad() + " ";
        }

        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setString(1, n.getId_cliente());
            ps.setString(2, "clientes");
            ps.setString(3, detalle);
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void afterInsertCompras(compras c, String id_usu) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setInt(1, c.getId_compra());
            ps.setString(2, "compras");
            ps.setString(3, "Se realizó la compra con factura N°: " + c.getFactura_compra() + " el dia " + c.getFecha_compra() + " con monto: " + c.getTotal_compra());
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void afterUpdateCompras(compras n, compras o, String id_usu) {
        Connection con = conexion.getConnection();
        String detalle = "Se actualizó la compra con factura N° " + n.getFactura_compra() + ": ";
        if (!n.getId_proveedor().equals(o.getId_proveedor())) {
            detalle += "el proveedor " + o.getId_proveedor() + " por " + n.getId_proveedor() + " ";
        }

        if (!n.getFecha_compra().equals(o.getFecha_compra())) {
            detalle += "la fecha " + o.getFecha_compra() + " por " + n.getFecha_compra() + " ";
        }

        if (!n.getFactura_compra().equals(o.getFactura_compra())) {
            detalle += "el número de factura " + o.getFactura_compra() + " por " + n.getFactura_compra() + " ";
        }

        if (!n.getTotal_compra().equals(o.getTotal_compra())) {
            detalle += "el total compra " + o.getTotal_compra() + " por " + n.getTotal_compra() + " ";
        }

        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setInt(1, n.getId_compra());
            ps.setString(2, "compras");
            ps.setString(3, detalle);
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void afterInsertOrdendeCompra(ordendecompra oc, String id_usu) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setString(1, oc.getId_orden_compra());
            ps.setString(2, "ordendecompra");
            ps.setString(3, "Se agregó la orden N° " + oc.getId_orden_compra() + " con el monto " + oc.getTotal_ord());
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void afterUpdateOrdendeCompra(ordendecompra n, ordendecompra o, String id_usu) {
        DAOproveedores dpro = new DAOproveedores();
        Connection con = conexion.getConnection();
        String detalle = "Se actualizó la orden N° " + n.getId_orden_compra() + " con el monto " + n.getTotal_ord() + " ";
        if (!n.getId_proveedor().equals(o.getId_proveedor())) {
            proveedores pn = dpro.BuscarProveedores(n.getId_proveedor());
            proveedores po = dpro.BuscarProveedores(o.getId_proveedor());
            detalle += "el proveedor " + po.getNombre_pro() + " con RUC " + po.getNdocumento_pro() + "-" + po.getDv_pro() + " por " + pn.getNombre_pro() + " con RUC " + pn.getNdocumento_pro() + "-" + pn.getDv_pro() + " ";
        }

        if (!n.getTotal_ord().equals(o.getTotal_ord())) {
            detalle += "el total " + o.getTotal_ord() + " por " + n.getTotal_ord() + " ";
        }

        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setString(1, n.getId_orden_compra());
            ps.setString(2, "ordendecompra");
            ps.setString(3, detalle);
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void afterInsertProveedores(proveedores p, String id_usu) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setString(1, p.getId_proveedor());
            ps.setString(2, "proveedores");
            ps.setString(3, "Se agregó el proveedor " + p.getNombre_pro() + " con el RUC " + p.getNdocumento_pro() + "-" + p.getDv_pro());
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void afterUpdateProveedores(proveedores n, proveedores o, String id_usu) {
        Connection con = conexion.getConnection();
        String detalle = "Se actualizó el proveedor " + n.getNombre_pro() + " con el RUC " + n.getNdocumento_pro() + "-" + n.getDv_pro() + " ";
        if (!n.getNombre_pro().equals(o.getNombre_pro())) {
            detalle += "el nombre " + o.getNombre_pro() + " por " + n.getNombre_pro() + " ";
        }

        if (!n.getNdocumento_pro().equals(o.getNdocumento_pro())) {
            detalle += "el RUC " + o.getNdocumento_pro() + "-" + o.getDv_pro() + " por " + n.getNdocumento_pro() + "-" + n.getDv_pro() + " ";
        }

        if (!n.getRepresentante_legal_pro().equals(o.getRepresentante_legal_pro())) {
            detalle += "el representante legal " + o.getRepresentante_legal_pro() + " por " + n.getRepresentante_legal_pro() + " ";
        }

        if (!n.getDireccion_pro().equals(o.getDireccion_pro())) {
            detalle += "la dirección " + o.getDireccion_pro() + " por " + n.getDireccion_pro() + " ";
        }

        if (!n.getTelefono_pro().equals(o.getTelefono_pro())) {
            detalle += "el teléfono " + o.getTelefono_pro() + " por " + n.getTelefono_pro() + " ";
        }

        if (!n.getCelular_pro().equals(o.getCelular_pro())) {
            detalle += "el celular " + o.getCelular_pro() + " por " + n.getCelular_pro() + " ";
        }

        if (!n.getCorreo_pro().equals(o.getCorreo_pro())) {
            detalle += "el correo " + o.getCorreo_pro() + " por " + n.getCorreo_pro() + " ";
        }

        if (!n.getObservacion_pro().equals(o.getObservacion_pro())) {
            detalle += "la observación " + o.getObservacion_pro() + " por " + n.getObservacion_pro();
        }
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setString(1, n.getId_proveedor());
            ps.setString(2, "proveedores");
            ps.setString(3, detalle);
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void afterAnularArticulos(articulos a, String id_usu, String accion) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setString(1, a.getId_articulo());
            ps.setString(2, "articulos");
            ps.setString(3, "Se " + accion + " el artículo " + a.getNombre_ar() + " con el código N° " + a.getCodigo_ar());
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void afterAnularClientes(clientes c, String id_usu, String accion) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setString(1, c.getId_cliente());
            ps.setString(2, "clientes");
            ps.setString(3, "Se " + accion + " el cliente " + c.getNombre_cli() + " con el RUC N° " + c.getNdocumento_cli() + "-" + c.getDv_cli());
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }

    }

    public void afterAnularCompras(compras c, String id_usu, String accion) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setInt(1, c.getId_compra());
            ps.setString(2, "compras");
            ps.setString(3, "Se " + accion + " la compra con factura N° " + c.getFactura_compra());
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void afterAnularProveedores(proveedores p, String id_usu, String accion) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setString(1, p.getId_proveedor());
            ps.setString(2, "proveedores");
            ps.setString(3, "Se " + accion + " el proveedor " + p.getNombre_pro() + " con RUC N° " + p.getNdocumento_pro() + "-" + p.getDv_pro());
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void afterInsertUsuarios(usuarios u, String id_usu) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setString(1, u.getId_usuario());
            ps.setString(2, "usuarios");
            ps.setString(3, "Se agregó el usuario " + u.getAlias_usu() + " con el nombre " + u.getNombre_emp() + " " + u.getApellido_emp());
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void afterUpdateUsuarios(usuarios n, usuarios o, String id_usu) {
        Connection con = conexion.getConnection();
        String detalle = "Se actualizó el usuario con el alias " + n.getAlias_usu() + ": ";

        if (!n.getAlias_usu().equals(o.getAlias_usu())) {
            detalle += "el alias " + o.getAlias_usu() + " por " + n.getAlias_usu() + " ";
        }

        if (!n.getNombre_emp().equals(o.getNombre_emp()) || !n.getApellido_emp().equals(o.getApellido_emp())) {
            detalle += "el nombre " + o.getNombre_emp() + " " + o.getApellido_emp() + " por " + n.getNombre_emp() + " " + n.getApellido_emp();
        }
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setString(1, n.getId_usuario());
            ps.setString(2, "usuarios");
            ps.setString(3, detalle);
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void afterAnularUsuarios(usuarios u, String id_usu, String accion) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria (id_auditado, tabla_au, detalle_au, usuario_au) VALUES  (?, ?, ?, ?)");
            ps.setString(1, u.getId_usuario());
            ps.setString(2, "usuarios");
            ps.setString(3, "Se " + accion + " el usuario " + u.getNombre_emp() + " " + u.getApellido_emp() + " con nombre de usuario " + u.getAlias_usu());
            ps.setString(4, id_usu);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public void beforeReimprimirVenta(ventas v) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO auditoria(id_auditado, tabla_au, detalle_au, usuario_au) VALUES (?, ?, ?, ?)");
            ps.setString(1, v.getId_venta());
            ps.setString(2, "ventas");
            ps.setString(3, "SE REIMPRIMIO LA VENTA N° " + v.getNfactura_venta() + " HECHA PARA EL CLIENTE " + v.getNombre_cli() + "CON R.U.C. N° " + v.getNdocumento_cli() + "-" + v.getDv_cli() + " CON EL MONTO DE " + v.getTotal_venta());
            ps.setInt(4, Integer.valueOf(v.getId_usuario()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOauditoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }
}
