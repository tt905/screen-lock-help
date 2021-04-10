package com.mo.screenlock

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.mo.screenlock.databinding.ActivityMainBinding
import com.mo.screenlock.receiver.AdminActivateReceiver
import com.mo.screenlock.service.Mp3Service
import com.mo.screenlock.utils.PowerUtils
import com.mo.screenlock.utils.VibrateUtils

/**
 * Desc: Main Page
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mReceiver: ComponentName
    private lateinit var mForegroundIntent: Intent
    private lateinit var mBinder: Mp3Service.MyBinder

    private var mSimpleExoPlayer: SimpleExoPlayer? = null

    private lateinit var mMediaPlayer: MediaPlayer
    private var isPrepare = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mSimpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        mReceiver = ComponentName(this, AdminActivateReceiver::class.java)

        setPlayer()
        iniView()

        startMp3Service()
    }

    private fun setPlayer() {
//        val defaultDataSourceFactory =
//            DefaultDataSourceFactory(this, "audio/mpeg") //  userAgent -> audio/mpeg  不能为空
//        val concatenatingMediaSource = ConcatenatingMediaSource() //创建一个媒体连接源
//        val mediaSource1 =
//            ProgressiveMediaSource.Factory(defaultDataSourceFactory).createMediaSource(
//
//            ) //创建一个播放数据源
//        concatenatingMediaSource.addMediaSource(mediaSource1)
//        mSimpleExoPlayer?.playWhenReady = true
//        mSimpleExoPlayer?.prepare(concatenatingMediaSource)

//        val mediaItem =
//            MediaItem.fromUri(Uri.parse("android:resource://" + packageName + "/" + R.raw.wusheng))
//        mSimpleExoPlayer?.setMediaItem(mediaItem)
//        mSimpleExoPlayer?.prepare()

        mMediaPlayer = MediaPlayer.create(this, R.raw.wusheng)
        mMediaPlayer.isLooping = true
    }


    private fun iniView() {
        with(mBinding) {
            buttonLock.setOnClickListener {
                if (!PowerUtils.isAdminActive(mReceiver)) {
                    Toast.makeText(this@MainActivity, "请先开启设备管理", Toast.LENGTH_SHORT).show()
                    openDeviceAdmin()
                }

                VibrateUtils.vibrate(this@MainActivity)
                PowerUtils.lockScreen()
            }

            buttonStart.setOnClickListener {
//                mBinder.playMusic()
//                mSimpleExoPlayer?.play()
                mMediaPlayer.start()
            }
        }
    }

    private fun startMp3Service() {
        if (Mp3Service.serviceIsLive) return
        mForegroundIntent = Intent(this, Mp3Service::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(mForegroundIntent)
        } else {
            startService(mForegroundIntent)
        }

        bindService(mForegroundIntent, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                mBinder = service as Mp3Service.MyBinder
            }

            override fun onServiceDisconnected(name: ComponentName?) {

            }
        }, BIND_AUTO_CREATE)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("确定退出吗?")
            .setPositiveButton("确定") { dialog, which ->
                stopMp3Service()
                mMediaPlayer.release()
                super.onBackPressed()
            }
            .setNegativeButton("取消", null)
            .create().show()
    }

    private fun stopMp3Service() {
        //停止服务
        stopService(mForegroundIntent)
    }

    /**
     * 激活设备管理
     */
    private fun openDeviceAdmin() {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mReceiver)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启后就可以使用锁屏功能了...")
        startActivityForResult(intent, 0)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                Log.i("MainActivity", "volume down")
                PowerUtils.wakeUpAndUnlock()
                return false
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}