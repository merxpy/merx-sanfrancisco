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
public class ventas extends tipo_pago {

    String id_venta;
    String nfactura_venta;
    String fecha_venta;
    String hora_venta;
    String tipo_venta;
    String total_venta;
    String total_iva_venta;
    String close_venta;
    String sucursal_id_sucursal;
    int totaliva5;
    int totaliva10;
    int grav10_venta;
    int grav5_venta;
    int exenta_venta;
    String cupon_venta;
    int impreso;
    int total_descuento;
    String id_pedido;

    public String getId_venta() {
        return id_venta;
    }

    public void setId_venta(String id_venta) {
        this.id_venta = id_venta;
    }

    public String getNfactura_venta() {
        return nfactura_venta;
    }

    public void setNfactura_venta(String nfactura_venta) {
        this.nfactura_venta = nfactura_venta;
    }

    public String getFecha_venta() {
        return fecha_venta;
    }

    public void setFecha_venta(String fecha_venta) {
        this.fecha_venta = fecha_venta;
    }

    public String getHora_venta() {
        return hora_venta;
    }

    public void setHora_venta(String hora_venta) {
        this.hora_venta = hora_venta;
    }

    public String getTipo_venta() {
        return tipo_venta;
    }

    public void setTipo_venta(String tipo_venta) {
        this.tipo_venta = tipo_venta;
    }

    public String getTotal_venta() {
        return total_venta;
    }

    public void setTotal_venta(String total_venta) {
        this.total_venta = total_venta;
    }

    public String getTotal_iva_venta() {
        return total_iva_venta;
    }

    public void setTotal_iva_venta(String total_iva_venta) {
        this.total_iva_venta = total_iva_venta;
    }

    public String getClose_venta() {
        return close_venta;
    }

    public void setClose_venta(String close_venta) {
        this.close_venta = close_venta;
    }

    public String getSucursal_id_sucursal() {
        return sucursal_id_sucursal;
    }

    public void setSucursal_id_sucursal(String sucursal_id_sucursal) {
        this.sucursal_id_sucursal = sucursal_id_sucursal;
    }

    public int getTotaliva5() {
        return totaliva5;
    }

    public void setTotaliva5(int totaliva5) {
        this.totaliva5 = totaliva5;
    }

    public int getTotaliva10() {
        return totaliva10;
    }

    public void setTotaliva10(int totaliva10) {
        this.totaliva10 = totaliva10;
    }

    public String getCupon_venta() {
        return cupon_venta;
    }

    public void setCupon_venta(String cupon_venta) {
        this.cupon_venta = cupon_venta;
    }

    public int getImpreso() {
        return impreso;
    }

    public void setImpreso(int impreso) {
        this.impreso = impreso;
    }

    public int getGrav10_venta() {
        return grav10_venta;
    }

    public void setGrav10_venta(int grav10_venta) {
        this.grav10_venta = grav10_venta;
    }

    public int getGrav5_venta() {
        return grav5_venta;
    }

    public void setGrav5_venta(int grav5_venta) {
        this.grav5_venta = grav5_venta;
    }

    public int getExenta_venta() {
        return exenta_venta;
    }

    public void setExenta_venta(int exenta_venta) {
        this.exenta_venta = exenta_venta;
    }

    public int getTotal_descuento() {
        return total_descuento;
    }

    public void setTotal_descuento(int total_descuento) {
        this.total_descuento = total_descuento;
    }

    public String getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(String id_pedido) {
        this.id_pedido = id_pedido;
    }

}
