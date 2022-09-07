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
import modelo.notificaciones;

/**
 *
 * @author 2HDEV
 */
public class DAOnotificaciones {

    public ArrayList<notificaciones> ListarNotificaciones() {
        Connection con = conexion.getConnection();
        ArrayList<notificaciones> notifications = new ArrayList<>();
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("SELECT id_cliente as id_notificado, CONCAT_WS(' ','Felicita a', nombre_cli, 'por su cumpleaños') as mensaje "
                    + "FROM clientes WHERE id_cliente  AND DAY(fecha_nac)=DAY(NOW()) AND MONTH(fecha_nac)=MONTH(NOW()) AND close_cli=0");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                notificaciones notificacion = new notificaciones();
                notificacion.setId_notificado(rs.getInt("id_notificado"));
                notificacion.setMensaje(rs.getString("mensaje"));
                notifications.add(notificacion);
            }
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DAOnotificaciones.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return notifications;
    }

}
