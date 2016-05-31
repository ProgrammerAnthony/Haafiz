package edu.com.mvpCommon.umeng.config;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.YWSDKGlobalConfig;

/**
 *
 * 全局配置示例
 * 开发者通过继承YWSDKGlobalConfig，Override方法的返回值进行全局配置
 * @author shuheng
 */
public class YWSDKGlobalConfigSample extends YWSDKGlobalConfig {


    private YWSDKGlobalConfigSample(Pointcut pointcut) {
        super(pointcut);
    }

    //用于给主程序层拿配置
    public volatile static YWSDKGlobalConfigSample mInstance =null;

    public static YWSDKGlobalConfigSample getInstance(){
        if (mInstance == null) {
            synchronized(YWSDKGlobalConfigSample.class) {
                if (mInstance == null) {
                    mInstance = new YWSDKGlobalConfigSample(null);
                }
            }
        }
        return mInstance;
    }



    /**
     * 群聊@功能的开关.[关闭后群聊界面输入@不会弹出群成员列表，窗口右上角也不会右上角的@交互图标]
     *  默认：开启
     * @return true： 开启 false： 关闭
     */
    @Override
    public boolean enableTheTribeAtRelatedCharacteristic(){
        return true;
    }
    /**
     *  ［自动登录的开关］备注：若配置了YWAPI.enableSDKLogOutput(),将以YWAPI.enableSDKLogOutput（）的配置为准
     *  默认：开启
     * @return 1： 开启 0： 关闭
     */
    @Override
    public boolean enableAutoLogin(){
        return true;
    }


    /**
     *  黑名单功能的开关，如果需要使用黑名单功能，请务必调用该方法，否则黑名单功能无效。
     *  备注：若配置IYWContactService.enableBlackList();，将以IYWContactService.enableBlackList();的配置为准
     *  默认：开启
     * @return true： 开启 false： 关闭
     */
    @Override
    public boolean enableBlackList(){
        return true;
    }
    /**
     *  压缩联系人、会话、聊天界面中等头像图片的开关.开启后图像会压缩到几十KB的大小
     *  默认：开启
     * @return true： 开启 false： 关闭
     */
    @Override
    public boolean enableCompressContactHead() {
        return true;
    }

    /**
     *  聊天显示“对方正在输入中”状态的开关
     *  默认：开启
     * @return true： 开启 false： 关闭
     */
    @Override
    public boolean enableInputStatus() {
        return true;
    }


    /**
     *  是否支持单聊消息的单条消息精确显示已读未读
     *  默认：关闭
     * @return true： 开启 false： 关闭
     */
    @Override
    public boolean enableMsgReadStatus(){
        return true;
    }
}
