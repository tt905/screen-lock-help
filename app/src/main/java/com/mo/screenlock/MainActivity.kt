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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mo.screenlock.databinding.ActivityMainBinding
import com.mo.screenlock.receiver.AdminActivateReceiver
import com.mo.screenlock.service.Mp3Service
import com.mo.screenlock.utils.PowerUtils
import com.mo.screenlock.utils.VibrateUtils

/**
 * Desc: Main Page
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mLayoutBinding: ActivityMainBinding
    private lateinit var mReceiver: ComponentName
    private lateinit var mForegroundIntent: Intent
    private lateinit var mBinder: Mp3Service.MyBinder

    private lateinit var mMediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLayoutBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mLayoutBinding.root)
        mReceiver = ComponentName(this, AdminActivateReceiver::class.java)

        setPlayer()
        iniView()
        startMp3Service()

        mLayoutBinding.root.postDelayed({
            mMediaPlayer.start()
            Toast.makeText(this@MainActivity, "锁屏帮助已启动", Toast.LENGTH_SHORT).show()
        }, 2000);
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("onNewIntent", "is me too")
    }


    private fun setPlayer() {
        mMediaPlayer = MediaPlayer.create(this, R.raw.wusheng)
        mMediaPlayer.setOnCompletionListener {
            Log.d("onCompletion", "duration: ${mMediaPlayer.currentPosition}")
            mMediaPlayer.start()
        }
//        mMediaPlayer.isLooping = true
    }


    private fun iniView() {
        with(mLayoutBinding) {
            buttonLock.setOnClickListener {
                if (!PowerUtils.isAdminActive(mReceiver)) {
                    Toast.makeText(this@MainActivity, "请先开启设备管理", Toast.LENGTH_SHORT).show()
                    openDeviceAdmin()
                }

                VibrateUtils.vibrate(this@MainActivity)
                PowerUtils.lockScreen()
            }

            buttonStart.setOnClickListener {
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
                super.onBackPressed()
            }
            .setNegativeButton("取消", null)
            .create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer.release()
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

//    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
//        when (keyCode) {
//            KeyEvent.KEYCODE_VOLUME_DOWN -> {
//                Log.i("MainActivity", "volume down")
//                PowerUtils.wakeUpAndUnlock()
//                return false
//            }
//        }
//        return super.onKeyDown(keyCode, event)
//    }

}