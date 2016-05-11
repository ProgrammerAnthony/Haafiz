package edu.com.mvpcommon;

import android.view.View;
import android.widget.RelativeLayout;

import butterknife.Bind;

import edu.com.mvplibrary.ui.fragment.AbsBaseFragment;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 */
public class TestFragment extends AbsBaseFragment implements View.OnClickListener {

    @Bind(R.id.test_layout1)
    RelativeLayout test_layout1;

    @Override
    protected int getContentViewID() {
        return  R.layout.test_layout;
    }

    @Override
    protected View getLoadingTargetView() {
        return test_layout1;
    }

    @Override
    protected void initViewsAndEvents(View rootView) {
//        super.initViewsAndEvents(rootView);
//        toggleShowEmpty(true,"this is empty",this);
        toggleShowLoading(true,"loading");
//        showEmpty();

    }

    @Override
    public void onPause() {
        super.onPause();
        toggleShowLoading(false,"");
    }

    @Override
    public void onClick(View v) {

    }


}
