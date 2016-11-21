package com.example.ale.myapplicatio;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
    private int count = 0;
    private Button altro;




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

        if (getArguments() != null) {
            selectedCity = getArguments().getString("selectedCity");
            selectedItem = getArguments().getString("selectedItem");

        }
       // Log.e(TAG, "SelectedItem =  " + selectedCity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ricerca_activity_fragment_list, container, false);
        arrayList = new ArrayList<ItemRicercaActivity>();
        itemsListView = (ListView) view.findViewById(R.id.lista);
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        //avalenza89@gmail.com
        altro = (Button) view.findViewById(R.id.altri_risultati);
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
        altro.setOnClickListener(this);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        count++;
        new GetPOI().execute(selectedCityLocation);
        SystemClock.sleep(500);
        if(count > 1){
            altro.setEnabled(false);

        }
    }

    private class GetPOI extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... message) {
            HttpHandler sh = new HttpHandler();
            String output = "/json";
            String type = "type=";
            String[] typesCoseDaVedere = {"amusement_park", "aquarium", "art_gallery", "casino", "church",  "museum", "stadium", "zoo"};
            //String[] typesDoveMangiare = {"bar", "cafe", "restaurant"};
            String url = "";
            String parameters;
            String tipo_ricerca = "";
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
                int k = selectedCity.indexOf(",");
                selectedCity = selectedCity.substring(0, k);
                parameters = "query=restaurant+in+" + selectedCity + "&" + key;
            }
            url = "https://maps.googleapis.com/maps/api/place/" + tipo_ricerca + output + "?" + parameters;

            //String key = "key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
            // METTERE LA TIPOLOGIA


            boolean bool ;
            bool = false;
            //do{


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
                        JSONArray html = jsonObj.getJSONArray("html_attributions");
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

                                /*if (geometry.has("viewport")) {
                                    JSONObject viewport = geometry.getJSONObject("viewport");
                                    JSONObject northeast = viewport.getJSONObject("northeast");
                                    String lat2 = northeast.getString("lat");
                                    String lng2 = northeast.getString("lng");
                                    JSONObject southwest = viewport.getJSONObject("southwest");
                                    String lat3 = southwest.getString("lat");
                                    String lng3 = southwest.getString("lng");
                                }*/


                            }
                            String icon = "";
                            if (p.has("icon")) {
                                icon = p.getString("icon");
                            }

                            /*if (p.has("id")) {
                              String id = p.getString("id");
                            }*/
                            String name = null;
                            if (p.has("name")) {
                                name = p.getString("name");
                            }
                            String open_now = "";
                            if (p.has("opening_hours")) {
                                JSONObject opening_hours = p.getJSONObject("opening_hours");
                                 open_now = opening_hours.getString("open_now");
                                String weekday_text = opening_hours.getString("weekday_text");
                            }
                            String photo_reference_url="";
                            if (p.has("photos")) {
                                JSONArray photos = p.getJSONArray("photos");
                                for (int j = 0; j < photos.length(); j++) {
                                    JSONObject foto = photos.getJSONObject(j);
                                    //String height = foto.getString("height");
                                    /*if (foto.has("html_attributes")) {
                                        String html_attributes = foto.getString("html_attributes");
                                    }*/
                                   String photo_reference = foto.getString("photo_reference");
                                   /* String photo_reference= "";
                                    if(p.has("icon")){
                                        photo_reference = foto.getString("icon");
                                    }
                                    */
                                    photo_reference_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photo_reference + "&sensor=false&" + key;
                                    //photo_reference_url = photo_reference;
                                    //String width = foto.getString("width");
                                }
                            }
                            String place_id = "";
                            if (p.has("place_id")) {
                                place_id = p.getString("place_id");
                            }

                            /*if (p.has("rating")) {
                                String rating = p.getString("rating");
                            }*/
                            String reference="";
                            if (p.has("reference")) {
                                 reference = p.getString("reference");
                            }
                            /*if (p.has("scope")) {
                                String scope = p.getString("scope");
                            }
                            if (p.has("types")) {
                                String types1 = p.getString("types");
                            }*/
                            if (p.has("vicinity")) {
                                vicinity = p.getString("vicinity");
                            }
                            ItemRicercaActivity item = new ItemRicercaActivity(name, place_id, vicinity, reference,open_now, lat, lng);
                           // Log.e(TAG, "photo " + photo_reference_url);
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
           // } while(bool==false && count<3);

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ItemAdapterMenu adapter = new ItemAdapterMenu(getActivity(), arrayList);
            adapter.notifyDataSetChanged();
            itemsListView.setAdapter(adapter);
            if(count >0 ){
                itemsListView.setSelection(itemsListView.getAdapter().getCount()-21);
            }



        }
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RicercaActivityFragmentListItem newFragment = new RicercaActivityFragmentListItem();
        Bundle args = new Bundle();
        args.putString("place_id",arrayList.get(position).getPlace_id());
        args.putString("lat", arrayList.get(position).getLatitudine());
        args.putString("lng", arrayList.get(position).getLongitudine());
        args.putString("orario",arrayList.get(position).getOrario());
        args.putString("selectedItem", selectedItem);
        newFragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }



   /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
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
     *
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
     */
}
