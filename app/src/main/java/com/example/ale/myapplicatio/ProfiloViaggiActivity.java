package com.example.ale.myapplicatio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProfiloViaggiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_viaggi);
        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            FragmentProfilo profiloFragment = new FragmentProfilo();
            //profiloFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, profiloFragment).commit();
        }
    }
}
