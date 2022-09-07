/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Time;
import java.util.Date;

/**
 *
 * @author 2HDEV
 */
public class arqueocaja extends detalle_caja {

    int id_arqueo;
    Date fecha_arqueo;
    Time hora_arqueo;
    int calculado;
    int contado;
    int diferencia;
    int retirado;
    String estado;

    public int getId_arqueo() {
        return id_arqueo;
    }

    public void setId_arqueo(int id_arqueo) {
        this.id_arqueo = id_arqueo;
    }

    public Date getFecha_arqueo() {
        return fecha_arqueo;
    }

    public void setFecha_arqueo(Date fecha_arqueo) {
        this.fecha_arqueo = fecha_arqueo;
    }

    public Time getHora_arqueo() {
        return hora_arqueo;
    }

    public void setHora_arqueo(Time hora_arqueo) {
        this.hora_arqueo = hora_arqueo;
    }

    public int getCalculado() {
        return calculado;
    }

    public void setCalculado(int calculado) {
        this.calculado = calculado;
    }

    public int getContado() {
        return contado;
    }

    public void setContado(int contado) {
        this.contado = contado;
    }

    public int getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(int diferencia) {
        this.diferencia = diferencia;
    }

    public int getRetirado() {
        return retirado;
    }

    public void setRetirado(int retirado) {
        this.retirado = retirado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
