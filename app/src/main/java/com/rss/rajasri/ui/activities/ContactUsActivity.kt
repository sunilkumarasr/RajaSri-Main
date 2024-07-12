package com.rss.rajasri.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rss.rajasri.R
import com.rss.rajasri.databinding.ActivityContactUsBinding
import com.rss.rajasri.datamodels.response.ContactDM
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.showToastMsg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsActivity : AppCompatActivity() {
    val binding: ActivityContactUsBinding by lazy {
        ActivityContactUsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.includedHeaderLL.backButtonIV.setOnClickListener {
            finish()
        }
        callConatactApi()
    }

    private fun callConatactApi() {
        if (!this.isInternetAvailable()) {
            this.showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this)
        val apiCall = RetrofitClient.api.callContactApi()
        apiCall.enqueue(object : Callback<ContactDM> {
            override fun onResponse(call: Call<ContactDM>, response: Response<ContactDM>) {

                response.body()?.data?.contact?.map {

                    binding.mail.text = "" + it.email
                    binding.mobile.text = "" + it.mobile
                }




                CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<ContactDM>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }

}