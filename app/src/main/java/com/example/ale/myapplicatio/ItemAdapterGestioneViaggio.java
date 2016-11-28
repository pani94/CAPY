package com.example.ale.myapplicatio;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Paola on 28/11/2016.
 */

public class ItemAdapterGestioneViaggio extends ArrayAdapter<String> {
    private String[] opzioni;
    private ImageView icon;
    private DataBase db;
    private String nome_viaggio_get;

    public ItemAdapterGestioneViaggio(Context context, String[] opzioni, String nome_viaggio_get){
        super(context, 0, opzioni);
        this.opzioni= opzioni;
        this.nome_viaggio_get = nome_viaggio_get;
    }

    public View getView(final int position, View view, ViewGroup parent){
        final String scelta = opzioni[position];
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.lista_gestione_viaggio_item, parent, false);
        }
            TextView name = (TextView) view.findViewById(R.id.lista_gestione_viaggio_item_testo);
            icon = (ImageView) view.findViewById(R.id.lista_gestione_viaggio_item_icon);
            name.setText(scelta);
            db=new DataBase(getContext());

            if(scelta.equals("Attivita")){
                icon.setImageResource(R.drawable.ic_account_balance_black_24dp);
            }else if(scelta.equals("Agenda")){
                icon.setImageResource(R.drawable.ic_event_black_24dp);
            }else if(scelta.equals("Galleria")){
                icon.setImageResource(R.drawable.ic_local_see_black_24dp);
            }

        return view;
    }
}
