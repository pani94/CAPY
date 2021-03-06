package com.example.ale.myapplicatio;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentITuoiPreferiti extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<Attivita> arrayList;
    private ListView itemListView;
    private TextView titolo_ituoipreferiti;
    private TextView listavuota;

    public FragmentITuoiPreferiti() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ituoi_preferiti, container, false);
        titolo_ituoipreferiti = (TextView) view.findViewById(R.id.fragment_ituoi_preferiti_titolo);
        listavuota = (TextView) view.findViewById(R.id.fragment_ituoi_preferiti_textview);
        arrayList = new ArrayList<Attivita>();
        final DataBase db = new DataBase(getActivity());
        arrayList = db.getAttivitaPreferite();
        if(arrayList.isEmpty()){
            listavuota.setText("Non hai ancora aggiunto preferiti");
        }else{
            listavuota.setText("");
        }
        itemListView = (ListView) view.findViewById(R.id.fragment_ituoi_preferiti_list);
        final ItemAdapterAttivita adapter = new ItemAdapterAttivita(getActivity(), arrayList,"","preferiti");
        adapter.notifyDataSetChanged();
        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener(this);
        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Elimina preferito")
                        .setMessage("Sei sicuro di volere eliminare dai tuoi preferiti " + arrayList.get(pos).getNome() + " ?")
                        .setPositiveButton("si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                long delete = db.deletePreferiti(arrayList.get(pos).getPlace_id());
                                if(delete > 0){
                                    String stampa = arrayList.get(pos).getNome() + " è stato eliminato dai tuoi preferiti";
                                    Toast.makeText(getContext(), stampa,
                                            Toast.LENGTH_SHORT).show();
                                    arrayList.remove(pos);
                                    adapter.notifyDataSetChanged();
                                    if(arrayList.isEmpty()){
                                        listavuota.setText("Non hai preferiti");
                                    }
                                }


                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
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
        ITuoiPreferitiFragmentListItem newFragment = new ITuoiPreferitiFragmentListItem();
        Bundle args = new Bundle();
        args.putString("preferiti_placeid", arrayList.get(position).getPlace_id());
        args.putString("preferiti_titolo", arrayList.get(position).getNome());
        args.putString("preferiti_foto", arrayList.get(position).getFoto());
        args.putString("preferiti_orario", arrayList.get(position).getOrario());
        args.putString("preferiti_link", arrayList.get(position).getLink());
        args.putString("preferiti_telefono", arrayList.get(position).getTelefono());
        args.putString("preferiti_indirizzo", arrayList.get(position).getIndirizzo());
        args.putString("preferiti_latitudine", arrayList.get(position).getLatitudine());
        args.putString("preferiti_longitudine", arrayList.get(position).getLongitudine());
        newFragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container_profilo, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
