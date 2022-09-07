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
import modelo.usuarios;

/**
 *
 * @author 2HDEV
 */
public class DAOempleados {

    funciones f = new funciones();

    public usuarios BuscarEmpleado(int id) {
        Connection con = conexion.getConnection();
        usuarios emp = new usuarios();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usuarios u INNER JOIN empleados e ON u.empleados_id_empleado = e.id_empleado WHERE id_empleado = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                emp.setId_empleado(rs.getInt("id_empleado"));
                emp.setNombre_emp(rs.getString("nombre_emp"));
                emp.setApellido_emp(rs.getString("apellido_emp"));
                emp.setNdocumento_emp(rs.getString("ndocumento_emp"));
                emp.setFnac_emp(f.ParseDI(rs.getString("fnac_emp")));
                emp.setDireccion_emp(rs.getString("direccion_emp"));
                emp.setTelefono_emp(rs.getString("telefono_emp"));
                emp.setCelular_emp(rs.getString("celular_emp"));
                emp.setObservacion_emp(rs.getString("observacion_emp"));
                emp.setPagina_inicio(rs.getString("pagina_inicio"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOempleados.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return emp;
    }

    public int ActualizarEmpleados(usuarios emp) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("UPDATE empleados SET nombre_emp=?, apellido_emp=?, ndocumento_emp=?, fnac_emp=?, direccion_emp=?, telefono_emp=?, celular_emp=?, observacion_emp=? WHERE id_empleado=?");
            ps.setString(1, emp.getNombre_emp());
            ps.setString(2, emp.getApellido_emp());
            ps.setString(3, f.NoPosee(emp.getNdocumento_emp()));
            ps.setString(4, f.ParseDate(emp.getFnac_emp()));
            ps.setString(5, f.NoPosee(emp.getDireccion_emp()));
            ps.setString(6, f.NoPosee(emp.getTelefono_emp()));
            ps.setString(7, f.NoPosee(emp.getCelular_emp()));
            ps.setString(8, f.NoPosee(emp.getObservacion_emp()));
            ps.setInt(9, emp.getId_empleado());
            r = ps.executeUpdate();
            ps = con.prepareStatement("UPDATE usuarios SET pagina_inicio = ? WHERE id_usuario=?");
            ps.setString(1, emp.getPagina_inicio());
            ps.setInt(2, Integer.valueOf(emp.getId_usuario()));
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOempleados.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.commit();
            } catch (SQLException ex) {
                Logger.getLogger(DAOempleados.class.getName()).log(Level.SEVERE, null, ex);
            }
            conexion.Close(con);
        }
        return r;
    }
}
