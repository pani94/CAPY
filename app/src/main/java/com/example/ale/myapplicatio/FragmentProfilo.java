package com.example.ale.myapplicatio;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentProfilo extends Fragment implements AdapterView.OnItemClickListener {

    private String[] scelte;
    private ListView profilo_listView;
    private TextView profilo_scelta;

    //private OnFragmentInteractionListener mListener;

    public FragmentProfilo() {

    }


    /*public static FragmentProfilo newInstance(String param1, String param2) {
        FragmentProfilo fragment = new FragmentProfilo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profilo, container, false);
        scelte = new String[2];
        scelte[0] = "I tuoi viaggi";
        scelte[1] = "I tuoi Preferiti";
        profilo_scelta = (TextView) view.findViewById(R.id.profilo_scelta);
        profilo_listView = (ListView) view.findViewById(R.id.profilo_listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, scelte);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(RicercaActivityFragmentMenu.this, R.layout.fragment_ricerca_activity_menu_item, scelte);
        adapter.notifyDataSetChanged();
        profilo_listView.setAdapter(adapter);
        profilo_listView.setOnItemClickListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    /*@Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        /*FragmentITuoiViaggi viaggiFragment = new FragmentITuoiViaggi();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, viaggiFragment);
        transaction.addToBackStack(null);
        transaction.commit();*/

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
