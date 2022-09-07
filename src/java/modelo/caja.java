/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author akio
 */
public class caja extends detalle_cierre {

    String fecha_caja;
    String hora_caja;
    int saldo_anterior;
    int saldo_actual;
    int debe_caja;
    int haber_caja;
    String concepto_caja;
    String compras_id_compra;
    String ventas_id_venta;
    String estado_caja;

    public String getFecha_caja() {
        return fecha_caja;
    }

    public void setFecha_caja(String fecha_caja) {
        this.fecha_caja = fecha_caja;
    }

    public String getHora_caja() {
        return hora_caja;
    }

    public void setHora_caja(String hora_caja) {
        this.hora_caja = hora_caja;
    }

    public int getSaldo_anterior() {
        return saldo_anterior;
    }

    public void setSaldo_anterior(int saldo_anterior) {
        this.saldo_anterior = saldo_anterior;
    }

    public int getSaldo_actual() {
        return saldo_actual;
    }

    public void setSaldo_actual(int saldo_actual) {
        this.saldo_actual = saldo_actual;
    }

    public int getDebe_caja() {
        return debe_caja;
    }

    public void setDebe_caja(int debe_caja) {
        this.debe_caja = debe_caja;
    }

    public int getHaber_caja() {
        return haber_caja;
    }

    public void setHaber_caja(int haber_caja) {
        this.haber_caja = haber_caja;
    }

    public String getConcepto_caja() {
        return concepto_caja;
    }

    public void setConcepto_caja(String concepto_caja) {
        this.concepto_caja = concepto_caja;
    }

    public String getCompras_id_compra() {
        return compras_id_compra;
    }

    public void setCompras_id_compra(String compras_id_compra) {
        this.compras_id_compra = compras_id_compra;
    }

    public String getVentas_id_venta() {
        return ventas_id_venta;
    }

    public void setVentas_id_venta(String ventas_id_venta) {
        this.ventas_id_venta = ventas_id_venta;
    }

    public String getEstado_caja() {
        return estado_caja;
    }

    public void setEstado_caja(String estado_caja) {
        this.estado_caja = estado_caja;
    }
}
