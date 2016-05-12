package edu.com.mvpcommon.umeng.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMContactsUI;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.com.mvpcommon.R;
import edu.com.mvpcommon.main.DrawerMainActivity;
import edu.com.mvpcommon.umeng.helper.CustomSampleHelper;
import edu.com.mvplibrary.ui.widget.CircleImageView;
import edu.com.mvplibrary.util.ToastUtils;


/**
 * Created by Anthony on 2016/4/12.
 * Class Note:
 * custom ui in the Contacts pages, initialize in{@link CustomSampleHelper#initCustom()}
 * 自定义的联系人界面ui修改，请在{@link CustomSampleHelper#initCustom()} 中进行初始化
 */
public class ContactsUICustom extends IMContactsUI {
    public ContactsUICustom(Pointcut pointcut) {
        super(pointcut);
    }

    @Override
    public View getCustomTitle(final Fragment fragment, final Context context, LayoutInflater inflater) {
        //TODO 重要：必须以该形式初始化customView---［inflate(R.layout.**, new RelativeLayout(context),false)］------，以让inflater知道父布局的类型，否则布局xml**中定义的高度和宽度无效，均被默认的wrap_content替代
        RelativeLayout customView = (RelativeLayout) inflater
                .inflate(R.layout.title_bar_common, new RelativeLayout(context), false);
        CircleImageView titleImageLeft = (CircleImageView) customView.findViewById(R.id.title_image_left);
        TextView titleTxtCenter = (TextView) customView.findViewById(R.id.title_txt_center);
        TextView titleTxtRight = (TextView) customView.findViewById(R.id.title_txt_right);

        titleImageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof DrawerMainActivity) {
                    DrawerMainActivity.openDrawer();
                }
            }
        });
        titleImageLeft.setImageResource(R.drawable.icon_head);

        titleTxtRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.getInstance().showToast("add");
//                Intent intent = new Intent(context, FriendsAddActivity.class);
//                context.startActivity(intent);
            }
        });
        titleTxtCenter.setText("好友1");
        titleTxtRight.setText("添加1");
        return customView;
    }
}
