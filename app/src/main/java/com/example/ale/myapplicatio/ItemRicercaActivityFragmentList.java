package com.example.ale.myapplicatio;

/**
 * Created by Annalisa on 14/11/2016.
 */

public class ItemRicercaActivityFragmentList {
    private String name;
    private String phone;
    private String website;
    private String photo_reference;
    private String address;

    public ItemRicercaActivityFragmentList(){
        this.name = null;
        this.phone = null;
        this.website = null;
        this.photo_reference = null;
        this.address = null;
    }

    public ItemRicercaActivityFragmentList(String name, String phone, String website, String photo_reference,String address){
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.photo_reference = photo_reference;
        this.address = address;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setWebsite(String website){
        this.website = website;
    }

    public void setPhoto_reference(String photo_reference){
        this.photo_reference = photo_reference;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getName(){
        return this.name;
    }

    public String getPhone(){
        return this.phone;
    }

    public String getWebsite(){
        return this.website;
    }

    public String getPhoto_reference(){
        return this.photo_reference;
    }

    public String getAddress(){
        return this.address;
    }





}
