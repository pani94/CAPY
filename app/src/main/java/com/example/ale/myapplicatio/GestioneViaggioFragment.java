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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class GestioneViaggioFragment extends Fragment {

    private TextView nome_viaggio;
    private TextView daquando_aquando;
    private Button bottone_attivita;
    private Button bottone_agenda;
    private Button bottone_galleria;
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
        bottone_attivita = (Button) view.findViewById(R.id.gestione_viaggio_bottone_attivita);
        bottone_agenda = (Button) view.findViewById(R.id.gestione_viaggio_bottone_agenda);
        bottone_galleria = (Button) view.findViewById(R.id.gestione_viaggio_bottone_galleria);
        autoCompleteCerca = (AutoCompleteTextView) view.findViewById(R.id.gestione_viaggio_autocomplete);
        bottone_cerca = (Button) view.findViewById(R.id.gestione_viaggio_bottone_cerca);
        nome_viaggio.setText(nome_viaggio_get);
        daquando_aquando.setText(daquando_aquando_get);
        bottone_attivita.setOnClickListener(buttonListener);
        bottone_agenda.setOnClickListener(buttonListener);
        bottone_galleria.setOnClickListener(buttonListener);
        bottone_cerca.setOnClickListener(buttonListener);

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
                    Log.e("messaggini", "a!=null");
                    if(!a.isEmpty()){
                        Log.e("messaggini", "a!=empty");
                        for(int i=0; i<a.getCount(); i++){
                            //stringa = a.getItem(0).toString();
                            Log.e("messaggini", a.getItem(i).toString());
                            if(s.equals(a.getItem(i).toString())){
                                Log.e("messaggini", "isValid return true");
                                return true;
                            }
                        }
                    }else{
                        Log.e("messaggini", "a vuoto");
                        if(check){
                            Log.e("messaggini", "check true && isValid return true");
                            return true;
                        }
                    }
                }
                Log.e("messaggini", "isValid return false");
                return false;
            }

            @Override
            public String fixText(CharSequence charSequence) {
                String s = charSequence.toString();
                Adapter a = autoCompleteCerca.getAdapter();
                if (a != null) {
                    Log.e("messaggini", "fix text: a!=null");
                    if (!a.isEmpty()) {
                        Log.e("messaggini", "fix text: a!=empty");
                        Log.e("messaggini", a.getItem(0).toString());
                        return a.getItem(0).toString();
                    }else{
                        //Log.e("messaggini", stringa);
                        //return stringa;
                    }
                } else {
                    if (check) {
                        Log.e("messaggini", "check true && fix text return s: " + s);
                        return s;
                    }
                }
                Log.e("messaggini", "non puoi andare avanti");
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

    private class GetCity extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Toast.makeText(MainActivity.this, "Json Data is downloading " , Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();
            String url1 = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=";
            String url2 ="&types=(cities)&language=it&key=AIzaSyAD1xAMtZ0YaMSii5iDkTJrFv0jz9cEz2U";
            //String url2 ="&types=(cities)&key=AIzaSyBieTKI8Lmg7TuF2MgUUtK93bjpWylxLBM";
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
            switch (v.getId()){
                case R.id.gestione_viaggio_bottone_attivita:
                    if(db.getViaggiAttivitaBool(db.getIdViaggio(nome_viaggio_get))){
                        Intent intent = new Intent(getActivity(), GestioneViaggioAttivitaActivity.class);
                        intent.putExtra("attivita_nomeviaggio",nome_viaggio_get);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getActivity(),"Non hai ancora inserito attivita per questo viaggio",Toast.LENGTH_SHORT).show();
                    }

                    //Log.e("gestioneViaggioFrament", nome_viaggio_get);
                    break;
                case R.id.gestione_viaggio_bottone_agenda:
                    Intent intent = new Intent(getActivity(), GestioneViaggioAgendaActivity.class);
                    intent.putExtra("numgiorni",db.getNumeroDiGiorni(db.getIdViaggio(nome_viaggio_get)));
                    intent.putExtra("attivita_nomeviaggio",nome_viaggio_get);
                    startActivity(intent);
                    break;
                case R.id.gestione_viaggio_bottone_galleria :
                    Intent intentG = new Intent(getActivity(), GestioneViaggioGalleriaActivity.class);
                    startActivity(intentG);
                    break;
                case R.id.gestione_viaggio_bottone_cerca:
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
                    break;
                default:


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
