package com.example.ale.myapplicatio;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Paola on 28/11/2016.
 */

public class ItemAdapterGestioneViaggio extends ArrayAdapter<String> {
    private String[] opzioni;
    private ImageView icon;

    public ItemAdapterGestioneViaggio(Context context, String[] opzioni){
        super(context, 0, opzioni);
        this.opzioni= opzioni;
    }

    public View getView(final int position, View view, ViewGroup parent){
        final String scelta = opzioni[position];
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.lista_gestione_viaggio_item, parent, false);
        }
            TextView name = (TextView) view.findViewById(R.id.lista_gestione_viaggio_item_testo);
            icon = (ImageView) view.findViewById(R.id.lista_gestione_viaggio_image_view);
            name.setText(scelta);

            if(scelta.equals("Attività")){
                icon.setImageResource(R.drawable.occhialiiiiii);
                icon.setBackgroundColor(Color.TRANSPARENT);
            }else if(scelta.equals("Agenda")){
                icon.setImageResource(R.drawable.ic_event_black_24dp);
            }else if(scelta.equals("Galleria")){
                icon.setImageResource(R.drawable.ic_local_see_black_24dp);
            }

        return view;
    }
}
