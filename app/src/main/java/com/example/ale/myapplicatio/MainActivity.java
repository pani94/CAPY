package com.example.ale.myapplicatio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView titolo;
    private TextView footer;
    private ImageButton bottone_cerca;
    private AutoCompleteTextView cerca;
    private String TAG = MainActivity.class.getSimpleName();
    ArrayList<String> cityList;
    private boolean apporto_modifiche = false;

    //per SlideMenu
    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    //private RelativeLayout mainContent;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private GetCity getCityRequest;
    private boolean check = false;
    private String stringa;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getCityRequest = null;
        DataBase db = new DataBase(this);
        ButtonListener buttonListener = new ButtonListener();
        cityList = new ArrayList<>();

        cerca = (AutoCompleteTextView) findViewById(R.id.cerca);
        titolo = (TextView) findViewById(R.id.activity_main_titolo);
        footer = (TextView) findViewById(R.id.footer);
        cerca.addTextChangedListener(passwordWatcher);
        bottone_cerca = (ImageButton) findViewById(R.id.activity_main_cerca);
        bottone_cerca.setOnClickListener(buttonListener);

        titolo.setShadowLayer(5, 0, 0, Color.WHITE);
        footer.setShadowLayer(1, 0, 0, Color.WHITE);

        cerca.setOnEditorActionListener(editTextListener);
        cerca.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                check = true;
            }
        });
        cerca.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence charSequence) {
                String s = charSequence.toString();
                Adapter a = cerca.getAdapter();
                if (a != null) {
                    if (!a.isEmpty()) {
                        for (int i = 0; i < a.getCount(); i++) {
                            if (s.equals(a.getItem(i).toString())) {
                                return true;
                            }
                        }
                    } else {
                        if (check) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public String fixText(CharSequence charSequence) {
                String s = charSequence.toString();
                Adapter a = cerca.getAdapter();
                if (a != null) {
                    if (!a.isEmpty()) {
                        return a.getItem(0).toString();
                    }
                } else {
                    if (check) {
                        return s;
                    }
                }
                return "";
            }
        });

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    final TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            check = false;
            ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (s.length() > 0) {
                    if (getCityRequest == null) {
                        getCityRequest = new GetCity();
                        getCityRequest.execute(s.toString());
                    } else {
                        getCityRequest.cancel(true);
                        getCityRequest = new GetCity();
                        getCityRequest.execute(s.toString());
                    }
                }
            } else {
                /*new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Attenzione")
                        .setMessage("Non hai connessione.")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();*/

            }
        }

        public void afterTextChanged(Editable s) {
            cityList.clear();
        }
    };


    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
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
                Intent intent_home = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent_home);
                break;
            case 1:
                Intent intent_creaViaggio = new Intent(MainActivity.this, CreaIlTuoViaggioActivity.class);
                startActivity(intent_creaViaggio);
                break;
            case 2:
                Intent intent_viaggi = new Intent(MainActivity.this, ProfiloViaggiActivity.class);
                intent_viaggi.putExtra("viaggio", "viaggio");
                startActivity(intent_viaggi);
                break;
            case 3:
                Intent intent_preferiti = new Intent(MainActivity.this, ProfiloViaggiActivity.class);
                intent_preferiti.putExtra("preferiti", "preferiti");
                startActivity(intent_preferiti);
                break;
            case 4:
                Intent intent_impostazioni = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent_impostazioni);
                /*final SwitchCompat sw = new SwitchCompat(MainActivity.this);
                //sw.setTextOn("ON");
                //sw.setTextOff("OFF");

                LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                linearLayout.addView(sw);

                AlertDialog.Builder myDialog = new    AlertDialog.Builder(MainActivity.this);
                myDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(sw.isChecked()){
                            Log.e("messaggini", "confirm, true");
                            SharedPreferences.Editor editor = getSharedPreferences("com.example.xyz", MODE_PRIVATE).edit();
                            editor.putBoolean("NameOfThingToSave", true);
                            editor.commit();
                        }else{
                            Log.e("messaggini", "confirm, false");
                        }
                    }
                });
                myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(sw.isChecked()){
                            Log.e("messaggini", "cancel, true");
                        }else{
                            Log.e("messaggini", "cancel, false");
                        }
                    }
                });
                myDialog.setTitle("Impostazioni");
                myDialog.setMessage("Notifiche:");
                myDialog.setView(linearLayout);

                myDialog.show();*/
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

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.e("switch_compat", isChecked + "");
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class GetCity extends AsyncTask<String, Void, Void> {
        //ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Toast.makeText(MainActivity.this, "Json Data is downloading " , Toast.LENGTH_LONG).show();
            //pd= ProgressDialog.show(MainActivity.this, "", "Caricamento in corso...", true, false);
        }

        @Override
        protected Void doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();
            String url1 = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=";
            String url2 = "&types=(cities)&language=it&";
            //String chiave = "key=AIzaSyDg0CUi5HwJsPRxlrR_8VFBxng3eY2aMXk";
            //String chiave ="key=AIzaSyBieTKI8Lmg7TuF2MgUUtK93bjpWylxLBM";
            String chiave = "key=AIzaSyAD1xAMtZ0YaMSii5iDkTJrFv0jz9cEz2U";
            //String chiave = "key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
            String url = url1 + arg0[0] + url2 + chiave;
            Log.e("messaggini", url);
            String jsonStr = sh.makeServiceCall(url);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray predictions = jsonObj.getJSONArray("predictions");
                    // looping through All Contacts
                    for (int i = 0; i < predictions.length(); i++) {
                        if (isCancelled()) {

                        }
                        JSONObject c = predictions.getJSONObject(i);
                        String description = c.getString("description");
                        //String id = c.getString("id");
                        /*JSONArray matched_substrings = c.getJSONArray("matched_substrings");
                        for (int j = 0; j < matched_substrings.length(); j++) {
                            JSONObject m = matched_substrings.getJSONObject(j);
                            String length = m.getString("length");
                            String offset = m.getString("offset");
                        }*/
                        //String placeid = c.getString("place_id");
                        //String reference = c.getString("reference");
                        //JSONObject structured_formatting = c.getJSONObject("structured_formatting");
                        //String main_text = structured_formatting.getString("main_text");
                        /*JSONArray main_text_matched_substrings = structured_formatting.getJSONArray("main_text_matched_substrings");
                        for (int k = 0; k < main_text_matched_substrings.length(); k++) {
                            JSONObject mt = main_text_matched_substrings.getJSONObject(k);
                            String length1 = mt.getString("length");
                            String offset1 = mt.getString("offset");
                        }*/
                        //String secondary_text = structured_formatting.getString("secondary_text");
                        /*JSONArray terms = c.getJSONArray("terms");
                        for (int p = 0; p < terms.length(); p++) {
                            JSONObject t = terms.getJSONObject(p);
                            String offset = t.getString("offset");
                            String value = t.getString("value");
                        }*/
                        //String types = c.getString("types");
                        cityList.add(description);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();*/
                        }
                    });

                }

            } else {
                //Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(getApplicationContext(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, cityList);
            adapter.notifyDataSetChanged();
            cerca.setAdapter(adapter);
            if (!cerca.isPopupShowing()) {
                cerca.showDropDown();
            }
            getCityRequest = null;
            //pd.dismiss();
        }
    }

    public class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            switch (v.getId()) {
                case R.id.activity_main_cerca:
                    String ricerca = cerca.getText().toString();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        if (!ricerca.equals("")) {
                            if (ricerca.charAt(0) >= 65 && ricerca.charAt(0) <= 90 || ricerca.charAt(0) >= 97 && ricerca.charAt(0) <= 122) {
                                if (!cerca.getValidator().isValid(ricerca)) {
                                    ricerca = cerca.getValidator().fixText(ricerca).toString();
                                    if (!ricerca.equals("")) {
                                        Intent intent = new Intent(MainActivity.this, RicercaActivity.class);
                                        intent.putExtra("citta", ricerca);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Inserisci una città valida", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(MainActivity.this, RicercaActivity.class);
                                    intent.putExtra("citta", ricerca);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Inserisci una città valida", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Inserisci una città valida", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        new AlertDialog.Builder(MainActivity.this)
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
                    break;
            }

            /*HttpHandler sh = new HttpHandler();
            String url_part1 = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=";
            String url_part2 = "&types=";
            String vaffa1 = "(";
            String vaffa2 = "cities";
            String vaffa3 = ")";
            String vaffa4 = "&language=it&key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
                    //"key=AIzaSyAD1xAMtZ0YaMSii5iDkTJrFv0jz9cEz2U";
            String ricercaurl = ricerca.replace(" ", "+").toString();
            String url_finale = url_part1 + ricercaurl + url_part2 + vaffa1 + vaffa2 + vaffa3 + vaffa4;


            Log.e("messaggini", url_finale);
            String jsonStr = sh.makeServiceCall(url_finale);
            //Log.e("messaggini", jsonStr);
            if (jsonStr != null) {
                Log.e("messaggini", "dentro l'if");
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray predictions = jsonObj.getJSONArray("predictions");
                    JSONArray status = jsonObj.getJSONArray("status");
                    Log.e("messaggini", status.getString(0));
                    JSONObject c = predictions.getJSONObject(0);
                    String description = c.getString("description");
                    Log.e("messaggini", description);

                }catch (final JSONException e){
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                Intent intent = new Intent(MainActivity.this, RicercaActivity.class);
                intent.putExtra("citta", ricerca);
                startActivity(intent);
            }*/

        }


    }

    private TextView.OnEditorActionListener editTextListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                String ricerca = cerca.getText().toString();
                if (!ricerca.equals("")) {
                    if (ricerca.charAt(0) >= 65 && ricerca.charAt(0) <= 90 || ricerca.charAt(0) >= 97 && ricerca.charAt(0) <= 122) {
                        if (!cerca.getValidator().isValid(ricerca)) {
                            ricerca = cerca.getValidator().fixText(ricerca).toString();
                            if (!ricerca.equals("")) {
                                Intent intent = new Intent(MainActivity.this, RicercaActivity.class);
                                intent.putExtra("citta", ricerca);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Inserisci una città valida", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent(MainActivity.this, RicercaActivity.class);
                            intent.putExtra("citta", ricerca);
                            startActivity(intent);
                        }
                        if (check) {
                            Intent intent = new Intent(MainActivity.this, RicercaActivity.class);
                            intent.putExtra("citta", ricerca);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Inserisci una città valida", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Inserisci una città valida", Toast.LENGTH_LONG).show();
                }
            }
            return false;
        }
    };

}


