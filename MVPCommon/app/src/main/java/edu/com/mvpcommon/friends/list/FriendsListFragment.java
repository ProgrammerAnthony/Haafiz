package edu.com.mvpcommon.friends.list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.mvpcommon.R;
import edu.com.mvpcommon.main.DrawerMainActivity;
import edu.com.mvplibrary.ui.fragment.AbsBaseFragment;
import edu.com.mvplibrary.ui.fragment.AbsTitleFragment;

/**
 * Created by Anthony on 2016/5/10.
 * Class Note:
 */
public class FriendsListFragment extends AbsTitleFragment {
    @Bind(R.id.title_txt_center)
    TextView mTitle;
    @Bind(R.id.title_image_left)
    ImageView mIcon;
    @Bind(R.id.title_txt_right)
    TextView mTitleRight;

    @OnClick(R.id.title_image_left)
    public void openDrawer(){
        if (mContext instanceof DrawerMainActivity) {
            DrawerMainActivity.openDrawer();
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
        mTitle.setText("好友列表");
        mTitleRight.setVisibility(View.GONE);
        mIcon.setImageResource(R.drawable.icon_head);
        toggleShowLoading(true,"loading");
    }

    @Override
    public void onPause() {
        super.onPause();
        toggleShowLoading(false,"");
    }
}
