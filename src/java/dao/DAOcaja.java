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
import modelo.caja;

/**
 *
 * @author akio
 */
public class DAOcaja {

    funciones f = new funciones();

    public caja BuscarSaldoActual() {
        Connection con = conexion.getConnection();
        caja c = new caja();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM caja ORDER BY id_caja DESC LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c.setId_caja(rs.getInt("id_caja"));
                c.setFecha_caja(rs.getString("fecha_caja"));
                c.setSaldo_actual(rs.getInt("saldo_actual"));
                c.setSaldo_anterior(rs.getInt("saldo_anterior"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return c;
    }

    public int AgregarMovimentos(caja c, ArrayList<caja> det) {
        int r = 0;
        Connection con = conexion.getConnection();
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("INSERT INTO caja (fecha_caja, hora_caja, concepto_caja, saldo_anterior, saldo_actual, debe_caja, haber_caja, ventas_id_venta, compras_id_compra, id_usuario, estado_caja) VALUES (NOW(), NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, c.getConcepto_caja());
            ps.setInt(2, c.getSaldo_anterior());
            ps.setInt(3, c.getSaldo_anterior());
            ps.setInt(4, c.getDebe_caja());
            ps.setInt(5, c.getHaber_caja());
            ps.setString(6, f.VeriId(c.getVentas_id_venta()));
            ps.setString(7, f.VeriId(c.getCompras_id_compra()));
            ps.setString(8, c.getId_usuario());
            ps.setString(9, c.getEstado_caja());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                c.setId_caja((int) rs.getLong(1));
                for (caja d : det) {
                    ps = con.prepareStatement("INSERT INTO detalle_cierre (caja_id_caja, monedas_id_moneda, cantidad, monto_total) VALUES (?, ?, ?, ?)");
                    ps.setInt(1, c.getId_caja());
                    ps.setInt(2, d.getId_moneda());
                    ps.setInt(3, d.getCantidad_cierre());
                    ps.setInt(4, d.getMonto_total());
                    r = ps.executeUpdate();
                }
            }
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DAOcaja.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DAOcaja.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int VerificarAperturaCaja() {
        //Si devuelve 0 no existe una apertura, si devuelve 1 esta ok, si devuelve 2 no se realizo el cierre de caja del día anterior
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM detalle_caja WHERE fecha_detalle = DATE_FORMAT(NOW(), '%Y-%m-%d') AND tipo_detalle='APERTURA'");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                r = 1;
            } else {
                ps = con.prepareStatement("SELECT * FROM detalle_caja WHERE fecha_detalle = DATE_FORMAT(NOW(), '%Y-%m-%d') AND (tipo_detalle='CIERRE' OR tipo_detalle='CIERRE PARCIAL')");
                rs = ps.executeQuery();
                if (!rs.next()) {
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

    public void cargarMovimientos(caja c) {
        Connection con = conexion.getConnection();
        caja sa = BuscarSaldoActual();
        int saldo = Integer.valueOf(sa.getSaldo_actual()) + Integer.valueOf(c.getSaldo_actual());
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO caja (fecha_caja, hora_caja, concepto_caja, saldo_anterior, saldo_actual, debe_caja, haber_caja, ventas_id_venta, compras_id_compra, usuarios_id_usuario, estado_caja) VALUES (NOW(), NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, c.getConcepto_caja());
            ps.setInt(2, sa.getSaldo_actual());
            ps.setInt(3, saldo);
            ps.setInt(4, c.getDebe_caja());
            ps.setInt(5, c.getHaber_caja());
            ps.setString(6, f.VeriId(c.getVentas_id_venta()));
            ps.setString(7, f.VeriId(c.getCompras_id_compra()));
            ps.setString(8, c.getId_usuario());
            ps.setString(9, "A");
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOcaja.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<caja> ListarCaja() {
        ArrayList<caja> cja = new ArrayList<>();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM caja WHERE fecha_detalle = DATE_FORMAT(NOW(),'%Y/%m/%d')");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                caja c = new caja();
                c.setId_caja(rs.getInt("id_caja"));
                c.setFecha_caja(f.ParseDI(rs.getString("fecha_caja")));
                c.setHora_caja(rs.getString("hora_caja"));
                c.setConcepto_caja(rs.getString("concepto_caja"));
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

    public ArrayList<caja> BuscarCaja(String fecha) {
        ArrayList<caja> cja = new ArrayList<>();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM caja WHERE fecha_detalle = ?");
            ps.setString(1, fecha);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                caja c = new caja();
                c.setId_caja(rs.getInt("id_caja"));
                c.setFecha_caja(f.ParseDI(rs.getString("fecha_caja")));
                c.setHora_caja(rs.getString("hora_caja"));
                c.setConcepto_caja(rs.getString("concepto_caja"));
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

    public ArrayList<caja> ListarCajas() {
        ArrayList<caja> cja = new ArrayList<>();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM caja");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                caja c = new caja();
                c.setId_caja(rs.getInt("id_caja"));
                c.setFecha_caja(rs.getString("fecha_caja"));
                c.setHora_caja(rs.getString("hora_caja"));
                c.setConcepto_caja(rs.getString("concepto_caja"));
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

    public ArrayList<caja> BuscarCambioInicial() {
        Connection con = conexion.getConnection();
        ArrayList<caja> ca = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM caja c, detalle_cierre d WHERE c.id_caja=(SELECT MAX(id_caja) FROM caja) AND d.caja_id_caja=(SELECT MAX(id_caja) FROM caja) AND (estado_caja='S' OR estado_caja='P')");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                caja c = new caja();
                c.setId_moneda(rs.getInt("monedas_id_moneda"));
                c.setCantidad_cierre(rs.getInt("cantidad"));
                c.setSaldo_actual(rs.getInt("saldo_actual"));
                ca.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return ca;
    }

    public caja BuscarUltimoSaldoActivo() {
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        caja c = new caja();
        try {
            ps = con.prepareStatement("SELECT * FROM caja WHERE id_caja = (SELECT max(id_caja) FROM caja) AND estado_caja='A'");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c.setSaldo_actual(rs.getInt("saldo_actual"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOcaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return c;
    }

}
