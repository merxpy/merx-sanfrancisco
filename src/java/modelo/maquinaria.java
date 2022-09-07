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
public class maquinaria extends marcas {

    int id_maquinaria;
    String tipo_maquinaria;
    String modelo;
    String serie;

    public int getId_maquinaria() {
        return id_maquinaria;
    }

    public void setId_maquinaria(int id_maquinaria) {
        this.id_maquinaria = id_maquinaria;
    }

    public String getTipo_maquinaria() {
        return tipo_maquinaria;
    }

    public void setTipo_maquinaria(String tipo_maquinaria) {
        this.tipo_maquinaria = tipo_maquinaria;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

}
