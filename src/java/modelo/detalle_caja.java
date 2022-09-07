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
public class detalle_caja extends detalle_cierre {

    int id_detalle;
    String fecha_detalle;
    String hora_detalle;
    int saldo_anterior;
    int saldo_actual;
    int debe_caja;
    int haber_caja;
    String concepto_detalle;
    int tipo_pago;
    String compras_id_compra;
    String ventas_id_venta;
    String estado_caja;
    int arqueo_caja;
    String tipo_detalle;
    int id_compra;
    int id_venta;

    public int getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(int id_detalle) {
        this.id_detalle = id_detalle;
    }

    public String getFecha_detalle() {
        return fecha_detalle;
    }

    public void setFecha_detalle(String fecha_detalle) {
        this.fecha_detalle = fecha_detalle;
    }

    public String getHora_detalle() {
        return hora_detalle;
    }

    public void setHora_detalle(String hora_detalle) {
        this.hora_detalle = hora_detalle;
    }

    public String getConcepto_detalle() {
        return concepto_detalle;
    }

    public void setConcepto_detalle(String concepto_detalle) {
        this.concepto_detalle = concepto_detalle;
    }

    public int getTipo_pago() {
        return tipo_pago;
    }

    public void setTipo_pago(int tipo_pago) {
        this.tipo_pago = tipo_pago;
    }

    public int getArqueo_caja() {
        return arqueo_caja;
    }

    public void setArqueo_caja(int arqueo_caja) {
        this.arqueo_caja = arqueo_caja;
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

    public String getTipo_detalle() {
        return tipo_detalle;
    }

    public void setTipo_detalle(String tipo_detalle) {
        this.tipo_detalle = tipo_detalle;
    }

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

}
