package com.example.ale.myapplicatio;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FragmentITuoiViaggi extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ArrayList <Viaggio> arrayList;
    private ListView itemListView;
    private ImageButton bottone_aggiungi;
    private TextView testo_aggiungi;

    public FragmentITuoiViaggi() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ituoi_viaggi, container, false);
        bottone_aggiungi = (ImageButton) view.findViewById(R.id.fragment_ituoi_viaggi_bottoneaggiungi);
        testo_aggiungi = (TextView) view.findViewById(R.id.fragment_ituoi_viaggi_testoaggiungi);
        arrayList = new ArrayList<>();
        final DataBase db = new DataBase(getActivity());
        arrayList = db.getViaggi();

        if(arrayList.isEmpty()){
            testo_aggiungi.setText("Crea un nuovo viaggio");
            bottone_aggiungi.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);
            bottone_aggiungi.setVisibility(View.VISIBLE);
        }else{
            testo_aggiungi.setText("");
            bottone_aggiungi.setVisibility(View.INVISIBLE);
        }
        itemListView = (ListView) view.findViewById(R.id.lista_viaggi);
        final ItemAdapterViaggio adapter = new ItemAdapterViaggio(getActivity(), arrayList);
        adapter.notifyDataSetChanged();
        bottone_aggiungi.setOnClickListener(this);
        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener(this);
        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Elimina/Modifica")
                        .setMessage("Vuoi eliminare o modificare il viaggio " + arrayList.get(pos).getNome_viaggio() + " ?")
                        .setPositiveButton("Modifica", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String nomeViaggio = arrayList.get(pos).getNome_viaggio();
                                int id_viaggio = db.getIdViaggio(arrayList.get(pos).getNome_viaggio());
                                Intent intent = new Intent(getActivity(), CreaIlTuoViaggioActivity.class);
                                intent.putExtra("id_viaggio", id_viaggio);
                                intent.putExtra("nome_viaggio", nomeViaggio);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Elimina", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                long delete =db.deleteViaggio(arrayList.get(pos).getId_viaggio());
                                String stampa = arrayList.get(pos).getNome_viaggio() +" è stato eliminato dai tuoi viaggi";
                                if (delete > 0){
                                    MyCalendar myCalendar = new MyCalendar(getActivity());
                                    myCalendar.deleteEvent(arrayList.get(pos).getEvento_id(),getContext());
                                    Log.e("eliminato", Long.toString(arrayList.get(pos).getEvento_id()));
                                    Toast.makeText(getContext(),stampa,Toast.LENGTH_SHORT).show();
                                    arrayList.remove(pos);
                                    adapter.notifyDataSetChanged();
                                    if(arrayList.isEmpty()){
                                        testo_aggiungi.setText("Crea un nuovo viaggio");
                                        bottone_aggiungi.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);
                                        bottone_aggiungi.setVisibility(View.VISIBLE);
                                    }
                                }

                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GestioneViaggioFragment newFragment = new GestioneViaggioFragment();
                Bundle args = new Bundle();
                args.putString("nome_viaggio", arrayList.get(position).getNome_viaggio());
                args.putString("daquando", arrayList.get(position).getPartenza());
                args.putString("aquando",arrayList.get(position).getArrivo());
                newFragment.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container_profilo, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
        }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CreaIlTuoViaggioActivity.class);
        startActivity(intent);
    }
}



