package com.example.ale.myapplicatio;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {

    private AutoCompleteTextView cerca;
    private Button bottone;
    private String TAG = MainActivity.class.getSimpleName();
    ArrayList<String> cityList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getCityRequest = null;
        if (findViewById(R.id.fragment_bottone_crea) != null) {
            if (savedInstanceState != null) {
                return;
            }

            FragmentBottoneCreaIlTuoViaggio fragment_bottone = new FragmentBottoneCreaIlTuoViaggio();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_bottone_crea, fragment_bottone).commit();
        }


        DataBase db = new DataBase(this);
        ButtonListener buttonListener = new ButtonListener();
        cityList = new ArrayList<>();
        bottone = (Button) findViewById(R.id.bottone);
        cerca = (AutoCompleteTextView) findViewById(R.id.cerca);
        cerca.addTextChangedListener(passwordWatcher);
        bottone.setOnClickListener(buttonListener);
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
                if(a != null){
                    Log.e("messaggini", "a!=null");
                    if(!a.isEmpty()){
                        Log.e("messaggini", "a!=empty");
                        for(int i=0; i<a.getCount(); i++){
                            //stringa = a.getItem(0).toString();
                            Log.e("messaggini", a.getItem(i).toString());
                            if(s.equals(a.getItem(i).toString())){
                                Log.e("messaggini", "isValid return true");
                                return true;
                            }
                        }
                    }else{
                        Log.e("messaggini", "a vuoto");
                        if(check){
                            Log.e("messaggini", "check true && isValid return true");
                            return true;
                        }
                    }
                }
                Log.e("messaggini", "isValid return false");
                return false;
            }

            @Override
            public String fixText(CharSequence charSequence) {
                String s = charSequence.toString();
                Adapter a = cerca.getAdapter();
                if (a != null) {
                    Log.e("messaggini", "fix text: a!=null");
                    if (!a.isEmpty()) {
                        Log.e("messaggini", "fix text: a!=empty");
                        Log.e("messaggini", a.getItem(0).toString());
                        return a.getItem(0).toString();
                    }else{
                        //Log.e("messaggini", stringa);
                        //return stringa;
                    }
                } else {
                    if (check) {
                        Log.e("messaggini", "check true && fix text return s: " + s);
                        return s;
                    }
                }
                Log.e("messaggini", "non puoi andare avanti");
                return "";
            }
        });

        //sliding menu
        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mainContent = (RelativeLayout) findViewById(R.id.main_content);
        listSliding = new ArrayList<>();

        //add item for sliding list
        listSliding.add(new ItemSlideMenu(R.drawable.ic_account_circle_black_24dp, "Profilo"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_business_center_black_24dp, "Crea un nuovo viaggio"));
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

        final TextWatcher passwordWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                check = false;
                if(s.length() >0) {
                    if(getCityRequest == null){
                        getCityRequest = new GetCity();
                        getCityRequest.execute(s.toString());
                    }else{
                        getCityRequest.cancel(true);
                        getCityRequest = new GetCity();
                        getCityRequest.execute(s.toString());
                    }
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
                Intent intent = new Intent(MainActivity.this, ProfiloViaggiActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent2 = new Intent(MainActivity.this, CreaIlTuoViaggioActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }

        if (fragment != null){
            FragmentManager  fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.activity_main, fragment);
            transaction.commit();


        }
    }

    private class GetCity extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // Toast.makeText(MainActivity.this, "Json Data is downloading " , Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();
            String url1 = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=";
            String url2 ="&types=(cities)&language=it&key=AIzaSyAD1xAMtZ0YaMSii5iDkTJrFv0jz9cEz2U";
            //String url2 ="&types=(cities)&key=AIzaSyBieTKI8Lmg7TuF2MgUUtK93bjpWylxLBM";
            String url= url1 + arg0[0] + url2 ;
            Log.e("messaggini", url);
            String jsonStr = sh.makeServiceCall(url);
                       if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray predictions = jsonObj.getJSONArray("predictions");
                    // looping through All Contacts
                    for (int i = 0; i < predictions.length(); i++) {
                        if(isCancelled()){
                            Log.e("messaggini", "cancellato");
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
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
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
        }
    }

    public class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String ricerca = cerca.getText().toString();
            if(!ricerca.equals("")){
                if(ricerca.charAt(0) >= 65 && ricerca.charAt(0) <= 90 || ricerca.charAt(0) >= 97 && ricerca.charAt(0) <= 122 ){
                    if(!cerca.getValidator().isValid(ricerca)){
                        ricerca = cerca.getValidator().fixText(ricerca).toString();
                        if(!ricerca.equals("")){
                            Intent intent = new Intent(MainActivity.this, RicercaActivity.class);
                            intent.putExtra("citta", ricerca);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(),"Inserisci una città valida",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Intent intent = new Intent(MainActivity.this, RicercaActivity.class);
                        intent.putExtra("citta", ricerca);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Inserisci una città valida",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Inserisci una città valida",Toast.LENGTH_LONG).show();
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
                if(!ricerca.equals("")){
                    if(ricerca.charAt(0) >= 65 && ricerca.charAt(0) <= 90 || ricerca.charAt(0) >= 97 && ricerca.charAt(0) <= 122 ){
                        if(!cerca.getValidator().isValid(ricerca)){
                            ricerca = cerca.getValidator().fixText(ricerca).toString();
                            if(!ricerca.equals("")){
                                Intent intent = new Intent(MainActivity.this, RicercaActivity.class);
                                intent.putExtra("citta", ricerca);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getApplicationContext(),"Inserisci una città valida",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Intent intent = new Intent(MainActivity.this, RicercaActivity.class);
                            intent.putExtra("citta", ricerca);
                            startActivity(intent);
                        }
                        if(check){
                            Intent intent = new Intent(MainActivity.this, RicercaActivity.class);
                            intent.putExtra("citta", ricerca);
                            startActivity(intent);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Inserisci una città valida",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Inserisci una città valida",Toast.LENGTH_LONG).show();
                }
            }
            return false;
        }
    };

}


