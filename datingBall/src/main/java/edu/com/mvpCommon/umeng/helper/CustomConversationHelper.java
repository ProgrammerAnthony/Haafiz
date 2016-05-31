package edu.com.mvpCommon.umeng.helper;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWCustomConversationUpdateModel;

import java.util.Date;

/**
 * 会话列表增加一个自定义会话
 *
 * @author zhaoxu
 */
public class CustomConversationHelper {

    private static YWCustomConversationUpdateModel mCustomConversation;
    private static YWCustomConversationUpdateModel mCustomViewConversation;

    public static void addCustomConversation(String conversationID, String content) {

        mCustomConversation = new YWCustomConversationUpdateModel();
        mCustomConversation.setIdentity(conversationID);
        mCustomConversation.setContent(content);
        mCustomConversation.setLastestTime(new Date().getTime());

        YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
        IYWConversationService conversationService = imKit.getConversationService();
        YWConversation conversation = conversationService.getCustomConversationByConversationId(conversationID);
        if (conversation != null){
            conversation.getUnreadCount();
        }

        if (conversationService.getCustomConversationByConversationId(conversationID) == null) {
            conversationService.updateOrCreateCustomConversation(mCustomConversation);
        }
    }

    public static void addCustomViewConversation(String conversationID, String content) {

        mCustomViewConversation = new YWCustomConversationUpdateModel();
        mCustomViewConversation.setIdentity(conversationID);
        mCustomViewConversation.setContent(content);
        mCustomViewConversation.setLastestTime(new Date().getTime());

        YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
        IYWConversationService conversationService = imKit.getConversationService();
        if (conversationService.getCustomViewConversationByConversationId(conversationID) == null) {
            conversationService.updateOrCreateCustomViewConversation(mCustomViewConversation);
        }
    }

    public static void updateCustomConversationContent(String content) {
        if (mCustomConversation == null)
            return;
        mCustomConversation.setContent(content);
        mCustomConversation.setLastestTime(new Date().getTime());

        YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
        IYWConversationService conversationService = imKit.getConversationService();
        conversationService.updateOrCreateCustomConversation(mCustomConversation);
    }

    public static void updateCustomViewConversationContent(String content) {
        if (mCustomViewConversation == null)
            return;
        mCustomViewConversation.setContent(content);
        mCustomViewConversation.setLastestTime(new Date().getTime());

        YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
        IYWConversationService conversationService = imKit.getConversationService();
        conversationService.updateOrCreateCustomViewConversation(mCustomViewConversation);
    }
}
