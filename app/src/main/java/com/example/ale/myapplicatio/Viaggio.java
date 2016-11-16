package com.example.ale.myapplicatio;
public class Viaggio {
    private long id_viaggio;
    private String nome_viaggio;
    private String partenza;
    private String arrivo;

    public Viaggio(){
        nome_viaggio ="";
        partenza="";
        arrivo="";
    }
    public Viaggio(String nome, String part , String arr){
        nome_viaggio = nome;
        partenza = part;
        arrivo = arr;
    }
    public Viaggio(long id,String nome, String part , String arr){
        id_viaggio = id;
        nome_viaggio = nome;
        partenza = part;
        arrivo = arr;
    }

    public void setId_viaggio(long id){
        id_viaggio = id;
    }
    public void setNome_viaggio(String n){
        nome_viaggio = n;
    }
    public void setPartenza(String p){
        partenza = p;
    }
    public void setArrivo(String a){
        arrivo = a;
    }
    public long getId_viaggio(){
        return id_viaggio;
    }
    public String getNome_viaggio(){
        return nome_viaggio;
    }
    public String getPartenza(){
        return partenza;
    }
    public String getArrivo(){
        return arrivo;
    }
}

