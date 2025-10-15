package com.example.progresshabitplanner

import android.content.Intent
import com.example.progresshabitplanner.databinding.ActivitySplashBinding
import android.os.Bundle
import android.util.Log
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivityTag"
    private val SPLASH_TIME_OUT: Long = 2000
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: Activity created.")

        // Időzített futtatás
        Handler(Looper.getMainLooper()).postDelayed({
            // Navigálás a MainActivity-re
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)

            // Bezárjuk a SplashActivity-t, hogy ne lehessen visszalépni
            finish()
        }, SPLASH_TIME_OUT)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Activity is about to become visible.")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Activity is now visible and interactive.")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Activity is partially obscured.")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Activity is no longer visible.")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: Activity is about to be restarted.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Activity is about to be destroyed.")
    }
}