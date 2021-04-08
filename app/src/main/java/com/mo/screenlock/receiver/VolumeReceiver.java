package com.mo.screenlock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.mo.screenlock.utils.PowerUtils;

/**
 * @author : beta mtw905@gmail.com
 * @date : 2021/4/7-15:15
 * @desc : desc
 */
public class VolumeReceiver extends BroadcastReceiver {

    public void init(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        mContext.registerReceiver(this, filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
            Log.d("VolumeReceiver", "音量变化");
            PowerUtils.wakeUpAndUnlock();
        }
    }
}
