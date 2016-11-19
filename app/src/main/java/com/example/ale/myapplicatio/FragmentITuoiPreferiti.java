package com.example.ale.myapplicatio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FragmentITuoiPreferiti extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<Attivita> arrayList;
    private ListView itemListView;

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
        arrayList = new ArrayList<Attivita>();
        DataBase db = new DataBase(getActivity());
        arrayList = db.getAttivitaPreferite();
        itemListView = (ListView) view.findViewById(R.id.fragment_ituoi_preferiti_list);
        ItemAdapterAttivita adapter = new ItemAdapterAttivita(getActivity(), arrayList);
        adapter.notifyDataSetChanged();
        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
