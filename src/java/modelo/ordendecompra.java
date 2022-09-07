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
public class ordendecompra extends detalle_orden {

    String id_orden_compra;
    String fecha_ord;
    String aprobado_ord;
    String recibido_ord;
    String close_ord;
    String total_ord;
    String total_iva_ord;
    String comentario_ord;

    public String getId_orden_compra() {
        return id_orden_compra;
    }

    public void setId_orden_compra(String id_orden_compra) {
        this.id_orden_compra = id_orden_compra;
    }

    public String getFecha_ord() {
        return fecha_ord;
    }

    public void setFecha_ord(String fecha_ord) {
        this.fecha_ord = fecha_ord;
    }

    public String getAprobado_ord() {
        return aprobado_ord;
    }

    public void setAprobado_ord(String aprobado_ord) {
        this.aprobado_ord = aprobado_ord;
    }

    public String getRecibido_ord() {
        return recibido_ord;
    }

    public void setRecibido_ord(String recibido_ord) {
        this.recibido_ord = recibido_ord;
    }

    public String getClose_ord() {
        return close_ord;
    }

    public void setClose_ord(String close_ord) {
        this.close_ord = close_ord;
    }

    public String getTotal_ord() {
        return total_ord;
    }

    public void setTotal_ord(String total_ord) {
        this.total_ord = total_ord;
    }

    public String getTotal_iva_ord() {
        return total_iva_ord;
    }

    public void setTotal_iva_ord(String total_iva_ord) {
        this.total_iva_ord = total_iva_ord;
    }

    public String getComentario_ord() {
        return comentario_ord;
    }

    public void setComentario_ord(String comentario_ord) {
        this.comentario_ord = comentario_ord;
    }

}
