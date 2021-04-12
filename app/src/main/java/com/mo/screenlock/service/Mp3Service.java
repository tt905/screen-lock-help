package com.mo.screenlock.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.mo.screenlock.MainActivity;
import com.mo.screenlock.R;
import com.mo.screenlock.receiver.VolumeReceiver;

/**
 * @author : beta mtw905@gmail.com
 * @date : 2021/4/8-16:52
 * @desc : desc
 */
public class Mp3Service extends Service {

    public static final int NOTIFICATION_ID = 0xF0;
    public static boolean serviceIsLive = false;

    private MediaPlayer mMediaPlayer;
    private MyBinder mBinder = new MyBinder();
    private VolumeReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
//        mMediaPlayer = MediaPlayer.create(this, R.raw.wusheng);
        // 获取服务通知
        Notification notification = createForegroundNotification();
        //将服务置于启动状态 ,NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIFICATION_ID, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIsLive = true;
        receiver = new VolumeReceiver();
        receiver.init(this);


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        stopForeground(true);
        unregisterReceiver(receiver);
        serviceIsLive = false;
    }

    /**
     * 创建服务通知
     */
    private Notification createForegroundNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 唯一的通知通道的id.
        String notificationChannelId = "screen_lock_channel_id_01";

        // Android8.0以上的系统，新建消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //用户可见的通道名称
            String channelName = "锁屏帮助channel";
            //通道的重要程度
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, channelName, importance);
            notificationChannel.setDescription("锁屏帮助channel");
            //LED灯
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
            //震动
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannelId);
        //通知小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //通知标题
        builder.setContentTitle("锁屏帮助");
        //通知内容
        builder.setContentText("锁屏帮助服务");
        //设定通知显示的时间
        builder.setWhen(System.currentTimeMillis());
        //设定启动的内容
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        //创建通知并返回
        return builder.build();
    }

    public class MyBinder extends Binder {

        /**
         * 播放音乐
         */
        public void playMusic() {
//            if (!mMediaPlayer.isPlaying()) {
//                //如果还没开始播放，就开始
//                mMediaPlayer.start();
//            }
        }
    }
}
