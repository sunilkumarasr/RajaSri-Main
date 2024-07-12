package com.rss.rajasri.ui

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.ActivityLanguageSelectionScreenBinding
import com.rss.rajasri.ui.activities.PendingPropertiesActivity
import com.rss.rajasri.ui.authentication.LoginScreen
import java.util.Locale


class LanguageSelectionScreen : AppCompatActivity() {
    lateinit var binding:ActivityLanguageSelectionScreenBinding
    var language="en"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLanguageSelectionScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        language=RajaSriApp.appPreference.getLanguage()

        if(language=="en")
            binding.radioEng.isChecked=true
        else
            binding.radioTelugu.isChecked=true

        binding.radioLanguage.setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                if(checkedId==R.id.radio_eng)
                {
                    language="en"
                }else{
                    language="te"
                }
            }

        })
        binding.submitSupport.setOnClickListener {
            RajaSriApp.appPreference.setLanguage(language)
            val locale = Locale(language)
            Locale.setDefault(locale)
            var config=Configuration()
            config.locale=locale
            baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                RajaSriApp.updateResources(applicationContext, language);
            }else {
                // for devices having lower version of android os
                RajaSriApp.updateResourcesLegacy(applicationContext, language);
            }*/
            navigateToValidScreens()
        }


    }
    private fun navigateToValidScreens() {
        if(RajaSriApp.appPreference.isLoggedIn()){
            val intent=Intent(this@LanguageSelectionScreen, PendingPropertiesActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }else{
            val intent=Intent(this@LanguageSelectionScreen, LoginScreen::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        finishAffinity()
    }


}