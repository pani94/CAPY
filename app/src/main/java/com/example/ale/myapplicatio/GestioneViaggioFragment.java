package com.example.ale.myapplicatio;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class GestioneViaggioFragment extends Fragment implements AdapterView.OnItemClickListener {

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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Toast.makeText(MainActivity.this, "Json Data is downloading " , Toast.LENGTH_LONG).show();

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
        }
    }


    public class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            DataBase db = new DataBase(getActivity());
            String ricerca = autoCompleteCerca.getText().toString();
            if(!ricerca.equals("")){
                if(ricerca.charAt(0) >= 65 && ricerca.charAt(0) <= 90 || ricerca.charAt(0) >= 97 && ricerca.charAt(0) <= 122 ){
                    if(!autoCompleteCerca.getValidator().isValid(ricerca)){
                        ricerca = autoCompleteCerca.getValidator().fixText(ricerca).toString();
                        if(!ricerca.equals("")){
                            Intent intent_cerca = new Intent(getActivity(), RicercaActivity.class);
                            intent_cerca.putExtra("citta", ricerca);
                            startActivity(intent_cerca);
                        }else {
                            Toast.makeText(getActivity(),"Inserisci una città valida",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Intent intent_cerca = new Intent(getActivity(), RicercaActivity.class);
                        intent_cerca.putExtra("citta", ricerca);
                        startActivity(intent_cerca);
                    }
                }else{
                    Toast.makeText(getActivity(),"Inserisci una città valida",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getActivity(),"Inserisci una città valida",Toast.LENGTH_LONG).show();
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
