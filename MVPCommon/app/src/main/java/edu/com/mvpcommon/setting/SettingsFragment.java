package edu.com.mvpcommon.setting;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import edu.com.mvpcommon.R;

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
}
