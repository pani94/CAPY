package com.example.ale.myapplicatio;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


public class RicercaActivityFragmentMenu extends Fragment  {

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
        nomeCitta = (TextView) view.findViewById(R.id.fragment_ricerca_activity_menu_selectedCity);
        nomeCitta.setText(selectedCity);

        ItemAdapterVedereMangiareDormire adapter = new ItemAdapterVedereMangiareDormire(getActivity(), scelte,this);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(RicercaActivityFragmentMenu.this, R.layout.fragment_ricerca_activity_menu_item, scelte);
        adapter.notifyDataSetChanged();
        listaFragment.setAdapter(adapter);
        // Inflate the layout for this fragment
        return view;
    }

    public void fragmentStart(int i) {
        Log.e("messaggini", "click");
        RicercaActivityFragmentList newFragment = new RicercaActivityFragmentList();
        Bundle args = new Bundle();
        args.putString("selectedCity", selectedCity);
        if(scelte[i].equals("Cosa vedere?")){
            selectedItem = "vedere";
        }else if(scelte[i].equals("Dove mangiare?")){
            selectedItem = "mangiare";
        }else if(scelte[i].equals("Dove dormire?")){
            selectedItem = "dormire";
        }
        args.putString("selectedItem", selectedItem);
        newFragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("messaggini", "click");
        RicercaActivityFragmentList newFragment = new RicercaActivityFragmentList();
        Bundle args = new Bundle();
        args.putString("selectedCity",selectedCity);
        Log.e("messaggini", scelte[position]);
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
    }*/



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

