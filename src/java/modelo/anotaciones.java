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
public class anotaciones extends usuarios {

    int id_anotacion;
    String titulo_anotacion;
    String contenido_anotacion;
    String color_anotacion;
    String fechahora_anotacion;
    String vencimiento_anotacion;

    public int getId_anotacion() {
        return id_anotacion;
    }

    public void setId_anotacion(int id_anotacion) {
        this.id_anotacion = id_anotacion;
    }

    public String getTitulo_anotacion() {
        return titulo_anotacion;
    }

    public void setTitulo_anotacion(String titulo_anotacion) {
        this.titulo_anotacion = titulo_anotacion;
    }

    public String getContenido_anotacion() {
        return contenido_anotacion;
    }

    public void setContenido_anotacion(String contenido_anotacion) {
        this.contenido_anotacion = contenido_anotacion;
    }

    public String getColor_anotacion() {
        return color_anotacion;
    }

    public void setColor_anotacion(String color_anotacion) {
        this.color_anotacion = color_anotacion;
    }

    public String getFechahora_anotacion() {
        return fechahora_anotacion;
    }

    public void setFechahora_anotacion(String fechahora_anotacion) {
        this.fechahora_anotacion = fechahora_anotacion;
    }

    public String getVencimiento_anotacion() {
        return vencimiento_anotacion;
    }

    public void setVencimiento_anotacion(String vencimiento_anotacion) {
        this.vencimiento_anotacion = vencimiento_anotacion;
    }
}
