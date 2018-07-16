package com.dthfish.hencoderdemo;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Description
 * Author DthFish
 * Date  2018/7/16.
 */

public class Utils {

    public static float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
