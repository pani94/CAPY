package com.example.ale.myapplicatio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestioneViaggioGalleriaActivity extends AppCompatActivity {
    private Button btnSelect;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private DataBase db;
    private String nome_viaggio;
    private ImageView icona;
    private TextView galleria;

    //per SlideMenu
    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    //private RelativeLayout mainContent;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nome_viaggio = getIntent().getStringExtra("attivita_nomeviaggio");
        setContentView(R.layout.activity_gestione_viaggio_galleria);
        btnSelect = (Button) findViewById(R.id.btnSelectPhoto);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        if (findViewById(R.id.fragmentGestioneViaggioGalleria) != null) {
            if (savedInstanceState != null) {
                return;
            }
            GestioneViaggioGalleriaFragmentGrid firstFragment = new GestioneViaggioGalleriaFragmentGrid();
            Bundle bundle = new Bundle();
            bundle.putString("nome_viaggio", nome_viaggio);
            firstFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentGestioneViaggioGalleria, firstFragment).commit();

        }
        //sliding menu
        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        icona = (ImageView) findViewById(R.id.gestione_viaggio_galleria_icona);
        galleria = (TextView) findViewById(R.id.gestione_viaggio_galleria_testo);
        // RelativeLayout mainContent = (RelativeLayout) findViewById(R.id.main_content);
        List<ItemSlideMenu> listSliding = new ArrayList<>();

        //add item for sliding list
        listSliding.add(new ItemSlideMenu(R.drawable.ic_home_black_24dp, "Home"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_create_black_24dp, "Crea un nuovo viaggio"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_business_center_black_24dp, "I miei viaggi"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_star_black_24dp, "I miei preferiti"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_settings_black_24dp, "Impostazioni"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_info_black_24dp, "About"));

        SlidingMenuAdapter adapter = new SlidingMenuAdapter(this, listSliding);
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
                //item selected
                listViewSliding.setItemChecked(position, true);
                replaceFragment(position);
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
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (actionBarDrawerToggle!= null){
            actionBarDrawerToggle.syncState();
        }else{
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
    //create method replace fragment
    private void replaceFragment(int pos) {
        Fragment fragment = null;
        switch (pos) {
            case 0:
                Intent intent = new Intent(GestioneViaggioGalleriaActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent_creaViaggio = new Intent(GestioneViaggioGalleriaActivity.this, CreaIlTuoViaggioActivity.class);
                startActivity(intent_creaViaggio);
                break;
            case 2:
                Intent intent_viaggi = new Intent(GestioneViaggioGalleriaActivity.this, ProfiloViaggiActivity.class);
                intent_viaggi.putExtra("viaggio", "viaggio");
                startActivity(intent_viaggi);
                break;
            case 3:
                Intent intent_preferiti = new Intent(GestioneViaggioGalleriaActivity.this, ProfiloViaggiActivity.class);
                intent_preferiti.putExtra("preferiti", "preferiti");
                startActivity(intent_preferiti);
                break;
            case 4:
                Intent intent_impostazioni = new Intent(GestioneViaggioGalleriaActivity.this, SettingsActivity.class);
                startActivity(intent_impostazioni);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.activity_main, fragment);
            transaction.commit();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Scatta una foto", "Scegli dalla libreria",
                "Indietro" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aggiungi una foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(getApplicationContext());

                if (items[item].equals("Scatta una foto")) {
                    userChoosenTask ="Scatta una foto";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Scegli dalla libreria")) {
                    userChoosenTask ="Scegli dalla libreria";
                    if(result)
                       galleryIntent();

                } else if (items[item].equals("Indietro")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bm= (Bitmap) data.getExtras().get("data");
        new GetFoto().execute(bm);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            new GetFoto().execute(bm);
        }

    }
    private String saveToInternalStorage(Bitmap bitmapImage,String nome_viaggio){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        String nome_foto =  nome_viaggio + System.currentTimeMillis();
        File mypath=new File(directory,nome_foto);
        FileOutputStream fos = null;
        db= new DataBase(getApplicationContext());
        int id_viaggio = db.getIdViaggio(nome_viaggio);
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        Foto foto = new Foto(id_viaggio,mypath.getAbsolutePath());
        db.insertFoto(foto);
        return mypath.getAbsolutePath();
    }
    private class GetFoto extends AsyncTask<Bitmap, Void, Void> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Bitmap... bm) {
            String prova =saveToInternalStorage(bm[0],nome_viaggio);
            GestioneViaggioGalleriaFragmentGrid firstFragment = new GestioneViaggioGalleriaFragmentGrid();
            Bundle bundle = new Bundle();
            bundle.putString("nome_viaggio",nome_viaggio);
            firstFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentGestioneViaggioGalleria, firstFragment).commit();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }



}
