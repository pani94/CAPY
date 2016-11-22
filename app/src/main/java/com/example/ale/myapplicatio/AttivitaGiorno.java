package com.example.ale.myapplicatio;


public class AttivitaGiorno {

    private long id_viaggio;
    private String place_id;
    private String data;
    private String quando;

    public AttivitaGiorno(){
        id_viaggio= 0;
        place_id="";
        data="";
        quando="";
    }
    public AttivitaGiorno( String place_id , String data,long id_viaggio, String quando){
        this.id_viaggio = id_viaggio;
        this.place_id = place_id;
        this.data = data;
        this.quando = quando;
    }

    public void setId_viaggio(long id_viaggio){
        this.id_viaggio = id_viaggio;
    }
    public void setPlace_id(String place_id){
        this.place_id = place_id;
    }
    public void setData(String data){
        this.data = data;
    }
    public void setQuando(String quando) { this.quando = quando; }
    public long getId_viaggio(){
        return this.id_viaggio;
    }
    public String getPlace_id(){
        return this.place_id;
    }
    public String getData(){
        return this.data;
    }
    public String getQuando() { return this.quando; }

}
