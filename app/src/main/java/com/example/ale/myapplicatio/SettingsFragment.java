package com.example.ale.myapplicatio;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView titolo = new TextView(getActivity());
        ImageView icon = new ImageView(getActivity());
        addPreferencesFromResource(R.xml.preferences);
    }

}