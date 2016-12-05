package com.example.ale.myapplicatio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RicercaActivityFragmentList extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private String selectedCity;
    private String selectedCityLocation;
    private ArrayList<ItemRicercaActivity> arrayList;
    private ListView itemsListView;
    private String TAG = MainActivity.class.getSimpleName();
    private String selectedItem;
    private String next_page = "";
    private int count;
    private GetPOI request;
    private Button altri;




    public RicercaActivityFragmentList() {

    }

    /*public static RicercaActivityFragmentList newInstance(String param1, String param2) {
        RicercaActivityFragmentList fragment = new RicercaActivityFragmentList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        count = 0;
        if (getArguments() != null) {
            selectedCity = getArguments().getString("selectedCity");
            selectedItem = getArguments().getString("selectedItem");
            /*if(selectedItem.equals("vedere")){
                ((RicercaActivity) getActivity()).setActionBarTitle(selectedCity + " - Cosa vedere");
            }else if(selectedItem.equals("mangiare")){
                ((RicercaActivity) getActivity()).setActionBarTitle(selectedCity + " - Dove mangiare");
            }else {
                ((RicercaActivity) getActivity()).setActionBarTitle(selectedCity + " - Dove dormire");
            }*/
        }
       // Log.e(TAG, "SelectedItem =  " + selectedCity);
    }



    //@Override
    //public void onResume() {
      //  super.onResume();
        // Set title
        /*AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle("Let's go");*/
        /*if(selectedItem.equals("vedere")){
            ((RicercaActivity) getActivity()).setActionBarTitle(selectedCity + " - Cosa vedere");
        }else if(selectedItem.equals("mangiare")){
            ((RicercaActivity) getActivity()).setActionBarTitle(selectedCity + " - Dove mangiare");
        }else {
            ((RicercaActivity) getActivity()).setActionBarTitle(selectedCity + " - Dove dormire");
        }*/
    //}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ricerca_activity_fragment_list, container, false);
        ConnectivityManager cm = (ConnectivityManager)  getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null &&
                networkInfo.isConnected()){
            arrayList = new ArrayList<>();
            itemsListView = (ListView) view.findViewById(R.id.lista);
            altri = (Button) view.findViewById(R.id.ricerca_activity_fragment_list_altri);
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocationName(selectedCity, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addresses.get(0);
            double longitude = address.getLongitude();
            double latitude = address.getLatitude();
            selectedCityLocation = "location=" + Double.toString(latitude) + "," + Double.toString(longitude);
            new GetPOI().execute(selectedCityLocation);
            itemsListView.setOnItemClickListener(this);
            altri.setOnClickListener(this);
        }else {
            new AlertDialog.Builder(getContext())
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

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        ConnectivityManager cm = (ConnectivityManager)  getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            count++;
            request = new GetPOI();
            request.execute(selectedCityLocation);
        }else{
            new AlertDialog.Builder(getContext())
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

    }

    private class GetPOI extends AsyncTask<String, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= ProgressDialog.show(getContext(), "", "Caricamento in corso...", true, false);
        }

        @Override
        protected Void doInBackground(String... message) {

            HttpHandler sh = new HttpHandler();
            String output = "/json";
            String type = "type=";
            String[] typesCoseDaVedere = {"amusement_park", "aquarium", "art_gallery", "casino", "church",  "museum", "stadium", "zoo"};
            String url = "";
            String parameters = "";
            String tipo_ricerca = "";
            //String key = "language=it&key=AIzaSyDg0CUi5HwJsPRxlrR_8VFBxng3eY2aMXk";
            //String key = "language=it&key=AIzaSyBieTKI8Lmg7TuF2MgUUtK93bjpWylxLBM";
            //String key = "language=itkey=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
            String key = "language=it&key=AIzaSyAD1xAMtZ0YaMSii5iDkTJrFv0jz9cEz2U";

            if(selectedItem.equals("vedere")){
                tipo_ricerca = "nearbysearch";
                String radius_sensor = "radius=5000&sensor=false";
                for (int i = 0; i < typesCoseDaVedere.length; i++) {
                    if (i != 0) {
                        type += "|" + typesCoseDaVedere[i];
                    } else {
                        type += typesCoseDaVedere[i];
                    }
                }
                parameters = message[0] + "&" + type + "&" + radius_sensor + "&" + key;
            }
            else {
                tipo_ricerca = "textsearch";
                int virgola = selectedCity.indexOf(",");
                int spazio = selectedCity.indexOf(" ");
                String citta = "";
                if(spazio < virgola) {
                    citta = selectedCity.substring(0, spazio) + "+" + selectedCity.substring(spazio + 1, virgola);
                }else{
                    citta = selectedCity.substring(0, virgola);
                }
                if (selectedItem.equals("mangiare")) {
                    parameters = "query=restaurant+in+" + citta + "&" + key;
                } else if (selectedItem.equals("dormire")) {
                    parameters = "query=hotel+in+" + citta + "&" + key;
                }
            }
            url = "https://maps.googleapis.com/maps/api/place/" + tipo_ricerca + output + "?" + parameters;
            if(count == 0){
                url = "https://maps.googleapis.com/maps/api/place/" + tipo_ricerca + output + "?" + parameters;
            }
            else if( count > 0  && next_page != ""){
                url = url + "&pagetoken=" + next_page;
            }else{
                //ount = 3;
                url = "";
            }
            Log.e(TAG,"url " + url);
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    if(jsonObj.has("next_page_token")){
                        next_page = jsonObj.getString("next_page_token");
                    }
                    else{
                        count = 3;
                    }
                    String lat = "";
                    String lng = "";
                    JSONArray places = jsonObj.getJSONArray("results");
                    for (int i = 0; i < places.length(); i++) {
                        JSONObject p = places.getJSONObject(i);
                        String vicinity = "";
                        if(p.has("formatted_address")){
                            vicinity = p.getString("formatted_address");
                        }
                        if (p.has("geometry")) {
                            JSONObject geometry = p.getJSONObject("geometry");
                            if (geometry.has("location")) {
                                JSONObject location = geometry.getJSONObject("location");
                                lat = location.getString("lat");
                                lng = location.getString("lng");
                            }



                        }
                        String name = null;
                        if (p.has("name")) {
                            name = p.getString("name");
                        }
                        String open_now = "";
                        if (p.has("opening_hours")) {
                            JSONObject opening_hours = p.getJSONObject("opening_hours");
                            open_now = opening_hours.getString("open_now");
                        }
                        String photo_reference="";
                        if (p.has("photos")) {
                            JSONArray photos = p.getJSONArray("photos");
                            for (int j = 0; j < photos.length(); j++) {
                                JSONObject foto = photos.getJSONObject(j);
                                photo_reference = foto.getString("photo_reference");
                            }
                        }
                        String place_id = "";
                        if (p.has("place_id")) {
                            place_id = p.getString("place_id");
                        }
                        String reference="";
                        if (p.has("reference")) {
                            reference = p.getString("reference");
                        }

                        if (p.has("vicinity")) {
                            vicinity = p.getString("vicinity");
                        }
                        ItemRicercaActivity item = new ItemRicercaActivity(name, place_id, vicinity, reference,open_now, photo_reference, lat, lng);
                        arrayList.add(item);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
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
            if(count > 2){
                altri.setEnabled(false);
                altri.setTextColor(Color.LTGRAY);
            }
            ItemAdapterMenu adapter = new ItemAdapterMenu(getActivity(), arrayList);
            adapter.notifyDataSetChanged();
            itemsListView.setAdapter(adapter);
            if(count >0 ){
                itemsListView.setSelection(itemsListView.getAdapter().getCount()-21);
            }
            pd.dismiss();
        }
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ConnectivityManager cm = (ConnectivityManager)  getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            RicercaActivityFragmentListItem newFragment = new RicercaActivityFragmentListItem();
            Bundle args = new Bundle();
            args.putString("place_id",arrayList.get(position).getPlace_id());
            args.putString("lat", arrayList.get(position).getLatitudine());
            args.putString("lng", arrayList.get(position).getLongitudine());
            args.putString("orario",arrayList.get(position).getOrario());
            args.putString("selectedItem", selectedItem);
            args.putString("selectedCity", selectedCity);
            newFragment.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, newFragment);
            transaction.hide(this);
            transaction.show(newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }else{
            new AlertDialog.Builder(getContext())
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

    }

}