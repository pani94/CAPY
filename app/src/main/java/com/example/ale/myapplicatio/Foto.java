package com.example.ale.myapplicatio;

/**
 * Created by ale on 25/11/2016.
 */

public class Foto {
    private int id_viaggio;
    private String path;

    public Foto() {
        id_viaggio = 0;
        path="";
    }

    public Foto(int id_viaggio, String path) {
        this.id_viaggio = id_viaggio;
        this.path = path;
    }

    public void setId_viaggio(int id_viaggio) {
        this.id_viaggio = id_viaggio;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public int getId_viaggio() {
        return id_viaggio;
    }



}
