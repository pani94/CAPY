package com.example.ale.myapplicatio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GestioneViaggioGalleriaActivity extends AppCompatActivity {
    private Button btnSelect;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private DataBase db ;
    private String nome_viaggio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nome_viaggio =  getIntent().getStringExtra("attivita_nomeviaggio");
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
            bundle.putString("nome_viaggio",nome_viaggio);
            firstFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentGestioneViaggioGalleria, firstFragment).commit();

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
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(getApplicationContext());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                       galleryIntent();

                } else if (items[item].equals("Cancel")) {
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
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        String prova =saveToInternalStorage(thumbnail, nome_viaggio);
        GestioneViaggioGalleriaFragmentGrid firstFragment = new GestioneViaggioGalleriaFragmentGrid();
        Bundle bundle = new Bundle();
        bundle.putString("nome_viaggio",nome_viaggio);
        firstFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentGestioneViaggioGalleria, firstFragment).commit();
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
        }
        String prova =saveToInternalStorage(bm,nome_viaggio);
        GestioneViaggioGalleriaFragmentGrid firstFragment = new GestioneViaggioGalleriaFragmentGrid();
        Bundle bundle = new Bundle();
        bundle.putString("nome_viaggio",nome_viaggio);
        firstFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentGestioneViaggioGalleria, firstFragment).commit();
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



}
