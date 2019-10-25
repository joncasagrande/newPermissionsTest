package com.joncasagrande.settinglocker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.content.pm.PackageManager
import android.app.AppOpsManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.joncasagrande.settinglocker.service.AppCheckServices
import com.takwolf.android.lock9.Lock9View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        if (checkAccessPermission() && checkOverlayPermission()) {
            startMonitorService()
        }else{
            requestOverlayPermission()
            requestAccessModePermission()
        }
    }

    private fun startMonitorService(){
        startService(Intent(this, AppCheckServices::class.java))
    }



    private fun requestOverlayPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + getPackageName()))
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun requestAccessModePermission(){
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (checkAccessPermission() && checkOverlayPermission()) {
                startMonitorService()

            }
        }
    }

    @SuppressLint("NewApi")
    private fun checkAccessPermission(): Boolean{
        try {
            val packageManager = getPackageManager()
            val applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0)
            val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                applicationInfo.uid,
                applicationInfo.packageName
            )
            return mode == AppOpsManager.MODE_ALLOWED

        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    private fun checkOverlayPermission() :Boolean = Settings.canDrawOverlays(this)

    companion object{
        const val REQUEST_CODE = 1098
    }
}
