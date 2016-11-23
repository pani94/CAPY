package com.example.ale.myapplicatio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;


public class GestioneViaggioGalleriaFragmentGrid extends Fragment {


    private GridView gridView;
    private GridViewAdapter gridAdapter;


    public GestioneViaggioGalleriaFragmentGrid() {
        // Required empty public constructor
    }

   /*
    public static GestioneViaggioGalleriaFragmentGrid newInstance(String param1, String param2) {
        GestioneViaggioGalleriaFragmentGrid fragment = new GestioneViaggioGalleriaFragmentGrid();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList <ImageItem> immagini = new ArrayList<>();
        Bitmap bitmap = getArguments().getParcelable("image");
        immagini.add(new ImageItem(bitmap,"image 1"));
        View view = inflater.inflate(R.layout.fragment_gestione_viaggio_galleria_fragment_grid, container, false);
        gridView = (GridView) view.findViewById(R.id.gridViewGalleria);
        gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, immagini);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

            }
        });
        return view;
    }

}
