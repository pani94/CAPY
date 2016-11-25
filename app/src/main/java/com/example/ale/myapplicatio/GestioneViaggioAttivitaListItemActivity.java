package com.example.ale.myapplicatio;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class GestioneViaggioAttivitaListItemActivity extends AppCompatActivity {
    private TextView attivita_listitem_titolo;
    private ImageView attivita_listitem_foto;
    private TextView attivita_listitem_orario;
    private TextView attivita_listitem_link;
    private TextView attivita_listitem_telefono;
    private TextView attivita_listitem_indirizzo;
    private Button attivita_listitem_button;

    private String titolo_get;
    private String orario_get;
    private String foto_get;
    private String link_get;
    private String telefono_get;
    private String indirizzo_get;
    private String placeid_get;
    private String tipologia;
    private DataBase database;
    private ArrayList<ViaggioGiorno> giorni;
    private int id_viaggio;
    private String nomeViaggio;
    private String data;
    private String parte_giornata;
    private String[] date;
    private String[] momento_mangiare = {"Pranzo", "Cena", "Sera"};
    private String[] momento_vedere = {"Mattina", "Pomeriggio", "Sera"};
    private String[] momento_dormire = {"Dormire"};
    private String[] momento;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_viaggio_attivita_list_item);
        if (getIntent().getExtras() != null) {
            nomeViaggio = getIntent().getStringExtra("nomeViaggio");
            titolo_get = getIntent().getStringExtra("titolo");
            orario_get = getIntent().getStringExtra("orario");
            foto_get = getIntent().getStringExtra("foto");
            link_get = getIntent().getStringExtra("link");
            telefono_get = getIntent().getStringExtra("telefono");
            indirizzo_get = getIntent().getStringExtra("indirizzo");
            placeid_get = getIntent().getStringExtra("placeid");
            tipologia = getIntent().getStringExtra("tipologia");
            attivita_listitem_titolo = (TextView) findViewById(R.id.gestione_viaggio_attivita_list_item_titolo);
            attivita_listitem_foto = (ImageView) findViewById(R.id.gestione_viaggio_attivita_list_item_foto);
            attivita_listitem_orario = (TextView) findViewById(R.id.gestione_viaggio_attivita_list_item_orario);
            attivita_listitem_link = (TextView) findViewById(R.id.gestione_viaggio_attivita_list_item_link);
            attivita_listitem_telefono = (TextView) findViewById(R.id.gestione_viaggio_attivita_list_item_telefono);
            attivita_listitem_indirizzo = (TextView) findViewById(R.id.gestione_viaggio_attivita_list_item_indirizzo);
            attivita_listitem_button = (Button) findViewById(R.id.gestione_viaggio_attivita_list_item_button);
            attivita_listitem_titolo.setText(titolo_get);
            attivita_listitem_orario.setText(orario_get);
            attivita_listitem_link.setText(link_get);
            attivita_listitem_telefono.setText(telefono_get);
            attivita_listitem_indirizzo.setText(indirizzo_get);
            if(getIntent().hasExtra("provenienza")){
                if(getIntent().getStringExtra("provenienza").equals("agenda")){
                    attivita_listitem_button.setVisibility(View.GONE);
                }
            }

            ButtonListener buttonListener = new ButtonListener();
            database = new DataBase(GestioneViaggioAttivitaListItemActivity.this);
            id_viaggio = database.getIdViaggio(nomeViaggio);
            giorni = database.getGiorni(id_viaggio);
            attivita_listitem_button.setOnClickListener(buttonListener);
            attivita_listitem_titolo.setShadowLayer(5,0,0, Color.BLACK);
            String photo_reference_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + foto_get + "&sensor=false&key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
            new LoadImageTask().execute(photo_reference_url);
        }
    }
    public class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            date = new String[giorni.size()];
            for (int k = 0; k < giorni.size(); k++) {
                date[k] = giorni.get(k).getData();
            }
            builder = new AlertDialog.Builder(GestioneViaggioAttivitaListItemActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(GestioneViaggioAttivitaListItemActivity.this);
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
                                    AttivitaGiorno attivitaGiorno = new AttivitaGiorno(placeid_get,data,id_viaggio,parte_giornata);
                                    database.insertAttivitaGiorno(attivitaGiorno);
                                    String stampa = "L'attività è stata aggiunta al " + parte_giornata + " del " + data;
                                    Toast.makeText(GestioneViaggioAttivitaListItemActivity.this,
                                            stampa,
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(GestioneViaggioAttivitaListItemActivity.this,
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
                        Toast.makeText(GestioneViaggioAttivitaListItemActivity.this, "Devi selezionare un giorno",
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
                attivita_listitem_foto.setImageBitmap(bitmap);


            }
        }
    }
}
