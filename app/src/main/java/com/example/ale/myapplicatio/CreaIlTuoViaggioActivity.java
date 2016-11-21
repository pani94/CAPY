package com.example.ale.myapplicatio;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreaIlTuoViaggioActivity extends AppCompatActivity {
    private EditText nomeViaggio;
    private TextView partenza;
    private TextView arrivo;
    private Button bottone_partenza;
    private Button bottone_arrivo;
    private Button bottone_fatto;
    private Boolean modifica = false;
    private TextView nomeActivity;
    int id_viaggio;

    //per SlideMenu
    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private RelativeLayout mainContent;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_il_tuo_viaggio);
        ButtonListener buttonListener = new ButtonListener();
        nomeActivity = (TextView) findViewById(R.id.testo);
        nomeViaggio = (EditText) findViewById(R.id.editTextNomeViaggio);
        partenza = (TextView) findViewById(R.id.partenza);
        arrivo = (TextView) findViewById(R.id.arrivo);
        bottone_partenza = (Button) findViewById(R.id.button_partenza);
        bottone_arrivo = (Button) findViewById(R.id.button_arrivo);
        bottone_fatto = (Button) findViewById(R.id.buttonFatto);
        bottone_fatto.setOnClickListener(buttonListener);
        bottone_arrivo.setOnClickListener(buttonListener);
        bottone_partenza.setOnClickListener(buttonListener);
        if(getIntent().getExtras() != null){
            id_viaggio = getIntent().getIntExtra("id_viaggio", 10);
            String stringa_nomeViaggio = getIntent().getStringExtra("nome_viaggio");
            DataBase db = new DataBase(this);
            String data_partenza = db.getDataPartenza(id_viaggio);
            String data_arrivo = db.getDataArrivo(id_viaggio);
            nomeActivity.setText("Modifica Viaggio");
            nomeViaggio.setText(stringa_nomeViaggio);
            partenza.setText(data_partenza);
            arrivo.setText(data_arrivo);
            modifica = true;
        }else{
            bottone_arrivo.setEnabled(false);
        }

        //sliding menu
        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mainContent = (RelativeLayout) findViewById(R.id.main_content);
        listSliding = new ArrayList<>();

        //add item for sliding list
        listSliding.add(new ItemSlideMenu(R.drawable.ic_home, "Home"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_account_circle_black_24dp, "Profilo"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_settings_black_24dp, "Impostazioni"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_about, "About"));

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
        actionBarDrawerToggle.syncState();
    }

    //create method replace fragment
    private void replaceFragment(int pos){
        Fragment fragment = null;
        switch (pos){
            case 0:
                Intent intent = new Intent(CreaIlTuoViaggioActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent2 = new Intent(CreaIlTuoViaggioActivity.this, ProfiloViaggiActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }

        if (fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.activity_main, fragment);
            transaction.commit();


        }
    }

    public class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.buttonFatto:
                    String NViaggio = nomeViaggio.getText().toString();
                    String p = partenza.getText().toString();
                    String a = arrivo.getText().toString();
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date part = null;
                    Date arr = null;
                    if(!NViaggio.equals("") && !p.equals("gg/mm/aaaa") && !a.equals("gg/mm/aaaa")){
                        try {
                            part = formatter.parse(p);
                            arr = formatter.parse(a);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                        if(part != null & arr != null){
                            if(CheckDates(part, arr)){
                                DataBase db = new DataBase(CreaIlTuoViaggioActivity.this);
                                if(modifica){
                                    Viaggio viaggio = new Viaggio(id_viaggio, NViaggio, p, a);
                                    db.UpdateViaggio(viaggio);
                                }else{
                                    Viaggio viaggio = new Viaggio(NViaggio, p, a);
                                    db.insertViaggio(viaggio);
                                }
                                salvaGiorni(part, arr, NViaggio);
                                Intent intent = new Intent(CreaIlTuoViaggioActivity.this, ProfiloViaggiActivity.class);
                                intent.putExtra("viaggio_creato","viaggio creato");
                                startActivity(intent);
                                /*int i = p.indexOf("/");
                                String giorno = p.substring(0, i);
                                int day = Integer.parseInt(giorno);
                                String culo = String.valueOf(day);
                                Log.e("giorno", culo);*/
                            }else{
                                Toast.makeText(getApplicationContext(),"Date non valide",Toast.LENGTH_LONG).show();
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Inserisci i dati del viaggio",Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.button_partenza:
                    showDatePickerDialog(bottone_partenza, "partenza");
                    break;

                case R.id.button_arrivo:
                    showDatePickerDialog(bottone_arrivo, "arrivo");
                    break;
            }
        }
    }

    public void showDatePickerDialog(View v, String selezione) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle arg = new Bundle();
        arg.putString("selezionato", selezione);
        newFragment.setArguments(arg);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static boolean CheckDates(Date partenza, Date arrivo)    {
        //Log.e("messaggini", "checkdates function");
        Calendar c_partenza = Calendar.getInstance();
        Calendar c_arrivo = Calendar.getInstance();
        c_partenza.setTime(partenza);
        c_arrivo.setTime(arrivo);
        boolean b = false;
        try {
            if(c_partenza.before(c_arrivo))
            {
                b = true;
            }
            else if(c_partenza.equals(c_arrivo))
            {
                b = true;//If two dates are equal
            }
            else
            {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return b;
    }

    public void salvaGiorni(Date startDate, Date endDate, String NViaggio){
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        int elapsedDays = (int) (different / daysInMilli);
        //elapsedDays = elapsedDays+1;
        //Log.e("messaggini", String.valueOf(elapsedDays));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        int month = calendar.get(calendar.MONTH);
        int year = calendar.get(calendar.YEAR);
        String p = day + "/" + (month+1) + "/" + year;
        DataBase db = new DataBase(CreaIlTuoViaggioActivity.this);
        Giorno g = new Giorno(p);
        db.insertGiorno(g);
        long id_viaggio = db.getIdViaggio(NViaggio);
        ViaggioGiorno va = new ViaggioGiorno(id_viaggio, p);
        db.insertViaggioGiorno(va);
        //Log.e("messaggini", p);
        for(int k=0; k<elapsedDays; k++){
            if(month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9){
                if(day == 31){
                    month = month+1;
                    day = 1;
                }else{
                    day = day+1;
                }
            }else if(month == 1){
                if(day == 28){
                    month = month+1;
                    day = 1;
                }else{
                    day = day+1;
                }
            }else if(month == 11){
                if(day == 31){
                    year = year+1;
                    month = 0;
                    day = 1;
                }else{
                    day = day+1;
                }
            }else{
                if(day == 30){
                    month = month+1;
                    day = 1;
                }else{
                    day = day+1;
                }
            }

            calendar.set(calendar.DAY_OF_MONTH, day);
            calendar.set(calendar.MONTH, month);
            calendar.set(calendar.YEAR, year);

            int day_iesimo = calendar.get(calendar.DAY_OF_MONTH);
            int month_iesimo = calendar.get(calendar.MONTH);
            int year_iesimo = calendar.get(calendar.YEAR);
            String p_iesimo = day_iesimo + "/" + (month_iesimo+1) + "/" + year_iesimo;
            g = new Giorno(p_iesimo);
            db.insertGiorno(g);
            va = new ViaggioGiorno(id_viaggio, p_iesimo);
            db.insertViaggioGiorno(va);
            //Log.e("messaggini", p_iesimo);
        }
    }
}
