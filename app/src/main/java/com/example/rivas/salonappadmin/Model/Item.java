package com.example.rivas.salonappadmin.Model;

import android.graphics.Bitmap;

public class Item {

    public String id;
    public String nombre;
    public String descripcion;
    public String precio;
    public String ruta_img;
    public Bitmap imgItem;


    public Item(){

    }

    public Item(String id, String nombre, String descripcion, String precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public Item(String id, String nombre, String descripcion, String precio, String ruta_img) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.ruta_img = ruta_img;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() { return precio; }

    public void setPrecio(String precio) { this.precio = precio; }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getRuta_img() {
        return ruta_img;
    }

    public void setRuta_img(String ruta_img) {
        this.ruta_img = ruta_img;
    }

    public Bitmap getImgItem() {
        return imgItem;
    }

    public void setImgItem(Bitmap imgItem) {
        this.imgItem = imgItem;
    }
}
