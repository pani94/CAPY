package com.example.ale.myapplicatio;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;


public class FragmentITuoiViaggi extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList <Viaggio> arrayList;
    private ListView itemListView;
    public FragmentITuoiViaggi() {
        // Required empty public constructor
    }

    /*
    public static FragmentITuoiViaggi newInstance(String param1, String param2) {
        FragmentITuoiViaggi fragment = new FragmentITuoiViaggi();
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_ituoi_viaggi, container, false);
        arrayList = new ArrayList<Viaggio>();
        final DataBase db = new DataBase(getActivity());
        arrayList = db.getViaggi();
        itemListView = (ListView) view.findViewById(R.id.lista_viaggi);
        final ItemAdapterViaggio adapter = new ItemAdapterViaggio(getActivity(), arrayList);
        adapter.notifyDataSetChanged();
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
                                    Toast.makeText(getContext(),stampa,Toast.LENGTH_SHORT);
                                    arrayList.remove(pos);
                                    adapter.notifyDataSetChanged();
                                }

                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
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


    }

   /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */

