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
public class categorias extends etiquetas {

    int id_categoria;
    String categoria;
    String descripcion_cat;
    Boolean close_cat;
    int tipo_cat;

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion_cat() {
        return descripcion_cat;
    }

    public void setDescripcion_cat(String descripcion_cat) {
        this.descripcion_cat = descripcion_cat;
    }

    public Boolean getClose_cat() {
        return close_cat;
    }

    public void setClose_cat(Boolean close_cat) {
        this.close_cat = close_cat;
    }

    public int getTipo_cat() {
        return tipo_cat;
    }

    public void setTipo_cat(int tipo_cat) {
        this.tipo_cat = tipo_cat;
    }

}
