package com.rss.rajasri.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import com.google.firebase.messaging.FirebaseMessaging
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.ActivityLoginScreenBinding
import com.rss.rajasri.datamodels.response.LoginDataModel
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LoginScreen : AppCompatActivity() {

    val binding:ActivityLoginScreenBinding by lazy {
        ActivityLoginScreenBinding.inflate(layoutInflater)
    }

    var fireBasetoken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //binding.phoneNumberET.editableText.append("Enter Phone Number")
        binding.apply {
            loginBT.setOnClickListener {
                if(!checkMobileNumberValidation()){
                    showToastMsg(resources.getString(R.string.enter_valid_number))
                }else{
                    try {
                        callLoginApi()
                    }catch (e:Exception){
                        showToastMsg(e.message)
                        e.printStackTrace()
                    }
                }
            }

            loginTV.setOnClickListener {
                startActivity(Intent(applicationContext,SignUpNewActivity::class.java))
                finish()
            }
        }

        fireBaseToken()
    }

    private fun fireBaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fireBasetoken = task.result
                Log.e("FCMfireBasetoken",fireBasetoken)
                // Now you have the FCM token, you can use it as needed.
            } else {
                Log.e("FCM Token", "Failed to get token ${task.exception}")
            }
        }
    }

   private fun callLoginApi(){
        if(!this.isInternetAvailable()){
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
       CommonMethods.showProgress(this)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("mobile_number",binding.phoneNumberET.editableText.trim().toString())
        jsonObject.put("token",fireBasetoken);
        val body = jsonObject.toString().toRequestBody(mediaType)
       val apiCall = RetrofitClient.api.callLoginApi(body)
       apiCall.enqueue(object :Callback<LoginDataModel>{
           override fun onResponse(call: Call<LoginDataModel>, response: Response<LoginDataModel>) {
               setDataToUI(response.body())
               CommonMethods.hideProgress()
           }

           override fun onFailure(call: Call<LoginDataModel>, t: Throwable) {
               showToastMsg("${t.message}")
               t.printStackTrace()
               CommonMethods.hideProgress()

           }

       })
    }

    private fun setDataToUI(data:LoginDataModel?) {
        if(data?.status == "200"){
            Log.d("data","Login data ${data.message}")
            Log.d("data","Login data ${data.data.toString()}")
            Log.d("data","Login data ${data.data?.customerId}")
            RajaSriApp.appPreference.setCustomerID( data.data?.customerId?:"")
            navigateToOTPScreen(data.data?.otp_number?:0)
        }
        showToastMsg("${data?.message}")
    }

    private fun navigateToOTPScreen(otp: Int){
        Log.e("otp_",otp.toString())
        startActivity(Intent(this@LoginScreen,OTPScreenActivity::class.java).apply {
            putExtra("mobileNumber",binding.phoneNumberET.editableText.trim().toString())
            putExtra("otp_",otp.toString())
        })
        finish()
    }

    private fun checkMobileNumberValidation():Boolean {
        val mobilePattern = "^[6-9][0-9]{9}\$"
        return Patterns.PHONE.matcher(binding.phoneNumberET.editableText.trim().toString()).matches() && binding.phoneNumberET.editableText.trim().toString().matches(Regex(mobilePattern))
    }
}