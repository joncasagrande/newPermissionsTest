package com.joncasagrande.settinglocker.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.joncasagrande.settinglocker.service.AppCheckServices
import com.joncasagrande.settinglocker.service.LockerService
import com.takwolf.android.lock9.Lock9View

class LockerView(context: Context)  : FrameLayout(context)  {


    val view : View
    lateinit var lockView : Lock9View
    lateinit var forgetPass: Button
    init {

        view = LayoutInflater.from(context).inflate(com.joncasagrande.settinglocker.R.layout.locker_view,this)
        findViewById()

        forgetPass.setOnClickListener {
            val launchIntent = context.packageManager.getLaunchIntentForPackage("com.joncasagrande.settinglocker")
            context.startActivity(launchIntent)
        }

        lockView.setCallBack{ password ->
            if(password.equals("1458963")){
                context.stopService(Intent(context, LockerService::class.java))
            }
        }
    }

    private fun findViewById(){
        lockView = view.findViewById(com.joncasagrande.settinglocker.R.id.lock_9_view)
        forgetPass = view.findViewById(com.joncasagrande.settinglocker.R.id.forgetPassword)
    }



}