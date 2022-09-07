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
public class auditoria extends usuarios {

    String id_auditoria;
    String id_auditado;
    String tabla_au;
    String detalle_au;
    String fecha_hora_au;
    String usuario_au;

    public String getId_auditoria() {
        return id_auditoria;
    }

    public void setId_auditoria(String id_auditoria) {
        this.id_auditoria = id_auditoria;
    }

    public String getId_auditado() {
        return id_auditado;
    }

    public void setId_auditado(String id_auditado) {
        this.id_auditado = id_auditado;
    }

    public String getTabla_au() {
        return tabla_au;
    }

    public void setTabla_au(String tabla_au) {
        this.tabla_au = tabla_au;
    }

    public String getDetalle_au() {
        return detalle_au;
    }

    public void setDetalle_au(String detalle_au) {
        this.detalle_au = detalle_au;
    }

    public String getFecha_hora_au() {
        return fecha_hora_au;
    }

    public void setFecha_hora_au(String fecha_hora_au) {
        this.fecha_hora_au = fecha_hora_au;
    }

    public String getUsuario_au() {
        return usuario_au;
    }

    public void setUsuario_au(String usuario_au) {
        this.usuario_au = usuario_au;
    }

}
