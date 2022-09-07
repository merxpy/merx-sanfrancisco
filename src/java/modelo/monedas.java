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
public class monedas extends paises {

    int id_moneda;
    int valor_moneda;
    int close_moneda;

    public int getId_moneda() {
        return id_moneda;
    }

    public void setId_moneda(int id_moneda) {
        this.id_moneda = id_moneda;
    }

    public int getValor_moneda() {
        return valor_moneda;
    }

    public void setValor_moneda(int valor_moneda) {
        this.valor_moneda = valor_moneda;
    }

    public int getClose_moneda() {
        return close_moneda;
    }

    public void setClose_moneda(int close_moneda) {
        this.close_moneda = close_moneda;
    }

}
