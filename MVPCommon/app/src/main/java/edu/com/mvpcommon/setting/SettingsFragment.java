package edu.com.mvpcommon.setting;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import edu.com.mvpcommon.R;
import edu.com.mvplibrary.model.bean.Event;
import edu.com.mvplibrary.model.rx.RxBus;
import edu.com.mvplibrary.util.ToastUtils;

/**
 * Created by Anthony on 2016/5/13.
 * Class Note:
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if(preference.getKey().equals("pref_key_wifi_loading_img")){

            CheckBoxPreference boxPreference= (CheckBoxPreference) findPreference("pref_key_wifi_loading_img");
            if(boxPreference.isChecked()){
                ToastUtils.getInstance().showToast("isChecked");
                RxBus.getDefault().post(new Event("001","wifi"));
            }else{
                ToastUtils.getInstance().showToast("unCheked");
                RxBus.getDefault().post(new Event("002","not wifi"));
            }

        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
