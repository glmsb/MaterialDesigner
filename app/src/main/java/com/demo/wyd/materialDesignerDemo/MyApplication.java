package com.demo.wyd.materialDesignerDemo;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;
import com.wanjian.sak.LayoutManager;

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

        //可以直接在android手机屏幕上显示当前Activity中所有控件（不管是否隐藏）的边界，内外边距大小，
        // 每一个控件大小，图片大小，字体颜色，大小，以及自定义信息。同时可以直接在屏幕上取色，
        // 另外还提供了直尺（单位为px和dp），圆角尺（单位dp）工具，可以直接测量大小。
        // 针对android开发者还提供了布局树查看功能，可以直接在手机屏幕查看当前Activity中所有控件层次信息等。
        LayoutManager.init(this);
    }
}
