package com.example.ale.myapplicatio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ale on 18/11/2016.
 */

public class ItemAdapterAttivita extends ArrayAdapter<Attivita>{
    ArrayList<Attivita> arrayList;
    ImageView foto;
    public ItemAdapterAttivita(Context context, ArrayList<Attivita> Items) {
        super(context, 0, Items);
        arrayList = Items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Attivita item = arrayList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_gestione_viaggio_attivita, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.fragment_gestione_viaggio_attivita_item_nomeviaggio);
        TextView indirizzo = (TextView) convertView.findViewById(R.id.fragment_gestione_viaggio_attivita_item_indirizzo);
        ImageView foto = (ImageView) convertView.findViewById(R.id.fragment_gestione_viaggio_attivita_item_foto);
        name.setText(item.getNome());
        indirizzo.setText(item.getIndirizzo());
        new LoadImageTask().execute(item.getFoto());
        //Return the completed view to render on screen
        return convertView;
    }
    public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {




        @Override
        protected Bitmap doInBackground(String... args) {

            try {

                return BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (bitmap != null) {
                foto.setImageBitmap(bitmap);


            }
        }
    }
}
