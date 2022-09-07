/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author 2HDEV
 */
public class historial_de_bajas extends mermas {

    int id_historial_de_baja;
    String fecha_hora_baja;
    int cantidad_baja;
    int total_baja;

    public int getId_historial_de_baja() {
        return id_historial_de_baja;
    }

    public void setId_historial_de_baja(int id_historial_de_baja) {
        this.id_historial_de_baja = id_historial_de_baja;
    }

    public String getFecha_hora_baja() {
        return fecha_hora_baja;
    }

    public void setFecha_hora_baja(String fecha_hora_baja) {
        this.fecha_hora_baja = fecha_hora_baja;
    }

    public int getCantidad_baja() {
        return cantidad_baja;
    }

    public void setCantidad_baja(int cantidad_baja) {
        this.cantidad_baja = cantidad_baja;
    }

    public int getTotal_baja() {
        return total_baja;
    }

    public void setTotal_baja(int total_baja) {
        this.total_baja = total_baja;
    }

}
