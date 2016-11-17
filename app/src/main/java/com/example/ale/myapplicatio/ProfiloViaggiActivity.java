package com.example.ale.myapplicatio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProfiloViaggiActivity extends AppCompatActivity {

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
    }
}
