package com.example.ale.myapplicatio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ale on 17/11/2016.
 */

public class ItemAdapterViaggio extends ArrayAdapter<Viaggio> {

        ArrayList<Viaggio> arrayList;
        public ItemAdapterViaggio(Context context, ArrayList<Viaggio> Items) {
            super(context, 0, Items);
            arrayList = Items;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Viaggio item = arrayList.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_i_tuoi_viaggi, parent, false);
            }
            // Lookup view for data population
            TextView name = (TextView) convertView.findViewById(R.id.item_i_tuoi_viaggi_nome);
            TextView data = (TextView) convertView.findViewById(R.id.item_i_tuoi_viaggi_data);
            name.setText(item.getNome_viaggio());
            String data_stringa = "Da " + item.getPartenza() + " a " + item.getArrivo();
            data.setText(data_stringa);
            // new LoadImageTask().execute(item.getIcon());
            // Return the completed view to render on screen
            return convertView;
        }
}
