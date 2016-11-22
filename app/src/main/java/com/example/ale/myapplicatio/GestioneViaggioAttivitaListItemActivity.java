package com.example.ale.myapplicatio;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private DataBase database;
    private ArrayList<ViaggioGiorno> giorni;
    private int id_viaggio;
    private String nomeViaggio;
    private String data;
    private String parte_giornata;
    private String[] date;
    private String[] giornata = {"Mattina", "Pranzo", "Pomeriggio", "Cena", "Sera"};
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
            ButtonListener buttonListener = new ButtonListener();
            database = new DataBase(GestioneViaggioAttivitaListItemActivity.this);
            id_viaggio = database.getIdViaggio(nomeViaggio);
            giorni = database.getGiorni(id_viaggio);
            attivita_listitem_button.setOnClickListener(buttonListener);
            Log.e("messaggini", String.valueOf(id_viaggio));
            for(int i=0; i<giorni.size(); i++){
                Log.e("messaggini", giorni.get(i).getData());
            }
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(GestioneViaggioAttivitaListItemActivity.this);
                        builder1.setTitle("INSERISCI L'ATTIVITA' NELLA TUA GIORNATA");
                        builder1.setSingleChoiceItems(giornata, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int k) {
                                parte_giornata = giornata[k];
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
}
