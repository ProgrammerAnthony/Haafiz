package edu.com.mvplibrary.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.lang.ref.WeakReference;


/**
 * Night Mode Helper
 * <p/>
 * <p>Helps use utilise the night and notnight resource qualifiers without
 * being in car or dock mode.
 * <p/>
 * <p>Implementation is simple. Add the follow line at the top of your
 * activity's onCreate just after the super.onCreate(); The idea here
 * is to do it before we create any views. So the new views will use
 * the correct Configuration.
 * <p/>
 * <pre>
 * mNightModeHelper = new NightModeHelper(this, R.style.AppTheme);
 * </pre>
 * <p/>
 * You can now use your instance of NightModeHelper to control which mode
 * you are in. You can choose to persist the current setting and hand
 * it back to this class as the defaultUiMode, otherwise this is done
 * for you automatically.
 * <p/>
 * <p>I'd suggest you setup your Theme as follows:
 * <p/>
 * <ul>
 * <li>
 * <b>res\values\styles.xml</b>
 * <pre>&lt;style name=&quot;AppTheme&quot; parent=&quot;AppBaseTheme&quot;&gt;&lt;/style&gt;</pre>
 * </li>
 * <li>
 * <b>res\values-night\styles.xml</b>
 * <pre>&lt;style name=&quot;AppBaseTheme&quot; parent=&quot;@android:style/Theme.Holo&quot;&gt;&lt;/style&gt;</pre>
 * </li>
 * <li>
 * <b>res\values-notnight\styles.xml</b>
 * <pre>&lt;style name=&quot;AppBaseTheme&quot; parent=&quot;@android:style/Theme.Holo.Light&quot;&gt;&lt;/style&gt;</pre>
 * </li>
 * </ul>
 *
 * @author Simon Lightfoot <simon@demondevelopers.com>
 */
public class NightModeHelper {

    private static final String PREF_KEY = "nightModeState";

    private static int sUiNightMode = Configuration.UI_MODE_NIGHT_UNDEFINED;

    private WeakReference<Activity> mActivity;
    private SharedPreferences mPrefs;


    /**
     * Default behaviour is to automatically save the setting and restore it.
     */
    public NightModeHelper(Activity activity) {
        int currentMode = (activity.getResources().getConfiguration()
                .uiMode & Configuration.UI_MODE_NIGHT_MASK);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        init(activity, -1, mPrefs.getInt(PREF_KEY, currentMode));
    }

    /**
     * Default behaviour is to automatically save the setting and restore it.
     */
    public NightModeHelper(Activity activity, int theme) {
        int currentMode = (activity.getResources().getConfiguration()
                .uiMode & Configuration.UI_MODE_NIGHT_MASK);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        init(activity, theme, mPrefs.getInt(PREF_KEY, currentMode));
    }

    /**
     * If you don't want the autoSave feature and instead want to provide
     * your own persisted storage for the mode, use the defaultUiMode for it.
     */
    public NightModeHelper(Activity activity, int theme, int defaultUiMode) {
        init(activity, theme, defaultUiMode);
    }

    private void init(Activity activity, int theme, int defaultUiMode) {
        mActivity = new WeakReference<Activity>(activity);
        if (sUiNightMode == Configuration.UI_MODE_NIGHT_UNDEFINED) {
            sUiNightMode = defaultUiMode;
        }
        updateConfig(sUiNightMode);

        // This may seem pointless but it forces the Theme to be reloaded
        // with new styles that would change due to new Configuration.
        if (theme != -1) {
            activity.setTheme(theme);
        }
    }

    private void updateConfig(int uiNightMode) {
        Activity activity = mActivity.get();
        if (activity == null) {
            throw new IllegalStateException("Activity went away?");
        }
        Configuration newConfig = new Configuration(activity.getResources().getConfiguration());
        newConfig.uiMode &= ~Configuration.UI_MODE_NIGHT_MASK;
        newConfig.uiMode |= uiNightMode;
        activity.getResources().updateConfiguration(newConfig, null);
        sUiNightMode = uiNightMode;
        if (mPrefs != null) {
            mPrefs.edit()
                    .putInt(PREF_KEY, sUiNightMode)
                    .apply();
        }
    }

    public static int getUiNightMode() {
        return sUiNightMode;
    }

    public void toggle() {
        if (sUiNightMode == Configuration.UI_MODE_NIGHT_YES) {
            notNight();
        } else {
            night();
        }
    }




    public void notNight() {
        updateConfig(Configuration.UI_MODE_NIGHT_NO);
        System.gc();
        System.runFinalization(); // added in https://github.com/android/platform_frameworks_base/commit/6f3a38f3afd79ed6dddcef5c83cb442d6749e2ff
        System.gc();
        mActivity.get().recreate();
    }

    public void night() {
        updateConfig(Configuration.UI_MODE_NIGHT_YES);
        System.gc();
        System.runFinalization(); // added in https://github.com/android/platform_frameworks_base/commit/6f3a38f3afd79ed6dddcef5c83cb442d6749e2ff
        System.gc();
        mActivity.get().recreate();
    }
}
