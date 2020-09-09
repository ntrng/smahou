package com.moripro.morina.mylawdialog;

import android.os.Bundle;
import android.view.View;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.moripro.morina.mylawdialog.preference.SettingsPref;


public class SettingFragment extends PreferenceFragmentCompat {

    private ListPreference listPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        listPreference = findPreference("text_size");
        if(listPreference.getValue() == null) {
            listPreference.setValueIndex(1);
        }

        Preference.OnPreferenceChangeListener changeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (preference.getKey().equals("text_size")){
                    SettingsPref.setTextPref(App.getMcontext(), newValue.toString());
                    return true;
                }
                return false;
            }
        };
        listPreference.setOnPreferenceChangeListener(changeListener);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
