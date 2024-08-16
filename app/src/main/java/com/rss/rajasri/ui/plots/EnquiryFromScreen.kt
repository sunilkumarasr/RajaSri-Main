package com.rss.rajasri.ui.plots

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.ActivityEnquiryFromScreenBinding
import com.rss.rajasri.datamodels.response.EnquiryResponse
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
import java.util.Locale

class EnquiryFromScreen : AppCompatActivity() {
    var property_id=""
    var plot_id=""
    var full_name=""
    var email=""
    var mobile_number=""
    var investment=""
    var comments=""
    lateinit var binding:ActivityEnquiryFromScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RajaSriApp.setLangConfig(baseContext);

        binding=ActivityEnquiryFromScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        property_id=intent.getStringExtra("property_id").toString()
        plot_id=intent.getStringExtra("plot_id").toString()
        binding.backButtonIV.setOnClickListener {
            finish()
        }
        binding.btnSend.setOnClickListener {
            full_name=binding.editName.text.toString().trim()
            email=binding.editEmail.text.toString().trim()
            mobile_number=binding.editNumber.text.toString().trim()
            investment=binding.editInvestment.text.toString().trim()
            comments=binding.editComments.text.toString().trim()

            if(full_name.isEmpty()||email.isEmpty()||mobile_number.isEmpty()||investment.isEmpty()||comments.isEmpty())
            {
                showToastMsg(resources.getString(R.string.please_fill_all_details))
                return@setOnClickListener
            }
            sendEnquiry()
        }
    }
     fun sendEnquiry() {
        if(!this@EnquiryFromScreen.isInternetAvailable()){
            this@EnquiryFromScreen.showToastMsg(resources.getString(R.string.check_internet))
            return
        }

        CommonMethods.showProgress(this@EnquiryFromScreen)


        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("user_id", RajaSriApp.appPreference.getCustomerID())
        jsonObject.put("property_id", property_id)
        jsonObject.put("full_name", full_name)
        jsonObject.put("email", email)
        jsonObject.put("mobile_number", mobile_number)
        jsonObject.put("investment", investment)
        jsonObject.put("comments", comments)
        Log.e("user_id_", RajaSriApp.appPreference.getCustomerID())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.sendEnquiry(body)
        apiCall.enqueue(object : Callback<EnquiryResponse> {
            override fun onResponse(call: Call<EnquiryResponse>, response: Response<EnquiryResponse>) {
                CommonMethods.hideProgress()
                showToastMsg(response.body()!!.message)
                if(response.body()!!.status=="200")
                {
                    finish()
                }
                Log.e("Response", "Response success ${response.code()}")



                //response.code()

            }

            override fun onFailure(call: Call<EnquiryResponse>, t: Throwable) {
                Log.e("Response", "Response error pending ${t.message}")
                CommonMethods.hideProgress()

            }

        })
    }
}