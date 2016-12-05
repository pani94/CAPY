package com.example.ale.myapplicatio;
public class Viaggio {
    private long id_viaggio;
    private String nome_viaggio;
    private String partenza;
    private String arrivo;
    private long evento_id;
    public Viaggio(long id,String nome, String part , String arr,long evento_id){
        id_viaggio = id;
        nome_viaggio = nome;
        partenza = part;
        arrivo = arr;
        this.evento_id = evento_id;
    }
    public Viaggio(String nome, String part , String arr,long evento_id){
        nome_viaggio = nome;
        partenza = part;
        arrivo = arr;
        this.evento_id = evento_id;
    }




    public void setId_viaggio(long id){
        id_viaggio = id;
    }
    public void setNome_viaggio(String n){
        nome_viaggio = n;
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
    public long getEvento_id() {
        return evento_id;
    }
}

