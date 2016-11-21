package com.example.ale.myapplicatio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;


public class GestioneViaggioFragment extends Fragment {

    private TextView nome_viaggio;
    private TextView daquando_aquando;
    private Button bottone_attivita;
    private Button bottone_agenda;

    private String nome_viaggio_get;
    private String daquando_get;
    private String aquando_get;
    private String daquando_aquando_get;
    private DataBase db ;
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
        nome_viaggio.setText(nome_viaggio_get);
        daquando_aquando.setText(daquando_aquando_get);
        bottone_attivita.setOnClickListener(buttonListener);
        bottone_agenda.setOnClickListener(buttonListener);

        return view;
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
                default:


            }

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
