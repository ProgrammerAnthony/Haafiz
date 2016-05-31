package edu.com.mvpCommon.personal.info;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.mvpCommon.R;
import edu.com.base.ui.fragment.AbsTitleFragment;
import edu.com.mvpCommon.main.MainActivity;

/**
 * Created by Anthony on 2016/5/10.
 * Class Note:
 */
public class PersonalInfoFragment extends AbsTitleFragment {
    @Bind(R.id.title_txt_center)
    TextView mTitle;
    @Bind(R.id.title_image_left)
    ImageView mIcon;
    @Bind(R.id.title_txt_right)
    TextView mTitleRight;

    @OnClick(R.id.title_image_left)
    public void openDrawer(){
        if (mContext instanceof MainActivity) {
//            MainActivity.openDrawer();
        }
    }
    @Override
    protected int getCenterViewID() {
        return 0;
    }

    @Override
    protected int getTopBarViewID() {
        return R.layout.title_bar_common;
    }

    @Override
    protected void initViewsAndEvents(View rootView) {
        mTitle.setText("个人中心");
        mTitleRight.setVisibility(View.GONE);
        mIcon.setImageResource(R.drawable.icon_head);
//        toggleShowLoading(true,"loading");
    }

    @Override
    public void onPause() {
        super.onPause();
//        toggleShowLoading(false,"");
    }
}
