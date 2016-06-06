package edu.com.app.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.app.R;

import edu.com.base.ui.fragment.AbsBaseFragment;
import edu.com.base.ui.fragment.AbsTitleFragment;
import edu.com.base.util.ToastUtils;

/**
 * Created by Anthony on 2016/5/10.
 * Class Note:聊天列表界面 集成中
 */
public class ChattingListFragment extends AbsBaseFragment{



    @Override
    protected void initViewsAndEvents(View rootView) {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_chatting_list;
    }






}
