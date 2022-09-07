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
public class detalle_orden extends proveedores {

    String codigo_ord;
    String descripcion_ord;
    String cantidad_ord;
    String precio_ord;
    String iva_ord;
    String subtotal_ord;

    public String getCodigo_ord() {
        return codigo_ord;
    }

    public void setCodigo_ord(String codigo_ord) {
        this.codigo_ord = codigo_ord;
    }

    public String getDescripcion_ord() {
        return descripcion_ord;
    }

    public void setDescripcion_ord(String descripcion_ord) {
        this.descripcion_ord = descripcion_ord;
    }

    public String getCantidad_ord() {
        return cantidad_ord;
    }

    public void setCantidad_ord(String cantidad_ord) {
        this.cantidad_ord = cantidad_ord;
    }

    public String getPrecio_ord() {
        return precio_ord;
    }

    public void setPrecio_ord(String precio_ord) {
        this.precio_ord = precio_ord;
    }

    public String getIva_ord() {
        return iva_ord;
    }

    public void setIva_ord(String iva_ord) {
        this.iva_ord = iva_ord;
    }

    public String getSubtotal_ord() {
        return subtotal_ord;
    }

    public void setSubtotal_ord(String subtotal_ord) {
        this.subtotal_ord = subtotal_ord;
    }
}
