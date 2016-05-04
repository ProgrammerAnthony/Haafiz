package edu.com.mvpcommon.news.newsList;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import java.util.ArrayList;

import butterknife.Bind;
import edu.com.mvpcommon.R;
import edu.com.mvplibrary.model.Channel;
import edu.com.mvplibrary.model.Topic;
import edu.com.mvplibrary.ui.fragment.AbsBaseFragment;
import edu.com.mvplibrary.util.ToastUtils;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 */
public class NewsListFragment extends AbsBaseFragment implements NewsListContract.View, View.OnClickListener{
    @Bind(R.id.test_layout2)
    RelativeLayout test_layout2;

    @Override
    protected int getContentViewID() {
        return R.layout.test_layout2;
    }

    @Override
    protected View getLoadingTargetView() {
        return test_layout2;
    }

    @Override
    protected void initViewsAndEvents(View rootView) {
        super.initViewsAndEvents(rootView);
//        toggleShowEmpty(true,"empty",this);
//        toggleShowError(true,"error",this);
//        showLoading("loading");

//        showError("no data now",new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                ToastUtils.getInstance().showToast("no data clicked");
//            }
//        });


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void loadMore(ArrayList<Channel> news, ArrayList<Topic> topics) {

    }

    @Override
    public void refresh(ArrayList<Channel> news, ArrayList<Topic> topics) {

    }
}
