package com.joncasagrande.settinglocker.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import com.joncasagrande.settinglocker.ui.LockerView

class LockerService : Service() {

    private var mWindowManager: WindowManager? = null
    lateinit var mWindowsParams: WindowManager.LayoutParams
    var notificationView : LockerView? = null
    lateinit var context : Context


    override fun onCreate() {
        super.onCreate()
        context = application.applicationContext

    }


    override fun onBind(intent: Intent): IBinder?= null

    override fun onDestroy() {
        super.onDestroy()
        if(notificationView != null){
            mWindowManager!!.removeView(notificationView)
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        notificationView = LockerView(context)

        moveView()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun moveView() {
        mWindowsParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY or WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, // Not displaying keyboard on bg activity's EditText
            PixelFormat.TRANSLUCENT)

        mWindowsParams.gravity = Gravity.TOP or Gravity.START
        mWindowsParams.x = 0
        mWindowsParams.y = 0
        mWindowManager!!.addView(notificationView, mWindowsParams)
    }


}