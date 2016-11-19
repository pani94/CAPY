package com.example.ale.myapplicatio;

import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GestioneViaggioAttivitaActivity extends AppCompatActivity {
    private static String NomeViaggio;
    float historicX = Float.NaN, historicY = Float.NaN;
    static final int DELTA = 50;
    enum Direction {LEFT, RIGHT;}

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private TextView attivita_nomeviaggio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_viaggio_attivita);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NomeViaggio = getIntent().getStringExtra("attivita_nomeviaggio");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_attivita);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //attivita_nomeviaggio = (TextView) findViewById(R.id.activity_gestione_viaggio_attivita_nomeviaggio);

        //attivita_nomeviaggio.setT


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gestione_viaggio_attivita, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return GestioneViaggioAttivitaTabTutte.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "TUTTE";
                case 1:
                    return "DA VEDERE";
                case 2:
                    return "DOVE MANGIARE";
            }
            return null;
        }

        /**
         * Created by Paola on 18/11/2016.
         */

    }
    public static class GestioneViaggioAttivitaTabTutte extends Fragment implements AdapterView.OnItemClickListener {

        private ArrayList<Attivita> attivitas;
        private ListView itemListView;
        private String nomeViaggio;

        public GestioneViaggioAttivitaTabTutte() {

        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                nomeViaggio = getArguments().getString("attivita_nomeviaggio");

            }


        }

        public static GestioneViaggioAttivitaTabTutte newInstance(int sectionNumber) {
            GestioneViaggioAttivitaTabTutte fragment = new GestioneViaggioAttivitaTabTutte();
            Bundle args = new Bundle();
            switch (sectionNumber){
                case 0 :
                        args.putString("tabselected", "tutte");
                        break;
                case 1 :
                        args.putString("tabselected", "vedere");
                        break;
                case 2 :
                        args.putString("tabselected", "mangiare");
                        break;
                default:
                        args.putString("tabselected", "tutte");
                        break;

            }

            fragment.setArguments(args);
            return fragment;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_gestione_viaggio_attivita, container, false);
            attivitas = new ArrayList<Attivita>();
            DataBase db = new DataBase(getActivity());
            attivitas = db.getAttivita(NomeViaggio, getArguments().getString("tabselected"));
            itemListView = (ListView) rootView.findViewById(R.id.fragment_gestione_viaggio_attivita_lista);
            ItemAdapterAttivita adapter = new ItemAdapterAttivita(getActivity(), attivitas);
            adapter.notifyDataSetChanged();
            itemListView.setAdapter(adapter);
            itemListView.setOnItemClickListener(this);
            itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")
                            .setPositiveButton("si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
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

            return rootView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }
}
