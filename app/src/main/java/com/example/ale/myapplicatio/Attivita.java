package com.example.ale.myapplicatio;

public class Attivita {
    private String place_id;
    private String nome;
    private String indirizzo;
    private String orario;
    private String telefono;
    private String link;
    private String tipologia;
    private String foto;
    private String preferito;
    private String latitudine;
    private String longitudine;

    public Attivita(){
        this.place_id = null;
        this.nome = null;
        this.indirizzo = null;
        this.orario = null;
        this.telefono = null;
        this.link = null;
        this.tipologia = null;
        this.foto = null;
        this.preferito = null;
        this.latitudine = null;
        this.longitudine = null;
    }

    public Attivita(String place_id, String nome, String indirizzo, String orario, String telefono, String link, String tipologia, String foto, String preferito,String latitudine,String longitudine){
        this.place_id = place_id;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.orario = orario;
        this.telefono = telefono;
        this.link = link;
        this.tipologia = tipologia;
        this.foto = foto;
        this.preferito = preferito;
        this.foto = foto;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }



    public void setFoto(String foto){
        this.foto = foto;
    }
    public String getPlace_id(){
        return this.place_id;
    }
    public String getNome(){
        return this.nome;
    }
    public String getIndirizzo(){
        return this.indirizzo;
    }
    public String getOrario(){
        return this.orario;
    }
    public String getTelefono(){
        return this.telefono;
    }
    public String getLink(){
        return this.link;
    }
    public String getTipologia(){
        return this.tipologia;
    }
    public String getFoto(){
        return this.foto;
    }
    public String getPreferito(){
        return this.preferito;
    }
    public String getLongitudine() {
        return longitudine;
    }
    public String getLatitudine() {
        return latitudine;
    }
}
