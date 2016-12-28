package com.example.ale.myapplicatio;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ale on 18/11/2016.
 */

public class ItemAdapterAttivita extends ArrayAdapter<Attivita> implements View.OnClickListener{
    ArrayList<Attivita> arrayList;
    ImageView icon;
    ImageButton bottonepiu;
    String nome_viaggio;
    String data;
    String parte_giornata;
    String tipologia;
    String[] momento;
    String provenienza;
    ViewHolder holder;
    public ItemAdapterAttivita(Context context, ArrayList<Attivita> Items,String nomeViaggio,String provenienza) {
        super(context, 0, Items);
        arrayList = Items;
        nome_viaggio = nomeViaggio;
        this.provenienza = provenienza;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Attivita item = arrayList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_gestione_viaggio_attivita_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.fragment_gestione_viaggio_attivita_item_nomeviaggio);
            viewHolder.vicinity = (TextView) convertView.findViewById(R.id.fragment_gestione_viaggio_attivita_item_indirizzo);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.fragment_gestione_viaggio_attivita_icon);
            convertView.setTag(viewHolder);
        }
        // Lookup view for data population
        //TextView name = (TextView) convertView.findViewById(R.id.fragment_gestione_viaggio_attivita_item_nomeviaggio);
        //TextView indirizzo = (TextView) convertView.findViewById(R.id.fragment_gestione_viaggio_attivita_item_indirizzo);
        bottonepiu = (ImageButton) convertView.findViewById(R.id.imageButton);
        //ImageView icon = (ImageView) convertView.findViewById(R.id.fragment_gestione_viaggio_attivita_icon);

        holder = (ViewHolder) convertView.getTag();
        String nome = item.getNome();
        String indirizzo = item.getIndirizzo();
        holder.name.setText(nome);
        holder.vicinity.setText(indirizzo);

        //name.setText(item.getNome());
        //indirizzo.setText(item.getIndirizzo());
        bottonepiu.setOnClickListener(this);
        tipologia = item.getTipologia();
        //new LoadImageTask().execute(item.getFoto());
        //Return the completed view to render on screen

        String key = "key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
        String photo_reference_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&maxheight=100&photoreference="+item.getFoto()+"&sensor=false&" + key;
        holder.icon.setTag(photo_reference_url);
        //if(photo_reference_url != holder.icon.getTag()){
        new ImageDownloaderTask(holder.icon).execute(photo_reference_url);
        //new LoadImageTask().execute(photo_reference_url);
        //}
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageButton && provenienza.equals("attivita")) {
            View parentRow = (View) v.getParent();
            ListView listView = (ListView) parentRow.getParent();
            final int position = listView.getPositionForView(parentRow);
            final DataBase dataBase = new DataBase(getContext());
            final int id_viaggio = dataBase.getIdViaggio(nome_viaggio);
            final ArrayList <ViaggioGiorno> giorni = dataBase.getGiorni(id_viaggio);
            final String[] date = new String[giorni.size()];
            for (int k = 0; k < giorni.size(); k++) {
                date[k] = giorni.get(k).getData();
            }
            final String[] momento_mangiare = {"Pranzo", "Cena"};
            final String[] momento_vedere = {"Mattina", "Pomeriggio", "Sera"};
            final String[] momento_dormire = {"Dormire"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("INSERISCI L'ATTIVITA' AD UN GIORNO");
            builder.setSingleChoiceItems(date, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    data = giorni.get(i).getData();
                }

            });
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (!data.equals("")) {
                        if(tipologia.equals("vedere")){
                            momento = new String[momento_vedere.length];
                            for(int j=0; j<momento_vedere.length; j++){
                                momento[j] = momento_vedere[j];
                            }
                        }else if(tipologia.equals("mangiare")){
                            momento = new String[momento_mangiare.length];
                            for(int j=0; j<momento_mangiare.length; j++){
                                momento[j] = momento_mangiare[j];
                            }
                        }else if(tipologia.equals("dormire")){
                            momento = new String[momento_dormire.length];
                            for(int j=0; j<momento_dormire.length; j++){
                                momento[j] = momento_dormire[j];
                            }
                        }
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setTitle("INSERISCI L'ATTIVITA' NELLA TUA GIORNATA");
                        builder1.setSingleChoiceItems(momento, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int k) {
                                parte_giornata = momento[k];
                            }
                        });
                        builder1.setPositiveButton("Confirm", new DialogInterface.OnClickListener()  {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(!parte_giornata.equals("")){
                                    int evento_id = 0;
                                    AttivitaGiorno attivitaGiorno = new AttivitaGiorno(arrayList.get(position).getPlace_id(),data,id_viaggio, parte_giornata,evento_id);
                                    Log.e("pd",parte_giornata);
                                    long insert =dataBase.insertAttivitaGiorno(attivitaGiorno);
                                    if(insert > 0){
                                        String stampa = "L'attività è stata aggiunta al " + parte_giornata + " del " + data;
                                        Toast.makeText(getContext().getApplicationContext(),
                                                stampa,
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }else{
                                    Toast.makeText(getContext().getApplicationContext(),
                                            "Devi selezionare un momento della giornata",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {
                            public void onClick(DialogInterface dialog, int which)  {

                            }
                        });
                        builder1.show();
                    } else {
                        Toast.makeText(getContext().getApplicationContext(), "Devi selezionare un giorno",
                                Toast.LENGTH_SHORT).show();
                    }


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();


        }
        else if (v.getId() == R.id.imageButton && provenienza.equals("preferiti")){
            final DataBase database = new DataBase(getContext());
             View parentRow = (View) v.getParent();
             ListView listView = (ListView) parentRow.getParent();
            final int position = listView.getPositionForView(parentRow);
            if(database.getViaggiBool()) {
                final ArrayList <Viaggio>arrayListViaggi = database.getViaggi();
                final String[] nomeViaggi = new String[arrayListViaggi.size()];
                for (int k = 0; k < arrayListViaggi.size(); k++) {
                    nomeViaggi[k] = arrayListViaggi.get(k).getNome_viaggio();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("I TUOI VIAGGI");
                builder.setSingleChoiceItems(nomeViaggi,-1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        nome_viaggio = arrayListViaggi.get(i).getNome_viaggio();

                    }

                });
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(!nome_viaggio.equals("")){
                            ViaggioAttivita viaggioattivita = new ViaggioAttivita(database.getIdViaggio(nome_viaggio), arrayList.get(position).getPlace_id());
                            database.insertViaggioAttivita(viaggioattivita);
                            String stampa = "L'attività è stata aggiunta al viaggio: " + nome_viaggio;
                            Toast.makeText(getContext(),
                                    stampa,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(),"Devi selezionare un viaggio",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }else{
                new android.support.v7.app.AlertDialog.Builder(getContext())
                        .setTitle("ATTENZIONE")
                        .setMessage("Per poter inserire un'attività devi creare prima un viaggio.")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
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
                holder.icon.setImageBitmap(bitmap);


            }
        }
    }

    public class ViewHolder{
        TextView name;
        TextView vicinity;
        ImageView icon;
    }


}