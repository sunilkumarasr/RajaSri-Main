package com.rss.rajasri.ui.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.ActivityLoanDetailsBinding
import com.rss.rajasri.databinding.TransactionsViewDetailsBinding
import com.rss.rajasri.datamodels.response.AllProperties
import com.rss.rajasri.datamodels.response.EmiDetails
import com.rss.rajasri.datamodels.response.EmiDetailsDM
import com.rss.rajasri.datamodels.response.EmiItemDataModel
import com.rss.rajasri.datamodels.response.LoanPropertyDetailsDataModel
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.activities.loan.EmiAdapter
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.PROPERTY_DETAILS
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LoanDetailsActivity : AppCompatActivity() {
    val binding: ActivityLoanDetailsBinding by lazy {
        ActivityLoanDetailsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.includedHeaderLL.backButtonIV.setOnClickListener {
            finish()
        }
        try {
            callPropertiesApi()
            callEmiDetailsApi()
        }catch (e: Exception){
            e.printStackTrace()
            showToastMsg(e.message)
        }
        binding.viewDetailsCV2.setOnClickListener {
            binding.viewDetailsCV1.performClick()
        }
        binding.viewDetailsCV3.setOnClickListener {
            binding.viewDetailsCV1.performClick()
        }
        binding.viewDetailsCV4.setOnClickListener {
            binding.viewDetailsCV1.performClick()
        }
        binding.viewDetailsCV5.setOnClickListener {
            binding.viewDetailsCV1.performClick()
        }
    }
    private fun callPropertiesApi(){
        if(!isInternetAvailable()){
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        val dataModel = intent?.getStringExtra(PROPERTY_DETAILS)?:""
        val properties  = Gson().fromJson(dataModel, AllProperties::class.java)
        jsonObject.put("property_id", properties.id)
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callLoanPropertyDetailsApi(body)
        apiCall.enqueue(object : Callback<LoanPropertyDetailsDataModel> {
            override fun onResponse(call: Call<LoanPropertyDetailsDataModel>, response: Response<LoanPropertyDetailsDataModel>) {
                setDataToUI(response.body())
                CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<LoanPropertyDetailsDataModel>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }

    private fun callEmiDetailsApi(){
        if(!isInternetAvailable()){
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        val dataModel = intent?.getStringExtra(PROPERTY_DETAILS)?:""
        val properties  = Gson().fromJson(dataModel, AllProperties::class.java)
        jsonObject.put("property_id", properties.id)
        jsonObject.put("user_id", RajaSriApp.appPreference.getCustomerID())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callEmiDetailsApi(body)
        apiCall.enqueue(object : Callback<EmiDetailsDM> {
            override fun onResponse(call: Call<EmiDetailsDM>, response: Response<EmiDetailsDM>) {
                setDataToEmiRV(response.body())
                CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<EmiDetailsDM>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }

    var emid_date=""
    private fun setDataToEmiRV(body: EmiDetailsDM?) {
        if(body?.status=="200"){
            val adapter = EmiAdapter()
            binding.emiRV.adapter = adapter
            adapter.setOnClickListener {
                // show emi dialog
                emid_date= it.emiDate.toString()
                callEmiItemDataModel(it)
            }
            adapter.setData(body.data?.emiDetails?: arrayListOf())

        }else{
            showToastMsg(body?.message)
        }
    }

    private fun callEmiItemDataModel(emiDetails: EmiDetails) {
        if(!isInternetAvailable()){
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("emi_id", emiDetails.id)
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callEmiItemDetailsApi(body)
        apiCall.enqueue(object : Callback<EmiItemDataModel> {
            override fun onResponse(call: Call<EmiItemDataModel>, response: Response<EmiItemDataModel>) {
                setTransactionDialog(response.body())
                CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<EmiItemDataModel>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })

    }

    private fun setDataToUI(body: LoanPropertyDetailsDataModel?) {
        if(body?.status=="200"){
            body.data?.propertyDetails?.map { propertyDetails ->
                binding.apply {
                    titleTV.text = propertyDetails.name
                    descriptionTV.text = Html.fromHtml(propertyDetails.description)
                    ventureNameTV.text = propertyDetails.name
                    priceTV.text = "₹ ${propertyDetails.price}/-"
                    areaTV.text = "${propertyDetails.area} (${propertyDetails.area_name})"
                    emiPriceTV.text = "₹ ${propertyDetails.emiAmount}/-"
                   // fullNameTV.text = "${propertyDetails.fullName}"
                    emiDateTV.text = "${propertyDetails.emiDate}"
                    emiMonthsTV.text = "${propertyDetails.emiMonths}"
                    loanNumberTV.text = "#RT ${propertyDetails.loanNumber}"
                    Glide.with(bannerIV)
                        .load(propertyDetails.image)
                        .placeholder(R.drawable.venture_image_png)
                        .into(bannerIV)
                }
            }

        }else{
            showToastMsg(body?.message)
        }

    }

    private fun setTransactionDialog(body: EmiItemDataModel?) {
        val emiItemData = body?.data
        val dialogBinding  = TransactionsViewDetailsBinding.inflate(layoutInflater,null,false)
        emiItemData?.emiDetails?.map {
            dialogBinding.ventureNameTV.text  = it.name
            dialogBinding.fullNameTV.text  = it.fullName?:""
            dialogBinding.emiPriceTV.text  = "${it.emiAmount} /-"
            dialogBinding.emiPaidTV.text  = "${emid_date}"
            if(it.transactionId==null|| it.transactionId!!.isEmpty())
            dialogBinding.transactionTV.text  = "No Transaction"
            else
            dialogBinding.transactionTV.text  = "${it.transactionId}"
        }

        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding.root)
        dialog.show()
        val att = dialog.window?.attributes
        att?.width = WindowManager.LayoutParams.MATCH_PARENT
        att?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = att
        dialog.window?.setBackgroundDrawable(ColorDrawable().apply {
            color = Color.TRANSPARENT
        })
    }
}