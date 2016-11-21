package com.example.ale.myapplicatio;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class RicercaActivityFragmentMenu extends Fragment implements AdapterView.OnItemClickListener {

    private String[] scelte;
    private ListView listaFragment;
    private TextView scelta_menu;
    private String selectedCity;
    private String selectedItem;



    public RicercaActivityFragmentMenu() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedCity = getArguments().getString("citta");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ricerca_activity_fragment_menu, container, false);
        scelte = new String[3];
        scelte[0] = "Cosa vedere?";
        scelte[1] = "Dove mangiare?";
        scelte[2] = "Dove dormire?";
        scelta_menu = (TextView) view.findViewById(R.id.scelta_menu);
        listaFragment = (ListView) view.findViewById(R.id.listaFragment);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, scelte);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(RicercaActivityFragmentMenu.this, R.layout.fragment_ricerca_activity_menu_item, scelte);
        adapter.notifyDataSetChanged();
        listaFragment.setAdapter(adapter);
        listaFragment.setOnItemClickListener(this);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }



   /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

   // @Override
    /*public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
   /* @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
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
    }*/
}

