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
import modelo.detalle_caja;

/**
 *
 * @author akio
 */
public class DAOdetallecaja {

    funciones f = new funciones();

    public detalle_caja BuscarSaldoActual() {
        Connection con = conexion.getConnection();
        detalle_caja c = new detalle_caja();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM detalle_caja ORDER BY id_caja DESC LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c.setId_detalle(rs.getInt("id_detalle"));
                c.setFecha_detalle(rs.getString("fecha_caja"));
                c.setSaldo_actual(rs.getInt("saldo_actual"));
                c.setSaldo_anterior(rs.getInt("saldo_anterior"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOdetallecaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return c;
    }

    public int AgregarMovimentos(detalle_caja c, ArrayList<detalle_caja> det) {
        int r = 0;
        Connection con = conexion.getConnection();
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("SET @id_detalle=(SELECT max(detalle_caja.id_detalle) FROM detalle_caja, usuarios WHERE detalle_caja.id_caja=usuarios.id_caja AND usuarios.id_usuario=?)");
            ps.setInt(1, Integer.valueOf(c.getId_usuario()));
            ps.execute();

            ps = con.prepareStatement("SET @id_caja=(SELECT id_caja FROM usuarios WHERE id_usuario=?)");
            ps.setInt(1, Integer.valueOf(c.getId_usuario()));
            ps.execute();

            ps = con.prepareStatement("SET @saldo_actual=(SELECT saldo_actual FROM detalle_caja WHERE id_detalle=@id_detalle)");
            ps.execute();

            ps = con.prepareStatement("INSERT INTO detalle_caja (fecha_detalle, hora_detalle, concepto_detalle, saldo_anterior, saldo_actual, debe_caja, haber_caja, id_caja, id_venta, id_compra, id_usuario, tipo_detalle) VALUES (NOW(), NOW(), ?, IFNULL(@saldo_actual,0), IFNULL(@saldo_actual+?, ?), ?, ?, @id_caja, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, c.getConcepto_detalle());
            ps.setInt(2, c.getSaldo_actual());
            ps.setInt(3, c.getSaldo_actual());
            ps.setInt(4, c.getDebe_caja());
            ps.setInt(5, c.getHaber_caja());
            ps.setString(6, f.VeriId(c.getVentas_id_venta()));
            ps.setString(7, f.VeriId(c.getCompras_id_compra()));
            ps.setString(8, c.getId_usuario());
            ps.setString(9, c.getEstado_caja());
            r = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (!det.isEmpty()) {
                if (rs.next()) {
                    c.setId_caja((int) rs.getLong(1));
                    for (detalle_caja d : det) {
                        ps = con.prepareStatement("INSERT INTO detalle_cierre (id_caja, id_moneda, cantidad, monto_total) VALUES (?, ?, ?, ?)");
                        ps.setInt(1, c.getId_caja());
                        ps.setInt(2, d.getId_moneda());
                        ps.setInt(3, d.getCantidad_cierre());
                        ps.setInt(4, d.getMonto_total());
                        r = ps.executeUpdate();
                    }
                }
            }
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DAOdetallecaja.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DAOdetallecaja.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int VerificarAperturaCaja(int id_usuario) {
        //Si devuelve 0 no existe una apertura, si devuelve 1 esta ok, si devuelve 2 no se realizo el cierre de caja del día anterior
        Connection con = conexion.getConnection();
        int r = 0;

        try {
            PreparedStatement ps = con.prepareStatement("SET @id_detalle=(SELECT max(id_detalle) FROM detalle_caja, usuarios WHERE detalle_caja.tipo_detalle!='M' AND detalle_caja.tipo_detalle!='E' AND detalle_caja.tipo_detalle!='S' AND detalle_caja.id_caja=usuarios.id_caja AND usuarios.id_usuario=?)");
            ps.setInt(1, id_usuario);
            ps.execute();

            ps = con.prepareStatement("SET @id_caja=(SELECT id_caja FROM usuarios WHERE id_usuario=?)");
            ps.setInt(1, id_usuario);
            ps.execute();

            ps = con.prepareStatement("SELECT * FROM detalle_caja WHERE tipo_detalle='APERTURA' AND detalle_caja.id_caja=@id_caja AND id_detalle=@id_detalle");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps = con.prepareStatement("SELECT * FROM detalle_caja, usuarios WHERE tipo_detalle='SIGUIENTE' AND detalle_caja.id_caja=usuarios.id_caja AND id_detalle=@id_detalle AND usuarios.id_usuario=?");
                ps.setInt(1, id_usuario);
                rs = ps.executeQuery();
                if (rs.next()) {
                    r = 1;
                } else {
                    r = 2;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public void CargarMovimientos(detalle_caja c) {
        Connection con = conexion.getConnection();
        detalle_caja sa = BuscarSaldoActual();
        int saldo = sa.getSaldo_actual() + c.getSaldo_actual();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO detalle_caja (fecha_detalle, hora_detalle, concepto_detalle, saldo_anterior, saldo_actual, debe_caja, haber_caja, tipo_detalle, arqueo_caja, id_venta, id_compra) VALUES (NOW(), NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, c.getConcepto_detalle());
            ps.setInt(2, sa.getSaldo_actual());
            ps.setInt(3, saldo);
            ps.setInt(4, c.getDebe_caja());
            ps.setInt(5, c.getHaber_caja());
            ps.setString(6, "A");
            ps.setInt(7, c.getArqueo_caja());
            ps.setString(8, f.VeriId(c.getVentas_id_venta()));
            ps.setString(9, f.VeriId(c.getCompras_id_compra()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOdetallecaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public ArrayList<detalle_caja> ListarCaja(detalle_caja c) {
        ArrayList<detalle_caja> cja = new ArrayList<>();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps;
            ps = con.prepareStatement("SET @usuario = ?");
            ps.setInt(1, Integer.valueOf(c.getId_usuario()));
            ps.execute();
            if (c.getId_detalle() != 0) {
                ps = con.prepareStatement("SET @fecha_apertura=(SELECT STR_TO_DATE(CONCAT(fecha_detalle,' ', hora_detalle), '%Y-%m-%d %H:%i:%S') FROM apertura_caja WHERE id_detalle=?)");
                ps.setInt(1, c.getId_detalle());
                ps.execute();
                ps = con.prepareStatement("SET @id_cierre=(SELECT MIN(id_detalle) FROM cierre_caja WHERE fecha_detalle BETWEEN @fecha_apertura AND NOW())");
                ps.execute();
                ps = con.prepareStatement("SET @fecha_cierre=(SELECT STR_TO_DATE(CONCAT(fecha_detalle,' ', hora_detalle), '%Y-%m-%d %H:%i:%S') FROM cierre_caja WHERE id_detalle=@id_cierre)");
                ps.execute();
                ps = con.prepareStatement("SELECT * FROM detalle_caja, usuarios, tipo_pago WHERE STR_TO_DATE(CONCAT(fecha_detalle,' ',hora_detalle), '%Y-%m-%d %H:%i:%S') BETWEEN @fecha_apertura AND IFNULL(@fecha_cierre, NOW()) AND detalle_caja.id_caja = usuarios.id_caja AND detalle_caja.tipo_pago=tipo_pago.id_tipo_pago AND usuarios.id_usuario=? ORDER BY detalle_caja.id_detalle DESC");
                ps.setInt(1, Integer.valueOf(c.getId_usuario()));
                System.out.println(ps);
            } else {
                ps = con.prepareStatement("SET @id_detalle=(SELECT MAX(id_detalle) FROM detalle_caja, usuarios WHERE tipo_detalle='APERTURA' AND detalle_caja.id_caja=usuarios.id_caja AND usuarios.id_usuario=?)");
                ps.setInt(1, Integer.valueOf(c.getId_usuario()));
                ps.execute();
                ps = con.prepareStatement("SET @fecha_detalle=(SELECT STR_TO_DATE(CONCAT(fecha_detalle,' ',hora_detalle), '%Y-%m-%d %H:%i:%S') as fecha FROM detalle_caja WHERE id_detalle=@id_detalle)");
                ps.execute();
                ps = con.prepareStatement("SELECT * FROM detalle_caja, usuarios, tipo_pago WHERE STR_TO_DATE(CONCAT(fecha_detalle,' ',hora_detalle), '%Y-%m-%d %H:%i:%S') BETWEEN @fecha_detalle AND NOW() AND detalle_caja.id_caja = usuarios.id_caja AND detalle_caja.tipo_pago=tipo_pago.id_tipo_pago AND usuarios.id_usuario=? ORDER BY detalle_caja.id_detalle DESC");               
                ps.setInt(1, Integer.valueOf(c.getId_usuario()));
                System.out.println(ps);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                detalle_caja dc = new detalle_caja();
                dc.setId_detalle(rs.getInt("id_detalle"));
                dc.setFecha_detalle(rs.getString("fecha_detalle"));
                dc.setHora_detalle(rs.getString("hora_detalle"));
                dc.setConcepto_detalle(rs.getString("concepto_detalle"));
                dc.setDescripcion(rs.getString("descripcion"));
                dc.setSaldo_actual(rs.getInt("saldo_actual"));
                dc.setSaldo_anterior(rs.getInt("saldo_anterior"));
                dc.setDebe_caja(rs.getInt("debe_caja"));
                dc.setHaber_caja(rs.getInt("haber_caja"));
                dc.setTipo_detalle(rs.getString("tipo_detalle"));
                dc.setArqueo_caja(rs.getInt("id_arqueo"));
                dc.setId_caja(rs.getInt("id_caja"));
                dc.setId_compra(rs.getInt("id_compra"));
                dc.setId_venta(rs.getInt("id_venta"));
                cja.add(dc);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOdetallecaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return cja;
    }

    public ArrayList<detalle_caja> ListarCajasPorApertura(int id_usuario) {
        ArrayList<detalle_caja> cja = new ArrayList<>();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps;
            ps = con.prepareStatement("SET @usuario = ?");
            ps.setInt(1, id_usuario);
            ps.execute();
            ps = con.prepareStatement("SET @id_caja=(SELECT id_caja FROM usuarios WHERE id_usuario=?)");
            ps.setInt(1, id_usuario);
            ps.execute();

            ps = con.prepareStatement("SELECT * FROM apertura_caja WHERE id_caja=@id_caja");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                detalle_caja dc = new detalle_caja();
                dc.setId_detalle(rs.getInt("id_detalle"));
                dc.setFecha_detalle(rs.getString("fecha_detalle"));
                dc.setHora_detalle(rs.getString("hora_detalle"));
                dc.setConcepto_detalle(rs.getString("concepto_detalle"));
                dc.setDescripcion(rs.getString("descripcion"));
                dc.setSaldo_actual(rs.getInt("saldo_actual"));
                dc.setSaldo_anterior(rs.getInt("saldo_anterior"));
                dc.setDebe_caja(rs.getInt("debe_caja"));
                dc.setHaber_caja(rs.getInt("haber_caja"));
                dc.setTipo_detalle(rs.getString("tipo_detalle"));
                dc.setArqueo_caja(rs.getInt("id_arqueo"));
                dc.setId_caja(rs.getInt("id_caja"));
                dc.setId_compra(rs.getInt("id_compra"));
                dc.setId_venta(rs.getInt("id_venta"));
                cja.add(dc);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOdetallecaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return cja;
    }

    public ArrayList<detalle_caja> BuscarCaja(int id_usuario, String fecha) {
        ArrayList<detalle_caja> cja = new ArrayList<>();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM detalle_caja, usuarios WHERE fecha_detalle = ? AND detalle_caja.id_caja=usuarios.id_caja AND usuarios.id_usuario=?");
            ps.setString(1, fecha);
            ps.setInt(2, id_usuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                detalle_caja dc = new detalle_caja();
                dc.setId_detalle(rs.getInt("id_detalle"));
                dc.setFecha_detalle(rs.getString("fecha_detalle"));
                dc.setHora_detalle(rs.getString("hora_detalle"));
                dc.setConcepto_detalle(rs.getString("concepto_detalle"));
                dc.setSaldo_actual(rs.getInt("saldo_actual"));
                dc.setSaldo_anterior(rs.getInt("saldo_anterior"));
                dc.setDebe_caja(rs.getInt("debe_caja"));
                dc.setHaber_caja(rs.getInt("haber_caja"));
                dc.setTipo_detalle(rs.getString("tipo_detalle"));
                dc.setArqueo_caja(rs.getInt("arqueo_caja"));
                dc.setId_caja(rs.getInt("id_caja"));
                dc.setId_compra(rs.getInt("id_compra"));
                dc.setId_venta(rs.getInt("id_venta"));
                cja.add(dc);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOdetallecaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return cja;
    }

    public ArrayList<detalle_caja> ListarCajas(int id_usuario) {
        ArrayList<detalle_caja> cja = new ArrayList<>();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM detalle_caja d, caja c, sucursal s, tipo_pago t WHERE c.id_sucursal = s.id_sucursal AND c.id_sucursal=BuscarSucursalPorUsuario(?) AND d.id_caja = c.id_caja AND d.tipo_pago=t.id_tipo_pago");
            ps.setInt(1, id_usuario);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                detalle_caja c = new detalle_caja();
                c.setId_caja(rs.getInt("id_caja"));
                c.setFecha_detalle(rs.getString("fecha_detalle"));
                c.setHora_detalle(rs.getString("hora_detalle"));
                c.setConcepto_detalle(rs.getString("concepto_detalle"));
                c.setDebe_caja(rs.getInt("debe_caja"));
                c.setHaber_caja(rs.getInt("haber_caja"));
                c.setSaldo_actual(rs.getInt("saldo_actual"));
                c.setSaldo_anterior(rs.getInt("saldo_anterior"));
                cja.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return cja;
    }

    public ArrayList<detalle_caja> BuscarCambioInicial(int id_usuario) {
        Connection con = conexion.getConnection();
        ArrayList<detalle_caja> ca = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT detalle_cierre.*, monedas.valor_moneda, ObtenerSaldoActual(?) as saldo_actual FROM detalle_cierre, detalle_caja, monedas "
                    + "WHERE detalle_cierre.id_detalle=detalle_caja.id_detalle AND detalle_cierre.id_detalle=BuscarIdCierre() "
                    + "AND monedas.id_moneda=detalle_cierre.id_moneda");
            ps.setInt(1, id_usuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                detalle_caja c = new detalle_caja();
                c.setId_moneda(rs.getInt("id_moneda"));
                c.setValor_moneda(rs.getInt("valor_moneda"));
                c.setCantidad_cierre(rs.getInt("cantidad"));
                c.setSaldo_actual(rs.getInt("saldo_actual"));
                ca.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOdetallecaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return ca;
    }

    public detalle_caja BuscarUltimoSaldoActivo(int id_usuario) {
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        detalle_caja c = new detalle_caja();
        try {
            ps = con.prepareStatement("SELECT ObtenerSaldoActual(?) as saldo_actual");
            ps.setInt(1, id_usuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c.setSaldo_actual(rs.getInt("saldo_actual"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOdetallecaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return c;
    }

}
