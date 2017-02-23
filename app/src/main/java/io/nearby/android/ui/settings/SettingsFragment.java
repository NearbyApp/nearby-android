package io.nearby.android.ui.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import io.nearby.android.R;
import timber.log.Timber;

/**
 * Created by Marc on 2017-02-23.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        Timber.d(preference.getKey());
        switch (preference.getKey()){
            case "pref_connect_facebook":
                break;
            case "pref_connect_google":
                break;
            case "pref_log_out":
                break;
            case "pref_deactivate_account":
                break;

        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
