package com.mo.screenlock

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mo.screenlock.databinding.ActivityMainBinding
import com.mo.screenlock.receiver.AdminActivateReceiver
import com.mo.screenlock.service.Mp3Service
import com.mo.screenlock.utils.PowerUtils
import com.mo.screenlock.utils.VibrateUtils

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mReceiver: ComponentName

    private lateinit var mForegroundService: Intent

    private lateinit var mBinder: Mp3Service.MyBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mReceiver = ComponentName(this, AdminActivateReceiver::class.java)
        iniView()

        startMp3Service()
    }

    private fun startMp3Service() {
        if (Mp3Service.serviceIsLive) return
        mForegroundService = Intent(this, Mp3Service::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(mForegroundService);
        } else {
            startService(mForegroundService);
        }

        bindService(mForegroundService, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                mBinder = service as Mp3Service.MyBinder
            }

            override fun onServiceDisconnected(name: ComponentName?) {

            }
        }, BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        //停止服务
        mForegroundService = Intent(this, Mp3Service::class.java)
        stopService(mForegroundService)
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
                mBinder.playMusic()
            }
        }
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