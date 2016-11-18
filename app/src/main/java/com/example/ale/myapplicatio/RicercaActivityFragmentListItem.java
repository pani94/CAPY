package com.example.ale.myapplicatio;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapsInitializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class RicercaActivityFragmentListItem extends Fragment{

    private String place_id;
    private String latitudine;
    private String longitudine;
    private TextView titolo;
    private ImageView foto;
    private TextView orario;
    private TextView weekday;
    private String open_now;
    private TextView telefono;
    private TextView indirizzo;
    private TextView link;
    private Button aggiungiaviaggio;
    private Button preferito;
    private DataBase database;
    private ArrayList <Viaggio> arrayListViaggi;
    //private MapView mapView;
    //private GoogleMap googleMap;
    private String TAG = RicercaActivityFragmentListItem.class.getSimpleName();
    private ArrayList<ItemRicercaActivity> arrayList;
    private String nomeViaggio = "";
    private long id;
    ItemRicercaActivityFragmentList item=null;
   // private OnFragmentInteractionListener mListener;

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
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            place_id = getArguments().getString("place_id");
            open_now = getArguments().getString("orario");
            latitudine = getArguments().getString("lat");
            longitudine = getArguments().getString("lng");
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
        weekday = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemWeekDayText);
        telefono = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemTelefono);
        link = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemLink);
        indirizzo = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemIndirizzo);
        aggiungiaviaggio = (Button) view.findViewById(R.id.bottone_aggiungiaviaggio);
        preferito = (Button) view.findViewById(R.id.bottone_aggiungipreferiti);
        ButtonListener buttonListener = new ButtonListener();
        link.setOnClickListener(buttonListener);
        aggiungiaviaggio.setOnClickListener(buttonListener);
        preferito.setOnClickListener(buttonListener);
        database = new DataBase(getActivity());
        arrayListViaggi = database.getViaggi();

        for(int i = 0; i < arrayListViaggi.size(); i++){
            Log.e("viaggi", arrayListViaggi.get(i).getNome_viaggio());
        }
        //mapView = (MapView) view.findViewById(R.id.mapView);
        //mapView.onCreate(savedInstanceState);
        //mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(Double.parseDouble(latitudine), Double.parseDouble(longitudine));
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });*/
        new GetPOI().execute(place_id);

        return view;
    }

    /*@Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }*/

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
                case R.id.bottone_aggiungiaviaggio: Attivita attivita = new Attivita(place_id, titolo.toString(), indirizzo.toString(), weekday.toString(), telefono.toString(), "", "", foto.toString(), "false");
                                                    database.insertAttivita(attivita);
                                                    String[] nomeViaggi = new String[arrayListViaggi.size()];
                                                    for(int k=0; k<arrayListViaggi.size(); k++){
                                                        nomeViaggi[k] = arrayListViaggi.get(k).getNome_viaggio();
                                                    }
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                    builder.setTitle("I TUOI VIAGGI");
                                                    builder.setSingleChoiceItems(nomeViaggi, -1, new  DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            nomeViaggio = arrayListViaggi.get(i).getNome_viaggio();
                                                            id = arrayListViaggi.get(i).getId_viaggio();
                                                        }

                                                        });
                                                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                                                        public void onClick(DialogInterface dialog, int which){
                                                            ViaggioAttivita viaggioattivita = new ViaggioAttivita(id, place_id);
                                                            database.insertViaggioAttivita(viaggioattivita);
                                                            Toast.makeText(getActivity().getApplicationContext(),
                                                                    "L'attività è stata aggiunta al viaggio",
                                                                    Toast.LENGTH_LONG).show();

                                                        }
                                                    });
                                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                                                        public void onClick(DialogInterface dialog, int which){

                                                        }
                                                    });
                                                    builder.show();
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



            }

        }
    }















    private class GetPOI extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... message) {
            HttpHandler sh = new HttpHandler();
            String output = "json";
            //String key = "key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
            String key = "key=AIzaSyAD1xAMtZ0YaMSii5iDkTJrFv0jz9cEz2U";
            String parameters = "placeid=" + message[0] + "&language=it" + "&" + key;


                String url = " https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;
            Log.e(TAG, "urlPlace " +url);
                String jsonStr = sh.makeServiceCall(url);
            String name="";
            String international_phone_number="";
            String website ="";
            String photo_reference = "";
            String formatted_address ="";
            String open_now = "";
            JSONArray weekday_text = null;
            String weekday = "";
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

            titolo.setText(item.getName());
            telefono.setText(item.getPhone());
            link.setText(item.getWebsite());
            indirizzo.setText(item.getAddress());
            if(item.getOpen_now().equalsIgnoreCase("true")){
                orario.setText("APERTO ORA");
            }else if(item.getOpen_now().equalsIgnoreCase("false")){
                orario.setText("CHIUSO ORA");
            }else{
                orario.setText("");
            }
            weekday.setText(item.getWeekday_text());
            /*if(open_now.equalsIgnoreCase("true")){
                orario.setText("APERTO ORA");
            }else if(open_now.equalsIgnoreCase("false")){
                orario.setText("CHIUSO ORA");
            }else{
                orario.setText("");
            }*/

            String photo_reference_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + item.getPhoto_reference() + "&sensor=false&key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
            new LoadImageTask().execute(photo_reference_url);



        }

    }
    public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {




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
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(U;ri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
       */

}
