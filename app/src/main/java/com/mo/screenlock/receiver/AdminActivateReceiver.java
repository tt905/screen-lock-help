
package com.mo.screenlock.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 * @author : beta mtw905@gmail.com
 * @date : 2021/4/7-11:10
 * @desc : desc
 */
public class AdminActivateReceiver extends DeviceAdminReceiver {

    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        super.onEnabled(context, intent);
        Log.d("AdminActivateReceiver", "已启用锁屏");
    }

    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
        super.onDisabled(context, intent);
        Log.d("AdminActivateReceiver", "已关闭锁屏");
    }
}
