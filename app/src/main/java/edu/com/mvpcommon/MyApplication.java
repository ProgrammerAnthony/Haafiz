package edu.com.mvpcommon;

import android.content.Context;
import android.widget.Gallery;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.wxlib.util.SysUtil;

import edu.com.mvpcommon.umeng.helper.CustomSampleHelper;
import edu.com.mvpcommon.umeng.helper.LoginSampleHelper;
import edu.com.mvplibrary.AbsApplication;

/**
 * Created by Anthony on 2016/5/4.
 * Class Note:
 */
public class MyApplication extends AbsApplication {
    private static Context sContext;


    @Override
    public void onCreate() {
        super.onCreate();
        initYWSDK();

        initEaseUI();

    }

    private void initEaseUI() {
//        EaseUIHelper.getInstance(this).init();
    }



    private void initYWSDK() {
        //todo Application.onCreate中，首先执行这部分代码，以下代码固定在此处，不要改动，这里return是为了退出Application.onCreate！！！
        if (mustRunFirstInsideApplicationOnCreate()) {
            //todo 如果在":TCMSSevice"进程中，无需进行openIM和app业务的初始化，以节省内存
            return;
        }
        //todo 只在主进程进行云旺SDK的初始化!!!
        if(SysUtil.isMainProcess(this)){
            //TODO 注意：--------------------------------------
            //  以下步骤调用顺序有严格要求，请按照示例的步骤（todo step）
            // 的顺序调用！
            //TODO --------------------------------------------

            // ------[todo step1]-------------
            //［IM定制初始化］，如果不需要定制化，可以去掉此方法的调用
            //todo 注意：由于增加全局初始化，该配置需最先执行！

            CustomSampleHelper.initCustom();

            // ------[todo step2]-------------
            //SDK初始化
            LoginSampleHelper.getInstance().initSDK_Sample(this);


            //后期将使用Override的方式进行集中配置，请参照YWSDKGlobalConfigSample
            YWAPI.enableSDKLogOutput(true);
        }
    }

    private boolean mustRunFirstInsideApplicationOnCreate() {
        //必须的初始化
        SysUtil.setApplication(this);
        sContext = getApplicationContext();
        return SysUtil.isTCMSServiceProcess(sContext);
    }
    public static Context getContext() {
        return sContext;
    }

}
