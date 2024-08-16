package com.rss.rajasri.ui.fragments.Bullings.Childs

import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.FragmentUnpaidBinding
import com.rss.rajasri.datamodels.response.EmiDetailsDM
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.Razorpay.RazorpayActivity
import com.rss.rajasri.ui.activities.transaction_history.TransactionUnpaidAdapter
import com.rss.rajasri.ui.dashboard.HomeActivity
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


class EMIUnpaidFragment : Fragment() {

    private lateinit var binding: FragmentUnpaidBinding
   // private lateinit var mListener: WLCheckoutActivity.PaymentResponseListener

    var property_id: String = "";
    var emi_id: String = "";
    var emi_amount="";


    private lateinit var autoReadOtpHelper: BroadcastReceiver


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUnpaidBinding.inflate(inflater, container, false)

//        mListener = this
//        WLCheckoutActivity.setPaymentResponseListener(mListener)
//        WLCheckoutActivity.preloadData(requireContext())


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callEmiDetailsApi()
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
        Log.e("customer_id", RajaSriApp.appPreference.getCustomerID())
        jsonObject.put("payment_status", "unpaid")
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callBiilingPaidUnpaidByYearMonth(body)
        apiCall.enqueue(object : Callback<EmiDetailsDM> {
            override fun onResponse(call: Call<EmiDetailsDM>, response: Response<EmiDetailsDM>) {
                CommonMethods.hideProgress()
                if(response.code()==500)
                {
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        val status = jsonObject.getString("status")
                        if(status=="200")
                        {
                            val propertyDetails= Gson().fromJson(jsonObject.toString(),EmiDetailsDM::class.java)
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
            val paidTransactionList = body.data?.emiDetails?.filter { it.name?.equals("UnPaid",ignoreCase = true) ==true}?.toMutableList() as ArrayList

            if(paidTransactionList.isNotEmpty()){
                binding.unPaidRV.show()
                binding.noDataFoundTV.hide()
                val adapter = TransactionUnpaidAdapter(requireActivity())
                binding.unPaidRV.adapter = adapter

                adapter.setOnClickListener { it ->
                    property_id = Gson().toJson(it.customerPropertyLoanId)
                    emi_id = Gson().toJson(it.id)
                    emi_amount = Gson().toJson(it.emi_amount)

                    val extractedAmount = extractAndConvertJsonToInt(emi_amount)

                    //paynimoApiRun()
                    startActivityForResult(Intent(activity, RazorpayActivity()::class.java).apply {
                        putExtra("Amount",extractedAmount.toString())
                        putExtra("paymentType","EMI")
                        putExtra("PayId",property_id)
                        putExtra("Id",emi_id)
                    },200)
                    //EMIpaymentSuccess(property_id)
                }

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

    fun extractAndConvertJsonToInt(jsonString: String): Int {
        // Parse the JSON string to get the actual value
        val amountString = Gson().fromJson(jsonString, String::class.java)
        return amountString.toInt()
    }

    //old payment paynimo
//    private fun paynimoApiRun() {
//        val reqJson = JSONObject()
//
//        val jsonFeatures = JSONObject()
//        jsonFeatures.put("enableExpressPay", true)
//        jsonFeatures.put("enableInstrumentDeRegistration", true)
//        jsonFeatures.put("enableAbortResponse", true)
//        jsonFeatures.put("enableMerTxnDetails", true)
//        reqJson.put("features", jsonFeatures)
//
//        val jsonConsumerData = JSONObject()
//        jsonConsumerData.put("deviceId","AndroidSH2") // Possible values "AndroidSH1" or "AndroidSH2"
//        jsonConsumerData.put("token","e04be9ed85f134a8ca30f609dca6c1f36e742762590daf6ed6edda06275f378a2147f6244ca2295d134beba1e98c6e67140577893b99e6bd34c09d3f2350519c")
//        jsonConsumerData.put("paymentMode", "all")
//        jsonConsumerData.put("merchantLogoUrl","https://www.paynimo.com/CompanyDocs/company-logo-vertical.png")
//        jsonConsumerData.put("merchantId", "L3348")
//        jsonConsumerData.put("currency", "INR")
//        jsonConsumerData.put("consumerId", "c964634")
//        jsonConsumerData.put("txnId", "1708068696283")
//
//        val jArrayItems = JSONArray()
//        val jsonItem1 = JSONObject()
//        jsonItem1.put("itemId", "first")
//        jsonItem1.put("amount", "1") //invoiceAmount
//        jsonItem1.put("comAmt", "0")
//        jArrayItems.put(jsonItem1)
//        jsonConsumerData.put("items", jArrayItems)
//
//        val jsonCustomStyle = JSONObject()
//        jsonCustomStyle.put("PRIMARY_COLOR_CODE", "#45beaa")
//        jsonCustomStyle.put("SECONDARY_COLOR_CODE", "#ffffff")
//        jsonCustomStyle.put("BUTTON_COLOR_CODE_1", "#2d8c8c")
//        jsonCustomStyle.put("BUTTON_COLOR_CODE_2", "#ffffff")
//        jsonConsumerData.put("customStyle", jsonCustomStyle)
//
//        reqJson.put("consumerData", jsonConsumerData)
//
//        WLCheckoutActivity.open(requireContext(), reqJson)
//    }
//    override fun wlCheckoutPaymentResponse(response: JSONObject) {
//        Log.e("response_success", response.toString())
//
//        val jsonObject = JSONObject(response.toString())
//        val successValue = jsonObject.getString("msg").split("|")[1]
//
//        if (successValue.equals("success")){
//            //EMIpaymentSuccess()
//        }else{
//            Toast.makeText(requireContext(), "payment failed", Toast.LENGTH_LONG).show()
//        }
//
//    }
//    override fun wlCheckoutPaymentError(response: JSONObject) {
//        Toast.makeText(requireContext(), "Payment failed", Toast.LENGTH_LONG).show()
//        Log.e("response_error", response.toString())
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==200&&requestCode==200)
        {
            callEmiDetailsApi()
        }
    }
    private fun EMIpaymentSuccess(property_id: String) {
        if (!requireContext().isInternetAvailable()) {
            requireContext().showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(requireContext())
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        val propertyID = this.property_id.trim('"').toInt()
        val emiID = emi_id.trim('"').toInt()
        jsonObject.put("property_id", property_id)
        jsonObject.put("emi_id", emiID.toString())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.emipaymentSuccess(body)
        apiCall.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                //Toast.makeText(requireContext(), "payment success", Toast.LENGTH_LONG).show()
                startActivity(Intent(requireContext(), HomeActivity::class.java))

                Log.e("emipaymentSuccess_re",response.toString())

            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                t.printStackTrace()
                CommonMethods.hideProgress()
            }
        })
    }


}