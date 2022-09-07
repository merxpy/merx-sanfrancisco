/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.mysql.jdbc.Statement;
import conexion.conexion;
import extras.funciones;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.deliveries;

/**
 *
 * @author 2HDEV
 */
public class DAOdeliveries {

    funciones func = new funciones();

    public ArrayList<deliveries> ListarDeliveries(String estado) {
        ArrayList<deliveries> deliveries = new ArrayList<>();
        Connection con = conexion.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT deliveries.*, pedidos.id_pedido FROM deliveries LEFT JOIN pedidos ON deliveries.id_delivery=pedidos.id_delivery WHERE estado=? AND estado!='FINALIZADO'");
            ps.setString(1, estado);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                deliveries delivery = new deliveries();
                delivery.setId_delivery(rs.getInt("id_delivery"));
                delivery.setNombre(rs.getString("nombre"));
                delivery.setDireccion(rs.getString("direccion"));
                delivery.setUbicacion(rs.getString("ubicacion"));
                delivery.setReferencia(rs.getString("referencia"));
                delivery.setTelefono(rs.getString("telefono"));
                delivery.setFecha(rs.getString("fecha"));
                delivery.setHora_inicio(rs.getString("hora_inicio"));
                delivery.setHora_fin(rs.getString("hora_fin"));
                delivery.setObservaciones(rs.getString("observaciones"));
                delivery.setCliente_ref(rs.getInt("cliente_ref"));
                delivery.setId_ciudad(rs.getString("id_pedido"));
                deliveries.add(delivery);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOdeliveries.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return deliveries;
    }

    public deliveries BuscarDeliveries(int id) {
        Connection con = conexion.getConnection();
        deliveries delivery = new deliveries();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT deliveries.*, clientes.*, pedidos.total_ped, tipo_pago.descripcion "
                    + "FROM deliveries LEFT JOIN clientes ON clientes.id_cliente=deliveries.cliente_ref LEFT JOIN "
                    + "pedidos ON pedidos.id_delivery=deliveries.id_delivery, tipo_pago WHERE tipo_pago.id_tipo_pago=deliveries.id_tipo_pago "
                    + "AND deliveries.id_delivery=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                delivery.setId_delivery(rs.getInt("id_delivery"));
                delivery.setId_cliente(rs.getString("id_cliente"));
                delivery.setCedula(rs.getString("ndocumento_cli"));
                delivery.setNombre(rs.getString("nombre_cli"));
                delivery.setRazon_social(rs.getString("razon_social"));
                delivery.setRuc(rs.getString("ruc"));
                delivery.setRuc_dv(rs.getString("ruc_dv"));
                delivery.setDireccion(rs.getString("direccion"));
                delivery.setUbicacion(rs.getString("ubicacion"));
                delivery.setTelefono(rs.getString("telefono"));
                delivery.setFecha(func.ParseDI(rs.getString("fecha")));
                delivery.setHora_inicio(rs.getString("hora_inicio"));
                delivery.setHora_fin(rs.getString("hora_fin"));
                delivery.setCelular(rs.getString("celular"));
                delivery.setReferencia(rs.getString("referencia"));
                delivery.setObservaciones(rs.getString("observaciones"));
                delivery.setCliente_ref(rs.getInt("cliente_ref"));
                delivery.setDescripcion("Forma de Pago: " + rs.getString("descripcion") + "\nGs. " + rs.getInt("total_pago") + (rs.getInt("total_ped") > 0 ? "\nTotal a pagar: " + rs.getInt("total_ped") + "\nVuelto " + (rs.getInt("total_ped") - rs.getInt("total_ped")) : ""));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOdeliveries.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return delivery;
    }

    public int AgregarDelivery(deliveries delivery) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("INSERT INTO deliveries(nombre, direccion, ubicacion, referencia, telefono, celular, fecha, total_pago, "
                    + "hora_inicio, hora_fin, observaciones, cliente_ref, id_tipo_pago) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, delivery.getNombre());
            ps.setString(2, delivery.getDireccion());
            ps.setString(3, delivery.getUbicacion());
            ps.setString(4, delivery.getReferencia());
            ps.setString(5, delivery.getTelefono());
            ps.setString(6, delivery.getCelular());
            ps.setString(7, func.ParseDate(delivery.getFecha()));
            ps.setInt(8, delivery.getTotal_pago());
            ps.setString(9, delivery.getHora_inicio());
            ps.setString(10, delivery.getHora_fin());
            ps.setString(11, func.NoPosee(delivery.getObservaciones()));
            ps.setInt(12, delivery.getCliente_ref());
            ps.setInt(13, delivery.getId_tipo_pago());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                r = (int) rs.getLong(1);
                ps = con.prepareStatement("UPDATE clientes SET direccion_cli=?, googlemaps_cli=?, referencia_cli=?, hora_inicio_cli=?, hora_fin_cli=?, razon_social=?, ruc=?, ruc_dv=? WHERE id_cliente=?");
                ps.setString(1, delivery.getDireccion());
                ps.setString(2, delivery.getUbicacion());
                ps.setString(3, delivery.getReferencia());
                ps.setString(4, delivery.getHora_inicio());
                ps.setString(5, delivery.getHora_fin());
                ps.setString(6, delivery.getRazon_social());
                ps.setString(7, delivery.getRuc());
                ps.setString(8, delivery.getRuc_dv());
                ps.setInt(9, delivery.getCliente_ref());
                ps.executeUpdate();
            }
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DAOdeliveries.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int ActualizarDelivery(deliveries delivery) {
        Connection con = conexion.getConnection();
        int r = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE deliveries SET nombre=?, direccion=?, ubicacion=?, referencia=?, telefono=?, celular=?, fecha=?, total_pago=?, "
                    + "hora_inicio=?, hora_fin=?, observaciones=?, cliente_ref=?, id_tipo_pago=? WHERE id_delivery=?");
            ps.setString(1, delivery.getNombre());
            ps.setString(2, delivery.getDireccion());
            ps.setString(3, delivery.getUbicacion());
            ps.setString(4, delivery.getReferencia());
            ps.setString(5, delivery.getTelefono());
            ps.setString(6, delivery.getCelular());
            ps.setString(7, func.ParseDate(delivery.getFecha()));
            ps.setInt(8, delivery.getTotal_pago());
            ps.setString(9, delivery.getHora_inicio());
            ps.setString(10, delivery.getHora_fin());
            ps.setString(11, func.NoPosee(delivery.getObservaciones()));
            ps.setInt(12, delivery.getCliente_ref());
            ps.setInt(13, delivery.getId_tipo_pago());
            ps.setInt(14, delivery.getId_delivery());
            r = ps.executeUpdate();

            ps = con.prepareStatement("UPDATE clientes SET direccion_cli=?, googlemaps_cli=?, referencia_cli=?, hora_inicio_cli=?, hora_fin_cli=?, razon_social=?, ruc=?, ruc_dv=? WHERE id_cliente=?");
            ps.setString(1, delivery.getDireccion());
            ps.setString(2, delivery.getUbicacion());
            ps.setString(3, delivery.getReferencia());
            ps.setString(4, delivery.getHora_inicio());
            ps.setString(5, delivery.getHora_fin());
            ps.setString(6, delivery.getRazon_social());
            ps.setString(7, delivery.getRuc());
            ps.setString(8, delivery.getRuc_dv());
            ps.setInt(9, delivery.getCliente_ref());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOdeliveries.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int EliminarDelivery(int id) {
        Connection con = conexion.getConnection();
        int r = 0;
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("SELECT * FROM pedidos WHERE id_delivery=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("close_ped") != 0) {
                    ps = con.prepareStatement("UPDATE pedidos SET id_delivery=NULL WHERE id_delivery=?");
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    ps = con.prepareStatement("DELETE FROM deliveries WHERE id_delivery=?");
                    ps.setInt(1, id);
                    r = ps.executeUpdate();
                }
            } else {
                ps = con.prepareStatement("DELETE FROM deliveries WHERE id_delivery=?");
                ps.setInt(1, id);
                r = ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOdeliveries.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }

    public int UpdateEstado(deliveries delivery) {
        Connection con = conexion.getConnection();
        PreparedStatement ps;
        int r = 0;
        try {
            ps = con.prepareStatement("UPDATE deliveries SET estado=? WHERE id_delivery=?");
            ps.setString(1, delivery.getEstado());
            ps.setInt(2, delivery.getId_delivery());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOdeliveries.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.Close(con);
        }
        return r;
    }
}
