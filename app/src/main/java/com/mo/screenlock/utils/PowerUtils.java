package com.mo.screenlock.utils;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.PowerManager;

import com.mo.screenlock.App;


/**
 * @author : beta mtw905@gmail.com
 * @date : 2021/4/7-10:38
 * @desc : desc
 */
public class PowerUtils {

    /**
     * 唤醒手机屏幕并解锁
     */
    public static void wakeUpAndUnlock() {
        Context context = App.Companion.getApp().getApplicationContext();

        //获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            PowerManager.WakeLock wl = pm.newWakeLock((PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK), "my_app:com.mo.demo.utils.PowerUtils");
            wl.acquire(10000); // 点亮屏幕
            wl.release(); // 释放
        }

        //屏锁管理器
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
//        kl.reenableKeyguard(); // 屏幕锁定
    }

    /**
     * 锁定屏幕
     */
    public static void lockScreen() {
        Context context = App.Companion.getApp().getApplicationContext();
        DevicePolicyManager policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        policyManager.lockNow();
    }

    public static boolean isAdminActive(ComponentName componentName) {
        Context context = App.Companion.getApp().getApplicationContext();
        DevicePolicyManager policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        return policyManager.isAdminActive(componentName);
    }
}
