package com.rss.rajasri.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.rss.rajasri.R
import com.rss.rajasri.databinding.ActivityAboutUsBinding
import com.rss.rajasri.datamodels.response.DefaultDM
import com.rss.rajasri.datamodels.response.SupportRes
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.showToastMsg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AboutUsActivity : AppCompatActivity() {
    val binding:ActivityAboutUsBinding by lazy {
        ActivityAboutUsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.includedHeaderLL.backButtonIV.setOnClickListener {
            finish()
        }
        callAboutApi()
    }

    private fun callAboutApi() {
        if (!this.isInternetAvailable()) {
            this.showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this)
        val apiCall = RetrofitClient.api.callAboutApi()
        apiCall.enqueue(object : Callback<SupportRes> {
            override fun onResponse(call: Call<SupportRes>, response: Response<SupportRes>) {

                response.body()?.data?.about?.map {
                    Log.e("plain"," plain ${it.content}")
                    //val plain = Html.fromHtml(it?.content).toString()

                    it.content?.let { it1 -> binding.webview.loadData(it1,"text/html","utf-8") }
                    //binding.about.text = "" + plain


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