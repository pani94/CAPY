package com.example.ale.myapplicatio;

import android.net.Uri;

import java.net.URI;

public class ItemRicercaActivity {
    private String name;
    private String place_id  ;
    private String vicinity;
    private String icon;
    public ItemRicercaActivity(){
        name = "";
        place_id="";
        vicinity="";
        icon="";
    }
    public ItemRicercaActivity(String n, String p , String v,String i){
        name = n;
        place_id = p;
        vicinity = v;
        icon = i;
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
}
