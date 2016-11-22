package com.example.ale.myapplicatio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class GestioneViaggioAttivitaFragmentListItem extends Fragment {
    private TextView attivita_listitem_titolo;
    private ImageView attivita_listitem_foto;
    private TextView attivita_listitem_orario;
    private TextView attivita_listitem_link;
    private TextView attivita_listitem_telefono;
    private TextView attivita_listitem_indirizzo;
    private Button attivita_listitem_button;

    private String titolo_get;
    private String orario_get;
    private String foto_get;
    private String link_get;
    private String telefono_get;
    private String indirizzo_get;
    private String placeid_get;

    private DataBase database;
    private ArrayList<Giorno> giorni;

    public GestioneViaggioAttivitaFragmentListItem() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            titolo_get = getArguments().getString("titolo");
            orario_get = getArguments().getString("orario");
            foto_get = getArguments().getString("foto");
            link_get = getArguments().getString("link");
            telefono_get = getArguments().getString("telefono");
            indirizzo_get = getArguments().getString("indirizzo");
            placeid_get = getArguments().getString("placeid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("ciao", "ciao");
        View view = inflater.inflate(R.layout.fragment_gestione_viaggio_attivita_fragment_list_item, container,false);
        attivita_listitem_titolo = (TextView) view.findViewById(R.id.fragment_gestione_viaggio_attivita_fragment_list_item_titolo);
        attivita_listitem_foto = (ImageView) view.findViewById(R.id.fragment_gestione_viaggio_attivita_fragment_list_item_foto);
        attivita_listitem_orario = (TextView) view.findViewById(R.id.fragment_gestione_viaggio_attivita_fragment_list_item_orario);
        attivita_listitem_link = (TextView) view.findViewById(R.id.fragment_gestione_viaggio_attivita_fragment_list_item_link);
        attivita_listitem_telefono = (TextView) view.findViewById(R.id.fragment_gestione_viaggio_attivita_fragment_list_item_telefono);
        attivita_listitem_indirizzo = (TextView) view.findViewById(R.id.fragment_gestione_viaggio_attivita_fragment_list_item_indirizzo);
        attivita_listitem_button = (Button) view.findViewById(R.id.fragment_gestione_viaggio_attivita_fragment_list_item_button);
        attivita_listitem_titolo.setText(titolo_get);
        attivita_listitem_orario.setText(orario_get);
        attivita_listitem_link.setText(link_get);
        attivita_listitem_telefono.setText(telefono_get);
        attivita_listitem_indirizzo.setText(indirizzo_get);

        //ButtonListener buttonListener = new ButtonListener();
        //database = new DataBase(getActivity());
        //id_viaggio = database.getIdViaggio()
        //giorni = database.getGiorni();
        // Inflate the layout for this fragment
        return view;
    }

}
