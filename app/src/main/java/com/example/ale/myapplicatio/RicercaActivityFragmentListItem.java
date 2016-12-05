package com.example.ale.myapplicatio;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class RicercaActivityFragmentListItem extends Fragment implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private String place_id;
    private String latitudine;
    private String longitudine;
    private TextView titolo;
    private ImageView foto;
    private TextView orario;
    private TextView weekdayTextView;
    private TextView telefono;
    private TextView indirizzo;
    private TextView link;
    private TextView visita;
    private ImageButton preferiti_star;
    private ImageButton bottone_piu;
    private ScrollView scrollView;
    private DataBase database;
    private ArrayList<Viaggio> arrayListViaggi;
    private String name = "";
    private String international_phone_number = "";
    private String website = "";
    private String photo_reference = "";
    private String formatted_address = "";
    private String open_now = "";
    private String weekday = "";
    private String selectedItem;
    private String selectedCity;
    private MapView mMapView;
    private GoogleMap googleMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private String TAG = RicercaActivityFragmentListItem.class.getSimpleName();
    private ArrayList<ItemRicercaActivity> arrayList;

    private String nomeViaggio = "";
    private long id;
    private boolean giallo;
    ItemRicercaActivityFragmentList item = null;
    private ProgressDialog pd;


    public RicercaActivityFragmentListItem() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
   /* public static RicercaActivityFragmentListItem newInstance(String param1, String param2) {
        RicercaActivityFragmentListItem fragment = new RicercaActivityFragmentListItem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd= ProgressDialog.show(getContext(), "", "Caricamento in corso...", true, false);
        if (getArguments() != null) {
            place_id = getArguments().getString("place_id");
            open_now = getArguments().getString("orario");
            latitudine = getArguments().getString("lat");
            longitudine = getArguments().getString("lng");
            selectedItem = getArguments().getString("selectedItem");
            selectedCity = getArguments().getString("selectedCity");
            /*if(selectedItem.equals("vedere")){
                ((RicercaActivity) getActivity()).setActionBarTitle(selectedCity + " - Cosa vedere");
            }else if(selectedItem.equals("mangiare")){
                ((RicercaActivity) getActivity()).setActionBarTitle(selectedCity + " - Dove mangiare");
            }else {
                ((RicercaActivity) getActivity()).setActionBarTitle(selectedCity + " - Dove dormire");
            }*/
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ricerca_activity_fragment_list_item, container, false);
        arrayList = new ArrayList<ItemRicercaActivity>();
        titolo = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemTitolo);
        foto = (ImageView) view.findViewById(R.id.RicercaActivityFragmentListItemImmagine);
        orario = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemOrario);
        weekdayTextView = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemWeekDayText);
        telefono = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemTelefono);
        link = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemLink);
        visita = (TextView) view.findViewById(R.id.ricerca_activity_fragment_list_visita);
        indirizzo = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemIndirizzo);
        preferiti_star = (ImageButton) view.findViewById(R.id.preferiti_star);
        bottone_piu = (ImageButton) view.findViewById(R.id.fragment_ricerca_activity_list_item_bottonepiu);
        scrollView = (ScrollView) view.findViewById(R.id.ricerca_activity_fragment_list_item_scrollview);
        mMapView = (MapView) view.findViewById(R.id.mapView_ricerca_activity_list_item);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();  // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        MapGetMapAsync(latitudine,longitudine);

        ButtonListener buttonListener = new ButtonListener();
        link.setOnClickListener(buttonListener);
        bottone_piu.setOnClickListener(buttonListener);
        database = new DataBase(getActivity());
        arrayListViaggi = database.getViaggi();
        giallo = database.getAttivitaPreferita(place_id);
        if (giallo == true){
            preferiti_star.setImageResource(R.drawable.yellow_star);
        }else{
            preferiti_star.setImageResource(R.drawable.grey_star);
        }

        preferiti_star.setOnClickListener(buttonListener);
        titolo.setTypeface(Typeface.DEFAULT_BOLD);
        titolo.setShadowLayer(5,0,0, Color.BLACK);
        for(int i = 0; i < arrayListViaggi.size(); i++){
            Log.e("viaggi", arrayListViaggi.get(i).getNome_viaggio());
        }

        new GetPOI().execute(place_id);

        return view;
    }





    private class GetPOI extends AsyncTask<String, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pd = ProgressDialog.show(getActivity(), "", "Caricamento in corso...", true, false);
        }

        @Override
        protected Void doInBackground(String... message) {
            HttpHandler sh = new HttpHandler();
            String output = "json";
            //String key = "key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
            //String key = "key=AIzaSyDg0CUi5HwJsPRxlrR_8VFBxng3eY2aMXk";
            //String key = "key=AIzaSyBieTKI8Lmg7TuF2MgUUtK93bjpWylxLBM";
            String key = "key=AIzaSyAD1xAMtZ0YaMSii5iDkTJrFv0jz9cEz2U";
            String parameters = "placeid=" + message[0] + "&language=it" + "&" + key;


                String url = " https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;
            Log.e(TAG, "urlPlace " +url);
                String jsonStr = sh.makeServiceCall(url);
            JSONArray weekday_text = null;
            if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONObject result = jsonObj.getJSONObject("result");
                        if(result.has("formatted_address")){
                            formatted_address = result.getString("formatted_address");
                        }
                        if(result.has("international_phone_number")){
                            international_phone_number = result.getString("international_phone_number");
                        }
                        if(result.has("name")){
                            name = result.getString("name");
                        }
                        if (result.has("opening_hours")) {
                            JSONObject opening_hours = result.getJSONObject("opening_hours");
                            open_now = opening_hours.getString("open_now");
                            weekday_text = opening_hours.getJSONArray("weekday_text");
                            for(int i=0; i<weekday_text.length(); i++){
                                String wdt = weekday_text.getString(i);
                                weekday += wdt + "\n";
                            }
                        }
                        if(result.has("photos")){
                            JSONArray photos = result.getJSONArray("photos");
                            JSONObject p = photos.getJSONObject(0);
                            if(p.has("photo_reference")){
                                photo_reference = p.getString("photo_reference");
                            }
                        }
                        if(result.has("website")){
                            website = result.getString("website");
                        }





                        //  Log.e(TAG, "photo " + photo_reference_url);

                        item = new ItemRicercaActivityFragmentList(name,international_phone_number,website,photo_reference,formatted_address, open_now, weekday);
                          //  Log.e(TAG, "photo " + photo_reference_url);

                        }
                 catch (final JSONException e) {
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
            ConnectivityManager cm = (ConnectivityManager)  getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null &&
                    networkInfo.isConnected()){
                titolo.setText(item.getName());
                telefono.setText(item.getPhone());
                link.setText(item.getWebsite());
                if(link.getText().equals("")){
                    visita.setVisibility(View.INVISIBLE);
                }
                indirizzo.setText(item.getAddress());
                if(item.getOpen_now().equalsIgnoreCase("true")){
                    orario.setText("APERTO ORA");
                    orario.setTextColor(Color.GREEN);
                }else if(item.getOpen_now().equalsIgnoreCase("false")){
                    orario.setText("CHIUSO ORA");
                    orario.setTextColor(Color.RED);
                }else{
                    orario.setText("");
                }
                weekdayTextView.setText(item.getWeekday_text());
            }

            /*if(open_now.equalsIgnoreCase("true")){
                orario.setText("APERTO ORA");
            }else if(open_now.equalsIgnoreCase("false")){
                orario.setText("CHIUSO ORA");
            }else{
                orario.setText("");
            }*/

            String photo_reference_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + item.getPhoto_reference() + "&sensor=false&key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
            new LoadImageTask().execute(photo_reference_url);
            pd.dismiss();
        }

    }

    public class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.RicercaActivityFragmentListItemLink:  Bundle bundle = new Bundle();
                    String link = bundle.getString("link");
                    Uri viewUri = Uri.parse(link);
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, viewUri);
                    startActivity(viewIntent);
                    break;
                case R.id.fragment_ricerca_activity_list_item_bottonepiu:
                    if(database.getViaggiBool()) {
                        Attivita attivita = new Attivita(place_id, name, formatted_address, weekday, international_phone_number, website, selectedItem, photo_reference, "false",latitudine,longitudine);
                        database.insertAttivita(attivita);
                        final String[] nomeViaggi = new String[arrayListViaggi.size()];
                        for (int k = 0; k < arrayListViaggi.size(); k++) {
                            nomeViaggi[k] = arrayListViaggi.get(k).getNome_viaggio();
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("I TUOI VIAGGI");
                        builder.setSingleChoiceItems(nomeViaggi,-1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                nomeViaggio = arrayListViaggi.get(i).getNome_viaggio();
                                id = arrayListViaggi.get(i).getId_viaggio();
                            }

                        });
                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(!nomeViaggio.equals("")){
                                    ViaggioAttivita viaggioattivita = new ViaggioAttivita(id, place_id);
                                    database.insertViaggioAttivita(viaggioattivita);
                                    String stampa = "L'attività è stata aggiunta al viaggio: " + nomeViaggio;
                                    Toast.makeText(getActivity().getApplicationContext(),
                                            stampa,
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getActivity().getApplicationContext(),"Devi selezionare un viaggio",
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                    }else{
                        new android.support.v7.app.AlertDialog.Builder(getContext())
                                .setTitle("ATTENZIONE")
                                .setMessage("Per poter inserire un'attività devi creare prima un viaggio.")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                    break;

                                                    /*ItemAdapterViaggio adapter = new ItemAdapterViaggio(getActivity(), arrayListViaggi);
                                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                                    LayoutInflater inflater = getActivity().getLayoutInflater();
                                                    View convertView = (View) inflater.inflate(R.layout.alert_ituoi_viaggi, null);
                                                    alertDialog.setView(convertView);
                                                    alertDialog.setTitle("I TUOI VIAGGI");
                                                    ListView lv = (ListView) convertView.findViewById(R.id.alert_ituoi_viaggi_listaviaggi);
                                                    lv.setAdapter(adapter);
                                                    alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                                                        public void onClick(DialogInterface dialog, int which){

                                                        }
                                                    });
                                                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                                                        public void onClick(DialogInterface dialog, int which){

                                                        }
                                                    });
                                                    alertDialog.show();
                                                    break;*/

                case R.id.preferiti_star:
                    if(!giallo){
                        Attivita attivitapreferita = new Attivita(place_id, name, formatted_address, weekday, international_phone_number, website, selectedItem, photo_reference, "true",latitudine,longitudine);
                        database.UpdateAttivitaPreferita(attivitapreferita);
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Attività aggiunta ai preferiti",
                                Toast.LENGTH_SHORT).show();
                        preferiti_star.setImageResource(R.drawable.yellow_star);
                        giallo = true;
                    }else{

                        database.deletePreferiti(place_id);
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Attività eliminata dai preferiti",
                                Toast.LENGTH_SHORT).show();
                        preferiti_star.setImageResource(R.drawable.grey_star);
                        giallo = false;
                    }


                    break;



            }

        }
    }



    public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        //ProgressDialog pd;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //pd= ProgressDialog.show(getContext(), "", "Caricamento in corso...", true, false);
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
                foto.setImageBitmap(bitmap);
            }
            pd.dismiss();
        }
    }
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    public void MapGetMapAsync(final String latitudine, final String longitudine){
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                // For showing a move to my location button
              /*  if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("perm","permesso negato");
                    return;
                }
                googleMap.setMyLocationEnabled(true);*/
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                           android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        buildGoogleApiClient();
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
                LatLng position = new LatLng(Double.parseDouble(latitudine), Double.parseDouble(longitudine));
                googleMap.addMarker(new MarkerOptions().position(position));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected( Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }

                } else {

                    new android.support.v7.app.AlertDialog.Builder(getContext())
                            .setTitle("ATTENZIONE")
                            .setMessage("Permesso negato.")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

}
