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
public class timbrado extends cajas {

    int id_timbrado;
    String numero_tim;
    String vencimiento_tim;

    public int getId_timbrado() {
        return id_timbrado;
    }

    public void setId_timbrado(int id_timbrado) {
        this.id_timbrado = id_timbrado;
    }

    public String getNumero_tim() {
        return numero_tim;
    }

    public void setNumero_tim(String numero_tim) {
        this.numero_tim = numero_tim;
    }

    public String getVencimiento_tim() {
        return vencimiento_tim;
    }

    public void setVencimiento_tim(String vencimiento_tim) {
        this.vencimiento_tim = vencimiento_tim;
    }
}
