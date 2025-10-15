package com.example.progresshabitplanner

import android.os.Bundle
import android.util.Log
import com.example.progresshabitplanner.databinding.ActivitySplashBinding
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import android.os.Handler
import android.os.Looper
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivityTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NavController lekérése a NavHostFragment-ből
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment //
        val navController = navHostFragment.navController // [cite: 181]

        // Top-level célpontok definiálása az ActionBar-hoz (pl. hogy ne jelenjen meg a 'vissza' nyíl)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.profileFragment) //
        )
        setupActionBarWithNavController(navController, appBarConfiguration) //

        // BottomNavigationView összekapcsolása a NavController-rel
        binding.bottomNav.setupWithNavController(navController) //
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
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