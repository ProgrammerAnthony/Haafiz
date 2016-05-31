package edu.com.mvpcommon.umeng.helper;

import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;

import edu.com.mvpcommon.umeng.ui.ContactsUICustom;
import edu.com.mvpcommon.umeng.ui.ConversationUICustom;


/**
 * Created by Anthony on 2016/3/22.
 * Class Note:
 * <p>
 * IM定制化初始化统一入口，这里后续会增加更多的IM定制化功能
 */
public class CustomSampleHelper {
    private static String TAG = CustomSampleHelper.class.getSimpleName();

    public static void initCustom() {

        //聊天界面相关自定义-------
        //聊天界面的自定义风格1：［图片、文字小猪气泡］风格
//        AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_UI_POINTCUT, ChattingUICustom.class);

        //会话列表相关自定义------
        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT, ConversationUICustom.class);
        //联系人列表自定义------
        AdviceBinder.bindAdvice(PointCutEnum.CONTACTS_UI_POINTCUT, ContactsUICustom.class);
    }
}
