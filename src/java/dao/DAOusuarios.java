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
import modelo.usuarios;

/**
 *
 * @author akio
 */
public class DAOusuarios {

    DAOauditoria dao = new DAOauditoria();

    public ArrayList<usuarios> ListarUsuarios() {
        Connection con = conexion.getConnection();
        ArrayList<usuarios> usu = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usuarios u, empleados e, sucursal s WHERE u.empleados_id_empleado = e.id_empleado AND u.id_usuario !=1 AND u.sucursal_id_sucursal=s.id_sucursal");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usuarios u = new usuarios();
                u.setId_usuario(rs.getString("id_usuario"));
                u.setAlias_usu(rs.getString("alias_usu"));
                u.setCreacion_usu(rs.getString("creacion_usu"));
                u.setNombre_emp(rs.getString("nombre_emp"));
                u.setApellido_emp(rs.getString("apellido_emp"));
                u.setId_sucursal(rs.getString("sucursal_id_sucursal"));
                u.setNombre_sucursal(rs.getString("nombre_sucursal"));
                u.setEstado_usu(rs.getString("close_usu"));
                usu.add(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return usu;
    }

    public ArrayList<usuarios> ListarUsuariosReporte() {
        Connection con = conexion.getConnection();
        ArrayList<usuarios> usu = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usuarios WHERE close_usu!=1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usuarios u = new usuarios();
                u.setId_usuario(rs.getString("id_usuario"));
                u.setAlias_usu(rs.getString("alias_usu"));
                usu.add(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return usu;
    }

    public usuarios BuscarUsuarios(String id) {
        Connection con = conexion.getConnection();
        usuarios u = new usuarios();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usuarios u INNER JOIN empleados e ON u.empleados_id_empleado = e.id_empleado WHERE id_usuario = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u.setId_usuario(rs.getString("id_usuario"));
                u.setAlias_usu(rs.getString("alias_usu"));
                u.setCreacion_usu(rs.getString("creacion_usu"));
                u.setEstado_usu(rs.getString("close_usu"));
                u.setNombre_emp(rs.getString("nombre_emp"));
                u.setApellido_emp(rs.getString("apellido_emp"));
                u.setId_empleado(rs.getInt("empleados_id_empleado"));
                u.setId_sucursal(rs.getString("sucursal_id_sucursal"));
                u.setPagina_inicio(rs.getString("pagina_inicio"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return u;
    }

    public int AgregarUsuarios(ArrayList<usuarios> per, usuarios u, String id) {
        Connection con = conexion.getConnection();
        int r = 0;
        PreparedStatement ps;
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement("SET @usuario = ?");
            ps.setInt(1, Integer.valueOf(id));
            ps.execute();
            ps = con.prepareStatement("INSERT INTO empleados (nombre_emp, apellido_emp) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, u.getNombre_emp());
            ps.setString(2, u.getApellido_emp());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                u.setId_empleado((int) rs.getLong(1));
                ps = con.prepareStatement("INSERT INTO usuarios (alias_usu, password_usu, empleados_id_empleado, sucursal_id_sucursal) VALUES (?, MD5(?), ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, u.getAlias_usu());
                ps.setString(2, u.getPassword_usu());
                ps.setInt(3, u.getId_empleado());
                ps.setString(4, u.getId_sucursal());
                ps.executeUpdate();
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    u.setId_usuario(String.valueOf(rs.getLong(1)));
                    for (usuarios p : per) {
                        ps = con.prepareStatement("INSERT INTO usuarios_has_permisos(usuarios_id_usuario, permisos_id_permiso, ver, agregar, editar, aprobar, eliminar) VALUES (?, ?, ?, ?, ?, ?, ?)");
                        ps.setInt(1, Integer.valueOf(u.getId_usuario()));
                        ps.setInt(2, Integer.valueOf(p.getId_permiso()));
                        ps.setString(3, p.getVer());
                        ps.setString(4, p.getAgregar());
                        ps.setString(5, p.getEditar());
                        ps.setString(6, p.getAprobar());
                        ps.setString(7, p.getEliminar());
                        r = ps.executeUpdate();
                    }
                }
            }
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dao.afterInsertUsuarios(u, id);
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarUsuarios(ArrayList<usuarios> per, usuarios u, String id) {
        Connection con = conexion.getConnection();
        int r = 0;
        usuarios o = BuscarUsuarios(id);
        PreparedStatement ps;
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement("SET @usuario = ?");
            ps.setInt(1, Integer.valueOf(id));
            ps.execute();
            ps = con.prepareStatement("UPDATE usuarios SET alias_usu = ?, sucursal_id_sucursal = ?  WHERE id_usuario = ? AND id_usuario!=1");
            ps.setString(1, u.getAlias_usu());
            ps.setString(2, u.getId_sucursal());
            ps.setInt(3, Integer.valueOf(u.getId_usuario()));
            ps.executeUpdate();
            usuarios e = BuscarUsuarios(u.getId_usuario());
            ps = con.prepareStatement("UPDATE empleados SET nombre_emp = ?, apellido_emp = ? WHERE id_empleado = ?");
            ps.setString(1, u.getNombre_emp());
            ps.setString(2, u.getApellido_emp());
            ps.setInt(3, e.getId_empleado());
            ps.executeUpdate();
            ps = con.prepareStatement("DELETE FROM usuarios_has_permisos WHERE usuarios_id_usuario = ? AND usuarios_id_usuario!=1");
            ps.setInt(1, Integer.valueOf(u.getId_usuario()));
            ps.execute();
            for (usuarios p : per) {
                ps = con.prepareStatement("INSERT INTO usuarios_has_permisos (usuarios_id_usuario, permisos_id_permiso, ver, agregar, editar, aprobar, eliminar) VALUES (?, ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, Integer.valueOf(u.getId_usuario()));
                ps.setInt(2, Integer.valueOf(p.getId_permiso()));
                ps.setString(3, p.getVer());
                ps.setString(4, p.getAgregar());
                ps.setString(5, p.getEditar());
                ps.setString(6, p.getAprobar());
                ps.setString(7, p.getEliminar());
                r = ps.executeUpdate();
            }
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dao.afterUpdateUsuarios(u, o, id);
            conexion.Close(con);
        }
        return r;
    }

    public int AgregarPermisos(ArrayList<usuarios> per) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            for (usuarios p : per) {
                PreparedStatement ps = con.prepareStatement("INSERT INTO usuarios_has_permisos (usuarios_id_usuario, permisos_id_permiso, ver, agregar, editar) VALUES (?, ?, ?, ? ,?)");
                ps.setString(1, p.getId_usuario());
                ps.setString(2, p.getId_permiso());
                ps.setString(3, p.getVer());
                ps.setString(4, p.getAgregar());
                ps.setString(5, p.getEditar());
                r = ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public ArrayList<usuarios> BuscarPermisos(int id_usu) {
        Connection con = conexion.getConnection();
        ArrayList<usuarios> user = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usuarios_has_permisos up, permisos p WHERE up.permisos_id_permiso = p.id_permiso AND usuarios_id_usuario =?");
            ps.setInt(1, id_usu);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usuarios u = new usuarios();
                u.setId_usuario(rs.getString("usuarios_id_usuario"));
                u.setId_permiso(rs.getString("permisos_id_permiso"));
                u.setModulo_per(rs.getString("modulo_per"));
                u.setUrl(rs.getString("url"));
                u.setVer(rs.getString("ver"));
                u.setEditar(rs.getString("editar"));
                u.setAgregar(rs.getString("agregar"));
                u.setAprobar(rs.getString("aprobar"));
                u.setEliminar(rs.getString("eliminar"));
                user.add(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return user;
    }

    public usuarios BuscarPermisoxUsuario(int id_usu, int id_permiso) {
        Connection con = conexion.getConnection();
        usuarios u = new usuarios();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usuarios_has_permisos up, permisos p WHERE up.permisos_id_permiso = p.id_permiso AND up.permisos_id_permiso = ? AND usuarios_id_usuario = ?");
            ps.setInt(1, id_permiso);
            ps.setInt(2, id_usu);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                u.setId_usuario(rs.getString("usuarios_id_usuario"));
                u.setId_permiso(rs.getString("permisos_id_permiso"));
                u.setModulo_per(rs.getString("modulo_per"));
                u.setUrl(rs.getString("url"));
                u.setVer(rs.getString("ver"));
                u.setEditar(rs.getString("editar"));
                u.setAgregar(rs.getString("agregar"));
                u.setAprobar(rs.getString("aprobar"));
                u.setEliminar(rs.getString("eliminar"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return u;
    }

    public int VerificarUsuario(usuarios u) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usuarios WHERE alias_usu = ? AND password_usu = md5(?) AND close_usu = 0");
            ps.setString(1, u.getAlias_usu());
            ps.setString(2, u.getPassword_usu());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                r = rs.getInt("id_usuario");
            }
            ps = con.prepareStatement("SELECT date(fecha_2hdev) < Date(NOW()) AS tuhoraperro FROM usuarios LIMIT 1");
            rs = ps.executeQuery();
            if (rs.next()) {
                //Le llego su hora al perro
                if (rs.getBoolean("tuhoraperro")) {
                    Connection cnx = conexion.getControlnexion();
                    ps = con.prepareStatement("SELECT * FROM empresa");
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        ps = cnx.prepareStatement("SELECT * FROM clientes WHERE ruc_cl=? AND estado_cl='PAGADO'");
                        ps.setString(1, rs.getString("ruc_empresa"));
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            ps = con.prepareStatement("UPDATE usuarios SET fecha_2hdev = ?, estado_2hdev=?");
                            ps.setDate(1, rs.getDate("fecha_cl"));
                            ps.setString(2, rs.getString("estado_cl"));
                            ps.executeUpdate();
                        } else {
                            r = -1;
                        }
                    } else {
                        r = -1;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public void DeletePermisos(String id) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM usuarios_has_permisos WHERE usuarios_id_usuario = ?");
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public int CerrarUsuario(int id) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE usuarios SET close_usu = !close_usu WHERE id_usuario = ?");
            ps.setInt(1, id);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            usuarios u = BuscarUsuarios(String.valueOf(id));
            dao.afterAnularUsuarios(u, String.valueOf(id), u.getEstado_usu().equals("0") ? "habilito" : "inhabilito");
            conexion.Close(con);
        }
        return r;
    }

    public int VerificarPassword(usuarios u) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT password_usu FROM usuarios WHERE id_usuario=? AND password_usu=MD5(?)");
            ps.setInt(1, Integer.valueOf(u.getId_usuario()));
            ps.setString(2, u.getOldpass());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps = con.prepareStatement("UPDATE usuarios SET password_usu=MD5(?) WHERE id_usuario=?");
                ps.setString(1, u.getPassword_usu());
                ps.setInt(2, Integer.valueOf(u.getId_usuario()));
                r = ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarPassword(usuarios u) {
        Connection con = conexion.getConnection();
        int r = 0;
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("UPDATE usuarios SET password_usu=MD5(?) WHERE id_usuario = ?");
            ps.setString(1, u.getPassword_usu());
            ps.setInt(2, Integer.valueOf(u.getId_usuario()));
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public String VerificarAdmin(int id) {
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        String rpta = null;
        try {
            ps = con.prepareStatement("SELECT IF(COUNT(ver)=(SELECT COUNT(*) FROM permisos)&&COUNT(agregar)=(SELECT COUNT(*) FROM permisos)&&COUNT(editar)=(SELECT COUNT(*) FROM permisos)&&COUNT(eliminar)=(SELECT COUNT(*) FROM permisos)&&COUNT(aprobar)=(SELECT COUNT(*) FROM permisos),'SI', 'NO') admin FROM usuarios_has_permisos WHERE ver = 'Y' AND agregar = 'Y' AND editar = 'Y' AND usuarios_id_usuario = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rpta = rs.getString("admin");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOusuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return rpta;
    }
}
