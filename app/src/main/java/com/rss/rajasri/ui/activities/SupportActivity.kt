package com.rss.rajasri.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.ActivitySupportBinding
import com.rss.rajasri.datamodels.response.RequestSupport
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SupportActivity : AppCompatActivity() {
    lateinit var binding:ActivitySupportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RajaSriApp.setLangConfig(baseContext);
        binding=ActivitySupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.includedHeaderLL.backButtonIV.setOnClickListener {
            finish()
        }
       // val headerTitle=findViewById<TextView>(R.id.headerTitle)
        //binding.includedHeaderLL. headerTitle.setText("Support")
        binding.submitSupport.setOnClickListener {
            val name=binding.fullNameET.text.toString().trim()
            val email=binding.edEmail.text.toString().trim()
            val phone=binding.edPhonenumber.text.toString().trim()
            val subject=binding.edSubject.text.toString().trim()
            val comment=binding.edComment.text.toString().trim()

            if(name.isEmpty()||email.isEmpty()||phone.isEmpty()||subject.isEmpty()||comment.isEmpty())
            {
                showToastMsg("Please fill all details")
                return@setOnClickListener
            }
            val jsonObject = JSONObject()
            jsonObject.put("full_name", name)
            jsonObject.put("email_id", email)
            jsonObject.put("phone_number", phone)
            jsonObject.put("subject", subject)
            jsonObject.put("comment", comment)
            addSupportMessage(jsonObject)
        }
    }
    private fun addSupportMessage(jsonObject: JSONObject) {
        if(!isInternetAvailable()){
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this@SupportActivity)
        val mediaType = "application/json".toMediaTypeOrNull()
       jsonObject.put("customer_id", RajaSriApp.appPreference.getCustomerID())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.submitSupport(body)
        apiCall.enqueue(object : Callback<RequestSupport> {
            override fun onResponse(call: Call<RequestSupport>, response: Response<RequestSupport>) {
                CommonMethods.hideProgress()
                showToastMsg(response.body()?.message)
                if(response.body()?.status=="200")
                {
                    finish()
                }
            }

            override fun onFailure(call: Call<RequestSupport>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }
}