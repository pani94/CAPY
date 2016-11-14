package com.example.ale.myapplicatio;

public class ItemRicercaActivity {
    private String name;
    private String place_id  ;
    private String vicinity;
    private String icon;
    private String open_now;
    public ItemRicercaActivity(){
        name = "";
        place_id="";
        vicinity="";
        icon="";
        open_now="";
    }
    public ItemRicercaActivity(String n, String p , String v,String i, String on){
        name = n;
        place_id = p;
        vicinity = v;
        icon = i;
        open_now = on;
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
    public void setOpen_now(String on) { open_now=on; }
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
    public  String getOpen_now() { return open_now; }

}
