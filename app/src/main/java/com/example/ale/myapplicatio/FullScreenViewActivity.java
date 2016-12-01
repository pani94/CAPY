package com.example.ale.myapplicatio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

public class FullScreenViewActivity extends Activity{

    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    private DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);

        viewPager = (ViewPager) findViewById(R.id.full_screen_pager);


        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        db = new DataBase(this);
        ArrayList<Foto> fotos = db.getFoto(getIntent().getStringExtra("nome_viaggio"));
        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
                fotos);
//gli devo passare l'array con le foto
        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
    }
}

