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
public class paises extends usuarios {

    int id_pais;
    String nombre_pais;
    String moneda_pais;
    String abreviacion_pais;
    String iso_mon_pais;
    String phone_code;
    int close_pais;

    public int getId_pais() {
        return id_pais;
    }

    public void setId_pais(int id_pais) {
        this.id_pais = id_pais;
    }

    public String getNombre_pais() {
        return nombre_pais;
    }

    public void setNombre_pais(String nombre_pais) {
        this.nombre_pais = nombre_pais;
    }

    public String getMoneda_pais() {
        return moneda_pais;
    }

    public void setMoneda_pais(String moneda_pais) {
        this.moneda_pais = moneda_pais;
    }

    public String getAbreviacion_pais() {
        return abreviacion_pais;
    }

    public void setAbreviacion_pais(String abreviacion_pais) {
        this.abreviacion_pais = abreviacion_pais;
    }

    public String getIso_mon_pais() {
        return iso_mon_pais;
    }

    public void setIso_mon_pais(String iso_mon_pais) {
        this.iso_mon_pais = iso_mon_pais;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public int getClose_pais() {
        return close_pais;
    }

    public void setClose_pais(int close_pais) {
        this.close_pais = close_pais;
    }

}
