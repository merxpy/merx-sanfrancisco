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
public class nfacturas extends timbrado {

    int id_nfactura;
    String del_nfac;
    String al_nfac;
    String estado_nfac;

    public int getId_nfactura() {
        return id_nfactura;
    }

    public void setId_nfactura(int id_nfactura) {
        this.id_nfactura = id_nfactura;
    }

    public String getDel_nfac() {
        return del_nfac;
    }

    public void setDel_nfac(String del_nfac) {
        this.del_nfac = del_nfac;
    }

    public String getAl_nfac() {
        return al_nfac;
    }

    public void setAl_nfac(String al_nfac) {
        this.al_nfac = al_nfac;
    }

    public String getEstado_nfac() {
        return estado_nfac;
    }

    public void setEstado_nfac(String estado_nfac) {
        this.estado_nfac = estado_nfac;
    }
}
