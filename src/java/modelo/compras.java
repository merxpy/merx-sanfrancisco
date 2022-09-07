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
public class compras extends detalle_compra {

    int id_compra;
    String factura_compra;
    int timbrado_compra;
    String fecha_compra;
    String tipo_compra;
    String total_compra;
    String total_iva_compra;
    String close_compra;
    String aprobado_compra;
    String tipo_dato;
    int grav10_compra;
    int iva10_compra;
    int grav5_compra;
    int iva5_compra;
    int exenta_compra;
    int total_descuento;

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public String getFactura_compra() {
        return factura_compra;
    }

    public void setFactura_compra(String factura_compra) {
        this.factura_compra = factura_compra;
    }

    public int getTimbrado_compra() {
        return timbrado_compra;
    }

    public void setTimbrado_compra(int timbrado_compra) {
        this.timbrado_compra = timbrado_compra;
    }

    public String getFecha_compra() {
        return fecha_compra;
    }

    public void setFecha_compra(String fecha_compra) {
        this.fecha_compra = fecha_compra;
    }

    public String getTipo_compra() {
        return tipo_compra;
    }

    public void setTipo_compra(String tipo_compra) {
        this.tipo_compra = tipo_compra;
    }

    public String getTotal_compra() {
        return total_compra;
    }

    public void setTotal_compra(String total_compra) {
        this.total_compra = total_compra;
    }

    public String getClose_compra() {
        return close_compra;
    }

    public String getTotal_iva_compra() {
        return total_iva_compra;
    }

    public void setTotal_iva_compra(String total_iva_compra) {
        this.total_iva_compra = total_iva_compra;
    }

    public void setClose_compra(String close_compra) {
        this.close_compra = close_compra;
    }

    public String getAprobado_compra() {
        return aprobado_compra;
    }

    public void setAprobado_compra(String aprobado_compra) {
        this.aprobado_compra = aprobado_compra;
    }

    public String getTipo_dato() {
        return tipo_dato;
    }

    public void setTipo_dato(String tipo_dato) {
        this.tipo_dato = tipo_dato;
    }

    public int getGrav10_compra() {
        return grav10_compra;
    }

    public void setGrav10_compra(int grav10_compra) {
        this.grav10_compra = grav10_compra;
    }

    public int getIva10_compra() {
        return iva10_compra;
    }

    public void setIva10_compra(int iva10_compra) {
        this.iva10_compra = iva10_compra;
    }

    public int getGrav5_compra() {
        return grav5_compra;
    }

    public void setGrav5_compra(int grav5_compra) {
        this.grav5_compra = grav5_compra;
    }

    public int getIva5_compra() {
        return iva5_compra;
    }

    public void setIva5_compra(int iva5_compra) {
        this.iva5_compra = iva5_compra;
    }

    public int getExenta_compra() {
        return exenta_compra;
    }

    public void setExenta_compra(int exenta_compra) {
        this.exenta_compra = exenta_compra;
    }

    public int getTotal_descuento() {
        return total_descuento;
    }

    public void setTotal_descuento(int total_descuento) {
        this.total_descuento = total_descuento;
    }

}
