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
public class etiquetas extends unidades {

    String id_etiqueta;
    String nombre_e;
    int estado_etiqueta;
    String abreviacion_e;

    public String getId_etiqueta() {
        return id_etiqueta;
    }

    public void setId_etiqueta(String id_etiqueta) {
        this.id_etiqueta = id_etiqueta;
    }

    public String getNombre_e() {
        return nombre_e;
    }

    public void setNombre_e(String nombre_e) {
        this.nombre_e = nombre_e;
    }

    public int getEstado_etiqueta() {
        return estado_etiqueta;
    }

    public void setEstado_etiqueta(int estado_etiqueta) {
        this.estado_etiqueta = estado_etiqueta;
    }

    public String getAbreviacion_e() {
        return abreviacion_e;
    }

    public void setAbreviacion_e(String abreviatura_e) {
        this.abreviacion_e = abreviatura_e;
    }

}
