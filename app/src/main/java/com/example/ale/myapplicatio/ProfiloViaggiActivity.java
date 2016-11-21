package com.example.ale.myapplicatio;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class ProfiloViaggiActivity extends AppCompatActivity {

    //per SlideMenu
    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private RelativeLayout mainContent;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_viaggi);
        if (findViewById(R.id.fragment_container_profilo) != null) {

            if (savedInstanceState != null) {
                return;
            }
            if(getIntent().hasExtra("viaggio_creato")){
                FragmentITuoiViaggi fragmentITuoiViaggi = new FragmentITuoiViaggi();
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_profilo,fragmentITuoiViaggi).commit();
            }
            else{
                FragmentProfilo profiloFragment = new FragmentProfilo();
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_profilo, profiloFragment).commit();
            }

        }

        //sliding menu
        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mainContent = (RelativeLayout) findViewById(R.id.main_content);
        listSliding = new ArrayList<>();

        //add item for sliding list
        listSliding.add(new ItemSlideMenu(R.drawable.ic_home, "Home"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_business_center_black_24dp, "Crea un nuovo viaggio"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_settings_black_24dp, "Impostazioni"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_about, "About"));

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        /*switch (item.getItemId()) {
            case R.id.menu_profilo:
                startActivity(new Intent(getApplicationContext(), ProfiloViaggiActivity.class));
            case R.id.menu_settings:
             //   startActivity(new Intent(getApplicationContext(), RicercaActivity.class));
                return true;
            case R.id.menu_about:
               // startActivity(new Intent(getApplicationContext(), RicercaActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    //create method replace fragment
    private void replaceFragment(int pos){
        Fragment fragment = null;
        switch (pos){
            case 0:
                Intent intent = new Intent(ProfiloViaggiActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent2 = new Intent(ProfiloViaggiActivity.this, CreaIlTuoViaggioActivity.class);
                startActivity(intent2);
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
}
