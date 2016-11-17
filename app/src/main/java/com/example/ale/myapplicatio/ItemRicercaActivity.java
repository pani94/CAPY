package com.example.ale.myapplicatio;

public class ItemRicercaActivity {
    private String name;
    private String place_id  ;
    private String vicinity;
    private String icon;
    private String orario;
    private String latitudine;
    private String longitudine;
    public ItemRicercaActivity(){
        name = "";
        place_id="";
        vicinity="";
        icon="";
        orario="";
        latitudine = "";
        longitudine = "";
    }
    public ItemRicercaActivity(String n, String p , String v,String i,String o,String lat,String lng){
        name = n;
        place_id = p;
        vicinity = v;
        icon = i;
        orario= o;
        latitudine = lat;
        longitudine = lng;
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
    public void setLatitudine(String lat) { latitudine = lat; }
    public void setLongitudine(String lng) { longitudine = lng; }
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
    public String getLatitudine(){ return latitudine; }
    public String getLongitudine(){ return longitudine; }
}