package edu.com.app.ui.find;

import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.app.R;
import edu.com.app.base.AbsBaseFragment;
import edu.com.app.ui.main.MainActivity;
import edu.com.app.util.ToastUtils;

/**
 * Created by Anthony on 2016/5/31.
 * Class Note:
 * findFragment contains four parts:
 * 1 friend circle  todo
 * 2 nearby people  todo
 * 3 scan QR code
 * 4 shake phone to find someone shake at the sametime
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

    @Inject
    ToastUtils toastUtils;

    @Override
    protected void initDagger() {
        ((MainActivity) getActivity()).activityComponent().inject(this);
    }

    @Override
    protected void initViewsAndEvents(View rootView) {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_find;
    }


    @OnClick({R.id.txt_scan, R.id.txt_shake, R.id.txt_friendCircle, R.id.txt_nearby})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_friendCircle:
                toastUtils.showToast("friend circle");
                break;
            case R.id.txt_nearby:
                toastUtils.showToast("nearby");
                break;
            case R.id.txt_scan:
                toastUtils.showToast("scan");
                break;
            case R.id.txt_shake:
                toastUtils.showToast("shake");
                break;
        }
    }
}
