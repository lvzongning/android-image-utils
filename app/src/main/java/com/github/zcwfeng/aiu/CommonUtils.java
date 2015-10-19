package com.github.zcwfeng.aiu;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * ==========================================
 * Created by David Zhang on 2015/08/30.
 * Description：
 * Copyright © 2015 张传伟. All rights reserved.
 * Modified by:
 * Modified Content:
 * ==========================================
 */
public class CommonUtils {
    private static int screen_width;

    public static int getScreenWidth() {
        return screen_width;
    }

    public static int getScreenHeight() {
        return screen_height;
    }

    private static int screen_height;

    public static void initDip2px(Context context) {
        if (null != context) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            screen_width = metrics.widthPixels;
            screen_height = metrics.heightPixels;
        }
    }




}
