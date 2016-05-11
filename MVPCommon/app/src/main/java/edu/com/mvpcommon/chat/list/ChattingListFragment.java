package edu.com.mvpcommon.chat.list;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.mvpcommon.R;
import edu.com.mvpcommon.friends.add.FriendsAddActivity;
import edu.com.mvplibrary.ui.fragment.AbsBaseFragment;

/**
 * Created by Anthony on 2016/5/10.
 * Class Note:
 */
public class ChattingListFragment extends AbsBaseFragment {

    @Bind(R.id.title_txt_center)
    TextView mTitle;
    @Bind(R.id.title_image_left)
    ImageView mIcon;
    @Bind(R.id.title_txt_right)
    TextView mTitleRight;

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents(View rootView) {
        mTitle.setText("聊天列表");
        mTitleRight.setVisibility(View.GONE);
        mIcon.setImageResource(R.drawable.icon_head);
    }

    @Override
    protected int getContentViewID() {
        return super.getContentViewID();
    }

}
