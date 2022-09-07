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
import modelo.arqueocaja;

/**
 *
 * @author 2HDEV
 */
public class DAOarqueocaja {

    funciones f = new funciones();

    public ArrayList<arqueocaja> ListarArqueosdeCaja(int id_usuario) {
        Connection con = conexion.getConnection();
        ArrayList<arqueocaja> arqueo = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SET @id_caja=(SELECT id_caja FROM usuarios WHERE id_usuario=?)");
            ps.setInt(1, id_usuario);
            ps.execute();
            ps = con.prepareStatement("SELECT caja.codigo_caja, usuarios.alias_usu, arqueo_caja.fecha_arqueo from arqueo_caja, usuarios, caja WHERE estado='N' AND usuarios.id_usuario=arqueo_caja.id_usuario AND caja.id_caja=arqueo_caja.id_caja AND caja.id_caja=@id_caja");
            ps.setInt(1, id_usuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                arqueocaja corte = new arqueocaja();
                corte.setCodigo_caja(rs.getString("codigo_caja"));
                corte.setAlias_usu(rs.getString("alias_usu"));
                corte.setFecha_arqueo(rs.getDate("fecha_arqueo"));
                corte.setHora_arqueo(rs.getTime("fecha_arqueo"));
                arqueo.add(corte);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOarqueocaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return arqueo;
    }

    public int AgregarArqueo(arqueocaja cabecera, ArrayList<arqueocaja> ac) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("SET @id_caja=(SELECT id_caja FROM usuarios WHERE id_usuario=?)");
            ps.setInt(1, Integer.valueOf(cabecera.getId_usuario()));
            ps.execute();
            ps = con.prepareStatement("INSERT INTO arqueo_caja(fecha_arqueo, total_calculado, total_contado, total_diferencia, total_retirado, id_usuario, id_caja) VALUES (NOW(), ?, ?, ?, ?, ?, @id_caja)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, cabecera.getCalculado());
            ps.setInt(2, cabecera.getContado());
            ps.setInt(3, cabecera.getDiferencia());
            ps.setInt(4, cabecera.getRetirado());
            ps.setString(5, cabecera.getId_usuario());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                cabecera.setId_arqueo((int) rs.getLong(1));
                for (arqueocaja arqueo : ac) {
                    ps = con.prepareStatement("INSERT INTO detalle_arqueo(id_arqueo, id_tipo_pago, calculado, contado, diferencia, retirado) VALUES (?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, cabecera.getId_arqueo());
                    ps.setInt(2, arqueo.getId_tipo_pago());
                    ps.setInt(3, arqueo.getCalculado());
                    ps.setInt(4, arqueo.getContado());
                    ps.setInt(5, 0);
                    ps.setInt(6, arqueo.getRetirado());
                    r = ps.executeUpdate();

                    ps = con.prepareStatement("SET @id_detalle=(SELECT max(detalle_caja.id_detalle) FROM detalle_caja, usuarios WHERE detalle_caja.id_caja=usuarios.id_caja AND usuarios.id_usuario=?)");
                    ps.setInt(1, Integer.valueOf(cabecera.getId_usuario()));
                    ps.execute();

                    ps = con.prepareStatement("SET @id_caja=(SELECT id_caja FROM usuarios WHERE id_usuario=?)");
                    ps.setInt(1, Integer.valueOf(cabecera.getId_usuario()));
                    ps.execute();

                    ps = con.prepareStatement("SET @saldo_actual=(SELECT saldo_actual FROM detalle_caja WHERE id_detalle=@id_detalle)");
                    ps.execute();

                    ps = con.prepareStatement("INSERT INTO detalle_caja (fecha_detalle, hora_detalle, concepto_detalle, tipo_pago, saldo_anterior, saldo_actual, debe_caja, haber_caja, id_usuario, tipo_detalle, id_caja, id_arqueo) VALUES (NOW(), NOW(), ?, ?, IFNULL(@saldo_actual,0), (IFNULL(@saldo_actual,0)-?), ?, ?, ?, ?, @id_caja, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
                    if (arqueo.getRetirado() != 0) {
                        ps.setString(1, "RETIRO DE CAJA SEGÚN");
                        ps.setInt(2, arqueo.getId_tipo_pago());
                        ps.setInt(3, arqueo.getRetirado());
                        ps.setInt(4, 0);
                        ps.setInt(5, arqueo.getRetirado());
                        ps.setString(6, cabecera.getId_usuario());
                        ps.setString(7, "CIERRE");
                        ps.setInt(8, cabecera.getId_arqueo());
                        ps.executeUpdate();
                    }
                }

                for (arqueocaja arqueo : ac) {
                    ps = con.prepareStatement("SET @id_detalle=(SELECT max(detalle_caja.id_detalle) FROM detalle_caja, usuarios WHERE detalle_caja.id_caja=usuarios.id_caja AND usuarios.id_usuario=?)");
                    ps.setInt(1, Integer.valueOf(cabecera.getId_usuario()));
                    ps.execute();

                    ps = con.prepareStatement("SET @id_caja=(SELECT id_caja FROM usuarios WHERE id_usuario=?)");
                    ps.setInt(1, Integer.valueOf(cabecera.getId_usuario()));
                    ps.execute();

                    ps = con.prepareStatement("SET @saldo_actual=(SELECT saldo_actual FROM detalle_caja WHERE id_detalle=@id_detalle)");
                    ps.execute();

                    ps = con.prepareStatement("INSERT INTO detalle_caja (fecha_detalle, hora_detalle, concepto_detalle, tipo_pago, saldo_anterior, saldo_actual, debe_caja, haber_caja, id_usuario, tipo_detalle, id_caja, id_arqueo) VALUES (NOW(), NOW(), ?, ?, @saldo_actual,(@saldo_actual+?), ?, ?, ?, ?, @id_caja, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setString(1, "(=) CIERRE DE CAJA");
                    ps.setInt(2, arqueo.getId_tipo_pago());
                    ps.setInt(3, 0);
                    ps.setInt(4, 0);
                    ps.setInt(5, 0);
                    ps.setInt(6, Integer.valueOf(cabecera.getId_usuario()));
                    ps.setString(7, "SIGUIENTE");
                    ps.setInt(8, cabecera.getId_arqueo());
                    r = ps.executeUpdate();
                }
            }
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DAOarqueocaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int AbrirCaja(ArrayList<arqueocaja> ac, int id_usuario, int cambio) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            con.setAutoCommit(false);
            for (arqueocaja arqueo : ac) {
                arqueocaja arq = new arqueocaja();
                PreparedStatement ps = con.prepareStatement("SET @id_caja=(SELECT id_caja FROM usuarios WHERE id_usuario=?)");
                ps.setInt(1, id_usuario);
                ps.execute();
                ps = con.prepareStatement("SELECT SUM(debe_caja) as entrada, SUM(haber_caja) AS salida FROM detalle_caja WHERE id_caja=@id_caja");
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    arq.setCalculado(rs.getInt("entrada") - rs.getInt("salida"));
                }

                ps = con.prepareStatement("SET @id_detalle=(SELECT max(detalle_caja.id_detalle) FROM detalle_caja, usuarios WHERE detalle_caja.id_caja=usuarios.id_caja AND usuarios.id_usuario=?)");
                ps.setInt(1, Integer.valueOf(arqueo.getId_usuario()));
                ps.execute();

                ps = con.prepareStatement("SET @id_caja=(SELECT id_caja FROM usuarios WHERE id_usuario=?)");
                ps.setInt(1, Integer.valueOf(arqueo.getId_usuario()));
                ps.execute();

                ps = con.prepareStatement("SET @saldo_actual=(SELECT saldo_actual FROM detalle_caja WHERE id_detalle=@id_detalle)");
                ps.execute();
                ps = con.prepareStatement("INSERT INTO detalle_caja (fecha_detalle, hora_detalle, concepto_detalle, tipo_pago, saldo_anterior, saldo_actual, debe_caja, haber_caja, id_usuario, tipo_detalle, id_caja) VALUES (NOW(), NOW(), ?, ?, 0, ?, ?, ?, ?, ?, @id_caja)", PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, "APERTURA DE CAJA SEGÚN");
                ps.setInt(2, arqueo.getId_tipo_pago());
                ps.setInt(3, 0);
                ps.setInt(4, 0);
                ps.setInt(5, 0);
                ps.setInt(6, id_usuario);
                ps.setString(7, "APERTURA");
//                    if (arqueo.getContado() != rs.getInt("calculado")) {
//                        if (arqueo.getContado() > rs.getInt("calculado")) {
//                            ps.setString(1, "(+) AJUSTE AUTOMÁTICO DE CAJA SEGÚN");
//                            ps.setInt(2, arqueo.getId_tipo_pago());
//                            ps.setInt(3, id_usuario);//SaldoActual
//                            ps.setInt(4, id_usuario);//SaldoActual
//                            ps.setInt(5, arqueo.getContado() - rs.getInt("calculado"));
//                            ps.setInt(6, arqueo.getContado() - rs.getInt("calculado"));
//                            ps.setInt(7, 0);
//                            ps.setInt(8, id_usuario);
//                            ps.setString(9, "E");
//                            ps.setInt(10, id_usuario);
//                        } else {
//                            ps.setString(1, "(-) AJUSTE AUTOMÁTICO DE CAJA SEGÚN");
//                            ps.setInt(2, arqueo.getId_tipo_pago());
//                            ps.setInt(3, id_usuario);//SaldoActual
//                            ps.setInt(4, id_usuario);//SaldoActual
//                            ps.setInt(5, (rs.getInt("calculado") - arqueo.getContado()) * -1);
//                            ps.setInt(6, 0);
//                            ps.setInt(7, rs.getInt("calculado") - arqueo.getContado());
//                            ps.setInt(8, id_usuario);
//                            ps.setString(9, "S");
//                            ps.setInt(10, id_usuario);
//                        }
//                    } else {
//                        
//                    }
//                } else {
//                    if (arqueo.getContado() > 0) {
//                        ps.setString(1, "(+) AJUSTE AUTOMÁTICO DE CAJA SEGÚN");
//                        ps.setInt(2, arqueo.getId_tipo_pago());
//                        ps.setInt(3, id_usuario);//SaldoActual
//                        ps.setInt(4, id_usuario);//SaldoActual
//                        ps.setInt(5, arqueo.getContado());
//                        ps.setInt(6, arqueo.getContado());
//                        ps.setInt(7, 0);
//                        ps.setInt(8, id_usuario);
//                        ps.setString(9, "E");
//                        ps.setInt(10, id_usuario);
//                    } else {
//                        ps.setString(1, "(=) APERTURA DE CAJA SEGÚN");
//                        ps.setInt(2, arqueo.getId_tipo_pago());
//                        ps.setInt(3, id_usuario);//SaldoActual
//                        ps.setInt(4, id_usuario);//SaldoActual
//                        ps.setInt(5, arqueo.getContado());
//                        ps.setInt(6, arqueo.getContado());
//                        ps.setInt(7, 0);
//                        ps.setInt(8, id_usuario);
//                        ps.setString(9, "APERTURA");
//                        ps.setInt(10, id_usuario);
//                    }
                r = ps.executeUpdate();
            }
            PreparedStatement ps = con.prepareStatement("SET @id_detalle=(SELECT max(detalle_caja.id_detalle) FROM detalle_caja, usuarios WHERE detalle_caja.id_caja=usuarios.id_caja AND usuarios.id_usuario=?)");
            ps.setInt(1, id_usuario);
            ps.execute();

            ps = con.prepareStatement("SET @id_caja=(SELECT id_caja FROM usuarios WHERE id_usuario=?)");
            ps.setInt(1, id_usuario);
            ps.execute();

            ps = con.prepareStatement("SET @saldo_actual=(SELECT saldo_actual FROM detalle_caja WHERE id_detalle=@id_detalle)");
            ps.execute();

            ps = con.prepareStatement("INSERT INTO detalle_caja (fecha_detalle, hora_detalle, concepto_detalle, saldo_anterior, saldo_actual, debe_caja, haber_caja, id_caja, id_venta, id_compra, id_usuario, tipo_detalle) VALUES (NOW(), NOW(), ?, 0,  ?, ?, ?,  @id_caja, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, "APERTURA DE CAJA CAMBIO INICIAL DEL DÍA");
            ps.setInt(2, cambio);
            ps.setInt(3, cambio);
            ps.setInt(4, 0);
            ps.setString(5, null);
            ps.setString(6, null);
            ps.setInt(7, id_usuario);
            ps.setString(8, "APERTURA");
            r = ps.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DAOarqueocaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public ArrayList<arqueocaja> ObtenerMovientosCaja(int id_usuario) {
        Connection con = conexion.getConnection();
        ArrayList<arqueocaja> arqueo = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SET @id_caja=(SELECT id_caja FROM usuarios WHERE id_usuario=?)");
            ps.setInt(1, id_usuario);
            ps.execute();
            ps = con.prepareStatement("SET @id_detalle=(SELECT MAX(id_detalle) FROM detalle_caja, usuarios WHERE tipo_detalle='APERTURA' AND detalle_caja.id_caja=usuarios.id_caja AND usuarios.id_usuario=?)");
            ps.setInt(1, id_usuario);
            ps.execute();
            ps = con.prepareStatement("SET @fecha_detalle=(SELECT STR_TO_DATE(CONCAT(fecha_detalle,' ',hora_detalle), '%Y-%m-%d %H:%i:%S') as fecha FROM detalle_caja WHERE id_detalle=@id_detalle)");
            ps.execute();
            ps = con.prepareStatement("SELECT SUM(debe_caja) as entrada, SUM(haber_caja) AS salida FROM detalle_caja WHERE id_caja=@id_caja AND STR_TO_DATE(CONCAT(fecha_detalle,' ',hora_detalle), '%Y-%m-%d %H:%i:%S') BETWEEN @fecha_detalle AND NOW()");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                arqueocaja arq = new arqueocaja();
                arq.setId_tipo_pago(1);
                arq.setCalculado(rs.getInt("entrada") - rs.getInt("salida"));
                arqueo.add(arq);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOarqueocaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return arqueo;
    }

    public void UpdateEstadoArqueo(int id_usuario) {
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SET @id_caja=(SELECT id_caja FROM usuarios WHERE id_usuario=?)");
            ps.setInt(1, id_usuario);
            ps.execute();

            ps = con.prepareStatement("UPDATE arqueo_caja SET estado=? WHERE id_caja=@id_caja");
            ps.setString(1, "O");
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOarqueocaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
    }

    public int ValidarParametros(String fecha_inicio, String fecha_fin, int id_usuario) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps;
            if (id_usuario == 0) {
                ps = con.prepareStatement("SELECT * FROM arqueo_caja WHERE fecha_arqueo BETWEEN ? AND ?");
                ps.setString(1, f.ParseDate(fecha_inicio));
                ps.setString(2, f.ParseDate(fecha_fin));
            } else {
                ps = con.prepareStatement("SELECT * FROM arqueo_caja WHERE fecha_arqueo BETWEEN ? AND ? AND id_usuario=?");
                ps.setString(1, f.ParseDate(fecha_inicio));
                ps.setString(2, f.ParseDate(fecha_fin));
                ps.setInt(3, id_usuario);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                r = 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOarqueocaja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
