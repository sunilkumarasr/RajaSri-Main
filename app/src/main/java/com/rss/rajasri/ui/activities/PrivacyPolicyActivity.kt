package com.rss.rajasri.ui.activities

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.rss.rajasri.R
import com.rss.rajasri.databinding.ActivityPrivacyPolicyBinding
import com.rss.rajasri.datamodels.response.DefaultDM
import com.rss.rajasri.datamodels.response.SupportRes
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.showToastMsg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PrivacyPolicyActivity : AppCompatActivity() {
    private val binding: ActivityPrivacyPolicyBinding by lazy {
        ActivityPrivacyPolicyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.includedHeaderLL.backButtonIV.setOnClickListener {
            finish()
        }

        callPrivacyApi()   }

    private fun callPrivacyApi() {
        if (!this.isInternetAvailable()) {
            this.showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this)
        val apiCall = RetrofitClient.api.callPrivacyApi()
        apiCall.enqueue(object : Callback<SupportRes> {
            override fun onResponse(call: Call<SupportRes>, response: Response<SupportRes>) {

                response.body()?.data?.privacy?.map {
                    val plain = Html.fromHtml(it.content).toString()

                    binding.webView.text = "" + plain

                }
                CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<SupportRes>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }


}