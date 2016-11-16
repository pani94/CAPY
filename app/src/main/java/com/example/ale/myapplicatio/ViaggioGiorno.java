package com.example.ale.myapplicatio;

/**
 * Created by Annalisa on 16/11/2016.
 */

public class ViaggioGiorno {
    public long id_viaggio;
    public String data;

    public ViaggioGiorno(){
        this.id_viaggio = 0;
        this.data = null;
    }

    public ViaggioGiorno(long id_viaggio, String data){
        this.id_viaggio = id_viaggio;
        this.data = data;
    }

    public void setId_viaggio(long id_viaggio){
        this.id_viaggio = id_viaggio;
    }

    public void setData(String data){
        this.data = data;
    }

    public long getId_viaggio(){
        return this.id_viaggio;
    }

    public String getData(){
        return this.data;
    }
}
