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
public class imagenes extends articulos {

    String id_imagen;
    String image_path;
    String mime_type;

    public String getId_imagen() {
        return id_imagen;
    }

    public void setId_imagen(String id_imagen) {
        this.id_imagen = id_imagen;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    @Override
    public String toString() {
        return "imagenes{" + "id_imagen=" + id_imagen + ", image_path=" + image_path + ", mime_type=" + mime_type + '}';
    }

}
