package com.rss.rajasri.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.ui.activities.PendingPropertiesActivity
import com.rss.rajasri.ui.authentication.LoginScreen
import com.rss.rajasri.ui.authentication.SignUpScreenActivity
import com.rss.rajasri.ui.dashboard.HomeActivity
import java.util.Locale

class RajaSriEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raja_sri_entry)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(0, WindowInsets.Type.statusBars())
            window.insetsController?.setSystemBarsAppearance(WindowInsets.Type.statusBars(), WindowInsets.Type.statusBars())
            window.statusBarColor = getColor(R.color.splash_color)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = getColor(R.color.splash_color)
        }
      val  language=RajaSriApp.appPreference.getLanguage()

        val locale = Locale(language)
        Locale.setDefault(locale)
        var config= Configuration()
        config.locale=locale
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            RajaSriApp.updateResources(applicationContext, language);
        }else {
            // for devices having lower version of android os
            RajaSriApp.updateResourcesLegacy(applicationContext, language);
        }*/

        askNotificationPermission()

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
        {
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToValidScreens()
            }, 1500)
        }else
        {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val  language=RajaSriApp.appPreference.getLanguage()

        val locale = Locale(language)
        Locale.setDefault(locale)
        var config= Configuration()
        config.locale=locale
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.isNotEmpty()) {
                Handler(Looper.getMainLooper()).postDelayed({
                    navigateToValidScreens()
                }, 1500)
            }
        }
    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Handler(Looper.getMainLooper()).postDelayed({
                    navigateToValidScreens()
                }, 1500)
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // Display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
                ActivityCompat.requestPermissions(this@RajaSriEntryActivity,  arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 200);
            } else {
                // Directly ask for the permission
                ActivityCompat.requestPermissions(this@RajaSriEntryActivity,  arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 200);

                //requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToValidScreens()
        }, 1500)
    }

    private fun navigateToValidScreens() {
        if(RajaSriApp.appPreference.isLoggedIn()){
            startActivity(Intent(this@RajaSriEntryActivity, PendingPropertiesActivity::class.java))
        }else{
            startActivity(Intent(this@RajaSriEntryActivity, LanguageSelectionScreen::class.java))
        }
        finishAffinity()
    }
}