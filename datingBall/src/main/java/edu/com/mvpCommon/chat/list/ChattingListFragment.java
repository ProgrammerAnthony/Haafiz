package edu.com.mvpCommon.chat.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.mobileim.YWIMKit;

import edu.com.mvpCommon.R;
import edu.com.mvpCommon.umeng.helper.LoginSampleHelper;
import edu.com.base.ui.fragment.AbsTitleFragment;

/**
 * Created by Anthony on 2016/5/10.
 * Class Note:
 */
public class ChattingListFragment extends AbsTitleFragment{
    private YWIMKit mIMKit;
    private Fragment fragment;
    @Override
    protected int getCenterViewID() {
        return R.layout.fragment_frame;
    }

    @Override
    protected int getTopBarViewID() {
        return 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginSampleHelper loginHelper = LoginSampleHelper.getInstance();
        mIMKit = loginHelper.getIMKit();
        if (mIMKit == null) {
            LoginSampleHelper.getInstance().initIMKit("testpro74", "23015524");
            mIMKit = LoginSampleHelper.getInstance().getIMKit();
        }
        fragment =mIMKit.getConversationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_frame_id, fragment).commit();
        return view;
    }

    @Override
    protected void initViewsAndEvents(View rootView) {

//        notifyAll();

    }

    @Override
    public void onPause() {
        super.onPause();
//        toggleShowLoading(false,"");
    }
}
