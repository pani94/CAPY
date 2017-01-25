package com.example.ale.myapplicatio;


import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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


public class RicercaActivityFragmentMenu extends Fragment implements AdapterView.OnItemClickListener {

    private String[] scelte;
    private ListView listaFragment;
    private TextView scelta_menu;
    private String selectedCity;
    private String selectedItem;
    private TextView nomeCitta;


    public RicercaActivityFragmentMenu() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedCity = getArguments().getString("citta");
        }

    }

    //@Override
    //public void onResume() {
      //  super.onResume();
        // Set title
        /*AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle("Let's go");*/
        //((RicercaActivity) getActivity()).setActionBarTitle("Let's go!");
    //}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ricerca_activity_fragment_menu, container, false);
        scelte = new String[3];
        scelte[0] = "Cosa vedere";
        scelte[1] = "Dove mangiare";
        scelte[2] = "Dove dormire";

        scelta_menu = (TextView) view.findViewById(R.id.scelta_menu);
        listaFragment = (ListView) view.findViewById(R.id.listaFragment);
        listaFragment.setOnItemClickListener(this);
        nomeCitta = (TextView) view.findViewById(R.id.fragment_ricerca_activity_menu_selectedCity);
        nomeCitta.setText(selectedCity);

        ItemAdapterVedereMangiareDormire adapter = new ItemAdapterVedereMangiareDormire(getActivity(), scelte,this);
        adapter.notifyDataSetChanged();
        listaFragment.setAdapter(adapter);
        // Inflate the layout for this fragment
        return view;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            RicercaActivityFragmentList newFragment = new RicercaActivityFragmentList();
            Bundle args = new Bundle();
            args.putString("selectedCity",selectedCity);
            if(position == 0){
                selectedItem = "vedere";
            }else if(position == 1){
                selectedItem = "mangiare";
            }else if(position == 2){
                selectedItem = "dormire";
            }
            args.putString("selectedItem",selectedItem);
            newFragment.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(this);
            transaction.add(R.id.fragment_container, newFragment);
            transaction.show(newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }else{
            new AlertDialog.Builder(getContext())
                    .setTitle("Attenzione")
                    .setMessage("Non hai connessione.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
            }



}

