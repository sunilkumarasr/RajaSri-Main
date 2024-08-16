package com.rss.rajasri.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.ActivitySignUpScreenBinding
import com.rss.rajasri.datamodels.response.OTPDataModel
import com.rss.rajasri.datamodels.response.RegisterDataModel
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.activities.PrivacyPolicyActivity
import com.rss.rajasri.ui.activities.TermsAndConditionsActivity
import com.rss.rajasri.ui.dashboard.HomeActivity
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpScreenActivity : AppCompatActivity() {
    val binding: ActivitySignUpScreenBinding by lazy {
        ActivitySignUpScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            registerCV.setOnClickListener {
                validateUserInputs()

            }
            loginTV.setOnClickListener {
                startActivity(Intent(this@SignUpScreenActivity, LoginScreen::class.java))
                finish()
            }
            txtPrivacy.setOnClickListener {
                startActivity(Intent(it.context, PrivacyPolicyActivity::class.java))

            }
            txtTerms.setOnClickListener {
                startActivity(Intent(it.context, TermsAndConditionsActivity::class.java))

            }
        }
    }

    override fun onBackPressed() {

        startActivity(Intent(this@SignUpScreenActivity, LoginScreen::class.java))
        finish()
        super.onBackPressed()
    }


    private fun validateUserInputs() {
        if (!checkFullNameValidation()) {
            showToastMsg("please, enter valid Full Name")
            return
        }
        if (!checkEmailValidation()) {
            showToastMsg("please, enter valid Email")
            return
        }
        if (!checkMobileNumberValidation()) {
            showToastMsg(resources.getString(R.string.enter_valid_number))
            return
        }
        if (!checkPasswordValidation()) {
            showToastMsg("please, enter password")
            return
        }
        if (binding.edLocation.text?.trim()?.isEmpty() == true) {
            showToastMsg("please, enter Location")
            return
        }
        if (binding.edEmail.editableText.trim().toString()?.isEmpty() == true) {
            showToastMsg("please, EmailId")
            return
        }
        if (!binding.termscheck.isChecked) {
            showToastMsg("please, accept Terms & Privacy policy")
            return
        }
        try {
            callRegisterApi()
        } catch (e: Exception) {
            showToastMsg(e.message)
            e.printStackTrace()
        }

    }

    private fun callRegisterApi() {
        if (!this.isInternetAvailable()) {
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        val fullName = binding.fullNameET.editableText.trim().toString()
        val mobileNumber = binding.phoneNumberET.editableText.trim().toString()
        jsonObject.put("full_name", fullName)
        jsonObject.put("mobile_number", mobileNumber)
        jsonObject.put("email", binding.edEmail.editableText.trim().toString())
        jsonObject.put("password", binding.edPassword.editableText.trim().toString())
        jsonObject.put("location", binding.edLocation.editableText.trim().toString())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callRegisterApi(body)
        apiCall.enqueue(object : Callback<RegisterDataModel> {
            override fun onResponse(
                call: Call<RegisterDataModel>,
                response: Response<RegisterDataModel>
            ) {
                setDataToUI(response.body())
                CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<RegisterDataModel>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }

    private fun setDataToUI(data: RegisterDataModel?) {
        if (data?.status == "200") {
            navigateToOTPScreen()
        }
        showToastMsg("${data?.message}")
    }

    private fun navigateToOTPScreen() {
        startActivity(Intent(this@SignUpScreenActivity, LoginScreen::class.java))
        finishAffinity()
    }

    private fun checkMobileNumberValidation(): Boolean {
        val mobilePattern = "^[6-9][0-9]{9}\$"

        // You can also use Patterns class for more comprehensive validation
        return Patterns.PHONE.matcher(binding.phoneNumberET.editableText.trim().toString())
            .matches() && binding.phoneNumberET.editableText.trim().toString()
            .matches(Regex(mobilePattern))
    }

    private fun checkFullNameValidation(): Boolean {
        return binding.fullNameET.editableText.trim().toString().isNotEmpty()
    }

    private fun checkPasswordValidation(): Boolean {
        return binding.edPassword.editableText.trim().toString().isNotEmpty()
    }

    private fun checkEmailValidation(): Boolean {
        val regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
        return regex.matches(binding.edEmail.editableText.trim().toString())
    }

}