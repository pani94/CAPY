package com.example.ale.myapplicatio;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Annalisa on 16/11/2016.
 */

public class ViaggioGiorno implements Comparable <ViaggioGiorno> {
    public long id_viaggio;
    public String data;

    public ViaggioGiorno(){
        this.id_viaggio = 0;
        this.data = null;
    }

    public ViaggioGiorno(long id_viaggio, String data){
        this.id_viaggio = id_viaggio;
        this.data = data;
    }

    public void setId_viaggio(long id_viaggio){
        this.id_viaggio = id_viaggio;
    }

    public void setData(String data){
        this.data = data;
    }

    public long getId_viaggio(){
        return this.id_viaggio;
    }

    public String getData(){
        return this.data;
    }

    @Override
    public int compareTo(ViaggioGiorno o) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date part = null;
        Date arr = null;
        try {
            part = formatter.parse(this.getData());
            arr = formatter.parse(o.getData());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return part.compareTo(arr);

    }
}
