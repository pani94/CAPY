package com.example.ale.myapplicatio;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GestioneViaggioAttivitaActivity extends AppCompatActivity {
    private static String NomeViaggio;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TextView attivita_nomeviaggio;
    //per SlideMenu
    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    // private RelativeLayout mainContent;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_viaggio_attivita);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_attivita);
        setSupportActionBar(toolbar);
        NomeViaggio = getIntent().getStringExtra("attivita_nomeviaggio");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_attivita);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_attivita);
        tabLayout.setupWithViewPager(mViewPager);

        //sliding menu
        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSliding = new ArrayList<>();

        //add item for sliding list
        listSliding.add(new ItemSlideMenu(R.drawable.ic_home_black_24dp, "Home"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_create_black_24dp, "Crea un nuovo viaggio"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_business_center_black_24dp, "I miei viaggi"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_star_black_24dp, "I miei preferiti"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_settings_black_24dp, "Impostazioni"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_info_black_24dp, "About"));

        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

        //Display icon to open/close sliding list
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //item selected
        listViewSliding.setItemChecked(0, true);
        //close menu
        drawerLayout.closeDrawer(listViewSliding);

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //item selected
                listViewSliding.setItemChecked(position, true);

                replaceFragment(position);
                //close menu
                drawerLayout.closeDrawer(listViewSliding);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (actionBarDrawerToggle!= null){
            actionBarDrawerToggle.syncState();
        }
    }

    //create method replace fragment
    private void replaceFragment(int pos){
        Fragment fragment = null;
        switch (pos){
            case 0:
                Intent intent = new Intent(GestioneViaggioAttivitaActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent3 = new Intent(GestioneViaggioAttivitaActivity.this, CreaIlTuoViaggioActivity.class);
                startActivity(intent3);
                break;
            case 2:
                Intent intent_viaggi = new Intent(GestioneViaggioAttivitaActivity.this, ProfiloViaggiActivity.class);
                intent_viaggi.putExtra("viaggio", "viaggio");
                startActivity(intent_viaggi);
                break;
            case 3:
                Intent intent_preferiti = new Intent(GestioneViaggioAttivitaActivity.this, ProfiloViaggiActivity.class);
                intent_preferiti.putExtra("preferiti", "preferiti");
                startActivity(intent_preferiti);
                break;
            case 4:
                Intent intent_impostazioni = new Intent(GestioneViaggioAttivitaActivity.this, SettingsActivity.class);
                startActivity(intent_impostazioni);
                break;
            case 5: new AlertDialog.Builder(GestioneViaggioAttivitaActivity.this)
                    .setTitle("Let's go")
                    .setMessage("Questa applicazione è stata creata da: " +
                            "Alessandro Barlocco, Annalisa Bovone, Paola Silvestre")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(R.drawable.logo_pani_piccolo)
                    .show();
                break;
            default:
                break;
        }

        if (fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.activity_main, fragment);
            transaction.commit();


        }
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
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "DA VEDERE";
                case 1:
                    return "DOVE MANGIARE";
                case 2:
                    return "DOVE DORMIRE";

            }
            return null;
        }
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
                    args.putString("tabselected", "vedere");
                    break;
                case 1 :
                    args.putString("tabselected", "mangiare");
                    break;
                case 2:
                    args.putString("tabselected", "dormire");
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
            attivitas = new ArrayList<>();
            final DataBase db = new DataBase(getActivity());
            attivitas = db.getAttivita(NomeViaggio, getArguments().getString("tabselected"));

            itemListView = (ListView) rootView.findViewById(R.id.fragment_gestione_viaggio_attivita_lista);
            final ItemAdapterAttivita adapter = new ItemAdapterAttivita(getActivity(), attivitas, NomeViaggio,"attivita");
            adapter.notifyDataSetChanged();
            itemListView.setAdapter(adapter);
            itemListView.setOnItemClickListener(this);
            itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               final int pos, long id) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Elimina attività")
                            .setMessage("Sei sicuro di volere eliminare " + attivitas.get(pos).getNome() + " da " + NomeViaggio + "?")
                            .setPositiveButton("si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int delete = db.deleteViaggioAttivita(db.getIdViaggio(NomeViaggio), attivitas.get(pos).getPlace_id());
                                    int delete2 = db.deleteAttivitaGiorno(attivitas.get(pos).getPlace_id(), db.getIdViaggio(NomeViaggio));
                                    if(delete > 0){
                                        String stampa =  attivitas.get(pos).getNome() + "è stato eliminato dalle attività";
                                        Toast.makeText(getContext(),stampa,Toast.LENGTH_SHORT).show();
                                        attivitas.remove(pos);
                                        adapter.notifyDataSetChanged();

                                    }

                                }
                            })
                            .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

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
            Intent intent = new Intent(getActivity(), GestioneViaggioAttivitaListItemActivity.class);
            intent.putExtra("nomeViaggio", NomeViaggio);
            intent.putExtra("placeid", attivitas.get(position).getPlace_id());
            intent.putExtra("titolo", attivitas.get(position).getNome());
            intent.putExtra("foto", attivitas.get(position).getFoto());
            intent.putExtra("orario", attivitas.get(position).getOrario());
            intent.putExtra("link", attivitas.get(position).getLink());
            intent.putExtra("telefono", attivitas.get(position).getTelefono());
            intent.putExtra("indirizzo", attivitas.get(position).getIndirizzo());
            intent.putExtra("tipologia", getArguments().getString("tabselected"));
            intent.putExtra("provenienza", "attivita");
            startActivity(intent);
        }
    }
}