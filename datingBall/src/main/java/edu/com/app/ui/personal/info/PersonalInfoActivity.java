package edu.com.app.ui.personal.info;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.app.R;
import edu.com.app.ui.base.AbsSwipeBackActivity;
import edu.com.app.ui.widget.CircleImageView;

/**
 * Created by Anthony on 2016/5/26.
 * Class Note:
 * todo 利用RecyclerView 处理个人中心，添加顶部视图（头像和个人签名）和底部视图（）
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