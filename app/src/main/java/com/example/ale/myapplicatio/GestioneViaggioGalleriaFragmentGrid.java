package com.example.ale.myapplicatio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class GestioneViaggioGalleriaFragmentGrid extends Fragment implements AdapterView.OnItemLongClickListener {


    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private DataBase db ;
    String nome_viaggio;
    ArrayList <Foto> immagini;

    public GestioneViaggioGalleriaFragmentGrid() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nome_viaggio = getArguments().getString("nome_viaggio");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestione_viaggio_galleria_fragment_grid, container, false);
        immagini = new ArrayList<>();
        db = new DataBase(getContext());
        immagini = db.getFoto(nome_viaggio);
        if(immagini.size() > 0){
            gridView = (GridView) view.findViewById(R.id.gridViewGalleria);
            gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, immagini);
            gridView.setAdapter(gridAdapter);
            gridView.setOnItemLongClickListener(this);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent i = new Intent(getActivity(), FullScreenViewActivity.class);
                    i.putExtra("position", position);
                    i.putExtra("nome_viaggio",nome_viaggio);
                    getActivity().startActivity(i);


                }
            });

        }

        return view;
    }


    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Elimina foto")
                .setMessage("Sei sicuro di volere eliminare questa foto dalla galleria ?")
                .setPositiveButton("si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        long delete = db.deleteFoto(immagini.get(position).getPath(),nome_viaggio);
                        if(delete > 0){
                            String stampa =  "Foto eliminata con successo";
                            Toast.makeText(getContext(), stampa,
                                    Toast.LENGTH_SHORT).show();
                            immagini.remove(position);
                            gridAdapter.notifyDataSetChanged();

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
}

