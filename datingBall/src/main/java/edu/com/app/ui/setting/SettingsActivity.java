package edu.com.app.ui.setting;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.com.app.R;
import edu.com.app.data.bean.Event;
import edu.com.app.data.rx.RxBus;
import edu.com.app.ui.base.AbsSwipeBackActivity;
import edu.com.app.ui.widget.CircleImageView;
import edu.com.app.util.ToastUtils;
import rx.functions.Action1;


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
    @Inject
    ToastUtils toastUtils;

    @Inject
    RxBus rxBus;

    @Override
    protected void initViewsAndEvents() {
        // Display the fragment as the main content.
        titleTxtCenter.setText("设置");
        titleTxtRight.setVisibility(View.GONE);
        titleImageLeft.setImageResource(R.drawable.ico_back);
        rxBus.
                toObservable(Event.class).
                subscribe(new Action1<Event>() {
                              @Override
                              public void call(Event event) {
                                  String id = event.getId();
                                  String name = event.getName();
                                  toastUtils.showToast(id + " is " + name);
                              }
                          },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                toastUtils.showToast("error");
                            }
                        });

        getFragmentManager().beginTransaction()
                .replace(R.id.setting_content, new SettingsFragment())
                .commit();

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_settings;
    }

    @Override
    protected void injectDagger() {
        activityComponent().inject(this);
    }

    @Override
    protected void initToolBar() {

    }


    @OnClick(R.id.title_image_left)
    public void onClick() {
        scrollToFinishActivity();
    }
}
