package edu.com.app.ui.setting;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import javax.inject.Inject;

import edu.com.app.R;
import edu.com.app.data.bean.Event;
import edu.com.app.data.rx.RxBus;
import edu.com.app.ui.main.MainActivity;
import edu.com.app.util.ToastUtils;

/**
 * Created by Anthony on 2016/5/13.
 * Class Note:
 */
public class SettingsFragment extends PreferenceFragment {

    @Inject
    RxBus rxBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        initDagger();
    }

    private void initDagger() {
        ((SettingsActivity) getActivity()).activityComponent().inject(this);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if(preference.getKey().equals("pref_key_wifi_loading_img")){

            CheckBoxPreference boxPreference= (CheckBoxPreference) findPreference("pref_key_wifi_loading_img");
            if(boxPreference.isChecked()){
                rxBus.post(new Event("001","wifi"));
            }else{
                rxBus.post(new Event("002","not wifi"));
            }

        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
