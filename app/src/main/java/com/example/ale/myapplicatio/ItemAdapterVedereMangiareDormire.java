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

/**
 * Created by Annalisa on 24/11/2016.
 */

public class ItemAdapterVedereMangiareDormire extends ArrayAdapter<String> {
    private String[] scelte;
    private ImageView icon;
    private TextView name;
    private RicercaActivityFragmentMenu fragmentMenu;

    public ItemAdapterVedereMangiareDormire(Context context, String[] scelte, RicercaActivityFragmentMenu fragment){
        super(context,0, scelte);
        this.scelte = scelte;
        fragmentMenu = fragment;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final String scelta = scelte[position];
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_vedere_mangiare_dormire_item, parent, false);
        }
        name = (TextView) convertView.findViewById(R.id.vedere_mangiare_dormire_text_view);
        icon = (ImageView) convertView.findViewById(R.id.vedere_mangiare_dormire_image_view);
        // Lookup view for data population
        name.setText(scelta);
        if(scelta.equals("Cosa vedere")){
            icon.setImageResource(R.drawable.ic_account_balance_black_24dp);
        }else if(scelta.equals("Dove mangiare")){
            icon.setImageResource(R.drawable.ic_restaurant_black_24dp);
        }else if(scelta.equals("Dove dormire")){
            icon.setImageResource(R.drawable.ic_hotel_black_24dp);
        }
        new LoadImageTask();

        // Return the completed view to render on screen
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
                icon.setImageBitmap(bitmap);
            }
        }
    }

}
