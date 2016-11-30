package com.example.ale.myapplicatio;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Switch;

public class SettingsFragment extends PreferenceFragment {

    private SharedPreferences preferences;
    private boolean remember_notification;
    private Switch mySwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        //PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, true);
        //preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //preferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);

        /*SharedPreferences.Editor editor = preferences.edit();
        editor.putString("preference_notification", "true");
        editor.commit();*/
    }

    /*public void onResume(){
        remember_notification = preferences.getBoolean("preference_notification", true);
    }*/

}