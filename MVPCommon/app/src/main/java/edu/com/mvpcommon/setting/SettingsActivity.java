package edu.com.mvpcommon.setting;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.com.mvpcommon.R;
import edu.com.mvplibrary.ui.activity.AbsSwipeBackActivity;
import edu.com.mvplibrary.ui.widget.CircleImageView;


/**
 * Created by Anthony on 2016/5/10.
 * Class Note:
 * SettingsActivity using {@link PreferenceFragment},support swipe back
 */
public class SettingsActivity extends AbsSwipeBackActivity {
    @Bind(R.id.title_image_left)
    CircleImageView titleImageLeft;
    @Bind(R.id.title_txt_center)
    TextView titleTxtCenter;
    @Bind(R.id.title_txt_right)
    TextView titleTxtRight;
    @Bind(R.id.setting_content)
    RelativeLayout settingContent;
//    @Bind(R.id.setting_content)
//    RelativeLayout mSettingContent;

    @Override
    protected View getLoadingTargetView() {
        return settingContent;
    }

    @Override
    protected void initViewsAndEvents() {
        // Display the fragment as the main content.
        titleTxtCenter.setText("设置");
        titleTxtRight.setVisibility(View.GONE);
        titleImageLeft.setImageResource(R.mipmap.ico_back);
        getFragmentManager().beginTransaction()
                .replace(R.id.setting_content, new SettingsFragment())
                .commit();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_settings;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.title_image_left)
    public void onClick() {
        scrollToFinishActivity();

    }
}
