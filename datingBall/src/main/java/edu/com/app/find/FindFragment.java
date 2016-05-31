package edu.com.app.find;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.com.app.R;
import edu.com.base.ui.fragment.AbsBaseFragment;
import edu.com.base.util.ToastUtils;

/**
 * Created by Anthony on 2016/5/31.
 * Class Note:
 */
public class FindFragment extends AbsBaseFragment {
    @Bind(R.id.txt_friendCircle)
    TextView txtFriendCircle;
    @Bind(R.id.txt_nearby)
    TextView txtNearby;
    @Bind(R.id.txt_scan)
    TextView txtScan;
    @Bind(R.id.txt_shake)
    TextView txtShake;

    @Override
    protected void initViewsAndEvents(View rootView) {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_find;
    }






    @OnClick({R.id.txt_scan, R.id.txt_shake,R.id.txt_friendCircle, R.id.txt_nearby})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_scan:
                ToastUtils.getInstance().showToast("scan");
                break;
            case R.id.txt_shake:
                ToastUtils.getInstance().showToast("shake");
                break;
            case R.id.txt_friendCircle:
                ToastUtils.getInstance().showToast("friend circle");
                break;
            case R.id.txt_nearby:
                ToastUtils.getInstance().showToast("nearby");
                break;
        }
    }
}
