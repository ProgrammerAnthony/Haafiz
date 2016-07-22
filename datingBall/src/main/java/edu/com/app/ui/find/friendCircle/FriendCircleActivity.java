package edu.com.app.ui.find.friendCircle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.app.R;
import edu.com.app.base.AbsSwipeBackActivity;
import edu.com.app.injection.component.ActivityComponent;
import edu.com.app.widget.dialog.ChooseFriendCircleDialog;

/**
 * Created by Anthony on 2016/6/27.
 * Class Note:
 */
public class FriendCircleActivity extends AbsSwipeBackActivity {
    @Bind(R.id.title_image_left)
    ImageView titleImageLeft;
    @Bind(R.id.title_txt_center)
    TextView titleTxtCenter;
    @Bind(R.id.title_image_right)
    ImageView titleImageRight;

    @Inject
    ChooseFriendCircleDialog mDialog;

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_friend_circle;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
       activityComponent.inject(this);
    }



    @OnClick({R.id.title_image_left, R.id.title_image_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_image_left:
                scrollToFinishActivity();
                break;
            case R.id.title_image_right:
                mDialog.show();
                break;
        }
    }
}
