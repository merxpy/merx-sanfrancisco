/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;

/**
 *
 * @author 2HDEV
 */
public class gastos extends proveedores {

    int id_gasto;
    String nro_factura;
    int timbrado;
    Date fecha;
    String tipo_gasto;
    int total_5;
    int total_10;
    int total_exenta;
    int iva_5;
    int iva_10;
    int total_gasto;

    public int getId_gasto() {
        return id_gasto;
    }

    public void setId_gasto(int id_gasto) {
        this.id_gasto = id_gasto;
    }

    public String getNro_factura() {
        return nro_factura;
    }

    public void setNro_factura(String nro_factura) {
        this.nro_factura = nro_factura;
    }

    public int getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(int timbrado) {
        this.timbrado = timbrado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTipo_gasto() {
        return tipo_gasto;
    }

    public void setTipo_gasto(String tipo_gasto) {
        this.tipo_gasto = tipo_gasto;
    }

    public int getTotal_5() {
        return total_5;
    }

    public void setTotal_5(int total_5) {
        this.total_5 = total_5;
    }

    public int getTotal_10() {
        return total_10;
    }

    public void setTotal_10(int total_10) {
        this.total_10 = total_10;
    }

    public int getTotal_exenta() {
        return total_exenta;
    }

    public void setTotal_exenta(int total_exenta) {
        this.total_exenta = total_exenta;
    }

    public int getIva_5() {
        return iva_5;
    }

    public void setIva_5(int iva_5) {
        this.iva_5 = iva_5;
    }

    public int getIva_10() {
        return iva_10;
    }

    public void setIva_10(int iva_10) {
        this.iva_10 = iva_10;
    }

    public int getTotal_gasto() {
        return total_gasto;
    }

    public void setTotal_gasto(int total_gasto) {
        this.total_gasto = total_gasto;
    }

}
