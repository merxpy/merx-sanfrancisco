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
public class mermas extends stock {

    int id_merma;
    String concepto_merma;
    String tipo_merma;
    int cantidad;
    int total;

    public int getId_merma() {
        return id_merma;
    }

    public void setId_merma(int id_merma) {
        this.id_merma = id_merma;
    }

    public String getConcepto_merma() {
        return concepto_merma;
    }

    public void setConcepto_merma(String concepto_merma) {
        this.concepto_merma = concepto_merma;
    }

    public String getTipo_merma() {
        return tipo_merma;
    }

    public void setTipo_merma(String tipo_merma) {
        this.tipo_merma = tipo_merma;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
