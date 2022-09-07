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
public class unidades extends maquinaria {

    String id_unidad;
    String nombre_u;
    int estado_unidad;
    String abreviatura_u;

    public String getId_unidad() {
        return id_unidad;
    }

    public void setId_unidad(String id_unidad) {
        this.id_unidad = id_unidad;
    }

    public String getNombre_u() {
        return nombre_u;
    }

    public void setNombre_u(String nombre_u) {
        this.nombre_u = nombre_u;
    }

    public int getEstado_unidad() {
        return estado_unidad;
    }

    public void setEstado_unidad(int estado_unidad) {
        this.estado_unidad = estado_unidad;
    }

    public String getAbreviatura_u() {
        return abreviatura_u;
    }

    public void setAbreviatura_u(String abreviatura_u) {
        this.abreviatura_u = abreviatura_u;
    }

}
