package com.example.ale.myapplicatio;

import android.content.Context;
import android.net.Uri;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RicercaActivityFragmentListItem.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RicercaActivityFragmentListItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RicercaActivityFragmentListItem extends Fragment {

    private String reference;
    private TextView titolo;
    private ImageView foto;
    private TextView orario;
    private TextView telefono;
    private TextView indirizzo;
    private TextView link;
    private String TAG = RicercaActivityFragmentListItem.class.getSimpleName();
    private ArrayList<ItemRicercaActivity> arrayList;
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
            reference = getArguments().getString("reference");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ricerca_activity_fragment_list_item, container, false);
        titolo = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemTitolo);
        foto = (ImageView) view.findViewById(R.id.RicercaActivityFragmentListItemImmagine);
        orario = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemOrario);
        telefono = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemTelefono);
        link = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemLink);
        indirizzo = (TextView) view.findViewById(R.id.RicercaActivityFragmentListItemIndirizzo);
        new GetPOI().execute("location=44.1306408,8.2609671");


        return view;
    }
    private class GetPOI extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... message) {
            HttpHandler sh = new HttpHandler();
            String output = "json";
            String type = "type=";
            String[] typesCoseDaVedere = {"amusement_park", "aquarium", "art_gallery", "casino", "church",  "museum", "stadium", "zoo"};
            String[] typesDoveMangiare = {"bar", "cafe", "restaurant"};

                for (int i = 0; i < typesCoseDaVedere.length; i++) {
                    if (i != 0) {
                        type += "|" + typesCoseDaVedere[i];
                    } else {
                        type += typesCoseDaVedere[i];
                    }
                }


            String radius_sensor = "radius=5000&sensor=false";
            String key = "key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
            //String key = "key=AIzaSyAD1xAMtZ0YaMSii5iDkTJrFv0jz9cEz2U";
            String parameters = message[0] + "&" + type + "&" + radius_sensor + "&" + key;
            int count = 0;
            boolean bool ;
            bool = false;

                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/" + output + "?" + parameters;
                if( count > 0){
                   // url = url + "&page_token=" + next_page;

                }
                String jsonStr = sh.makeServiceCall(url);
            String name="";
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray results = jsonObj.getJSONArray("results");

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject result = results.getJSONObject(i);
                             name = result.getString("name");




                        }
                           ItemRicercaActivity item = new ItemRicercaActivity(name, "", "", "");
                          //  Log.e(TAG, "photo " + photo_reference_url);
                            arrayList.add(item);
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

            ItemAdapterMenu adapter = new ItemAdapterMenu(getActivity(), arrayList);
            itemsListView.setAdapter(adapter);


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
