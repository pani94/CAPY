package com.example.ale.myapplicatio;

//import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ParseException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
    private Boolean modifica = false;
    private int id_viaggio;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SharedPreferences prefs;
    Button bottone_partenza;
    Button bottone_arrivo;
    ImageButton bottone_fatto;
    String stringa_nomeViaggio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_il_tuo_viaggio);
        ButtonListener buttonListener = new ButtonListener();
        TextView nomeActivity = (TextView) findViewById(R.id.testo);
        nomeViaggio = (EditText) findViewById(R.id.editTextNomeViaggio);
        partenza = (TextView) findViewById(R.id.partenza);
        arrivo = (TextView) findViewById(R.id.arrivo);
        bottone_partenza = (Button) findViewById(R.id.button_partenza);
        bottone_arrivo = (Button) findViewById(R.id.button_arrivo);
        bottone_fatto = (ImageButton) findViewById(R.id.buttonFatto);
        bottone_fatto.setOnClickListener(buttonListener);
        bottone_arrivo.setOnClickListener(buttonListener);
        bottone_partenza.setOnClickListener(buttonListener);
        stringa_nomeViaggio = "";
        if (getIntent().getExtras() != null) {
            id_viaggio = getIntent().getIntExtra("id_viaggio", 10);
            stringa_nomeViaggio = getIntent().getStringExtra("nome_viaggio");
            DataBase db = new DataBase(this);
            String data_partenza = db.getDataPartenza(id_viaggio);
            String data_arrivo = db.getDataArrivo(id_viaggio);
            nomeActivity.setText("Modifica Viaggio");
            nomeViaggio.setText(stringa_nomeViaggio);
            partenza.setText(data_partenza);
            arrivo.setText(data_arrivo);
            modifica = true;
        } else {
            bottone_arrivo.setEnabled(false);
            bottone_arrivo.setBackgroundResource(R.drawable.bottone_crea_viaggio_false);;
        }
        // set the default values for the preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // get default SharedPreferences object
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //sliding menu
        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
       // RelativeLayout mainContent = (RelativeLayout) findViewById(R.id.main_content);
        List<ItemSlideMenu> listSliding = new ArrayList<>();

        //add item for sliding list
        listSliding.add(new ItemSlideMenu(R.drawable.ic_home_black_24dp, "Home"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_create_black_24dp, "Crea un nuovo viaggio"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_business_center_black_24dp, "I miei viaggi"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_star_black_24dp, "I miei preferiti"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_settings_black_24dp, "Impostazioni"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_info_black_24dp, "About"));

        SlidingMenuAdapter adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listViewSliding.setItemChecked(0, true);
        drawerLayout.closeDrawer(listViewSliding);
        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewSliding.setItemChecked(position, true);
                replaceFragment(position);//close menu
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
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (actionBarDrawerToggle != null) {
            actionBarDrawerToggle.syncState();
        } else {
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
            actionBarDrawerToggle.syncState();
        }
    }

    //create method replace fragment
    private void replaceFragment(int pos) {
        Fragment fragment = null;
        switch (pos) {
            case 0:
                Intent intent_home = new Intent(CreaIlTuoViaggioActivity.this, MainActivity.class);
                startActivity(intent_home);
                break;
            case 1:
                Intent intent_creaViaggio = new Intent(CreaIlTuoViaggioActivity.this, CreaIlTuoViaggioActivity.class);
                startActivity(intent_creaViaggio);
                break;
            case 2:
                Intent intent_viaggi = new Intent(CreaIlTuoViaggioActivity.this, ProfiloViaggiActivity.class);
                intent_viaggi.putExtra("viaggio", "viaggio");
                startActivity(intent_viaggi);
                break;
            case 3:
                Intent intent_preferiti = new Intent(CreaIlTuoViaggioActivity.this, ProfiloViaggiActivity.class);
                intent_preferiti.putExtra("preferiti", "preferiti");
                startActivity(intent_preferiti);
                break;
            case 4:
                Intent intent_impostazioni = new Intent(CreaIlTuoViaggioActivity.this, SettingsActivity.class);
                startActivity(intent_impostazioni);
                break;
            case 5: new AlertDialog.Builder(CreaIlTuoViaggioActivity.this)
                    .setTitle("Let's go")
                    .setMessage("Questa applicazione è stata creata da: " +
                            "Alessandro Barlocco, Annalisa Bovone, Paola Silvestre")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(R.drawable.logo_pani_piccolo)
                    .show();
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
            switch (v.getId()) {
                case R.id.buttonFatto:
                    String NViaggio = nomeViaggio.getText().toString();
                    String p = partenza.getText().toString();
                    String a = arrivo.getText().toString();
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date part = null;
                    Date arr = null;
                    DataBase db = new DataBase(CreaIlTuoViaggioActivity.this);
                    if (!NViaggio.equals("") && !p.equals("") && !a.equals("")) {
                        Log.e("ciaao", NViaggio + Boolean.toString(modifica) + stringa_nomeViaggio);
                        if(!db.getViaggio(NViaggio,modifica,stringa_nomeViaggio)){
                            try {
                                part = formatter.parse(p);
                                arr = formatter.parse(a);
                            } catch (java.text.ParseException e) {
                                e.printStackTrace();
                            }
                            if (part != null & arr != null) {
                                if (CheckDates(part, arr)) {
                                    //database
                                    MyCalendar myCalendar = new MyCalendar(CreaIlTuoViaggioActivity.this);
                                    long evento_id = myCalendar.addViaggioToCalendar(part,arr,NViaggio,CreaIlTuoViaggioActivity.this);
                                    if (modifica) {
                                        Viaggio viaggio = new Viaggio(id_viaggio, NViaggio, p, a,evento_id);
                                        long update = db.UpdateViaggio(viaggio);
                                        if (update > 0) {
                                            Toast.makeText(getApplicationContext(), "Viaggio modificato", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Viaggio viaggio = new Viaggio(NViaggio, p, a,evento_id);
                                        long update = db.insertViaggio(viaggio);
                                        if (update > 0) {
                                            Toast.makeText(getApplicationContext(), "Viaggio creato", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    salvaGiorni(part, arr, NViaggio);



                                    myCalendar.addNotify(CreaIlTuoViaggioActivity.this, part, arr, NViaggio, "one_week_before");
                                    myCalendar.addNotify(CreaIlTuoViaggioActivity.this, part, arr, NViaggio, "one_day_before");
                                    myCalendar.addNotify(CreaIlTuoViaggioActivity.this, part, arr, NViaggio, "one_day_after");


                                    Intent intent = new Intent(CreaIlTuoViaggioActivity.this, ProfiloViaggiActivity.class);
                                    intent.putExtra("viaggio_creato", "viaggio creato");
                                    startActivity(intent);
                                    finish();
                                } else {
                                    new AlertDialog.Builder(CreaIlTuoViaggioActivity.this)
                                            .setTitle("ATTENZIONE")
                                            .setMessage("Date non valide. La data di partenza deve precedere quella di arrivo.")                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            }
                        }
                        else{
                            new AlertDialog.Builder(CreaIlTuoViaggioActivity.this)
                                    .setTitle("ATTENZIONE")
                                    .setMessage("Dati non validi. Nome viaggio già in uso.")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }

                    } else {

                            new AlertDialog.Builder(CreaIlTuoViaggioActivity.this)
                                    .setTitle("ATTENZIONE")
                                    .setMessage("Dati non validi. Inserire il nome del viaggio e le date di partenza e di arrivo.")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                    }
                    break;

                case R.id.button_partenza:
                    showDatePickerDialog("partenza");
                    break;

                case R.id.button_arrivo:
                    showDatePickerDialog("arrivo");
                    break;
            }
        }
    }

    public void showDatePickerDialog(String selezione) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle arg = new Bundle();
        arg.putString("selezionato", selezione);
        arg.putString("data", (String) partenza.getText());
        newFragment.setArguments(arg);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static boolean CheckDates(Date partenza, Date arrivo) {
        Calendar c_partenza = Calendar.getInstance();
        Calendar c_arrivo = Calendar.getInstance();
        c_partenza.setTime(partenza);
        c_arrivo.setTime(arrivo);
        boolean b = false;
        try {
            if (c_partenza.before(c_arrivo)) {
                b = true;
            } else //If two dates are equal
//If start date is after the end date
                b = c_partenza.equals(c_arrivo);
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return b;
    }

    public void salvaGiorni(Date startDate, Date endDate, String NViaggio) {
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        int elapsedDays = (int) (different / daysInMilli);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String p = day + "/" + (month + 1) + "/" + year;
        DataBase db = new DataBase(CreaIlTuoViaggioActivity.this);
        Giorno g = new Giorno(p);
        db.insertGiorno(g);
        long id_viaggio = db.getIdViaggio(NViaggio);
        if (modifica) {
            db.deleteViaggioGiorni(id_viaggio);
        }
        ViaggioGiorno va = new ViaggioGiorno(id_viaggio, p);
        db.insertViaggioGiorno(va);
        for (int k = 0; k < elapsedDays; k++) {
            if (month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9) {
                if (day == 31) {
                    month = month + 1;
                    day = 1;
                } else {
                    day = day + 1;
                }
            } else if (month == 1) {
                if (day == 28) {
                    month = month + 1;
                    day = 1;
                } else {
                    day = day + 1;
                }
            } else if (month == 11) {
                if (day == 31) {
                    year = year + 1;
                    month = 0;
                    day = 1;
                } else {
                    day = day + 1;
                }
            } else {
                if (day == 30) {
                    month = month + 1;
                    day = 1;
                } else {
                    day = day + 1;
                }
            }

            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);

            int day_iesimo = calendar.get(Calendar.DAY_OF_MONTH);
            int month_iesimo = calendar.get(Calendar.MONTH);
            int year_iesimo = calendar.get(Calendar.YEAR);
            String p_iesimo = day_iesimo + "/" + (month_iesimo + 1) + "/" + year_iesimo;
            g = new Giorno(p_iesimo);
            db.insertGiorno(g);
            va = new ViaggioGiorno(id_viaggio, p_iesimo);
            db.insertViaggioGiorno(va);
        }
    }




}