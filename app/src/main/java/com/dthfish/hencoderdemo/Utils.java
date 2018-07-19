package com.dthfish.hencoderdemo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public static Bitmap getAvatar(Resources resource, int drawableId, int width) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resource, drawableId, option);
        option.inJustDecodeBounds = false;
        option.inDensity = option.outWidth;
        option.inTargetDensity = width;
        return BitmapFactory.decodeResource(resource, drawableId, option);
    }

    public static float getLocationZ() {
        return -6 * Resources.getSystem().getDisplayMetrics().density;
    }

}
