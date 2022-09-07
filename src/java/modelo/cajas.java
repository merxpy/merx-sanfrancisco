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
public class cajas extends sucursales {

    int id_caja;
    String codigo_caja;

    public int getId_caja() {
        return id_caja;
    }

    public void setId_caja(int id_caja) {
        this.id_caja = id_caja;
    }

    public String getCodigo_caja() {
        return codigo_caja;
    }

    public void setCodigo_caja(String codigo_caja) {
        this.codigo_caja = codigo_caja;
    }
}
