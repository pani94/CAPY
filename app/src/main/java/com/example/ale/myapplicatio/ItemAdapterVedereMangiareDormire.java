package com.example.ale.myapplicatio;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Annalisa on 24/11/2016.
 */

public class ItemAdapterVedereMangiareDormire extends ArrayAdapter<String> implements View.OnClickListener {
    private String[] scelte;
    private ImageButton icon;
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
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.item_vedere_mangiare_dormire_text_view);
        icon = (ImageButton) convertView.findViewById(R.id.item_vedere_mangiare_dormire_image_button);
        // Populate the data into the template view using the data object
        name.setText(scelta);
        name.setShadowLayer(20,10,10, android.R.color.black);
        if(scelta.equals("Cosa vedere?")){
            icon.setImageResource(R.drawable.vedere1);
        }else if(scelta.equals("Dove mangiare?")){
            icon.setImageResource(R.drawable.mangiare);
        }else if(scelta.equals("Dove dormire?")){
            icon.setImageResource(R.drawable.dormire);
        }

       icon.setOnClickListener(this);
        new LoadImageTask();

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onClick(View v) {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            View parentRow = (View) v.getParent();
            ListView listView = (ListView) parentRow.getParent();
            int position = listView.getPositionForView(parentRow);
            fragmentMenu.fragmentStart(position);
        }else{
            new AlertDialog.Builder(getContext())
                    .setTitle("Attenzione")
                    .setMessage("Non hai connessione.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }

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
