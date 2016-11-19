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
    }

    public Attivita(String place_id, String nome, String indirizzo, String orario, String telefono, String link, String tipologia, String foto, String preferito){
        this.place_id = place_id;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.orario = orario;
        this.telefono = telefono;
        this.link = link;
        this.tipologia = tipologia;
        this.foto = foto;
        this.preferito = preferito;
    }

    public void setPlace_id(String place_id){
        this.place_id = place_id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setIndirizzo(String indirizzo){
        this.indirizzo = indirizzo;
    }

    public void setOrario(String orario){
        this.orario = orario;
    }

    public void setTelefono(String telefono){
        this.telefono = telefono;
    }

    public void setLink(String link){
        this.link = link;
    }

    public void setTipologia(String tipologia){
        this.tipologia = tipologia;
    }

    public void setFoto(String foto){
        this.foto = foto;
    }

    public void setPreferito(String preferito){
        this.preferito = preferito;
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
}
