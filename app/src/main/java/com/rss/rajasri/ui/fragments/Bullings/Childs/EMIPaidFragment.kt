package com.rss.rajasri.ui.fragments.Bullings.Childs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.FragmentPaidBinding
import com.rss.rajasri.datamodels.response.EmiDetailsDM
import com.rss.rajasri.datamodels.response.PendingInvoicesDataModel
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.activities.transaction_history.TransactionAdapter
import com.rss.rajasri.ui.fragments.home.InvoicePaidAdapter
import com.rss.rajasri.utils.Adapter_Details
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.hide
import com.rss.rajasri.utils.show
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class EMIPaidFragment : Fragment() {
    private lateinit var binding:FragmentPaidBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPaidBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            callEmiDetailsApi()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun callEmiDetailsApi(){
        if(!requireContext().isInternetAvailable()){
            requireContext().showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(requireContext())
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
/*        val dataModel = intent?.getStringExtra(PROPERTY_DETAILS)?:""
        val properties  = Gson().fromJson(dataModel, AllProperties::class.java)*/
        jsonObject.put("customer_id", RajaSriApp.appPreference.getCustomerID())
        jsonObject.put("payment_status", "paid")

        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callBiilingPaidUnpaidByYearMonth(body)
        apiCall.enqueue(object : Callback<EmiDetailsDM> {
            override fun onResponse(call: Call<EmiDetailsDM>, response: Response<EmiDetailsDM>) {
                if(response.code()==500)
                {
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        val status = jsonObject.getString("status")
                        if(status=="200")
                        {
                            val propertyDetails=Gson().fromJson(jsonObject.toString(),EmiDetailsDM::class.java)
                            setDataToEmiRV(propertyDetails)
                            Log.e("Response","Response success my ${jsonObject.toString()}")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("Response","Response success ${e.message}")
                    }
                    return
                }
                setDataToEmiRV(response.body())
                CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<EmiDetailsDM>, t: Throwable) {
                context?.showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }


    private fun setDataToEmiRV(body: EmiDetailsDM?) {
        if(body?.status=="200"){
           val paidTransactionList = body.data?.emiDetails?.filter { it.name?.equals("Paid",ignoreCase = true) ==true}?.toMutableList() as ArrayList
            if(paidTransactionList.isNotEmpty()){
                binding.paidRV.show()
                binding.noDataFoundTV.hide()
                val adapter = TransactionAdapter(requireActivity())
                binding.paidRV.adapter = adapter
                adapter.setData(paidTransactionList)
            }else{
                binding.linearEmi.visibility = View.GONE
                binding.noDataFoundTV.visibility = View.VISIBLE
            }
        }else{
            binding.linearEmi.visibility = View.GONE
            binding.noDataFoundTV.visibility = View.VISIBLE
            context?.showToastMsg(body?.message)
        }
    }


}