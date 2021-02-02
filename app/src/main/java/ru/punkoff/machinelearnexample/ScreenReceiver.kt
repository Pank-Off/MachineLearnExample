package ru.punkoff.machinelearnexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView

class ScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("android.intent.action.BOOT_COMPLETED") ||
            intent?.action.equals("ru.punkoff.machinelearnexample")
        ) {
            Log.d(javaClass.simpleName, "Show window")
            showWindow(context)
        }
    }

    private fun showWindow(context: Context?) {
        windowManager = context?.getSystemService(WINDOW_SERVICE) as WindowManager
        val layoutInflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var layoutFlag: Int = WindowManager.LayoutParams.TYPE_PHONE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.END or Gravity.TOP
        windowLayout = layoutInflater.inflate(R.layout.window_layout, null) as ViewGroup
        windowManager.addView(windowLayout, params)
        setOnImageClickListener(context)
    }

    private fun setOnImageClickListener(context: Context?) {
        val androidImage = windowLayout.findViewById<ImageView>(R.id.android_image)
        androidImage.setOnClickListener {
            val intent = Intent(context, ScreenshotActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(intent)
        }
    }

    companion object {
        private lateinit var windowManager: WindowManager
        private lateinit var windowLayout: ViewGroup
    }
}