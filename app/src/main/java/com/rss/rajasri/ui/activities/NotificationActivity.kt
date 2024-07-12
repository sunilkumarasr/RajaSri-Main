package com.rss.rajasri.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.ActivityEditProfileBinding
import com.rss.rajasri.databinding.ActivityNotificationBinding
import com.rss.rajasri.datamodels.response.NotificationDataModel
import com.rss.rajasri.datamodels.response.PendingInvoicesDataModel
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.fragments.home.InvoiceUnpaidAdapter
import com.rss.rajasri.ui.fragments.home.NotificationsAdapter
import com.rss.rajasri.utils.Adapter_Details
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActivity : AppCompatActivity() {

    private val binding: ActivityNotificationBinding by lazy {
        ActivityNotificationBinding.inflate(layoutInflater)
    }

    val notificationsAdapter = NotificationsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        callNotificationsApi()

        binding.includedHeaderLL.backButtonIV.setOnClickListener {
            finish()
        }

    }


    private fun callNotificationsApi() {
        if(!this@NotificationActivity.isInternetAvailable()){
            this@NotificationActivity.showToastMsg(resources.getString(R.string.check_internet))
            return
        }

        CommonMethods.showProgress(this@NotificationActivity)

        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("user_id", RajaSriApp.appPreference.getCustomerID())
        Log.e("user_id_", RajaSriApp.appPreference.getCustomerID())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.notificationsApi(body)
        apiCall.enqueue(object : Callback<NotificationDataModel> {
            override fun onResponse( call: Call<NotificationDataModel>,response: Response<NotificationDataModel>) {
                CommonMethods.hideProgress()

                Log.e("Response", "Response success ${response.code()}")
                if (response.code() == 500) {
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        val status = jsonObject.getString("status")
                        if (status == "200") {
                            val propertyDetails =
                                Gson().fromJson(
                                    jsonObject.toString(),
                                    NotificationDataModel::class.java
                                )
                            setDataNotification(propertyDetails)
                            Log.e("Response_pendingInvoices", "Response success ${jsonObject.toString()}")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("Response", "Response success ${e.message}")
                    }
                    return
                }

                //response.code()
                setDataNotification(response.body())
            }

            override fun onFailure(call: Call<NotificationDataModel>, t: Throwable) {
                Log.e("Response", "Response error pending ")
                CommonMethods.hideProgress()
            }

        })
    }

    private fun setDataNotification(body: NotificationDataModel?) {
        if (body?.status == "200") {

            binding.notificationRv.adapter = notificationsAdapter
            notificationsAdapter.setOnClickListener { it ->
//                Toast.makeText(applicationContext,it.title.toString(), Toast.LENGTH_SHORT).show()
            }
            body.data?.notification?.let { notificationsAdapter.setData(it) }
            if (notificationsAdapter.itemCount <= 0) {
                noData()
            }
        } else {
            Log.e("body?.message", "body?.message ${body?.message}")
            //showToastMsg(body?.message)
            if (notificationsAdapter.itemCount <= 0) {
                noData()
            }
        }
    }


    private fun noData() {
        binding.notificationRv.visibility = View.GONE
        binding.noDataFoundTV.visibility = View.VISIBLE
    }

}
