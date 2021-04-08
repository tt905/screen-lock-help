package com.mo.screenlock.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * @author : beta mtw905@gmail.com
 * @date : 2021/4/7-14:08
 * @desc : desc
 */
public class VibrateUtils {


    public static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] patter = {1000, 1000, 2000, 50};
        vibrator.vibrate(patter, 0);
    }
}
