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
public class ciudades extends departamentos {

    String id_ciudad;
    String nombre_ciu;

    public String getId_ciudad() {
        return id_ciudad;
    }

    public void setId_ciudad(String id_ciudad) {
        this.id_ciudad = id_ciudad;
    }

    public String getNombre_ciu() {
        return nombre_ciu;
    }

    public void setNombre_ciu(String nombre_ciu) {
        this.nombre_ciu = nombre_ciu;
    }

}
