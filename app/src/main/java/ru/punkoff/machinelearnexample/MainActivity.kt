package ru.punkoff.machinelearnexample

import android.annotation.TargetApi
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val mScreenAlertWindowReceiver = ScreenReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkSystemAlertPermission()
    }

    private fun checkSystemAlertPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, PERMISSIONS_REQUEST_SYSTEM_ALERT_WINDOW)
        } else {
            sendBroadcastThenBootCompleted()
            sendBroadcastThenAppStarted()
            finish()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSIONS_REQUEST_SYSTEM_ALERT_WINDOW) {
            if (!Settings.canDrawOverlays(this)) {
                checkSystemAlertPermission()
            } else {
                sendBroadcastThenBootCompleted()
                sendBroadcastThenAppStarted()
                finish()
            }
        }
    }

    private fun sendBroadcastThenBootCompleted() {
        val screenStateFilter = IntentFilter()
        screenStateFilter.addAction(Intent.ACTION_BOOT_COMPLETED)
        registerReceiver(mScreenAlertWindowReceiver, screenStateFilter)
    }

    private fun sendBroadcastThenAppStarted() {
        registerReceiver(mScreenAlertWindowReceiver, IntentFilter("ru.punkoff.machinelearnexample"))
        val intent = Intent()
        intent.action = "ru.punkoff.machinelearnexample"
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mScreenAlertWindowReceiver)
    }

    companion object {
        private const val PERMISSIONS_REQUEST_SYSTEM_ALERT_WINDOW = 100
    }
}
