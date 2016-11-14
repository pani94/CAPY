package com.example.ale.myapplicatio;

import android.net.Uri;

import java.net.URI;

public class ItemRicercaActivity {
    private String name;
    private String place_id  ;
    private String vicinity;
    private String icon;
    private String orario;
    public ItemRicercaActivity(){
        name = "";
        place_id="";
        vicinity="";
        icon="";
        orario="";
    }
    public ItemRicercaActivity(String n, String p , String v,String i,String o){
        name = n;
        place_id = p;
        vicinity = v;
        icon = i;
        orario= o;
    }
    public void setName(String n){
        name=n;
    }
    public void setPlace_id(String p){
        place_id = p;
    }
    public void setVicinity(String v){
        vicinity = v;
    }
    public void setIcon(String i){
        icon = i;
    }
    public void setOrario(String o ){orario = o;}
    public String getName(){
        return name;
    }
    public String getPlace_id(){
        return place_id;
    }
    public String getVicinity(){
        return vicinity;
    }
    public String getIcon(){
        return icon;
    }
    public String getOrario(){ return orario;}
}
