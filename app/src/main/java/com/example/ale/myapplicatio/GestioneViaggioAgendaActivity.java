package com.example.ale.myapplicatio;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GestioneViaggioAgendaActivity extends AppCompatActivity{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static String NomeViaggio;



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
        setContentView(R.layout.activity_gestione_viaggio_agenda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_agenda);
        setSupportActionBar(toolbar);
        NomeViaggio = getIntent().getStringExtra("attivita_nomeviaggio");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_agenda);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_agenda);
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

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (actionBarDrawerToggle != null) {
            actionBarDrawerToggle.syncState();
        } else {
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
            actionBarDrawerToggle.syncState();
        }
    }

    private void replaceFragment(int pos){
        Fragment fragment = null;
        switch (pos){
            case 0:
                Intent intent = new Intent(GestioneViaggioAgendaActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent3 = new Intent(GestioneViaggioAgendaActivity.this, CreaIlTuoViaggioActivity.class);
                startActivity(intent3);
                break;
            case 2:
                Intent intent_viaggi = new Intent(GestioneViaggioAgendaActivity.this, ProfiloViaggiActivity.class);
                intent_viaggi.putExtra("viaggio", "viaggio");
                startActivity(intent_viaggi);
                break;
            case 3:
                Intent intent_preferiti = new Intent(GestioneViaggioAgendaActivity.this, ProfiloViaggiActivity.class);
                intent_preferiti.putExtra("preferiti", "preferiti");
                startActivity(intent_preferiti);
                break;
            case 4:
                Intent intent_impostazioni = new Intent(GestioneViaggioAgendaActivity.this, SettingsActivity.class);
                startActivity(intent_impostazioni);
                break;
            case 5: new AlertDialog.Builder(GestioneViaggioAgendaActivity.this)
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
    public static class AgendaFragment extends Fragment implements ExpandableListView.OnChildClickListener,AdapterView.OnItemLongClickListener {
        private ExpandableListView expandableListView;
        ExpandableListViewAttivitaGiornoAdapter adapter=null;
        ArrayList<ArrayList <AttivitaGiorno>> arrayListParent = new ArrayList<>();
        public AgendaFragment() {
        }



        public static AgendaFragment newInstance(int sectionNumber,String data) {
            AgendaFragment fragment = new AgendaFragment();
            Bundle args = new Bundle();
            args.putString("data", data);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_gestione_viaggio_agenda, container, false);
            expandableListView = (ExpandableListView) rootView.findViewById(R.id.exp_listview);
            String data = getArguments().getString("data");
            DataBase db = new DataBase(getContext());
            int id_viaggio = db.getIdViaggio(NomeViaggio);
            List <String> headings = new ArrayList<>();
            ArrayList <AttivitaGiorno> arrayListChildMattina = db.getAttivitaGiorno(data,id_viaggio,"Mattina");
            ArrayList <AttivitaGiorno> arrayListChildPranzo = db.getAttivitaGiorno(data,id_viaggio,"Pranzo");;
            ArrayList <AttivitaGiorno> arrayListChildPomeriggio= db.getAttivitaGiorno(data,id_viaggio,"Pomeriggio");;
            ArrayList <AttivitaGiorno> arrayListChildCena=db.getAttivitaGiorno(data,id_viaggio,"Cena");;
            ArrayList <AttivitaGiorno> arrayListChildSera = db.getAttivitaGiorno(data,id_viaggio,"Sera");
            ArrayList <AttivitaGiorno> arrayListChildNotte = db.getAttivitaGiorno(data,id_viaggio,"Dormire");
            arrayListParent.add(0,arrayListChildMattina);
            arrayListParent.add(1,arrayListChildPranzo);
            arrayListParent.add(2,arrayListChildPomeriggio);
            arrayListParent.add(3,arrayListChildCena);
            arrayListParent.add(4,arrayListChildSera);
            arrayListParent.add(5,arrayListChildNotte);
            String headings_item[] = getResources().getStringArray(R.array.header_titles);
            for (String title : headings_item){
                headings.add(title);
            }
            adapter = new ExpandableListViewAttivitaGiornoAdapter(getContext(),headings,arrayListParent,id_viaggio,data);
            expandableListView.setAdapter(adapter);
            expandableListView.setOnChildClickListener(this);
            expandableListView.setOnItemLongClickListener(this);
            return rootView;
        }

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            DataBase db = new DataBase(getContext());
            Attivita attivita =db.getAttivita(adapter.getChild(groupPosition,childPosition).getPlace_id());
            Intent intent = new Intent(getActivity(), GestioneViaggioAttivitaListItemActivity.class);
            intent.putExtra("nomeViaggio", NomeViaggio);
            intent.putExtra("placeid", attivita.getPlace_id());
            intent.putExtra("titolo", attivita.getNome());
            intent.putExtra("foto", attivita.getFoto());
            intent.putExtra("orario", attivita.getOrario());
            intent.putExtra("link", attivita.getLink());
            intent.putExtra("telefono", attivita.getTelefono());
            intent.putExtra("indirizzo", attivita.getIndirizzo());
            intent.putExtra("tipologia", attivita.getTipologia());
            intent.putExtra("provenienza", "agenda");
            startActivity(intent);
            return true;
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final DataBase db = new DataBase(getContext());
            if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                final int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                final int childPosition = ExpandableListView.getPackedPositionChild(id);
                new AlertDialog.Builder(getActivity())
                        .setTitle("Elimina attività")
                        .setMessage("Sei sicuro di volere eliminare " + db.getAttivita(adapter.getChild(groupPosition,childPosition).getPlace_id()).getNome() + " da " + NomeViaggio + "?")
                        .setPositiveButton("si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String place_id = adapter.getChild(groupPosition,childPosition).getPlace_id();
                                String data = adapter.getChild(groupPosition,childPosition).getData();
                                long idViaggio =  adapter.getChild(groupPosition,childPosition).getId_viaggio();
                                String quando = adapter.getChild(groupPosition,childPosition).getQuando();
                                int rowCount =   db.deleteAttivitaGiorno(place_id,data,idViaggio,quando);
                                if(rowCount > 0){
                                    String stampa = db.getAttivita(place_id).getNome() + " è stato eliminato";
                                    Toast.makeText(getContext(), stampa ,
                                            Toast.LENGTH_SHORT).show();
                                    arrayListParent.get(groupPosition).remove(childPosition);
                                    adapter.notifyDataSetChanged();
                                    expandableListView.setAdapter(adapter);

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

            return false;
        }





    }


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            DataBase db = new DataBase(getApplicationContext());
            ArrayList<ViaggioGiorno> giorni = db.getGiorni(db.getIdViaggio(NomeViaggio));
            return AgendaFragment.newInstance(position ,giorni.get(position).getData());
        }

        @Override
        public int getCount() {
                return getIntent().getIntExtra("numgiorni",1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            DataBase db = new DataBase(getApplicationContext());
            ArrayList<ViaggioGiorno> giorni = db.getGiorni(db.getIdViaggio(NomeViaggio));
            return giorni.get(position).getData();

        }
    }
}