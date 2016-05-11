package edu.com.mvpcommon.nearby.list;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.mvpcommon.R;
import edu.com.mvpcommon.nearby.detail.NearByDetailActivity;
import edu.com.mvpcommon.personal.edit.PersonalEditActivity;
import edu.com.mvplibrary.ui.fragment.AbsBaseFragment;
import edu.com.mvplibrary.util.ToastUtils;

/**
 * Created by Anthony on 2016/5/10.
 * Class Note:
 */
public class NearByListFragment extends AbsBaseFragment {

    @Bind(R.id.title_txt_center)
    TextView mTitle;
    @Bind(R.id.title_image_left)
    ImageView mIcon;
    @Bind(R.id.title_txt_right)
    TextView mTitleRight;
    @OnClick(R.id.title_txt_right)
    public void filterUser(){
//        ToastUtils.getInstance().showToast("not support now");
        Intent intent =new Intent(mContext, NearByDetailActivity.class);
        startActivity(intent);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents(View rootView) {
        mTitle.setText("附近的人");
        mTitleRight.setText("筛选");
        mIcon.setImageResource(R.drawable.icon_head);

    }

    @Override
    protected int getContentViewID() {
        return super.getContentViewID();
    }
}
