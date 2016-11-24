package com.example.ale.myapplicatio;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    ImageView foto;
    String nome_viaggio;
    String data;
    String parte_giornata;
    String tipologia;
    String[] momento;

    public ItemAdapterAttivita(Context context, ArrayList<Attivita> Items,String nomeViaggio) {
        super(context, 0, Items);
        arrayList = Items;
        nome_viaggio = nomeViaggio;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Attivita item = arrayList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_gestione_viaggio_attivita_item, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.fragment_gestione_viaggio_attivita_item_nomeviaggio);
        TextView indirizzo = (TextView) convertView.findViewById(R.id.fragment_gestione_viaggio_attivita_item_indirizzo);
        Button aggiungi = (Button) convertView.findViewById(R.id.fragment_gestione_viaggio_attivita_bottone_aggiungi);
        //ImageView foto = (ImageView) convertView.findViewById(R.id.fragment_gestione_viaggio_attivita_item_foto);
        name.setText(item.getNome());
        indirizzo.setText(item.getIndirizzo());
        aggiungi.setOnClickListener(this);
        tipologia = item.getTipologia();
        //new LoadImageTask().execute(item.getFoto());
        //Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_gestione_viaggio_attivita_bottone_aggiungi) {
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
            final String[] momento_mangiare = {"Pranzo", "Cena", "Sera"};
            final String[] momento_vedere = {"Mattina", "Pomeriggio", "Sera"};
            final String[] momento_dormire = {"Dormire"};
            //final String[] giornata = {"Mattina", "Pranzo", "Pomeriggio", "Cena", "Sera"};
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
                            String[] momento = new String[momento_vedere.length];
                            for(int j=0; j<momento_vedere.length; j++){
                                momento[j] = momento_vedere[j];
                            }
                        }else if(tipologia.equals("mangiare")){
                            String[] momento = new String[momento_mangiare.length];
                            for(int j=0; j<momento_mangiare.length; j++){
                                momento[j] = momento_mangiare[j];
                            }
                        }else if(tipologia.equals("dormire")){
                            String[] momento = new String[momento_dormire.length];
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
                                    AttivitaGiorno attivitaGiorno = new AttivitaGiorno(arrayList.get(position).getPlace_id(),data,id_viaggio, parte_giornata);
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