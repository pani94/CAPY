package com.example.ale.myapplicatio;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GestioneViaggioAttivitaListItemActivity extends AppCompatActivity {
    private TextView attivita_listitem_titolo;
    private ImageView attivita_listitem_foto;
    private TextView attivita_listitem_orario;
    private TextView attivita_listitem_link;
    private TextView visita;
    private TextView attivita_listitem_telefono;
    private TextView attivita_listitem_indirizzo;
    private ImageView attivita_listitem_button;

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
    private String[] momento_mangiare = {"Pranzo", "Cena"};
    private String[] momento_vedere = {"Mattina", "Pomeriggio", "Sera"};
    private String[] momento_dormire = {"Dormire"};
    private String[] momento;
    private AlertDialog.Builder builder;

    //per SlideMenu
    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    // private RelativeLayout mainContent;
    private ActionBarDrawerToggle actionBarDrawerToggle;

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
            visita =(TextView) findViewById(R.id.gestione_viaggio_attivita_list_item_visita);
            attivita_listitem_telefono = (TextView) findViewById(R.id.gestione_viaggio_attivita_list_item_telefono);
            attivita_listitem_indirizzo = (TextView) findViewById(R.id.gestione_viaggio_attivita_list_item_indirizzo);
            attivita_listitem_button = (ImageView) findViewById(R.id.gestione_viaggio_attivita_list_item_button);
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

            if(link_get.equals("")){
                visita.setVisibility(View.INVISIBLE);
            }
            ButtonListener buttonListener = new ButtonListener();
            database = new DataBase(GestioneViaggioAttivitaListItemActivity.this);
            id_viaggio = database.getIdViaggio(nomeViaggio);
            giorni = database.getGiorni(id_viaggio);
            attivita_listitem_button.setOnClickListener(buttonListener);
            attivita_listitem_titolo.setShadowLayer(5,0,0, Color.BLACK);
            String photo_reference_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + foto_get + "&sensor=false&key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
            new LoadImageTask().execute(photo_reference_url);

            //sliding menu
            listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            //mainContent = (RelativeLayout) findViewById(R.id.main_content);
            listSliding = new ArrayList<>();

            //add item for sliding list
            listSliding.add(new ItemSlideMenu(R.drawable.ic_home_black_24dp, "Home"));
            listSliding.add(new ItemSlideMenu(R.drawable.ic_create_black_24dp, "Crea un nuovo viaggio"));
            listSliding.add(new ItemSlideMenu(R.drawable.ic_business_center_black_24dp, "I miei viaggi"));
            listSliding.add(new ItemSlideMenu(R.drawable.ic_star_black_24dp, "I miei preferiti"));
            listSliding.add(new ItemSlideMenu(R.drawable.ic_settings_black_24dp, "Impostazioni"));
            listSliding.add(new ItemSlideMenu(R.drawable.ic_info_black_24dp, "About"));

            adapter = new SlidingMenuAdapter(this, listSliding);
            listViewSliding.setAdapter(adapter);

            //Display icon to open/close sliding list
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //set Title
            //setTitle(listSliding.get(0).getTitle());
            //item selected
            listViewSliding.setItemChecked(0, true);
            //close menu
            drawerLayout.closeDrawer(listViewSliding);

            //display fragmentProfilo when start

            listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //set title
                    //setTitle(listSliding.get(position).getTitle());
                    //item selected
                    listViewSliding.setItemChecked(position, true);

                    replaceFragment(position);
                    //close menu
                    drawerLayout.closeDrawer(listViewSliding);
                }
            });

            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    invalidateOptionsMenu();
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    invalidateOptionsMenu();
                }
            };

            drawerLayout.addDrawerListener(actionBarDrawerToggle);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        /*switch (item.getItemId()) {
            case R.id.menu_profilo:
                startActivity(new Intent(getApplicationContext(), ProfiloViaggiActivity.class));
            case R.id.menu_settings:
             //   startActivity(new Intent(getApplicationContext(), RicercaActivity.class));
                return true;
            case R.id.menu_about:
               // startActivity(new Intent(getApplicationContext(), RicercaActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (actionBarDrawerToggle!= null){
            actionBarDrawerToggle.syncState();
        }
    }

    //create method replace fragment
    private void replaceFragment(int pos) {
        Fragment fragment = null;
        switch (pos) {
            case 0:
                Intent intent_home = new Intent(GestioneViaggioAttivitaListItemActivity.this, MainActivity.class);
                startActivity(intent_home);
                break;

            case 1:
                Intent intent_creaViaggio = new Intent(GestioneViaggioAttivitaListItemActivity.this, CreaIlTuoViaggioActivity.class);
                startActivity(intent_creaViaggio);
                break;
            case 2:
                Intent intent_viaggi = new Intent(GestioneViaggioAttivitaListItemActivity.this, ProfiloViaggiActivity.class);
                intent_viaggi.putExtra("viaggio", "viaggio");
                startActivity(intent_viaggi);
                break;
            case 3:
                Intent intent_preferiti = new Intent(GestioneViaggioAttivitaListItemActivity.this, ProfiloViaggiActivity.class);
                intent_preferiti.putExtra("preferiti", "preferiti");
                startActivity(intent_preferiti);
                break;
            case 4:
                Intent intent_impostazioni = new Intent(GestioneViaggioAttivitaListItemActivity.this, SettingsActivity.class);
                startActivity(intent_impostazioni);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.activity_main, fragment);
            transaction.commit();


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
                                    // CREARE L'EVENTO
                                    int evento_id =0;
                                    AttivitaGiorno attivitaGiorno = new AttivitaGiorno(placeid_get,data,id_viaggio,parte_giornata,evento_id);
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
        //ProgressDialog pd;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //pd = ProgressDialog.show(GestioneViaggioAttivitaListItemActivity.this, "", "Caricamento in corso...", true, false);
        }

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
                //pd.dismiss();
            }
        }
    }
}
