package ru.punkoff.machinelearnexample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import eu.bolt.screenshotty.Screenshot
import eu.bolt.screenshotty.ScreenshotBitmap
import eu.bolt.screenshotty.ScreenshotManagerBuilder
import eu.bolt.screenshotty.rx.asRxScreenshotManager
import io.reactivex.disposables.Disposables

class ScreenshotActivity : AppCompatActivity() {

    private val screenshotManager by lazy {
        ScreenshotManagerBuilder(this)
            .withPermissionRequestCode(REQUEST_SCREENSHOT_PERMISSION)
            .build()
            .asRxScreenshotManager()
    }

    private var screenshotSubscription = Disposables.disposed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        takeScreenshot()
    }

    private fun takeScreenshot() {
        screenshotSubscription.dispose()
        screenshotSubscription = screenshotManager
            .makeScreenshot()
            .subscribe(
                ::handleScreenshot,
                ::handleScreenshotError
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        screenshotManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleScreenshot(screenshot: Screenshot) {
        val bitmap = when (screenshot) {
            is ScreenshotBitmap -> screenshot.bitmap
        }
        findViewById<ConstraintLayout>(R.id.screenshotPreview).background =
            bitmap.toDrawable(resources)
    }

    private fun handleScreenshotError(t: Throwable) {
        Log.e(javaClass.simpleName, t.message, t)
        Toast.makeText(this, t.message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        screenshotSubscription.dispose()
    }

    companion object {
        private const val REQUEST_SCREENSHOT_PERMISSION = 1234
    }
}