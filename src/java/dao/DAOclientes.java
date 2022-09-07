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
import modelo.clientes;

/**
 *
 * @author akio
 */
public class DAOclientes {

    funciones f = new funciones();
    DAOauditoria dao = new DAOauditoria();

    public ArrayList<clientes> ListarClientes() {
        Connection con = conexion.getConnection();
        ArrayList<clientes> cli = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM clientes cl INNER JOIN ciudades c ON cl.ciudades_id_ciudad = c.id_ciudad WHERE cl.close_cli = 0 AND id_cliente != 1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                clientes c = new clientes();
                c.setId_cliente(rs.getString("id_cliente"));
                c.setNombre_cli(rs.getString("nombre_cli"));
                c.setNdocumento_cli(rs.getString("ndocumento_cli"));
                c.setDv_cli(rs.getString("dv_cli"));
                c.setTelefono_cli(f.NoPosee(rs.getString("telefono_cli")));
                c.setCelular_cli(f.NoPosee(rs.getString("celular_cli")));
                c.setDireccion_cli(f.NoPosee(rs.getString("direccion_cli")));
                c.setObservacion_cli(f.NoPosee(rs.getString("observacion_cli")));
                c.setClose_cli(rs.getString("close_cli"));
                c.setNombre_ciu(rs.getString("nombre_ciu"));
                c.setGooglemaps_cli(rs.getString("googlemaps_cli"));
                c.setEmail_cli(rs.getString("correo_cli"));
                c.setDescuento_cli(rs.getInt("descuento_cli"));
                System.out.println(ps);
                cli.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOclientes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return cli;
    }

    public ArrayList<clientes> ListarClientesInhabilitados() {
        ArrayList<clientes> cli = new ArrayList<>();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM clientes cl INNER JOIN ciudades c ON cl.ciudades_id_ciudad = c.id_ciudad WHERE cl.close_cli = 1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                clientes c = new clientes();
                c.setId_cliente(rs.getString("id_cliente"));
                c.setNombre_cli(rs.getString("nombre_cli"));
                c.setNdocumento_cli(rs.getString("ndocumento_cli"));
                c.setDv_cli(rs.getString("dv_cli"));
                c.setTelefono_cli(f.NoPosee(rs.getString("telefono_cli")));
                c.setCelular_cli(f.NoPosee(rs.getString("celular_cli")));
                c.setDireccion_cli(f.NoPosee(rs.getString("direccion_cli")));
                c.setObservacion_cli(f.NoPosee(rs.getString("observacion_cli")));
                c.setClose_cli(rs.getString("close_cli"));
                c.setNombre_ciu(rs.getString("nombre_ciu"));
                cli.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOclientes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return cli;
    }

    public clientes BuscarClientes(String id) {
        clientes c = new clientes();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM clientes cl INNER JOIN ciudades c ON cl.ciudades_id_ciudad = c.id_ciudad WHERE cl.id_cliente = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c.setId_cliente(rs.getString("id_cliente"));
                c.setNombre_cli(rs.getString("nombre_cli"));
                c.setNdocumento_cli(rs.getString("ndocumento_cli"));
                c.setDv_cli(rs.getString("dv_cli"));
                c.setRazon_social(rs.getString("razon_social"));
                c.setRuc(rs.getString("ruc"));
                c.setRuc_dv(rs.getString("ruc_dv"));
                c.setTelefono_cli(rs.getString("telefono_cli"));
                c.setCelular_cli(rs.getString("celular_cli"));
                c.setDireccion_cli(rs.getString("direccion_cli"));
                c.setObservacion_cli(rs.getString("observacion_cli"));
                c.setClose_cli(rs.getString("close_cli"));
                c.setNombre_ciu(rs.getString("nombre_ciu"));
                c.setId_ciudad(rs.getString("id_ciudad"));
                c.setEmail_cli(rs.getString("correo_cli"));
                c.setGooglemaps_cli(rs.getString("googlemaps_cli"));
                c.setDescuento_cli(rs.getInt("descuento_cli"));
                c.setFecha_nac(f.ParseDI(rs.getString("fecha_nac")));
                c.setReferencia_cli(rs.getString("referencia_cli"));
                c.setHora_inicio_cli(rs.getString("hora_inicio_cli"));
                c.setHora_fin_cli(rs.getString("hora_fin_cli"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOclientes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return c;
    }

    public int AgregarClientes(clientes c, String id_usu) {
        int r = 0;
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("SET @usuario = ?");
            ps.setInt(1, Integer.valueOf(id_usu));
            ps.execute();
            ps = con.prepareStatement("INSERT INTO clientes(nombre_cli, ndocumento_cli, dv_cli, razon_social, ruc, ruc_dv, telefono_cli, celular_cli, direccion_cli, observacion_cli, ciudades_id_ciudad, correo_cli, googlemaps_cli, referencia_cli, descuento_cli, fecha_nac) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, c.getNombre_cli());
            ps.setString(2, c.getNdocumento_cli().isEmpty() ? "-" : c.getNdocumento_cli());
            ps.setString(3, c.getNdocumento_cli().isEmpty() ? "-" : c.getDv_cli());
            ps.setString(4, c.getRazon_social().isEmpty() ? "-" : c.getRazon_social());
            ps.setString(5, c.getRuc().isEmpty() ? "-" : c.getRuc());
            ps.setString(6, c.getRuc().isEmpty() ? "-" : c.getRuc_dv());
            ps.setString(7, f.NoPosee(c.getTelefono_cli()));
            ps.setString(8, f.NoPosee(c.getCelular_cli()));
            ps.setString(9, f.NoPosee(c.getDireccion_cli()));
            ps.setString(10, f.NoPosee(c.getObservacion_cli()));
            ps.setString(11, c.getId_ciudad());
            ps.setString(12, f.NoPosee(c.getEmail_cli()));
            ps.setString(13, f.NoPosee(c.getGooglemaps_cli()));
            ps.setString(14, f.NoPosee(c.getReferencia_cli()));
            ps.setInt(15, c.getDescuento_cli());
            ps.setString(16, c.getFecha_nac().isEmpty() ? "1900-01-01" : f.ParseDate(c.getFecha_nac()));
            r = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                c.setId_cliente(String.valueOf(rs.getLong(1)));
            }
            dao.afterInsertClientes(c, id_usu);
        } catch (SQLException ex) {
            r = ex.getErrorCode();
            Logger.getLogger(DAOclientes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int Actualizarclientes(clientes c, String id_usu) {
        int r = 0;
        Connection con = conexion.getConnection();
        clientes o = BuscarClientes(c.getId_cliente());
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE clientes SET nombre_cli = ?, ndocumento_cli = ?, dv_cli = ?, razon_social=?, ruc=?, ruc_dv=?, telefono_cli = ?, celular_cli = ?, direccion_cli = ?, observacion_cli = ?, ciudades_id_ciudad = ?, correo_cli=?, googlemaps_cli=?, referencia_cli=?, descuento_cli=?, fecha_nac=? WHERE id_cliente = ?");
            ps.setString(1, c.getNombre_cli());
            ps.setString(2, c.getNdocumento_cli().isEmpty() ? "-" : c.getNdocumento_cli());
            ps.setString(3, c.getNdocumento_cli().isEmpty() ? "-" : c.getDv_cli());
            ps.setString(4, f.NoPosee(c.getRazon_social()));
            ps.setString(5, c.getRuc().isEmpty() ? "-" : c.getRuc());
            ps.setString(6, c.getRuc().isEmpty() ? "-" : c.getRuc_dv());
            ps.setString(7, f.NoPosee(c.getTelefono_cli()));
            ps.setString(8, f.NoPosee(c.getCelular_cli()));
            ps.setString(9, f.NoPosee(c.getDireccion_cli()));
            ps.setString(10, f.NoPosee(c.getObservacion_cli()));
            ps.setString(11, c.getId_ciudad());
            ps.setString(12, f.NoPosee(c.getEmail_cli()));
            ps.setString(13, f.NoPosee(c.getGooglemaps_cli()));
            ps.setString(14, f.NoPosee(c.getReferencia_cli()));
            ps.setInt(15, c.getDescuento_cli());
            ps.setString(16, c.getFecha_nac().isEmpty() ? "1900-01-01" : f.ParseDate(c.getFecha_nac()));
            ps.setString(17, c.getId_cliente());
            r = ps.executeUpdate();
            dao.afterUpdateClientes(c, o, id_usu);
        } catch (SQLException ex) {
            Logger.getLogger(DAOclientes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int CerrarClientes(String id, String a, String id_usu) {
        int r = 0;
        Connection con = conexion.getConnection();
        clientes c = BuscarClientes(id);
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE clientes SET close_cli = ? WHERE id_cliente = ?");
            ps.setString(1, a);
            ps.setInt(2, Integer.valueOf(id));
            if (a.equals("1")) {
                dao.afterAnularClientes(c, id_usu, "inhabilito");
            } else {
                dao.afterAnularClientes(c, id_usu, "habilito");
            }
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOclientes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public ArrayList<clientes> ClientesQuery(String q) {
        Connection con = conexion.getConnection();
        ArrayList<clientes> cliente = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id_cliente, nombre_cli, ndocumento_cli, dv_cli, razon_social, ruc, ruc_dv, googlemaps_cli, direccion_cli, celular_cli, hora_inicio_cli, hora_fin_cli, referencia_cli FROM clientes WHERE (nombre_cli LIKE ? OR ndocumento_cli LIKE ?)");
            ps.setString(1, q);
            ps.setString(2, q);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                clientes c = new clientes();
                c.setId_cliente(rs.getString("id_cliente"));
                c.setNombre_cli(rs.getString("nombre_cli"));
                c.setNdocumento_cli(rs.getString("ndocumento_cli"));
                c.setDv_cli(rs.getString("dv_cli"));
                c.setHora_inicio_cli(rs.getString("hora_inicio_cli"));
                c.setHora_fin_cli(rs.getString("hora_fin_cli"));
                c.setReferencia_cli(rs.getString("referencia_cli"));
                c.setGooglemaps_cli(rs.getString("googlemaps_cli"));
                c.setDireccion_cli(rs.getString("direccion_cli"));
                c.setCelular_cli(rs.getString("celular_cli"));
                c.setRazon_social(rs.getString("razon_social"));
                c.setRuc(rs.getString("ruc"));
                c.setRuc_dv(rs.getString("ruc_dv"));
                cliente.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOproveedores.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return cliente;
    }

    public clientes ValidarClientes(String ndoc) {
        Connection con = conexion.getConnection();
        clientes c = new clientes();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM clientes WHERE ndocumento_cli = ?");
            ps.setString(1, ndoc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c.setId_cliente(rs.getString("id_cliente"));
                c.setNombre_cli(rs.getString("nombre_cli"));
                c.setNdocumento_cli(rs.getString("ndocumento_cli"));
                c.setDv_cli(rs.getString("dv_cli"));
                c.setTelefono_cli(rs.getString("telefono_cli"));
                c.setCelular_cli(rs.getString("celular_cli"));
                c.setDireccion_cli(rs.getString("direccion_cli"));
                c.setObservacion_cli(rs.getString("observacion_cli"));
                c.setClose_cli(rs.getString("close_cli"));
                c.setId_ciudad(rs.getString("id_ciudad"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOclientes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return c;
    }

    public int InsertUpdateClientes(clientes cliente, String id_usu) {
        int r = 0;
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("SET @usuario = ?");
            ps.setInt(1, Integer.valueOf(id_usu));
            ps.execute();
            ps = con.prepareStatement("SELECT * FROM clientes WHERE ndocumento_cli=?");
            ps.setString(1, cliente.getNdocumento_cli());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                ps = con.prepareStatement("INSERT INTO clientes(nombre_cli, ndocumento_cli, dv_cli, telefono_cli, celular_cli, direccion_cli, observacion_cli, ciudades_id_ciudad, correo_cli, googlemaps_cli, descuento_cli, fecha_nac) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, cliente.getNombre_cli());
                ps.setString(2, cliente.getNdocumento_cli().isEmpty() ? "-" : cliente.getNdocumento_cli());
                ps.setString(3, cliente.getNdocumento_cli().isEmpty() ? "-" : cliente.getDv_cli());
                ps.setString(4, f.NoPosee(cliente.getTelefono_cli()));
                ps.setString(5, f.NoPosee(cliente.getCelular_cli()));
                ps.setString(6, f.NoPosee(cliente.getDireccion_cli()));
                ps.setString(7, f.NoPosee(cliente.getObservacion_cli()));
                ps.setString(8, cliente.getId_ciudad() == null ? "246" : cliente.getId_ciudad());
                ps.setString(9, f.NoPosee(cliente.getEmail_cli()));
                ps.setString(10, f.NoPosee(cliente.getGooglemaps_cli()));
                ps.setInt(11, cliente.getDescuento_cli());
                ps.setString(12, cliente.getFecha_nac() == null ? "1900-01-01" : f.ParseDate(cliente.getFecha_nac()));
                ps.executeUpdate();
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    cliente.setId_cliente(String.valueOf(rs.getLong(1)));
                }
                r = Integer.valueOf(cliente.getId_cliente());
            } else {
                ps = con.prepareStatement("UPDATE clientes SET nombre_cli = ? WHERE ndocumento_cli=?");
                ps.setString(1, cliente.getNombre_cli());
                ps.setString(2, cliente.getNdocumento_cli());
                ps.executeUpdate();
                cliente.setId_cliente(rs.getString("id_cliente"));
                r = rs.getInt("id_cliente");
            }
            dao.afterInsertClientes(cliente, id_usu);
        } catch (SQLException ex) {
            r = ex.getErrorCode();
            Logger.getLogger(DAOclientes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
