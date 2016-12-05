package com.example.ale.myapplicatio;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class RicercaActivity extends AppCompatActivity {
    private ListView itemsListView;
    private ArrayList<ItemRicercaActivity> arrayList;

    //per SlideMenu
    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private RelativeLayout mainContent;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca);
        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            RicercaActivityFragmentMenu firstFragment = new RicercaActivityFragmentMenu();
            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
        }

        //sliding menu
        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mainContent = (RelativeLayout) findViewById(R.id.main_content);
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

    /*@Override
    public void onResume() {
        super.onResume();
        // Set title
        //AppCompatActivity activity = (AppCompatActivity) getActivity();
        //ActionBar actionBar = this.getSupportActionBar();
        //actionBar.setTitle("Let's go");
        this.setActionBarTitle("Let's go!");
    }*/


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
    private void replaceFragment(int pos){
        Fragment fragment = null;
        switch (pos){
            case 0:
                Intent intent = new Intent(RicercaActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent_creaViaggio = new Intent(RicercaActivity.this, CreaIlTuoViaggioActivity.class);
                startActivity(intent_creaViaggio);
                break;
            case 2:
                Intent intent_viaggi = new Intent(RicercaActivity.this, ProfiloViaggiActivity.class);
                intent_viaggi.putExtra("viaggio", "viaggio");
                startActivity(intent_viaggi);
                break;
            case 3:
                Intent intent_preferiti = new Intent(RicercaActivity.this, ProfiloViaggiActivity.class);
                intent_preferiti.putExtra("preferiti", "preferiti");
                startActivity(intent_preferiti);
                break;
            case 4:
                Intent intent_impostazioni = new Intent(RicercaActivity.this, SettingsActivity.class);
                startActivity(intent_impostazioni);
                break;
            default:
                break;
        }

        if (fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            setActionBarTitle(getIntent().getStringExtra("citta"));
            transaction.add(R.id.activity_main, fragment);
            transaction.commit();
        }
    }

          /*  arrayList = new ArrayList<ItemRicercaActivity>();
            //itemsListView = (ListView) findViewById(R.id.lista);
            Intent intent = getIntent();
            String message = intent.getStringExtra("citta");
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            //avalenza89@gmail.com
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocationName(message, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addresses.get(0);
            double longitude = address.getLongitude();
            double latitude = address.getLatitude();
            message = "location=" + Double.toString(latitude) + "," + Double.toString(longitude);

            new GetPOI().execute(message);
        }


       private class GetPOI extends AsyncTask<String, Void, Void> {


            @Override
            protected Void doInBackground(String... message) {
                HttpHandler sh = new HttpHandler();
                String output = "json";
                String[] types = {"amusement_park", "aquarium", "art_gallery", "casino", "church", "city_hall", "museum", "stadium", "zoo"};
                String type = "type=";
                for (int i = 0; i < types.length; i++) {
                    if (i != 0) {
                        type += "|" + types[i];
                    } else {
                        type += types[i];
                    }
                }
                String radius_sensor = "radius=5000&sensor=false";
                String key = "key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
                String parameters = message[0] + "&" + type + "&" + radius_sensor + "&" + key;
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/" + output + "?" + parameters;
                Log.e(TAG, "Response from url: " + url);
                String jsonStr = sh.makeServiceCall(url);


                if (jsonStr != null) {
                    try {
                        //int g = jsonStr.indexOf("results");
                        //g = g - 1;
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray html = jsonObj.getJSONArray("html_attributions");
                        String next_page = jsonObj.getString("next_page_token");
                        //jsonStr = jsonStr.substring(g);
                        Log.e(TAG, "Response from url: " + jsonStr);
                        JSONArray places = jsonObj.getJSONArray("results");
                        for (int i = 0; i < places.length(); i++) {
                            JSONObject p = places.getJSONObject(i);
                            if (p.has("geometry")) {
                                JSONObject geometry = p.getJSONObject("geometry");
                                if (geometry.has("location")) {
                                    JSONObject location = geometry.getJSONObject("location");
                                    String lat = location.getString("lat");
                                    String lng = location.getString("lng");
                                }

                                if (geometry.has("viewport")) {
                                    JSONObject viewport = geometry.getJSONObject("viewport");
                                    JSONObject northeast = viewport.getJSONObject("northeast");
                                    String lat2 = northeast.getString("lat");
                                    String lng2 = northeast.getString("lng");
                                    JSONObject southwest = viewport.getJSONObject("southwest");
                                    String lat3 = southwest.getString("lat");
                                    String lng3 = southwest.getString("lng");
                                }


                            }
                            String icon = "";
                            Uri iconU = null;
                            if (p.has("icon")) {
                                icon = p.getString("icon");
                                iconU = Uri.parse(icon);
                            }
                            if (p.has("id")) {
                                String id = p.getString("id");
                            }
                            String name = null;
                            if (p.has("name")) {
                                name = p.getString("name");
                            }

                            if (p.has("opening_hours")) {
                                JSONObject opening_hours = p.getJSONObject("opening_hours");
                                String open_now = opening_hours.getString("open_now");
                                String weekday_text = opening_hours.getString("weekday_text");
                            }
                            if (p.has("photos")) {
                                JSONArray photos = p.getJSONArray("photos");
                                for (int j = 0; j < photos.length(); j++) {
                                    JSONObject foto = photos.getJSONObject(j);
                                    String height = foto.getString("height");
                                    if (foto.has("html_attributes")) {
                                        String html_attributes = foto.getString("html_attributes");
                                    }
                                    String photo_reference = foto.getString("photo_reference");
                                    String width = foto.getString("width");
                                }
                            }
                            String place_id = "";
                            if (p.has("place_id")) {
                                place_id = p.getString("place_id");
                            }

                            if (p.has("rating")) {
                                String rating = p.getString("rating");
                            }
                            if (p.has("reference")) {
                                String reference = p.getString("reference");
                            }
                            if (p.has("scope")) {
                                String scope = p.getString("scope");
                            }
                            if (p.has("types")) {
                                String types1 = p.getString("types");
                            }
                            String vicinity = "";
                            if (p.has("vicinity")) {
                                vicinity = p.getString("vicinity");
                            }
                            ItemRicercaActivity item = new ItemRicercaActivity(name, place_id, vicinity, iconU);

                            arrayList.add(item);

                            //  Log.e(TAG,  "nome " + item.getName() + " id " + item.getPlace_id()+ "vicinity " + item.getVicinity() + "icon " + item.getIcon() );
                            //  Log.e(TAG,  "nome " + name + " id " + place_id+ "vicinity " + vicinity + "icon " + icon );

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
                }
                return null;
            }

            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                ItemAdapter adapter = new ItemAdapter(RicercaActivity.this, arrayList);
                itemsListView.setAdapter(adapter);


            }
        }
        public class ItemAdapter extends ArrayAdapter<ItemRicercaActivity> {
            public ItemAdapter(Context context, ArrayList<ItemRicercaActivity> Items) {
                super(context, 0, Items);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ItemRicercaActivity item = arrayList.get(position);
                // Check if an existing view is being reused, otherwise inflate the view
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_ricerca_item, parent, false);
                }
                // Lookup view for data population
                TextView name = (TextView) convertView.findViewById(R.id.name);
                TextView vicinity = (TextView) convertView.findViewById(R.id.vicinity);
                ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
                // Populate the data into the template view using the data object
                name.setText(item.getName());
                vicinity.setText(item.getVicinity());
                icon.setImageURI(item.getIcon());
                // Return the completed view to render on screen
                return convertView;
            }*/


    }

