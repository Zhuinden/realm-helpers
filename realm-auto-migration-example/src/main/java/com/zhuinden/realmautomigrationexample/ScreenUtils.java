package com.zhuinden.realmautomigrationexample;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Zhuinden on 2017.09.24..
 */

public class ScreenUtils {
    private ScreenUtils() {
    }

    public static float pxToDp(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static float dpToPx(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
