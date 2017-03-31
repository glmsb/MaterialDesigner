package com.demo.wyd.materialDesignerDemo;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;

/**
 * Description :全局的application ，放置应用的一些配置信息
 * Created by wyd on 2016/10/20.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //普通统计场景类型
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.openActivityDurationTrack(false);
        //打开调试模式后，您可以在logcat中查看您的数据是否成功发送到友盟服务器，
        // 以及集成过程中的出错原因等，友盟相关log的tag是MobclickAgent。
        MobclickAgent.setDebugMode(true);
    }
}
