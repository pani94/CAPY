package com.example.ale.myapplicatio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ItemAdapterMenu extends ArrayAdapter<ItemRicercaActivity>  {
    ArrayList<ItemRicercaActivity> arrayList;
    ImageView icon;
    public ItemAdapterMenu(Context context, ArrayList<ItemRicercaActivity> Items) {
        super(context, 0, Items);
        arrayList = Items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ItemRicercaActivity item = arrayList.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_ricerca_item, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView vicinity = (TextView) convertView.findViewById(R.id.vicinity);
        icon = (ImageView) convertView.findViewById(R.id.icon);
        // Populate the data into the template view using the data object
        name.setText(item.getName());
        vicinity.setText(item.getVicinity());
       // new LoadImageTask().execute(item.getIcon());
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