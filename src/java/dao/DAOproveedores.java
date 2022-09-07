/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexion.conexion;
import extras.funciones;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.proveedores;

/**
 *
 * @author akio
 */
public class DAOproveedores {

    funciones f = new funciones();
    DAOauditoria dao = new DAOauditoria();

    public ArrayList<proveedores> ListarProveedores() {
        Connection con = conexion.getConnection();
        ArrayList<proveedores> pro = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM proveedores p INNER JOIN ciudades c ON c.id_ciudad = p.ciudades_id_ciudad WHERE p.close_pro = 0");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                proveedores p = new proveedores();
                p.setId_proveedor(rs.getString("id_proveedor"));
                p.setNombre_pro(rs.getString("nombre_pro"));
                p.setRepresentante_legal_pro(rs.getString("representante_legal_pro"));
                p.setNdocumento_pro(rs.getString("ndocumento_pro"));
                p.setDv_pro(rs.getString("dv_pro"));
                p.setDireccion_pro(rs.getString("direccion_pro"));
                p.setTelefono_pro(rs.getString("telefono_pro"));
                p.setCelular_pro(rs.getString("celular_pro"));
                p.setCorreo_pro(rs.getString("correo_pro"));
                p.setObservacion_pro(rs.getString("observacion_pro"));
                p.setClose_pro(rs.getString("close_pro"));
                p.setNombre_ciu(rs.getString("nombre_ciu"));
                pro.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOproveedores.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pro;
    }

    public proveedores BuscarProveedores(String id) {
        Connection con = conexion.getConnection();
        proveedores p = new proveedores();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM proveedores p INNER JOIN ciudades c ON c.id_ciudad = p.ciudades_id_ciudad WHERE id_proveedor = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p.setId_proveedor(rs.getString("id_proveedor"));
                p.setNombre_pro(rs.getString("nombre_pro"));
                p.setRepresentante_legal_pro(rs.getString("representante_legal_pro"));
                p.setNdocumento_pro(rs.getString("ndocumento_pro"));
                p.setDv_pro(rs.getString("dv_pro"));
                p.setDireccion_pro(rs.getString("direccion_pro"));
                p.setTelefono_pro(rs.getString("telefono_pro"));
                p.setCelular_pro(rs.getString("celular_pro"));
                p.setCorreo_pro(rs.getString("correo_pro"));
                p.setObservacion_pro(rs.getString("observacion_pro"));
                p.setClose_pro(rs.getString("close_pro"));
                p.setNombre_ciu(rs.getString("nombre_ciu"));
                p.setId_ciudad(rs.getString("id_ciudad"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOproveedores.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return p;
    }

    public ArrayList<proveedores> ProveedoresQuery(String q) {
        Connection con = conexion.getConnection();
        ArrayList<proveedores> proveedor = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id_proveedor, nombre_pro, ndocumento_pro, dv_pro FROM proveedores WHERE nombre_pro LIKE ? OR ndocumento_pro LIKE ?");
            ps.setString(1, q);
            ps.setString(2, q);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                proveedores p = new proveedores();
                p.setId_proveedor(rs.getString("id_proveedor"));
                p.setNombre_pro(rs.getString("nombre_pro"));
                p.setNdocumento_pro(rs.getString("ndocumento_pro"));
                p.setDv_pro(rs.getString("dv_pro"));
                proveedor.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOproveedores.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return proveedor;
    }

    public String AgregarProveedores(proveedores p, String id_usu) {
        Connection con = conexion.getConnection();
        String r = null;
        try {
            CallableStatement ps = con.prepareCall("{call proveedores(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            ps.setString(1, p.getNombre_pro());
            ps.setString(2, f.NoPosee(p.getRepresentante_legal_pro()));
            ps.setString(3, p.getNdocumento_pro());
            ps.setString(4, p.getDv_pro());
            ps.setString(5, f.NoPosee(p.getDireccion_pro()));
            ps.setString(6, f.NoPosee(p.getTelefono_pro()));
            ps.setString(7, f.NoPosee(p.getCelular_pro()));
            ps.setString(8, f.NoPosee(p.getCorreo_pro()));
            ps.setString(9, f.NoPosee(p.getObservacion_pro()));
            ps.setString(10, p.getId_ciudad());
            ps.registerOutParameter(11, java.sql.Types.INTEGER);
            ps.executeUpdate();
            p.setId_proveedor(ps.getString(11));
            dao.afterInsertProveedores(p, id_usu);
            r = ps.getString(11);

        } catch (SQLException ex) {
            Logger.getLogger(DAOproveedores.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarProveedores(proveedores p, String id_usu) {
        Connection con = conexion.getConnection();
        int r = 0;
        proveedores o = BuscarProveedores(p.getId_proveedor());
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE proveedores SET nombre_pro = ?, representante_legal_pro = ?, ndocumento_pro = ?, dv_pro = ?, direccion_pro = ?, telefono_pro = ?, celular_pro = ?, correo_pro = ?, observacion_pro = ?, ciudades_id_ciudad = ? WHERE id_proveedor = ?");
            ps.setString(1, p.getNombre_pro());
            ps.setString(2, f.NoPosee(p.getRepresentante_legal_pro()));
            ps.setString(3, p.getNdocumento_pro());
            ps.setString(4, p.getDv_pro());
            ps.setString(5, f.NoPosee(p.getDireccion_pro()));
            ps.setString(6, f.NoPosee(p.getTelefono_pro()));
            ps.setString(7, f.NoPosee(p.getCelular_pro()));
            ps.setString(8, f.NoPosee(p.getCorreo_pro()));
            ps.setString(9, f.NoPosee(p.getObservacion_pro()));
            ps.setString(10, p.getId_ciudad());
            ps.setString(11, p.getId_proveedor());
            r = ps.executeUpdate();
            dao.afterUpdateProveedores(p, o, id_usu);
            if (r > 0) {
                deleteMarcas(p.getId_proveedor());

            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOproveedores.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int CerrarProveedores(String id, String a, String id_usu) {
        Connection con = conexion.getConnection();
        int r = 0;
        proveedores p = BuscarProveedores(id);
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE proveedores SET close_pro = ? WHERE id_proveedor = ?");
            ps.setString(1, a);
            ps.setString(2, id);
            r = ps.executeUpdate();
            if (a.equals("1")) {
                dao.afterAnularProveedores(p, id_usu, "inhabilito");
            } else {
                dao.afterAnularProveedores(p, id_usu, "habilito");

            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOproveedores.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int CargarMarcasxProveedor(ArrayList<proveedores> pro) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO proveedores_x_marcas (proveedores_id_proveedor, marcas_id_marca) VALUES (?, ?)");
            for (proveedores p : pro) {
                ps.setString(1, p.getId_proveedor());
                ps.setString(2, p.getId_marca());
                r = ps.executeUpdate();

            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOproveedores.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public ArrayList<proveedores> BuscarMarcasxProveedor(String id) {
        Connection con = conexion.getConnection();
        ArrayList<proveedores> pro = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM proveedores_x_marcas WHERE proveedores_id_proveedor = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                proveedores p = new proveedores();
                p.setId_marca(rs.getString("marcas_id_marca"));
                pro.add(p);

            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOproveedores.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.getConnection();
        }
        return pro;
    }

    public int deleteMarcas(String id_proveedor) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM proveedores_x_marcas WHERE proveedores_id_proveedor = ?");
            ps.setString(1, id_proveedor);
            r = ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DAOproveedores.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public ArrayList<proveedores> ListarProveedoresInhabilitados() {
        Connection con = conexion.getConnection();
        ArrayList<proveedores> pro = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM proveedores p INNER JOIN ciudades c ON c.id_ciudad = p.ciudades_id_ciudad WHERE p.close_pro = 1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                proveedores p = new proveedores();
                p.setId_proveedor(rs.getString("id_proveedor"));
                p.setNombre_pro(rs.getString("nombre_pro"));
                p.setRepresentante_legal_pro(rs.getString("representante_legal_pro"));
                p.setNdocumento_pro(rs.getString("ndocumento_pro"));
                p.setDv_pro(rs.getString("dv_pro"));
                p.setDireccion_pro(rs.getString("direccion_pro"));
                p.setTelefono_pro(rs.getString("telefono_pro"));
                p.setCelular_pro(rs.getString("celular_pro"));
                p.setCorreo_pro(rs.getString("correo_pro"));
                p.setObservacion_pro(rs.getString("observacion_pro"));
                p.setClose_pro(rs.getString("close_pro"));
                p.setNombre_ciu(rs.getString("nombre_ciu"));
                pro.add(p);

            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOproveedores.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return pro;
    }
}
