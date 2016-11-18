package com.example.ale.myapplicatio;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class GestioneViaggioAttivitaFragmentListItem extends Fragment {
    private TextView attivita_listitem_titolo;
    private ImageView attivita_listitem_foto;
    private TextView attivita_listitem_orario;
    private TextView attivita_listitem_link;
    private TextView attivita_listitem_telefono;
    private TextView attivita_listitem_indirizzo;
    private TextView attivita_listitem_weekdaytext;

    public GestioneViaggioAttivitaFragmentListItem() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // mParam1 = getArguments().getString(ARG_PARAM1);
            // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestione_viaggio_attivita_fragment_list_item, container,false);
        attivita_listitem_titolo = (TextView) view.findViewById(R.id.fragment_gestione_viaggio_attivita_fragment_list_item_titolo);
        attivita_listitem_foto = (ImageView) view.findViewById(R.id.fragment_gestione_viaggio_attivita_fragment_list_item_foto);
        attivita_listitem_orario = (TextView) view.findViewById(R.id.fragment_gestione_viaggio_attivita_fragment_list_item_orario);
        attivita_listitem_link = (TextView) view.findViewById(R.id.fragment_gestione_viaggio_attivita_fragment_list_item_link);
        attivita_listitem_telefono = (TextView) view.findViewById(R.id.fragment_gestione_viaggio_attivita_fragment_list_item_telefono);
        attivita_listitem_indirizzo = (TextView) view.findViewById(R.id.fragment_gestione_viaggio_attivita_fragment_list_item_indirizzo);
        attivita_listitem_weekdaytext = (TextView) view.findViewById(R.id.fragment_gestione_viaggio_attivita_fragment_list_item_weekdaytext);


        // Inflate the layout for this fragment
        return view;
    }

}
