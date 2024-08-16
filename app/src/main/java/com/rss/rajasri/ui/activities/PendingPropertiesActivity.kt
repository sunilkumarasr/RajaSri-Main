package com.rss.rajasri.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.datamodels.response.PropertiesDataModel
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.Razorpay.RazorpayActivity
import com.rss.rajasri.ui.dashboard.HomeActivity
import com.rss.rajasri.ui.fragments.home.PendingPropertyAdapter
import com.rss.rajasri.utils.Adapter_Details
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.IS_FROM_PENDING
import com.rss.rajasri.utils.PROPERTY_DETAILS
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


class PendingPropertiesActivity : AppCompatActivity() {

    lateinit var recyclerPending:RecyclerView
    lateinit var noDataFoundTV:TextView
    val propertiesAdapter = PendingPropertyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RajaSriApp.setLangConfig(baseContext);
        setContentView(R.layout.activity_pending_properties)
        recyclerPending=findViewById(R.id.recycle_pending)
        noDataFoundTV=findViewById(R.id.noDataFoundTV)
        callPendingPropertiesApi()
    }
    private fun callPendingPropertiesApi(){
        if(!isInternetAvailable()){
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this@PendingPropertiesActivity)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("customer_id", RajaSriApp.appPreference.getCustomerID())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.pendingProperties(body)
        apiCall.enqueue(object : Callback<PropertiesDataModel> {
            override fun onResponse(call: Call<PropertiesDataModel>, response: Response<PropertiesDataModel>) {
                CommonMethods.hideProgress()
                Log.e("Response","Response success ${response.code()}")
                if(response.code()==500)
                {
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        val status = jsonObject.getString("status")
                        if(status=="200")
                        {
                            val propertyDetails=Gson().fromJson(jsonObject.toString(),PropertiesDataModel::class.java)
                            setDataToList(propertyDetails)
                            Log.e("Response","Response success ${jsonObject.toString()}")
                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("Response","Response success ${e.message}")

                    }
                    return
                }

                //response.code()
                setDataToList(response.body())

            }

            override fun onFailure(call: Call<PropertiesDataModel>, t: Throwable) {
                showToastMsg("${t.message}")
                Log.e("Response","Response error pending ")

                if (t is HttpException) {
                    val data = t.response()?.body()
                    if (t.code() == 500 && data is PropertiesDataModel){
                        showToastMsg("${t.message}")
                        Log.e("Response","Response error pending ${data.toString()}")

                        // onFailed(data.message)
                    } else {
                        //onFailed(t.message())
                    }
                } else {
                   // onFailed(t.message.toString())
                }
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }
    private fun setDataToList(body: PropertiesDataModel?) {
        if(body?.status=="200"){

            recyclerPending.adapter = propertiesAdapter
            propertiesAdapter.setOnClickListener {it,type->
                when(type){
                    Adapter_Details ->{
                        startActivityForResult(Intent(this@PendingPropertiesActivity,PlotDetailsActivity::class.java).apply {
                            putExtra(PROPERTY_DETAILS, Gson().toJson(it))
                            putExtra(IS_FROM_PENDING,true)
                        },100)
                    }

                }

            }
            body.data?.pendingProperties?.let { propertiesAdapter.setData(it) }
            if(propertiesAdapter.itemCount<=0)
            {
                startActivity(Intent(this@PendingPropertiesActivity, HomeActivity::class.java))
                finish()
            }
           // startActivity(Intent(this@PendingPropertiesActivity, HomeActivity::class.java))


        }else{
            Log.e("body?.message","body?.message ${body?.message}")
            //showToastMsg(body?.message)
            if(propertiesAdapter.itemCount<=0)
            {
                startActivity(Intent(this@PendingPropertiesActivity, HomeActivity::class.java))
                finish()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==200)
        {
            callPendingPropertiesApi()
        }
    }


}