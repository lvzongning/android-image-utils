package com.github.zcwfeng.aiu;

import android.app.Application;
import android.content.Context;

/**
 * ==========================================
 * Created by David Zhang on 2015/08/30.
 * Description：
 * Copyright © 2015 张传伟. All rights reserved.
 * Modified by:
 * Modified Content:
 * ==========================================
 */
public class MyApplication extends Application {

    private static MyApplication myApplication;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        mContext = getApplicationContext();
        CommonUtils.initDip2px(myApplication);
    }

    /**
     * 获取当前应用的上下文对象(和Application对象一致)
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }
}
