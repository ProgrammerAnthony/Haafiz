package edu.com.mvpCommon.umeng.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListUI;

import edu.com.mvpCommon.R;
import edu.com.mvpCommon.main.MainActivity;
import edu.com.mvpCommon.umeng.helper.CustomSampleHelper;
import edu.com.base.ui.widget.CircleImageView;

/**
 * Created by Anthony on 2016/4/12.
 * Class Note:
 * custom ui in the conversation page, init in{@link CustomSampleHelper#initCustom()}
 * 自定义的聊天列表界面ui修改，请在{@link CustomSampleHelper#initCustom()} 中进行初始化
 */
public class ConversationUICustom extends IMConversationListUI {
    public ConversationUICustom(Pointcut pointcut) {
        super(pointcut);
    }

    @Override
    public View getCustomConversationListTitle(final Fragment fragment,final Context context, LayoutInflater inflater) {
        //TODO 重要：必须以该形式初始化customView---［inflate(R.layout.**, new RelativeLayout(context),false)］------，以让inflater知道父布局的类型，否则布局xml**中定义的高度和宽度无效，均被默认的wrap_content替代
        RelativeLayout customView = (RelativeLayout) inflater
                .inflate(R.layout.title_bar_common, new RelativeLayout(context), false);
        CircleImageView titleImageLeft = (CircleImageView) customView.findViewById(R.id.title_image_left);
        TextView titleTxtCenter = (TextView) customView.findViewById(R.id.title_txt_center);
        TextView titleTxtRight = (TextView) customView.findViewById(R.id.title_txt_right);

        titleImageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof MainActivity){
                    MainActivity.openDrawer();
                }
            }
        });

        titleImageLeft.setImageResource(R.drawable.icon_head);
        titleTxtCenter.setText("聊天列表");
        titleTxtRight.setVisibility(View.GONE);
//        titleTxtCenter.setVisibility(View.GONE);
        return customView;
    }
}