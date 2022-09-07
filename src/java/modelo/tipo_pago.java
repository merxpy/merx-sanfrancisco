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
public class tipo_pago extends detalle_venta {

    int id_tipo_pago;
    String descripcion_pago;
    String abr;

    public int getId_tipo_pago() {
        return id_tipo_pago;
    }

    public void setId_tipo_pago(int id_tipo_pago) {
        this.id_tipo_pago = id_tipo_pago;
    }

    public String getDescripcion_pago() {
        return descripcion_pago;
    }

    public void setDescripcion_pago(String descripcion_pago) {
        this.descripcion_pago = descripcion_pago;
    }

    public String getAbr() {
        return abr;
    }

    public void setAbr(String abr) {
        this.abr = abr;
    }
}
