package com.rss.rajasri.ui.authentication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.ActivityLoginScreenBinding
import com.rss.rajasri.databinding.ActivityOtpscreenBinding
import com.rss.rajasri.datamodels.response.LoginDataModel
import com.rss.rajasri.datamodels.response.OTPDataModel
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.activities.EditProfileActivity
import com.rss.rajasri.ui.activities.PendingPropertiesActivity
import com.rss.rajasri.ui.dashboard.HomeActivity
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.PROFILE_NAVIGATED_FROM_LOGIN
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class OTPScreenActivity : AppCompatActivity() {
    val binding: ActivityOtpscreenBinding by lazy {
        ActivityOtpscreenBinding.inflate(layoutInflater)
    }

    lateinit var mobileNumber:String
    lateinit var otp:String

    fun AppCompatEditText.showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        this.requestFocus()
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mobileNumber= intent.getStringExtra("mobileNumber").toString()
        otp= intent.getStringExtra("otp_").toString()
        binding.txtOTP.text = otp

        binding.mobileNumberTV.text = "${resources.getString(R.string.otp_verify_account)} ${intent.getStringExtra("mobileNumber")}"
        var count = 0
        fun setFocusable(){
            count++
            binding.pinEdit1.isFocusable = true
            binding.pinEdit1.isFocusableInTouchMode = true
            binding.pinEdit2.isFocusable = true
            binding.pinEdit2.isFocusableInTouchMode = true
            binding.pinEdit3.isFocusable = true
            binding.pinEdit3.isFocusableInTouchMode = true
            binding.pinEdit4.isFocusable = true
            binding.pinEdit4.isFocusableInTouchMode = true
        }
        binding.pinEdit1.setOnClickListener {
            if(count==0){
                setFocusable()
                binding.pinEdit1.requestFocus()
                binding.pinEdit1.showKeyboard()
            }
        }
        binding.pinEdit2.setOnClickListener {
            if(count==0){
                setFocusable()
                binding.pinEdit2.requestFocus()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.pinEdit2.showKeyboard()
                },100)

            }
        }
        binding.pinEdit3.setOnClickListener {
            if(count==0){
                setFocusable()
                binding.pinEdit3.requestFocus()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.pinEdit3.showKeyboard()
                },100)

            }
        }
        binding.pinEdit4.setOnClickListener {
            if(count==0){
                setFocusable()
                binding.pinEdit4.requestFocus()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.pinEdit4.showKeyboard()
                },100)

            }
        }
        binding.pinEdit1.addTextChangedListener {
            if (it?.toString()?.length == 1) binding.pinEdit2.requestFocus()
        }

        binding.pinEdit2.addTextChangedListener {
            if (it?.toString()?.length == 1) binding.pinEdit3.requestFocus() else binding.pinEdit1.requestFocus()
        }
        binding.pinEdit3.addTextChangedListener {
            if (it?.toString()?.length == 1) binding.pinEdit4.requestFocus() else binding.pinEdit2.requestFocus()
        }
        binding.pinEdit4.addTextChangedListener {
            if ((it?.toString()?.length?:0)<1) binding.pinEdit3.requestFocus()
        }
        binding.apply {
            floatingBT.setOnClickListener {
                if(validateOtp()){
                    try {
                        callOtpApi()
                    }catch (e: Exception){
                        showToastMsg(e.message)
                        e.printStackTrace()
                    }
                }else{
                    showToastMsg(resources.getString(R.string.enter_valid_otp))
                }
            }
            resendCodeTV.setOnClickListener {
                try {
                    callLoginApi()
                }catch (e: Exception){
                    showToastMsg(e.message)
                    e.printStackTrace()
                }

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
        jsonObject.put("mobile_number",mobileNumber)
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callLoginApi(body)
        apiCall.enqueue(object :Callback<LoginDataModel>{
            override fun onResponse(call: Call<LoginDataModel>, response: Response<LoginDataModel>) {
                val data = response.body()
                if(data?.status == "200"){
                    RajaSriApp.appPreference.setCustomerID( data.data?.customerId?:"")
                }
                showToastMsg("${data?.message}")
                CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<LoginDataModel>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }

    private fun callOtpApi() {
        if(!this.isInternetAvailable()){
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        val pin1 = binding.pinEdit1.editableText.trim().toString()
        val pin2 = binding.pinEdit2.editableText.trim().toString()
        val pin3 = binding.pinEdit3.editableText.trim().toString()
        val pin4 = binding.pinEdit4.editableText.trim().toString()
        jsonObject.put("customer_id",RajaSriApp.appPreference.getCustomerID())
        jsonObject.put("otp_number","$pin1$pin2$pin3$pin4")
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callOTPApi(body)
        apiCall.enqueue(object : Callback<OTPDataModel> {
            override fun onResponse(call: Call<OTPDataModel>, response: Response<OTPDataModel>) {
                setDataToUI(response.body())
                CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<OTPDataModel>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }

    private fun setDataToUI(data:OTPDataModel?) {
        if(data?.status == "200"){
            Log.d("data","Login data ${data.message}")
            Log.d("data","Login data ${data.data.toString()}")
            Log.d("data","Login data ${data.data?.code}")
            navigateToOTPScreen(data.data?.code)
        }
        showToastMsg("${data?.message}")
    }

    private fun navigateToOTPScreen(code:Int?){
        /*if(code==1){
            RajaSriApp.appPreference.setUserMobileNumber(mobileNumber = intent.getStringExtra("mobileNumber")?:"")
            startActivity(Intent(this@OTPScreenActivity,EditProfileActivity::class.java).apply {
                putExtra(PROFILE_NAVIGATED_FROM_LOGIN,"navigated_from_OTP")
            })
            finishAffinity()
        }else if(code==2){*/
            RajaSriApp.appPreference.setUserMobileNumber(mobileNumber = intent.getStringExtra("mobileNumber")?:"")
                  RajaSriApp.appPreference.setLoggedIn(isLoggedIn = true)
            //startActivity(Intent(this@OTPScreenActivity,HomeActivity::class.java))
        startActivity(Intent(this@OTPScreenActivity, PendingPropertiesActivity::class.java))
        finish()
       // }

    }


    private fun validateOtp():Boolean{
        val pin1 = binding.pinEdit1.editableText.trim().toString()
        val pin2 = binding.pinEdit2.editableText.trim().toString()
        val pin3 = binding.pinEdit3.editableText.trim().toString()
        val pin4 = binding.pinEdit4.editableText.trim().toString()
        return  pin1.isNotEmpty() && pin2.isNotEmpty() && pin3.isNotEmpty() && pin4.isNotEmpty()
    }
}