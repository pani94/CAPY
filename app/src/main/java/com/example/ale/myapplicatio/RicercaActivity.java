package com.example.ale.myapplicatio;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RicercaActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private ListView itemsListView;
    private ArrayList<ItemRicercaActivity> arrayList;

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
            arrayList = new ArrayList<ItemRicercaActivity>();
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
            }
        }
    }

