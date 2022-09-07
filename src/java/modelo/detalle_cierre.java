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
public class detalle_cierre extends tipo_pago {

    int cantidad_cierre;
    int monto_total;

    public int getCantidad_cierre() {
        return cantidad_cierre;
    }

    public void setCantidad_cierre(int cantidad_cierre) {
        this.cantidad_cierre = cantidad_cierre;
    }

    public int getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(int monto_total) {
        this.monto_total = monto_total;
    }

}
