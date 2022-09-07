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
public class articulos extends categorias {

    String id_articulo;
    String codigo_ar;
    String nombre_ar;
    String barcode_ar;
    String descripcion_ar;
    String iva_ar;
    String precio_venta_ar;
    String precio_compra_ar;
    String utilidad;
    String monto_utilidad_ar;
    String precio_neto_ar;
    String precio_utilidad_ar;
    String iva_venta_ar;
    String foto_ar;
    String close_ar;
    String stock_minimo_ar;
    String stock_inicial;
    String unidades_id_unidad;
    String marcas_id_marca;
    String etiquetas_id_etiqueta;

    public String getId_articulo() {
        return id_articulo;
    }

    public void setId_articulo(String id_articulo) {
        this.id_articulo = id_articulo;
    }

    public String getCodigo_ar() {
        return codigo_ar;
    }

    public void setCodigo_ar(String codigo_ar) {
        this.codigo_ar = codigo_ar;
    }

    public String getNombre_ar() {
        return nombre_ar;
    }

    public void setNombre_ar(String nombre_ar) {
        this.nombre_ar = nombre_ar;
    }

    public String getBarcode_ar() {
        return barcode_ar;
    }

    public void setBarcode_ar(String barcode_ar) {
        this.barcode_ar = barcode_ar;
    }

    public String getDescripcion_ar() {
        return descripcion_ar;
    }

    public void setDescripcion_ar(String descripcion_ar) {
        this.descripcion_ar = descripcion_ar;
    }

    public String getIva_ar() {
        return iva_ar;
    }

    public void setIva_ar(String iva_ar) {
        this.iva_ar = iva_ar;
    }

    public String getPrecio_venta_ar() {
        return precio_venta_ar;
    }

    public void setPrecio_venta_ar(String precio_venta_ar) {
        this.precio_venta_ar = precio_venta_ar;
    }

    public String getPrecio_compra_ar() {
        return precio_compra_ar;
    }

    public void setPrecio_compra_ar(String precio_compra_ar) {
        this.precio_compra_ar = precio_compra_ar;
    }

    public String getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(String utilidad) {
        this.utilidad = utilidad;
    }

    public String getMonto_utilidad_ar() {
        return monto_utilidad_ar;
    }

    public void setMonto_utilidad_ar(String monto_utilidad_ar) {
        this.monto_utilidad_ar = monto_utilidad_ar;
    }

    public String getPrecio_neto_ar() {
        return precio_neto_ar;
    }

    public void setPrecio_neto_ar(String precio_neto_ar) {
        this.precio_neto_ar = precio_neto_ar;
    }

    public String getPrecio_utilidad_ar() {
        return precio_utilidad_ar;
    }

    public void setPrecio_utilidad_ar(String precio_utilidad_ar) {
        this.precio_utilidad_ar = precio_utilidad_ar;
    }

    public String getIva_venta_ar() {
        return iva_venta_ar;
    }

    public void setIva_venta_ar(String iva_venta_ar) {
        this.iva_venta_ar = iva_venta_ar;
    }

    public String getFoto_ar() {
        return foto_ar;
    }

    public void setFoto_ar(String foto_ar) {
        this.foto_ar = foto_ar;
    }

    public String getClose_ar() {
        return close_ar;
    }

    public void setClose_ar(String close_ar) {
        this.close_ar = close_ar;
    }

    public String getStock_minimo_ar() {
        return stock_minimo_ar;
    }

    public void setStock_minimo_ar(String stock_minimo_ar) {
        this.stock_minimo_ar = stock_minimo_ar;
    }

    public String getUnidades_id_unidad() {
        return unidades_id_unidad;
    }

    public void setUnidades_id_unidad(String unidades_id_unidad) {
        this.unidades_id_unidad = unidades_id_unidad;
    }

    public String getMarcas_id_marca() {
        return marcas_id_marca;
    }

    public void setMarcas_id_marca(String marcas_id_marca) {
        this.marcas_id_marca = marcas_id_marca;
    }

    public String getEtiquetas_id_etiqueta() {
        return etiquetas_id_etiqueta;
    }

    public void setEtiquetas_id_etiqueta(String etiquetas_id_etiqueta) {
        this.etiquetas_id_etiqueta = etiquetas_id_etiqueta;
    }

    public String getStock_inicial() {
        return stock_inicial;
    }

    public void setStock_inicial(String stock_inicial) {
        this.stock_inicial = stock_inicial;
    }

    @Override
    public String toString() {
        return "articulos{" + "id_articulo=" + id_articulo + ", codigo_ar=" + codigo_ar + ", nombre_ar=" + nombre_ar + ", barcode_ar=" + barcode_ar + ", descripcion_ar=" + descripcion_ar + ", iva_ar=" + iva_ar + ", precio_venta_ar=" + precio_venta_ar + ", precio_compra_ar=" + precio_compra_ar + ", utilidad=" + utilidad + ", monto_utilidad_ar=" + monto_utilidad_ar + ", precio_neto_ar=" + precio_neto_ar + ", precio_utilidad_ar=" + precio_utilidad_ar + ", iva_venta_ar=" + iva_venta_ar + ", foto_ar=" + foto_ar + ", close_ar=" + close_ar + ", stock_minimo_ar=" + stock_minimo_ar + ", stock_inicial=" + stock_inicial + ", unidades_id_unidad=" + unidades_id_unidad + ", marcas_id_marca=" + marcas_id_marca + ", etiquetas_id_etiqueta=" + etiquetas_id_etiqueta + '}';
    }

}
