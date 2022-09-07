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
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.empresa;

/**
 *
 * @author akio
 */
public class DAOempresa {

    funciones f = new funciones();

    public empresa MostrarEmpresa(int id_usuario) {
        Connection con = conexion.getConnection();
        empresa emp = new empresa();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM empresa e, sucursal s, ciudades c WHERE e.ciudades_id_ciudad = c.id_ciudad AND e.sucursal_empresa=BuscarSucursalPorUsuario(?)");
            ps.setInt(1, id_usuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                emp.setId_empresa(rs.getInt("id_empresa"));
                emp.setNombre_empresa(rs.getString("nombre_empresa"));
                emp.setRepresentante_legal(rs.getString("representante_legal"));
                emp.setRuc_empresa(rs.getString("ruc_empresa"));
                emp.setDv_empresa(rs.getString("dv_empresa"));
                emp.setTelefono_empresa(rs.getString("telefono_empresa"));
                emp.setCelular_empresa(rs.getString("celular_empresa"));
                emp.setImpresora(rs.getString("impresora"));
                emp.setThermal_printer(rs.getString("thermal_printer"));
                emp.setSitio_empresa(rs.getString("sitio_empresa"));
                emp.setSucursal_empresa(rs.getInt("sucursal_empresa"));
                emp.setNombre_ciu(rs.getString("nombre_ciu"));
                emp.setId_ciudad(rs.getString("ciudades_id_ciudad"));
                emp.setLogo_empresa(rs.getString("logo_empresa"));
                emp.setTimbrado_empresa(rs.getInt("timbrado_empresa"));
                emp.setInicio_vigencia(f.ParseDI(rs.getString("inicio_vigencia")));
                emp.setFin_vigencia(f.ParseDI(rs.getString("fin_vigencia")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOempresa.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return emp;
    }


    public int AgregarEmpresa(empresa emp) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO empresa(nombre_empresa, representante_legal, ruc_empresa, dv_empresa, telefono_empresa, celular_empresa, sitio_empresa, sucursal_empresa, ciudades_id_ciudad, logo_empresa, timbrado_empresa, inicio_vigencia, fin_vigencia, impresora, thermal_printer) VALUES (?, ?, ?, ?, ?, ?, ? ,BuscarSucursalPorUsuario(?), ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, emp.getNombre_empresa());
            ps.setString(2, emp.getRepresentante_legal());
            ps.setString(3, emp.getRuc_empresa());
            ps.setString(4, emp.getDv_empresa());
            ps.setString(5, emp.getTelefono_empresa());
            ps.setString(6, emp.getCelular_empresa());
            ps.setString(7, emp.getSitio_empresa());
            ps.setString(8, emp.getId_usuario());
            ps.setString(9, emp.getId_ciudad());
            ps.setString(10, emp.getLogo_empresa());
            ps.setInt(11, emp.getTimbrado_empresa());
            ps.setString(12, f.ParseDate(emp.getInicio_vigencia()));
            ps.setString(13, f.ParseDate(emp.getFin_vigencia()));
            ps.setString(14, emp.getImpresora());
            ps.setString(15, emp.getThermal_printer());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOempresa.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarEmpresa(empresa emp) {
        Connection con = conexion.getConnection();
        int r = 0;

        try {
            PreparedStatement ps = con.prepareStatement("UPDATE empresa SET nombre_empresa = ?, representante_legal = ?, ruc_empresa =?, dv_empresa = ?, telefono_empresa = ?, celular_empresa = ?, sitio_empresa = ?, sucursal_empresa=BuscarSucursalPorUsuario(?), ciudades_id_ciudad = ?, logo_empresa=?, timbrado_empresa=?, inicio_vigencia=?, fin_vigencia=?, impresora=?, thermal_printer=? WHERE id_empresa = ?");
            ps.setString(1, emp.getNombre_empresa());
            ps.setString(2, emp.getRepresentante_legal());
            ps.setString(3, emp.getRuc_empresa());
            ps.setString(4, emp.getDv_empresa());
            ps.setString(5, emp.getTelefono_empresa());
            ps.setString(6, emp.getCelular_empresa());
            ps.setString(7, emp.getSitio_empresa());
            ps.setString(8, emp.getId_usuario());
            ps.setString(9, emp.getId_ciudad());
            ps.setString(10, emp.getLogo_empresa());
            ps.setInt(11, emp.getTimbrado_empresa());
            ps.setString(12, f.ParseDate(emp.getInicio_vigencia()));
            ps.setString(13, f.ParseDate(emp.getFin_vigencia()));
            ps.setString(14, emp.getImpresora());
            ps.setString(15, emp.getThermal_printer());
            ps.setInt(16, emp.getId_empresa());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOempresa.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int VerificarTimbrado(int id) {
        Connection con = conexion.getConnection();

        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM empresa WHERE fin_vigencia<NOW() AND sucursal_empresa=BuscarSucursalPorUsuario(?)");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                r = 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOempresa.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
