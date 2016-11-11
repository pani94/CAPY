package com.example.ale.myapplicatio;

import android.net.Uri;

import java.net.URI;

public class ItemRicercaActivity {
    private String name;
    private String place_id  ;
    private String vicinity;
    private Uri icon;
    public ItemRicercaActivity(){
        name = "";
        place_id="";
        vicinity="";
        icon=null;
    }
    public ItemRicercaActivity(String n, String p , String v,Uri i){
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
    public void setIcon(Uri i){
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
    public Uri getIcon(){
        return icon;
    }
}
