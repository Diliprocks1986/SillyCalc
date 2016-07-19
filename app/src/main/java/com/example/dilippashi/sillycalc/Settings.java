package com.example.dilippashi.sillycalc;

import android.os.Bundle;
import android.preference.PreferenceFragment;


public class Settings extends PreferenceFragment {


    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);


    }


}
