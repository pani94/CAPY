package com.example.ale.myapplicatio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RicercaActivityFragmentListItem extends Fragment {

    private String place_id;
    private String hour;
    private TextView titolo;
    private ImageView foto;
    private TextView orario;
    private TextView telefono;
    private TextView indirizzo;
    private TextView link;
    private String TAG = RicercaActivityFragmentListItem.class.getSimpleName();
    private ArrayList<ItemRicercaActivity> arrayList;
    private int position;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            place_id = getArguments().getString("place_id");
            hour = getArguments().getString("open_now");
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
        telefono = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemTelefono);
        link = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemLink);
        indirizzo = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemIndirizzo);
        new GetPOI().execute(place_id);

        return view;
    }
    private class GetPOI extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... message) {
            HttpHandler sh = new HttpHandler();
            String output = "json";
            String key = "key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
            //String key = "key=AIzaSyAD1xAMtZ0YaMSii5iDkTJrFv0jz9cEz2U";
            String parameters = "placeid=" + message[0] +  "&" + key;


                String url = " https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;
            Log.e(TAG, "urlPlace " +url);
                String jsonStr = sh.makeServiceCall(url);
            String name="";
            String international_phone_number="";
            String website ="";
            String photo_reference = "";
            String formatted_address ="";
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

                        item = new ItemRicercaActivityFragmentList(name,international_phone_number,website,photo_reference,formatted_address);
                        Log.e(TAG, "placeName " + item.getName());
                        Log.e(TAG, "placeindirizzo " + item.getAddress());
                        Log.e(TAG, "placeNPhone " + item.getPhone());
                        Log.e(TAG, "placewebsite " + item.getWebsite());





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
            orario.setText(hour);


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
