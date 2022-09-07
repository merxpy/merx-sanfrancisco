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
public class pedidos extends deliveries {

    int id_pedido;
    int nro_orden;
    String fecha_ped;
    int close_ped;
    int total_ped;
    int total_iva_ped;
    int descuento_ped;
    String comentario_ped;
    int aprobado_ped;
    String pagado_ped;
    int facturado_ped;
    String fechahora_entrega;
    String lugar_entrega;
    String retirado_ped;
    int delivery_ped;
    int senha_ped;
    String nota_especial;
    int creado_por;
    int asignado_a;
    String motivo_anulado;
    int saldo;

    String nro_movimiento;

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getNro_orden() {
        return nro_orden;
    }

    public void setNro_orden(int nro_orden) {
        this.nro_orden = nro_orden;
    }

    public String getFecha_ped() {
        return fecha_ped;
    }

    public void setFecha_ped(String fecha_ped) {
        this.fecha_ped = fecha_ped;
    }

    public int getClose_ped() {
        return close_ped;
    }

    public void setClose_ped(int close_ped) {
        this.close_ped = close_ped;
    }

    public int getTotal_ped() {
        return total_ped;
    }

    public void setTotal_ped(int total_ped) {
        this.total_ped = total_ped;
    }

    public int getTotal_iva_ped() {
        return total_iva_ped;
    }

    public void setTotal_iva_ped(int total_iva_ped) {
        this.total_iva_ped = total_iva_ped;
    }

    public int getDescuento_ped() {
        return descuento_ped;
    }

    public void setDescuento_ped(int descuento_ped) {
        this.descuento_ped = descuento_ped;
    }

    public String getComentario_ped() {
        return comentario_ped;
    }

    public void setComentario_ped(String comentario_ped) {
        this.comentario_ped = comentario_ped;
    }

    public int getAprobado_ped() {
        return aprobado_ped;
    }

    public void setAprobado_ped(int aprobado_ped) {
        this.aprobado_ped = aprobado_ped;
    }

    public String getPagado_ped() {
        return pagado_ped;
    }

    public void setPagado_ped(String pagado_ped) {
        this.pagado_ped = pagado_ped;
    }

    public int getFacturado_ped() {
        return facturado_ped;
    }

    public void setFacturado_ped(int facturado_ped) {
        this.facturado_ped = facturado_ped;
    }

    public String getFechahora_entrega() {
        return fechahora_entrega;
    }

    public void setFechahora_entrega(String fechahora_entrega) {
        this.fechahora_entrega = fechahora_entrega;
    }

    public String getLugar_entrega() {
        return lugar_entrega;
    }

    public void setLugar_entrega(String lugar_entrega) {
        this.lugar_entrega = lugar_entrega;
    }

    public String getRetirado_ped() {
        return retirado_ped;
    }

    public void setRetirado_ped(String retirado_ped) {
        this.retirado_ped = retirado_ped;
    }

    public int getSenha_ped() {
        return senha_ped;
    }

    public void setSenha_ped(int senha_ped) {
        this.senha_ped = senha_ped;
    }

    public int getDelivery_ped() {
        return delivery_ped;
    }

    public void setDelivery_ped(int delivery_ped) {
        this.delivery_ped = delivery_ped;
    }

    public String getNota_especial() {
        return nota_especial;
    }

    public void setNota_especial(String nota_especial) {
        this.nota_especial = nota_especial;
    }

    public int getCreado_por() {
        return creado_por;
    }

    public void setCreado_por(int creado_por) {
        this.creado_por = creado_por;
    }

    public int getAsignado_a() {
        return asignado_a;
    }

    public void setAsignado_a(int asignado_a) {
        this.asignado_a = asignado_a;
    }

    public String getMotivo_anulado() {
        return motivo_anulado;
    }

    public void setMotivo_anulado(String motivo_anulado) {
        this.motivo_anulado = motivo_anulado;
    }

    public String getNro_movimiento() {
        return nro_movimiento;
    }

    public void setNro_movimiento(String nro_movimiento) {
        this.nro_movimiento = nro_movimiento;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
}
