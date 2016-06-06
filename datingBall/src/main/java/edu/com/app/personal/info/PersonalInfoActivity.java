package edu.com.app.personal.info;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.app.R;
import edu.com.base.ui.activity.AbsSwipeBackActivity;
import edu.com.base.ui.widget.CircleImageView;

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
    protected void injectDagger() {
//        mActivityComponent.inject(this);
    }

    @Override
    protected void initToolBar() {

    }


    @OnClick(R.id.title_image_left)
    public void onClick() {
        scrollToFinishActivity();
    }
}