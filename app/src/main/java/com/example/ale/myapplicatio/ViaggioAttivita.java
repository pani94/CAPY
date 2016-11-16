package com.example.ale.myapplicatio;

/**
 * Created by Annalisa on 16/11/2016.
 */

public class ViaggioAttivita {
    private long id_viaggio;
    private String place_id;

    public ViaggioAttivita(){
        this.id_viaggio = 0;
        this.place_id = null;
    }

    public ViaggioAttivita(long id_viaggio, String place_id){
        this.id_viaggio = id_viaggio;
        this.place_id = place_id;
    }

    public void setId_viaggio(long id_viaggio){
        this.id_viaggio = id_viaggio;
    }

    public void setPlace_id(String place_id){
        this.place_id = place_id;
    }

    public long getId_Viaggio(){
        return this.id_viaggio;
    }

    public String getPlace_id(){
        return this.place_id;
    }
}
