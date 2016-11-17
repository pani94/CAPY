package com.example.ale.myapplicatio;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class GestioneViaggioFragmentAttivita extends Fragment {

    private TextView nome_viaggio;
    private TextView daquando_aquando;
    private String nome_viaggio_get;
    private String daquando_get;
    private String aquando_get;
    private String daquando_aquando_get;

    public GestioneViaggioFragmentAttivita() {
        // Required empty public constructor
    }


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
        View view = inflater.inflate(R.layout.fragment_gestione_viaggio_fragment_attivita, container, false);
        nome_viaggio = (TextView) view.findViewById(R.id.gestione_viaggio_nome);
        daquando_aquando = (TextView) view.findViewById(R.id.gestione_viaggio_daquando_aquando);
        nome_viaggio.setText(nome_viaggio_get);
        daquando_aquando.setText(daquando_aquando_get);
        return view;
    }

}
