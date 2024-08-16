package com.rss.rajasri.ui.dashboard

import android.app.Activity
import android.app.Notification
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.ActivityHomeBinding
import com.rss.rajasri.datamodels.response.LoginDataModel
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.activities.EditProfileActivity
import com.rss.rajasri.ui.activities.NotificationActivity
import com.rss.rajasri.utils.APP_LAUNCHED
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RajaSriApp.setLangConfig(baseContext);
        setContentView(binding.root)

        token = FirebaseInstanceId.getInstance().token.toString()
        APP_LAUNCHED = true
        RajaSriApp.appPreference.setLoggedIn(isLoggedIn = true)
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        binding.bottomNav.setupWithNavController(navHost.navController)

        binding.profileIMG.setOnClickListener {
            startActivity(Intent(applicationContext, EditProfileActivity::class.java))
        }
        binding.frameNotification.setOnClickListener {
            startActivity(Intent(applicationContext, NotificationActivity::class.java))
        }

        setProfilePic()
        updateNotificationToken()
    }

    fun setProfilePic() {
        try {
            var path = RajaSriApp.appPreference.getProfilePath()
            Glide.with(applicationContext)
                .load(path)
                .transform(CircleCrop())
                .placeholder(R.drawable.account_circle_24)
                .into(binding.profileIMG)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun updateNotificationToken() {
        if (token == null || token.isEmpty())
            token = FirebaseInstanceId.getInstance().token.toString()
        if (!this.isInternetAvailable()) {
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        // CommonMethods.showProgress(this)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("customer_id", RajaSriApp.appPreference.getCustomerID())
        jsonObject.put("firebase_token", token)
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.updateToken(body)
        apiCall.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {

                //showToastMsg("${data?.message}")
                // CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                // showToastMsg("${t.message}")
                t.printStackTrace()
                // CommonMethods.hideProgress()
            }

        })
    }

}