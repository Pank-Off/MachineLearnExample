package ru.punkoff.machinelearnexample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ScreenshotActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        takeScreenshot()
    }

    private fun takeScreenshot() {
        Toast.makeText(this, "ScreenShot", Toast.LENGTH_SHORT).show()
    }
}