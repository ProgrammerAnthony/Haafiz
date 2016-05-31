package edu.com.mvpcommon.personal.info;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.com.mvpcommon.R;
import edu.com.mvpcommon.setting.SettingsFragment;
import edu.com.mvplibrary.model.bean.Event;
import edu.com.mvplibrary.model.rx.RxBus;
import edu.com.mvplibrary.ui.activity.AbsBaseActivity;
import edu.com.mvplibrary.ui.activity.AbsSwipeBackActivity;
import edu.com.mvplibrary.ui.widget.CircleImageView;
import edu.com.mvplibrary.util.ToastUtils;
import rx.functions.Action1;

/**
 * Created by Anthony on 2016/5/26.
 * Class Note:
 */
public class PersonalInfoActivity extends AbsSwipeBackActivity {
    @Bind(R.id.title_image_left)
    CircleImageView titleImageLeft;
    @Bind(R.id.title_txt_center)
    TextView titleTxtCenter;
    @Bind(R.id.title_txt_right)
    TextView titleTxtRight;



    @Override
    protected void initViewsAndEvents() {
        // Display the fragment as the main content.
        titleTxtCenter.setText("个人中心");
        titleTxtRight.setVisibility(View.GONE);
        titleImageLeft.setImageResource(R.drawable.ico_back);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initDagger() {

    }


    @OnClick(R.id.title_image_left)
    public void onClick() {
        scrollToFinishActivity();
    }
}