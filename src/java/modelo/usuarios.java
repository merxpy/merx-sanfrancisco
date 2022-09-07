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
public class usuarios extends usuarios_has_permisos {

    String id_usuario;
    String alias_usu;
    String oldpass;
    String password_usu;
    String estado_usu;
    String creacion_usu;

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getAlias_usu() {
        return alias_usu;
    }

    public void setAlias_usu(String alias_usu) {
        this.alias_usu = alias_usu;
    }

    public String getOldpass() {
        return oldpass;
    }

    public void setOldpass(String oldpass) {
        this.oldpass = oldpass;
    }

    public String getPassword_usu() {
        return password_usu;
    }

    public void setPassword_usu(String password_usu) {
        this.password_usu = password_usu;
    }

    public String getEstado_usu() {
        return estado_usu;
    }

    public void setEstado_usu(String estado_usu) {
        this.estado_usu = estado_usu;
    }

    public String getCreacion_usu() {
        return creacion_usu;
    }

    public void setCreacion_usu(String creacion_usu) {
        this.creacion_usu = creacion_usu;
    }

}
