package com.example.ale.myapplicatio;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

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
        listSliding.add(new ItemSlideMenu(R.drawable.ic_home, "Home"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_account_circle_black_24dp, "Profilo"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_business_center_black_24dp, "Crea un nuovo viaggio"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_settings_black_24dp, "Impostazioni"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_info_black_24dp, "About"));

        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

        //Display icon to open/close sliding list
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set Title
        //setTitle(listSliding.get(0).getTitle());
        //item selected
        listViewSliding.setItemChecked(0, true);
        //close menu
        drawerLayout.closeDrawer(listViewSliding);

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //set title
                //setTitle(listSliding.get(position).getTitle());
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
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_gestione_viaggio_agenda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    //create method replace fragment
    private void replaceFragment(int pos){
        Fragment fragment = null;
        switch (pos){
            case 0:
                Intent intent = new Intent(GestioneViaggioAgendaActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent2 = new Intent(GestioneViaggioAgendaActivity.this, ProfiloViaggiActivity.class);
                startActivity(intent2);
                break;
            case 2:
                Intent intent3 = new Intent(GestioneViaggioAgendaActivity.this, CreaIlTuoViaggioActivity.class);
                startActivity(intent3);
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


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class AgendaFragment extends Fragment {
        private ExpandableListView expandableListView;

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
            DataBase db = new DataBase(getContext());
            String data = getArguments().getString("data");
            int id_viaggio = db.getIdViaggio(NomeViaggio);
            List <String> headings = new ArrayList<String>();
            ArrayList<ArrayList <AttivitaGiorno>> arrayListParent = new ArrayList<>();
            ArrayList <AttivitaGiorno> arrayListChildMattina = db.getAttivitaGiorno(data,id_viaggio,"Mattina");
            ArrayList <AttivitaGiorno> arrayListChildPranzo = db.getAttivitaGiorno(data,id_viaggio,"Pranzo");;
            ArrayList <AttivitaGiorno> arrayListChildPomeriggio= db.getAttivitaGiorno(data,id_viaggio,"Pomeriggio");;
            ArrayList <AttivitaGiorno> arrayListChildCena=db.getAttivitaGiorno(data,id_viaggio,"Cena");;
            ArrayList <AttivitaGiorno> arrayListChildSera = db.getAttivitaGiorno(data,id_viaggio,"Sera");;
            arrayListParent.add(0,arrayListChildMattina);
            arrayListParent.add(1,arrayListChildPranzo);
            arrayListParent.add(2,arrayListChildPomeriggio);
            arrayListParent.add(3,arrayListChildCena);
            arrayListParent.add(4,arrayListChildSera);
            String headings_item[] = getResources().getStringArray(R.array.header_titles);
            for (String title : headings_item){
                headings.add(title);
            }
            ExpandableListViewAttivitaGiornoAdapter adapter = new ExpandableListViewAttivitaGiornoAdapter(getContext(),headings,arrayListParent);
            expandableListView.setAdapter(adapter);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
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
            // Show 3 total pages.
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
