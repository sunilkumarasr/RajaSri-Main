package com.rss.rajasri.application

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import com.rss.rajasri.preference.AppPreference
import java.util.Locale

class RajaSriApp:Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        appPreference = AppPreference(appContext)

    }

    companion object{
        lateinit var appContext:Context
        lateinit var appPreference:AppPreference

        fun setLangConfig(baseContext:Context)
        {
            val  language=RajaSriApp.appPreference.getLanguage()
            val locale = Locale(language)
            Locale.setDefault(locale)
            var config= Configuration()
            config.locale=locale
            baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)

        }

        @TargetApi(Build.VERSION_CODES.N)
        fun updateResources(context: Context, language: String): Context? {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val configuration: Configuration = context.resources.configuration
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            return context.createConfigurationContext(configuration)
        }


        @Suppress("deprecation")
        fun updateResourcesLegacy(context: Context, language: String): Context? {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val resources: Resources = context.resources
            val configuration: Configuration = resources.getConfiguration()
            configuration.locale = locale
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLayoutDirection(locale)
            }
            resources.updateConfiguration(configuration, resources.getDisplayMetrics())
            return context
        }
    }

}
