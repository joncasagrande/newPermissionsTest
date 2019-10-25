package com.joncasagrande.settinglocker.service

import android.app.ActivityManager
import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.view.WindowManager
import com.joncasagrande.settinglocker.ui.LockerView
import java.util.*
import java.util.concurrent.locks.Lock

class AppCheckServices() : Service() {

    lateinit var timer : Timer

    lateinit var context : Context
    var currentApp = ""
    internal var pakageName: MutableList<String> = mutableListOf()
    var isOpen = false


    override fun onCreate() {
        super.onCreate()
        context = application.applicationContext
        timer = Timer("AppCheckServices")
        timer.schedule(updateTask, 1000L, 1000L)
    }


    override fun onBind(intent: Intent): IBinder?= null

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }



    private val updateTask = object : TimerTask() {
        override fun run() {
            pakageName = ArrayList<String>()
            pakageName.add("com.android.settings")
            if (isConcernedAppIsInForeground() && !isOpen) {
                context.startService(Intent(context, LockerService::class.java))
                isOpen = true
            }
        }
    }


    fun isConcernedAppIsInForeground(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var mpackageName = manager.runningAppProcesses[0].processName
        val usage = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val time = System.currentTimeMillis()
        val stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, time)
        if (stats != null) {
            val runningTask = TreeMap<Long, UsageStats>()
            for (usageStats in stats) {
                runningTask[usageStats.lastTimeUsed] = usageStats
            }
            if (runningTask.isEmpty()) {
                Log.d("", "isEmpty Yes")
                mpackageName = ""
            } else {
                mpackageName = runningTask[runningTask.lastKey()]!!.packageName
                Log.d("", "isEmpty No : $mpackageName")
            }
        }

        pakageName.forEach {
            if(it == mpackageName){
                currentApp = it
                return true
            }
        }
        isOpen = false
        return false
    }



}