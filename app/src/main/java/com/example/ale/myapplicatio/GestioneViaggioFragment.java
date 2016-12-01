package com.example.ale.myapplicatio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class GestioneViaggioFragment extends Fragment implements AdapterView.OnItemClickListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private TextView nome_viaggio;
    private TextView daquando_aquando;
    private String nome_viaggio_get;
    private String daquando_get;
    private String aquando_get;
    private String daquando_aquando_get;
    private DataBase db ;
    private AutoCompleteTextView autoCompleteCerca;
    private Button bottone_cerca;
    private boolean check;
    private GetCity getCityRequest;
    ArrayList<String> cityList;
    private String[] scelte;
    private ListView listView;
    private DataBase database;
    private MapView mMapView;
    private GoogleMap googleMap;
    GoogleApiClient mGoogleApiClient;

    public GestioneViaggioFragment() {
        // Required empty public constructor
    }

    /*public static GestioneViaggioFragment newInstance(String param1, String param2) {
        GestioneViaggioFragment fragment = new GestioneViaggioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCityRequest = null;
        cityList = new ArrayList<>();
        if (getArguments() != null) {
            nome_viaggio_get = getArguments().getString("nome_viaggio");
            daquando_get = getArguments().getString("daquando");
            aquando_get = getArguments().getString("aquando");
            daquando_aquando_get = "da "+ daquando_get + " a " + aquando_get;
        }



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gestione_viaggio, container, false);
        ButtonListener buttonListener = new ButtonListener();
        nome_viaggio = (TextView) view.findViewById(R.id.gestione_viaggio_nome);
        daquando_aquando = (TextView) view.findViewById(R.id.gestione_viaggio_daquando_aquando);
        autoCompleteCerca = (AutoCompleteTextView) view.findViewById(R.id.gestione_viaggio_autocomplete);
        bottone_cerca = (Button) view.findViewById(R.id.gestione_viaggio_bottone_cerca);
        listView = (ListView) view.findViewById(R.id.lista_gestione_viaggio);
        nome_viaggio.setText(nome_viaggio_get);
        daquando_aquando.setText(daquando_aquando_get);
        mMapView = (MapView) view.findViewById(R.id.fragment_gestione_viaggio_mappa);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        MapGetMapAsync();
        bottone_cerca.setOnClickListener(buttonListener);

        scelte = new String[3];
        scelte[0] = "Attivita";
        scelte[1] = "Agenda";
        scelte[2] = "Galleria";

       ItemAdapterGestioneViaggio adapter = new ItemAdapterGestioneViaggio(getActivity(), scelte, nome_viaggio_get);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);


        autoCompleteCerca.addTextChangedListener(passwordWatcher);
        autoCompleteCerca.setOnEditorActionListener(editTextListener);
        autoCompleteCerca.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                check = true;
            }
        });
        autoCompleteCerca.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence charSequence) {
                String s = charSequence.toString();
                Adapter a = autoCompleteCerca.getAdapter();
                if(a != null){
                    if(!a.isEmpty()){
                        for(int i=0; i<a.getCount(); i++){
                            if(s.equals(a.getItem(i).toString())){
                                return true;
                            }
                        }
                    }else{
                        if(check){
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public String fixText(CharSequence charSequence) {
                String s = charSequence.toString();
                Adapter a = autoCompleteCerca.getAdapter();
                if (a != null) {
                    if (!a.isEmpty()) {
                        return a.getItem(0).toString();
                    }else{
                        //Log.e("messaggini", stringa);
                        //return stringa;
                    }
                } else {
                    if (check) {
                        return s;
                    }
                }
                return "";
            }
        });
        return view;
    }

    final TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            check = false;
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
            }
        }

        public void afterTextChanged(Editable s) {
            cityList.clear();
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        db = new DataBase(getActivity());
        if(scelte[position].equals("Attivita")){
            if(db.getViaggiAttivitaBool(db.getIdViaggio(nome_viaggio_get))){
                Intent intent = new Intent(getActivity(), GestioneViaggioAttivitaActivity.class);
                intent.putExtra("attivita_nomeviaggio",nome_viaggio_get);
                startActivity(intent);
            }else{
                Toast.makeText(getContext(),"Non hai ancora inserito attivita per questo viaggio",Toast.LENGTH_SHORT).show();
            }
        }else if(scelte[position].equals("Agenda")){
            Intent intent = new Intent(getActivity(), GestioneViaggioAgendaActivity.class);
            intent.putExtra("numgiorni",db.getNumeroDiGiorni(db.getIdViaggio(nome_viaggio_get)));
            intent.putExtra("attivita_nomeviaggio",nome_viaggio_get);
            startActivity(intent);
        }else if(scelte[position].equals("Galleria")){
            Intent intentG = new Intent(getActivity(), GestioneViaggioGalleriaActivity.class);
            intentG.putExtra("attivita_nomeviaggio",nome_viaggio_get);
            startActivity(intentG);
        }
    }

    private class GetCity extends AsyncTask<String, Void, Void> {
        //ProgressDialog pd;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //pd = ProgressDialog.show(getActivity(), "", "Caricamento in corso...", true, false);
        }

        @Override
        protected Void doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();
            //String chiave = "key=AIzaSyAD1xAMtZ0YaMSii5iDkTJrFv0jz9cEz2U";
            String chiave ="key=AIzaSyBieTKI8Lmg7TuF2MgUUtK93bjpWylxLBM";
            //String chiave = "key=AIzaSyDg0CUi5HwJsPRxlrR_8VFBxng3eY2aMXk";
            //String chiave = "key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
            String url1 = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=";
            String url2 ="&types=(cities)&language=it&" + chiave;
            String url= url1 + arg0[0] + url2 ;
            Log.e("messaggini", url);
            String jsonStr = sh.makeServiceCall(url);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray predictions = jsonObj.getJSONArray("predictions");
                    for (int i = 0; i < predictions.length(); i++) {
                        if(isCancelled()){
                            Log.e("messaggini", "cancellato");
                        }
                        JSONObject c = predictions.getJSONObject(i);
                        String description = c.getString("description");
                        cityList.add(description);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });*/

                }

            } else {
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });*/
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cityList);
            adapter.notifyDataSetChanged();
            autoCompleteCerca.setAdapter(adapter);
            if (!autoCompleteCerca.isPopupShowing()) {
                autoCompleteCerca.showDropDown();
            }
            getCityRequest = null;
            //pd.dismiss();
        }
    }


    public class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            DataBase db = new DataBase(getActivity());
            String ricerca = autoCompleteCerca.getText().toString();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (!ricerca.equals("")) {
                    if (ricerca.charAt(0) >= 65 && ricerca.charAt(0) <= 90 || ricerca.charAt(0) >= 97 && ricerca.charAt(0) <= 122) {
                        if (!autoCompleteCerca.getValidator().isValid(ricerca)) {
                            ricerca = autoCompleteCerca.getValidator().fixText(ricerca).toString();
                            if (!ricerca.equals("")) {
                                Intent intent_cerca = new Intent(getActivity(), RicercaActivity.class);
                                intent_cerca.putExtra("citta", ricerca);
                                startActivity(intent_cerca);
                            } else {
                                Toast.makeText(getActivity(), "Inserisci una città valida", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent_cerca = new Intent(getActivity(), RicercaActivity.class);
                            intent_cerca.putExtra("citta", ricerca);
                            startActivity(intent_cerca);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Inserisci una città valida", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Inserisci una città valida", Toast.LENGTH_LONG).show();
                }
            }else{
                new AlertDialog.Builder(getActivity())
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


    private TextView.OnEditorActionListener editTextListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                String ricerca = autoCompleteCerca.getText().toString();
                if(!ricerca.equals("")){
                    if(ricerca.charAt(0) >= 65 && ricerca.charAt(0) <= 90 || ricerca.charAt(0) >= 97 && ricerca.charAt(0) <= 122 ){
                        if(!autoCompleteCerca.getValidator().isValid(ricerca)){
                            ricerca = autoCompleteCerca.getValidator().fixText(ricerca).toString();
                            if(!ricerca.equals("")){
                                Intent intent = new Intent(getActivity(), RicercaActivity.class);
                                intent.putExtra("citta", ricerca);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getActivity(),"Inserisci una città valida",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Intent intent = new Intent(getActivity(), RicercaActivity.class);
                            intent.putExtra("citta", ricerca);
                            startActivity(intent);
                        }
                        if(check){
                            Intent intent = new Intent(getActivity(), RicercaActivity.class);
                            intent.putExtra("citta", ricerca);
                            startActivity(intent);
                        }
                    }else{
                        Toast.makeText(getActivity(),"Inserisci una città valida",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(),"Inserisci una città valida",Toast.LENGTH_LONG).show();
                }
            }
            return false;
        }
    };

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
    public void MapGetMapAsync(){
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
                LatLng position=null;
                DataBase db = new DataBase(getContext());
                ArrayList<Attivita> attivitas = db.getAttivita(nome_viaggio_get,"tutte");
                if(attivitas.size() > 0){
                    for (int i = 0; i < attivitas.size();i++){
                        position = new LatLng(Double.parseDouble(attivitas.get(i).getLatitudine()), Double.parseDouble(attivitas.get(i).getLongitudine()));
                        googleMap.addMarker(new MarkerOptions().position(position).title(attivitas.get(i).getNome()));
                    }
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }else{
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    String provider = locationManager.getBestProvider(criteria, true);
                    Location myLocation = locationManager.getLastKnownLocation(provider);
                    double latitude = myLocation.getLatitude();
                    double longitude = myLocation.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("").snippet(""));
                }



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

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
   /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    /*

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    /*

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    } */

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
