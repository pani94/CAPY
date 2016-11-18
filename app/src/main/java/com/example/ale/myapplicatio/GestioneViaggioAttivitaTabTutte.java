package com.example.ale.myapplicatio;

/**
 * Created by Paola on 18/11/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class GestioneViaggioAttivitaTabTutte extends Fragment implements AdapterView.OnItemClickListener{

    private ArrayList<Attivita> attivitas;
    private ListView itemListView;
    private String nomeViaggio;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nomeViaggio = getArguments().getString("attivita_nomeviaggio");

        }


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gestione_viaggio_attivita, container, false);
        attivitas = new ArrayList<Attivita>();
        DataBase db = new DataBase(getActivity());
        attivitas = db.getAttivita(nomeViaggio);
        itemListView = (ListView) rootView.findViewById(R.id.fragment_gestione_viaggio_attivita_lista);
        ItemAdapterAttivita adapter = new ItemAdapterAttivita(getActivity(),attivitas);
        adapter.notifyDataSetChanged();
        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}